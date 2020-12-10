package com.notemaster.android.ws.v1.notemasterweb.resource;

import com.notemaster.android.ws.v1.notemasterweb.database.DAOFactory;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.ILoggingTable;

public abstract class Logger implements ILoggerMethods,ILoggerConstants {

	private DAOFactory factory = DAOFactory.getFactory(DAOFactory.PSQL);
	public ILoggingTable loggingTable = factory.getLoggingTable();
//test
	public Logger() {
		
	}	

	@Override
	public void createErrorLogEntry(String method, String message) {
		loggingTable.createLogEntry(method, message, LOG_ERROR);		
	}

	@Override
	public void createInfoLogEntry(String method, String message) {
		loggingTable.createLogEntry(method, message, LOG_INFO);		
	}

	@Override
	public void createWarningLogEntry(String method, String message) {
		loggingTable.createLogEntry(method, message, LOG_WARNING);		
	}

	@Override
	public void createDebugLogEntry(String method, String message) {
		loggingTable.createLogEntry(method, message, LOG_DEBUG);		
	}

	
}
