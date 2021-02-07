package com.notemaster.android.ws.v1.notemasterweb.database;

import com.notemaster.android.ws.v1.notemasterweb.database.tables.IGraphicTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.INoteTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.ISharedPreferenceTable;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.IUserTable;
import com.notemaster.android.ws.v1.notemasterweb.exceptions.RequestException;
import com.notemaster.android.ws.v1.notemasterweb.payload.UserDataPayload;
import com.notemaster.android.ws.v1.notemasterweb.payload.WebUser;
import com.notemaster.android.ws.v1.notemasterweb.resource.LoggerTakeNote;
import com.notemaster.android.ws.v1.notemasterweb.response.UserDataResponse;

public class TypeIndependentResources {
	
	public WebUser getWebUser(WebUser webuser, IUserTable userTable, LoggerTakeNote logger) {
		
		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		
		if(logger != null) {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
		}
		
		userTable.setLogger(logger);	
		webuser = userTable.getWebUser(webuser);
		
		if(logger != null) {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Completed", internal_method_name));
		}
		return webuser;
	}

	public void unregisterWebUser(WebUser webuser, IUserTable userTable, LoggerTakeNote logger) {
		
		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		
		if(logger != null) {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
		}
		
		userTable.setLogger(logger);	
		userTable.deleteWebUser(webuser);
		
		if(logger != null) {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Completed", internal_method_name));
		}

	}	
	
	public void registerWebUser(WebUser webuser, IUserTable userTable, LoggerTakeNote logger) {
		
		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 	
		
		WebUser verificationUser=null;
		
		if(logger != null) {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
		}
		
		userTable.setLogger(logger);	
		// First try to get the user and check if the device_id's are the same -->
		verificationUser = userTable.getWebUser(webuser);
		if ((verificationUser != null) && (verificationUser.getDevice_id().equals(webuser.getDevice_id()))){
			// This cannot be allowed, this may be an entity trying to override the user password --> 		
			throw new RequestException("Credentials already exist for this device");
		}
		userTable.addWebUser(webuser);
		
		if(logger != null) {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Completed", internal_method_name));
		}		
	}	

	public void processUserDataPayload(UserDataPayload udp, LoggerTakeNote logger, 
			                           ISharedPreferenceTable sharedPreferenceTable,
			                           INoteTable noteTable,
			                           IGraphicTable imageTable) {

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

	public boolean deviceHasData(String device_id, LoggerTakeNote logger, 
 					             ISharedPreferenceTable sharedPreferenceTable,
					             INoteTable noteTable,
					             IGraphicTable imageTable) {
		
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
	
	public UserDataResponse getUserDataResponse(String device_id, LoggerTakeNote logger, 
									            ISharedPreferenceTable sharedPreferenceTable,
									            INoteTable noteTable,
									            IGraphicTable imageTable) {
		
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
