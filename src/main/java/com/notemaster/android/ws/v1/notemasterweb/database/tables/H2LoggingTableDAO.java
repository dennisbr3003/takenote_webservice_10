package com.notemaster.android.ws.v1.notemasterweb.database.tables;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.notemaster.android.ws.v1.notemasterweb.database.H2Database;
import com.notemaster.android.ws.v1.notemasterweb.database.constants.LoggingTableConstants;
import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;

public class H2LoggingTableDAO implements LoggingTableConstants, ILoggingTable{
	
	private String device_id;
	private String transaction_id;
	
	public H2LoggingTableDAO() {
		super();
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}	
	
	public void createLogEntry(String method, String message, String type) {

		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 									
		
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = H2Database.getInstance().getConnection().prepareStatement(
					String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?,?,?,?,?);", 
							TABLE_LOG, LOG_ID, LOG_GUID, LOG_METHOD, LOG_VALUE, LOG_TYPE));

			preparedStatement.setString(1, device_id);
			preparedStatement.setString(2, transaction_id);
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
				H2Database.getInstance().getConnection().commit();
			} catch (SQLException e) {
				throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));			}
		}				
	}

}
