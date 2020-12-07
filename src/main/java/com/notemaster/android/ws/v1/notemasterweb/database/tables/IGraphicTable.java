package com.notemaster.android.ws.v1.notemasterweb.database.tables;

import com.notemaster.android.ws.v1.notemasterweb.payload.Image;
import com.notemaster.android.ws.v1.notemasterweb.payload.UserDataPayload;
import com.notemaster.android.ws.v1.notemasterweb.resource.LoggerTakeNote;
import com.notemaster.android.ws.v1.notemasterweb.response.UserDataResponse;

public interface IGraphicTable {

	LoggerTakeNote getLogger();
	
	void setLogger(LoggerTakeNote logger);	
	
	boolean RecordExists(String id, String key);

	void saveImage(UserDataPayload udp);

	boolean isEmpty(String device_id);

	void insertRecord(Image image);

	void updateRecord(Image image);

	UserDataResponse getImageResponse(UserDataResponse udr);

}