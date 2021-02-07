package com.notemaster.android.ws.v1.notemasterweb.database;

import com.notemaster.android.ws.v1.notemasterweb.payload.UserDataPayload;
import com.notemaster.android.ws.v1.notemasterweb.payload.WebUser;
import com.notemaster.android.ws.v1.notemasterweb.resource.LoggerTakeNote;
import com.notemaster.android.ws.v1.notemasterweb.response.UserDataResponse;

public interface IDatabaseBusinessObject {

	boolean initDatabaseTables();

	LoggerTakeNote getLogger();

	void setLogger(LoggerTakeNote logger);
	 
	void processUserDataPayload(UserDataPayload udp);

	boolean deviceHasData(String device_id);

	UserDataResponse getUserDataResponse(String device_id);
	
	WebUser getWebUser(WebUser webuser);
	
	void registerWebUser(WebUser webuser);
	
	void unregisterWebUser(WebUser webuser);	

}