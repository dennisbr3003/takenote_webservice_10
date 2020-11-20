package com.notemaster.android.ws.v1.notemasterweb.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import com.notemaster.android.ws.v1.notemasterweb.database.constants.LoggingTableConstants;
import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;

public class LoggingTable implements LoggingTableConstants {

	private String global_id;
	private String device_id;
	private Connection connection;
	
	public LoggingTable(String device_id) {
		super();
		this.device_id = device_id;
	}	

	public LoggingTable() {
		super();
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}	
	
	public void setGlobal_id() {
		this.global_id = UUID.randomUUID().toString();	
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;	
	}
	
	
	public void createErrorLogEntry(String method, String message) {
		final String type = LOG_ERROR;
		createLogEntry(method, message, type);
	}
	
	public void createInfoLogEntry(String method, String message) {
		final String type = LOG_INFO;
		createLogEntry(method, message, type);
	}

	public void createWarningLogEntry(String method, String message) {
		final String type = LOG_WARNING;
		createLogEntry(method, message, type);
	}

	public void createDebugLogEntry(String method, String message) {
		final String type = LOG_DEBUG;
		createLogEntry(method, message, type);
	}
	
	private void createLogEntry(String method, String message, String type) {

		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 									
		
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connection.prepareStatement(
					String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?,?,?,?,?);", 
							TABLE_LOG, LOG_ID, LOG_GUID, LOG_METHOD, LOG_VALUE, LOG_TYPE));

			preparedStatement.setString(1, this.device_id);
			preparedStatement.setString(2, this.global_id);
			preparedStatement.setString(3, method);
			preparedStatement.setString(4, message);
			preparedStatement.setString(5, type);

			preparedStatement.executeUpdate();

		} catch (Exception e) {			
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}	
		finally {
			try {
				preparedStatement.close();
				connection.commit();
			} catch (SQLException e) {
				throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));			}
		}				
	}

}
