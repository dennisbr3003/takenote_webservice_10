package com.notemaster.android.ws.v1.notemasterweb.h2_database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;

public class Database implements SharedPreferenceConstants {

	private static String jdbcURL = "jdbc:h2:file:./data/notemaster;AUTO_SERVER=true";
	private static String jdbcUsername = "sa";
	private static String jdbcPassword = "";
	
	public Connection getConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			throw new CustomException(String.format("%s|%s", e.getMessage(), "getConnection()"));			
		}
		return connection;
	}	 

	public boolean testConnection() {
		try {
			Connection testConnection = getConnection();
			return(testConnection!=null);
		} catch(Exception e) {
			throw new CustomException(String.format("%s|%s", e.getMessage(), "testConnection()"));
		}
	}


	public boolean createTable(String table_name) {

		switch(table_name) {
		case TABLE_PRF:
			if(createTableSharedPreference()) {
				return true;
			};
			return false;
		default:
			throw new CustomException(String.format("%s|%s", "No valid tablename was passed to the createTable method", "createTable()"));
		}

	}

	private boolean createTableSharedPreference() {

		String SQL = String.format("CREATE TABLE IF NOT EXISTS %s (%s VARCHAR(100) NOT NULL, " + 
		                           "%s VARCHAR(40) PRIMARY KEY NOT NULL, %s VARCHAR(40) NOT NULL, %s VARCHAR(40), %s DATETIME NOT " + 
				                   "NULL DEFAULT CURRENT_TIMESTAMP, %s DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP);", 
				                   TABLE_PRF, PRF_ID, PRF_NAME, PRF_VALUE, PRF_DTYPE, PRF_CREATED, PRF_UPDATED);

		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			statement.execute(SQL);
			connection.commit();
			return true;
		} catch(SQLException e) {
			throw new CustomException(String.format("%s|%s", e.getMessage(), "createTableSharedPreference()"));
		}
	}

	public boolean initDatabase() {

		try {
			if(testConnection()) {
				return createTable(TABLE_PRF);
			}else {
				return false;
			}
		}catch(Exception e) {
			return false;
		}

	}


}


