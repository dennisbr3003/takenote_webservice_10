package com.notemaster.android.ws.v1.notemasterweb.resource;

public interface ILoggerMethods {
	
	void createErrorLogEntry(String method, String message);

	void createInfoLogEntry(String method, String message);

	void createWarningLogEntry(String method, String message);

	void createDebugLogEntry(String method, String message);
}
