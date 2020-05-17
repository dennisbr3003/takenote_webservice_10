package com.notemaster.android.ws.v1.notemasterweb.h2_database;

public interface DatabaseLoggingConstants {

	static final String TABLE_LOG = "LOG_LINES";
	static final String LOG_ID = "ID";
	static final String LOG_SEQ = "SEQUENCE";
	static final String LOG_GUID = "GLOBAL_ID";
	static final String LOG_CREATED = "CREATED";
	static final String LOG_TYPE = "TYPE";
	static final String LOG_METHOD = "METHOD";
	static final String LOG_VALUE = "VALUE";	
	static final String P_KEY_LOG = "IDX_LOG";
	
	static final String LOG_ERROR = "Error";
	static final String LOG_INFO = "Info";
	static final String LOG_WARNING = "Warning";
	static final String LOG_DEBUG = "Debug";
	
}