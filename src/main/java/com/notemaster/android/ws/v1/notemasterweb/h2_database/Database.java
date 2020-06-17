package com.notemaster.android.ws.v1.notemasterweb.h2_database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;

public class Database implements SharedPreferenceTableConstants, 
                                 LoggingTableConstants, 
                                 NoteTableConstants, 
                                 ImageTableConstants {

	private static String jdbcURL = "jdbc:h2:file:./data/notemaster;AUTO_SERVER=true";
	private static String jdbcUsername = "sa";
	private static String jdbcPassword = "";
	
	public Connection getConnection() {
		
		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 			
		
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));			
		}
		return connection;
	}	 

	public boolean testConnection() {

		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 	
		
		try {
			Connection testConnection = getConnection();
			return(testConnection!=null);
		} catch(Exception e) {
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}
	}

	
	private String getTableDefinition(String tableName) {

		switch(tableName) {
		case TABLE_LOG:
			return String.format("CREATE TABLE IF NOT EXISTS %s (%s VARCHAR(100) NOT NULL, " + 
					"%s INT NOT NULL AUTO_INCREMENT, %s VARCHAR(255) NOT NULL, "+ 
					"%s DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, %s VARCHAR(10) NOT NULL, " +
					"%s VARCHAR(100), %s VARCHAR(255), CONSTRAINT %s PRIMARY KEY (%s,%s));", 
					TABLE_LOG, LOG_ID, LOG_SEQ, LOG_GUID, LOG_CREATED, LOG_TYPE, LOG_METHOD, LOG_VALUE, 
					P_KEY_LOG, LOG_ID, LOG_SEQ);
		case TABLE_PRF:
			return String.format("CREATE TABLE IF NOT EXISTS %s (%s VARCHAR(100) NOT NULL, " + 
					"%s VARCHAR(40) NOT NULL, %s VARCHAR(40) NOT NULL, %s VARCHAR(40), %s DATETIME NOT " + 
					"NULL DEFAULT CURRENT_TIMESTAMP, %s DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " + 
					"CONSTRAINT %s PRIMARY KEY (%s,%s));", 
					TABLE_PRF, PRF_ID, PRF_NAME, PRF_VALUE, PRF_DTYPE, PRF_CREATED, PRF_UPDATED, 
					P_KEY_PRF, PRF_ID, PRF_NAME);
		case TABLE_NTS:
			return String.format("CREATE TABLE IF NOT EXISTS %s (%s VARCHAR(100) NOT NULL, %s VARCHAR(50) NOT NULL, " + 
					"%s VARCHAR(40) NOT NULL, %s BLOB, %s VARCHAR(50), %s DATETIME NOT " + 
					"NULL DEFAULT CURRENT_TIMESTAMP, %s DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " + 
					"CONSTRAINT %s PRIMARY KEY (%s,%s));", 
					TABLE_NTS, NTS_ID, NTS_NOTE_ID, NTS_NAME, NTS_VALUE, NTS_DTYPE, NTS_CREATED, NTS_UPDATED, 
					P_KEY_NTS, NTS_ID, NTS_NAME);
		case TABLE_PPI:
			return String.format("CREATE TABLE IF NOT EXISTS %s (%s VARCHAR(100) NOT NULL, %s VARCHAR(50) NOT NULL, " + 
					"%s VARCHAR(40) NOT NULL, %s BLOB, %s VARCHAR(50), %s DATETIME NOT " + 
					"NULL DEFAULT CURRENT_TIMESTAMP, %s DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " + 
					"CONSTRAINT %s PRIMARY KEY (%s,%s));", 
					TABLE_PPI, PPI_ID, PPI_IMAGE_ID, PPI_NAME, PPI_VALUE, PPI_DTYPE, PPI_CREATED, PPI_UPDATED, 
					P_KEY_PPI, PPI_ID, PPI_NAME);			
		default:
			return "";
		}

	}
	
	private boolean createTable(String tableName) {

		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 	
		
		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			statement.execute(getTableDefinition(tableName));
			connection.commit();						
			return true;
		} catch(Exception e) {
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}
		
	}
	

	public boolean initDatabase() {

		try {
			if(testConnection()) {
				try {
				    createTable(TABLE_PRF);
				    createTable(TABLE_LOG);
				    createTable(TABLE_NTS);
				    createTable(TABLE_PPI);
				    return true;
				} catch(Exception e) {
					System.out.println(e.getMessage());
					return false;
				}
			}else {
				return false;
			}
		}catch(Exception e) {
			return false;
		}

	}


}


