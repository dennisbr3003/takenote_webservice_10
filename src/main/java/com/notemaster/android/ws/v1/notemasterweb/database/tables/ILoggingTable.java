package com.notemaster.android.ws.v1.notemasterweb.database.tables;

public interface ILoggingTable {

	void setDevice_id(String device_id);
	void setTransaction_id(String transaction_id);
	void createLogEntry(String method, String message, String type);
	
}