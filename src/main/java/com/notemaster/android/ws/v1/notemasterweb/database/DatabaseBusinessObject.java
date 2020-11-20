package com.notemaster.android.ws.v1.notemasterweb.database;

import java.sql.Connection;

import com.notemaster.android.ws.v1.notemasterweb.database.tables.ImageTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.LoggingTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.NoteTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.SharedPreferenceTable;
import com.notemaster.android.ws.v1.notemasterweb.payload.UserDataPayload;
import com.notemaster.android.ws.v1.notemasterweb.response.UserDataResponse;

public class DatabaseBusinessObject {
	
	private Database database = new Database();	
	
	private SharedPreferenceTable sharedPreferenceTable = new SharedPreferenceTable();
	private NoteTable noteTable = new NoteTable(); 
	private ImageTable imageTable = new ImageTable();
	private LoggingTable logger = new LoggingTable();
	
	private Connection connection;
	
	public DatabaseBusinessObject() {
	
	}
			
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void closeConnection() {
		database.closeConnection(connection);
	}

	public boolean verifyConnection() {
		return database.testConnection(); // this method auto-closes the connection
	}
	
	public void setConnection() {
		this.connection = database.getConnection();
	}
	
	public LoggingTable getLogger() {
		return logger;
	}
	
	public void setLogger(String device_id) {
		logger.setDevice_id(device_id);
		logger.setGlobal_id();
		logger.setConnection(getConnection());		
	}
	
	// set userdata  
	public void processUserDataPayload(UserDataPayload udp) {

		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
		        .getMethodName(); 

		logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
		logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Number of elements in array shared preferences", String.valueOf(udp.getShared_preferenceSize())));
		logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Number of elements in array notes", String.valueOf(udp.getNoteListSize())));
		logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Number of elements in array passpoint image (s)", String.valueOf(udp.getPassPointImageListSize())));
				
		// first store the shared preferences
		sharedPreferenceTable.setConnection(connection);
		sharedPreferenceTable.setLogger(logger);
		sharedPreferenceTable.saveSharedPreferences(udp);
		
		// Store the notes
		noteTable.setConnection(connection);
		noteTable.setLogger(logger);
		noteTable.saveNote(udp);

		// Store the passpoint image(s)
		imageTable.setConnection(connection);
		imageTable.setLogger(logger);
		imageTable.saveImage(udp);

		logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Completed", internal_method_name));

	}

	public boolean deviceHasData(String device_id) {
		
		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 
		
		try {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
			
			sharedPreferenceTable.setConnection(connection);
			sharedPreferenceTable.setLogger(logger);
			
			noteTable.setConnection(connection);
			noteTable.setLogger(logger);
			
			imageTable.setConnection(connection);
			imageTable.setLogger(logger);
			
	        if(sharedPreferenceTable.isEmpty(device_id) && noteTable.isEmpty(device_id) && imageTable.isEmpty(device_id)) {
	        	return false;
	        }
			return true;
		}
		finally {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Completed", internal_method_name));
		}
		
	}
	
	// get userdata
	public UserDataResponse getUserDataResponse(String device_id) {
		
		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 
		
		logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));		
		logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Set to retrieve values for device id", device_id));
		
		UserDataResponse udr = new UserDataResponse();
		udr.setDevice_id(device_id);
			
    	sharedPreferenceTable.setConnection(connection);
    	sharedPreferenceTable.setLogger(logger);
		udr = sharedPreferenceTable.getSharedPreferenceResponse(udr);
			
		noteTable.setConnection(connection);
		noteTable.setLogger(logger);
		udr = noteTable.getNoteResponse(udr);
			
		imageTable.setConnection(connection);
		imageTable.setLogger(logger);
		udr = imageTable.getImageResponse(udr);
	
		logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Completed", internal_method_name));
		
		return udr;
		
	}
	
}
