package com.notemaster.android.ws.v1.notemasterweb.database.tables;

import com.notemaster.android.ws.v1.notemasterweb.payload.ArrayItemObject;
import com.notemaster.android.ws.v1.notemasterweb.payload.UserDataPayload;
import com.notemaster.android.ws.v1.notemasterweb.resource.LoggerTakeNote;
import com.notemaster.android.ws.v1.notemasterweb.response.UserDataResponse;

public interface ISharedPreferenceTable {

	LoggerTakeNote getLogger();
	
	void setLogger(LoggerTakeNote logger);	
	
	UserDataResponse getSharedPreferenceResponse(UserDataResponse udr);

	boolean RecordExists(String id, String key);

	void insertRecord(ArrayItemObject aio);

	boolean isEmpty(String device_id);

	void updateRecord(ArrayItemObject aio);

	void saveSharedPreferences(UserDataPayload udp);

}