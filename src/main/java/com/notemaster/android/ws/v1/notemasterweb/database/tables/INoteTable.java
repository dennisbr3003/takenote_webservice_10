package com.notemaster.android.ws.v1.notemasterweb.database.tables;

import com.notemaster.android.ws.v1.notemasterweb.payload.Note;
import com.notemaster.android.ws.v1.notemasterweb.payload.UserDataPayload;
import com.notemaster.android.ws.v1.notemasterweb.resource.LoggerTakeNote;
import com.notemaster.android.ws.v1.notemasterweb.response.UserDataResponse;

public interface INoteTable {

	LoggerTakeNote getLogger();
	
	void setLogger(LoggerTakeNote logger);		
	
	boolean RecordExists(String id, String key);

	void saveNote(UserDataPayload udp);

	void insertRecord(Note note);

	void updateRecord(Note note);

	UserDataResponse getNoteResponse(UserDataResponse udr);

	boolean isEmpty(String device_id);

}