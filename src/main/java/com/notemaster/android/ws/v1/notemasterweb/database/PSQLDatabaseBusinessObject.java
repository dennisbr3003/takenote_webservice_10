package com.notemaster.android.ws.v1.notemasterweb.database;

import java.sql.Statement;

import com.notemaster.android.ws.v1.notemasterweb.database.constants.ImageTableConstants;
import com.notemaster.android.ws.v1.notemasterweb.database.constants.LoggingTableConstants;
import com.notemaster.android.ws.v1.notemasterweb.database.constants.NoteTableConstants;
import com.notemaster.android.ws.v1.notemasterweb.database.constants.SharedPreferenceTableConstants;
import com.notemaster.android.ws.v1.notemasterweb.database.constants.UserTableConstants;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.IGraphicTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.INoteTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.ISharedPreferenceTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.IUserTable;
import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;
import com.notemaster.android.ws.v1.notemasterweb.payload.UserDataPayload;
import com.notemaster.android.ws.v1.notemasterweb.payload.WebUser;
import com.notemaster.android.ws.v1.notemasterweb.resource.LoggerTakeNote;
import com.notemaster.android.ws.v1.notemasterweb.response.UserDataResponse;

public class PSQLDatabaseBusinessObject implements 
             SharedPreferenceTableConstants,
             LoggingTableConstants,
             NoteTableConstants,
             ImageTableConstants, 
             UserTableConstants,
             IDatabaseBusinessObject
{

	private DAOFactory factory = DAOFactory.getFactory(DAOFactory.PSQL);	
	private ISharedPreferenceTable sharedPreferenceTable = factory.getSharedPreferenceTable();
	private INoteTable noteTable = factory.getNoteTable(); 
	private IGraphicTable imageTable = factory.getImageTable();
	private IUserTable userTable = factory.getUserTable();	
	private TypeIndependentResources typeIndependentResources = new TypeIndependentResources();	
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
		case TABLE_USR:
			return String.format("CREATE TABLE IF NOT EXISTS %s (%s VARCHAR(100) NOT NULL, %s VARCHAR(255) NOT NULL, %s VARCHAR(50) NOT NULL, " + 
					"%s VARCHAR(255), CONSTRAINT %s PRIMARY KEY (%s, %s));", 
					TABLE_USR, USR_NAME, USR_PASSWRD, USR_DID, USR_REMARK, P_KEY_USR, USR_DID, USR_NAME);			
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
		    createTable(TABLE_USR);
		    return true;
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}		
	  	
	@Override
	public UserDataResponse getUserDataResponse(String device_id) {		
		return typeIndependentResources.getUserDataResponse(device_id, logger, sharedPreferenceTable, noteTable, imageTable);				
	}

	@Override
	public void processUserDataPayload(UserDataPayload udp) {
		typeIndependentResources.processUserDataPayload(udp, logger, sharedPreferenceTable, noteTable, imageTable);		
	}

	@Override
	public boolean deviceHasData(String device_id) {		
		return typeIndependentResources.deviceHasData(device_id, logger, sharedPreferenceTable, noteTable, imageTable);		
	}
	
	@Override
	public WebUser getWebUser(String webusercode) {
		return typeIndependentResources.getWebUser(webusercode, userTable, logger);
	}

	@Override
	public void addWebUser(WebUser webuser) {
		typeIndependentResources.addWebUser(webuser, userTable, logger);		
	}
	
}
