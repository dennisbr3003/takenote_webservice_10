package com.notemaster.android.ws.v1.notemasterweb.database;

import java.sql.Statement;

import com.notemaster.android.ws.v1.notemasterweb.database.constants.ImageTableConstants;
import com.notemaster.android.ws.v1.notemasterweb.database.constants.LoggingTableConstants;
import com.notemaster.android.ws.v1.notemasterweb.database.constants.NoteTableConstants;
import com.notemaster.android.ws.v1.notemasterweb.database.constants.SharedPreferenceTableConstants;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.IGraphicTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.INoteTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.ISharedPreferenceTable;
import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;
import com.notemaster.android.ws.v1.notemasterweb.payload.UserDataPayload;
import com.notemaster.android.ws.v1.notemasterweb.resource.LoggerTakeNote;
import com.notemaster.android.ws.v1.notemasterweb.response.UserDataResponse;

public class PSQLDatabaseBusinessObject implements 
             SharedPreferenceTableConstants,
             LoggingTableConstants,
             NoteTableConstants,
             ImageTableConstants, 
             IDatabaseBusinessObject
{

	private DAOFactory factory = DAOFactory.getFactory(DAOFactory.PSQL);
	
	private ISharedPreferenceTable sharedPreferenceTable = factory.getSharedPreferenceTable();
	private INoteTable noteTable = factory.getNoteTable(); 
	private IGraphicTable imageTable = factory.getImageTable();

	private LoggerTakeNote logger;
	
	public LoggerTakeNote getLogger() {
		return logger;
	}

	public void setLogger(LoggerTakeNote logger) {
		this.logger = logger;
	}

	public PSQLDatabaseBusinessObject() {
	
	}
			
	private String getTableDefinition(String tableName) {

		//PostgreSQL
		switch(tableName) {
		case TABLE_LOG:
			return String.format("CREATE TABLE IF NOT EXISTS %s (%s VARCHAR(100) NOT NULL, " + 
					"%s SERIAL, %s VARCHAR(255) NOT NULL, "+ 
					"%s TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, %s VARCHAR(10) NOT NULL, " +
					"%s VARCHAR(100), %s VARCHAR(255), CONSTRAINT %s PRIMARY KEY (%s,%s));", 
					TABLE_LOG, LOG_ID, LOG_SEQ, LOG_GUID, LOG_CREATED, LOG_TYPE, LOG_METHOD, LOG_VALUE, 
					P_KEY_LOG, LOG_ID, LOG_SEQ);
		case TABLE_PRF:
			return String.format("CREATE TABLE IF NOT EXISTS %s (%s VARCHAR(100) NOT NULL, " + 
					"%s VARCHAR(40) NOT NULL, %s VARCHAR(40) NOT NULL, %s VARCHAR(40), %s TIMESTAMP NOT " + 
					"NULL DEFAULT CURRENT_TIMESTAMP, %s TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " + 
					"CONSTRAINT %s PRIMARY KEY (%s,%s));", 
					TABLE_PRF, PRF_ID, PRF_NAME, PRF_VALUE, PRF_DTYPE, PRF_CREATED, PRF_UPDATED, 
					P_KEY_PRF, PRF_ID, PRF_NAME);
		case TABLE_NTS:
			return String.format("CREATE TABLE IF NOT EXISTS %s (%s VARCHAR(100) NOT NULL, %s VARCHAR(50) NOT NULL, " + 
					"%s VARCHAR(40) NOT NULL, %s BYTEA, %s VARCHAR(50), %s VARCHAR(30), %s VARCHAR(30), %s TIMESTAMP NOT " + 
					"NULL DEFAULT CURRENT_TIMESTAMP, %s TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " + 
					"CONSTRAINT %s PRIMARY KEY (%s,%s));", 
					TABLE_NTS, NTS_ID, NTS_NOTE_ID, NTS_NAME, NTS_VALUE, NTS_DTYPE, NTS_NOTE_CREATED, NTS_NOTE_UPDATED, NTS_CREATED, NTS_UPDATED, 
					P_KEY_NTS, NTS_ID, NTS_NAME);
		case TABLE_PPI:
			return String.format("CREATE TABLE IF NOT EXISTS %s (%s VARCHAR(100) NOT NULL, %s VARCHAR(50) NOT NULL, " + 
					"%s VARCHAR(40) NOT NULL, %s BYTEA, %s VARCHAR(50), %s VARCHAR(30), %s VARCHAR(30), %s TIMESTAMP NOT " + 
					"NULL DEFAULT CURRENT_TIMESTAMP, %s TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " + 
					"CONSTRAINT %s PRIMARY KEY (%s,%s));", 
					TABLE_PPI, PPI_ID, PPI_IMAGE_ID, PPI_NAME, PPI_VALUE, PPI_DTYPE, PPI_IMG_CREATED, PPI_IMG_UPDATED, PPI_CREATED, PPI_UPDATED, 
					P_KEY_PPI, PPI_ID, PPI_NAME);			
		default:
			return "";
		}		

	}
	
	private boolean createTable(String tableName) {

		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 	
		
		try {
			Statement statement = PSQLDatabase.getInstance().getConnection().createStatement();
			statement.execute(getTableDefinition(tableName));
			PSQLDatabase.getInstance().getConnection().commit();
			return true;
		} catch(Exception e) {
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}
		
	}	
	
	@Override
	public boolean initDatabaseTables() {

		try {
		    createTable(TABLE_PRF);
		    createTable(TABLE_LOG);
		    createTable(TABLE_NTS);
		    createTable(TABLE_PPI);
		    return true;
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return false;
		}

	}		
	
	// set userdata  
	@Override
	public void processUserDataPayload(UserDataPayload udp) {

		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		
		if(logger != null) {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Number of elements in array shared preferences", String.valueOf(udp.getShared_preferenceSize())));
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Number of elements in array notes", String.valueOf(udp.getNoteListSize())));
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Number of elements in array passpoint image (s)", String.valueOf(udp.getPassPointImageListSize())));
		}
		
		sharedPreferenceTable.setLogger(logger);          // add logger to object
		noteTable.setLogger(logger);                      // add logger to object
		imageTable.setLogger(logger);                     // add logger to object		
		
		sharedPreferenceTable.saveSharedPreferences(udp); // first store the shared preferences
		noteTable.saveNote(udp);                          // Store the notes
		imageTable.saveImage(udp);                        // Store the pass point image(s)
		
		if(logger != null) {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Completed", internal_method_name));
		}
		
	}

	@Override
	public boolean deviceHasData(String device_id) {
		
		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		
		try {
			
			if(logger != null) {
				logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
			}			

			sharedPreferenceTable.setLogger(logger);          // add logger to object
			noteTable.setLogger(logger);                      // add logger to object
			imageTable.setLogger(logger);                     // add logger to object			
			
			if(sharedPreferenceTable.isEmpty(device_id) && noteTable.isEmpty(device_id) && imageTable.isEmpty(device_id)) {
	        	return false;
	        }
			
			return true;
		}
		finally {
			if(logger != null) {
				logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Completed", internal_method_name));
			}
		}
		
	}
	
	// get userdata
	@Override
	public UserDataResponse getUserDataResponse(String device_id) {
		
		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		
		if(logger != null) {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));		
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Set to retrieve values for device id", device_id));
		}
		
		UserDataResponse udr = new UserDataResponse();
		udr.setDevice_id(device_id);
			
		sharedPreferenceTable.setLogger(logger);          // add logger to object
		noteTable.setLogger(logger);                      // add logger to object
		imageTable.setLogger(logger);                     // add logger to object			
		
		udr = sharedPreferenceTable.getSharedPreferenceResponse(udr);
		udr = noteTable.getNoteResponse(udr);
		udr = imageTable.getImageResponse(udr);
		
		if(logger != null) {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Completed", internal_method_name));
		}
		
		return udr;
		
	}
	
}