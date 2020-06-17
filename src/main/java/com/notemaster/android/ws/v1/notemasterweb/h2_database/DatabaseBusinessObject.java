package com.notemaster.android.ws.v1.notemasterweb.h2_database;

import com.notemaster.android.ws.v1.notemasterweb.payload.UserDataPayload;

public class DatabaseBusinessObject {
	
	public LoggingTable dlt;
	private SharedPreferenceTable sp = new SharedPreferenceTable();
	private NoteTable nt = new NoteTable(); 
	
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
		sp.saveSharedPreferences(udp);
		
		// Store the notes
		nt.saveNote(udp);
		
		// Store the passpoint image(s)
		
				
	}
	
}
