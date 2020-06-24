package com.notemaster.android.ws.v1.notemasterweb.h2_database;

import com.notemaster.android.ws.v1.notemasterweb.payload.UserDataPayload;
import com.notemaster.android.ws.v1.notemasterweb.response.UserDataResponse;

public class DatabaseBusinessObject {
	
	public LoggingTable dlt;
	private SharedPreferenceTable sharedPreferenceTable = new SharedPreferenceTable();
	private NoteTable noteTable = new NoteTable(); 
	private ImageTable imageTable = new ImageTable();
	
	public DatabaseBusinessObject() {
		
	}
	
	public void processUserDataPayload(UserDataPayload udp) {

		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 
		
		// extra safety -->
		if(dlt == null) {
			dlt = new LoggingTable();
			dlt.setGlobal_id();
			dlt.setDevice_id(udp.getDevice_id());
		}
				
		dlt.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
		dlt.createInfoLogEntry(internal_method_name, String.format("%s %s", "Number of elements in array shared preferences", String.valueOf(udp.getShared_preferenceSize())));
		dlt.createInfoLogEntry(internal_method_name, String.format("%s %s", "Number of elements in array notes", String.valueOf(udp.getNoteListSize())));
		dlt.createInfoLogEntry(internal_method_name, String.format("%s %s", "Number of elements in array passpoint image (s)", String.valueOf(udp.getPassPointImageListSize())));
		
		// first store the shared preferences
		sharedPreferenceTable.saveSharedPreferences(udp);
		
		// Store the notes
		noteTable.saveNote(udp);
		
		// Store the passpoint image(s)
		imageTable.saveImage(udp);
		
		dlt.createInfoLogEntry(internal_method_name, String.format("%s %s", "Completed", internal_method_name));
				
	}
	
	public boolean deviceHasData(String device_id) {
		
		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 
		
		// extra safety -->
		if(dlt == null) {
			dlt = new LoggingTable();
			dlt.setGlobal_id();
			dlt.setDevice_id(device_id);
		}
		
		try {
			dlt.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
	
	        if(sharedPreferenceTable.isEmpty(device_id) && noteTable.isEmpty(device_id) && imageTable.isEmpty(device_id)) {
	        	return false;
	        }
			return true;
		}
		finally {
			dlt.createInfoLogEntry(internal_method_name, String.format("%s %s", "Completed", internal_method_name));
		}
		
	}
	
	public UserDataResponse getUserDataResponse(String device_id) {
		
		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 
		
		// extra safety -->
		if(dlt == null) {
			dlt = new LoggingTable();
			dlt.setGlobal_id();
			dlt.setDevice_id(device_id);
		}
		
		dlt.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
		
		dlt.createInfoLogEntry(internal_method_name, String.format("%s %s", "Set to retrieve values for device id", device_id));
		
		UserDataResponse udr = new UserDataResponse();
		udr.setDevice_id(device_id);
		
		udr = sharedPreferenceTable.getSharedPreferenceResponse(udr);
		
		udr = noteTable.getNoteResponse(udr);
		
		udr = imageTable.getImageResponse(udr);
		
		dlt.createInfoLogEntry(internal_method_name, String.format("%s %s", "Completed", internal_method_name));
		
		return udr;
		
	}
	
}
