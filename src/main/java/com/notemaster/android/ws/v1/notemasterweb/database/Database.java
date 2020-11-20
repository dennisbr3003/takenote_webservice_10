package com.notemaster.android.ws.v1.notemasterweb.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;

public class Database implements SharedPreferenceTableConstants, 
                                 LoggingTableConstants, 
                                 NoteTableConstants, 
                                 ImageTableConstants {

	
/* local
	private static String serverURL = "jdbc:postgresql://localhost:5432/";
	private static String jdbcUsername = "postgres";
	private static String jdbcPassword = "s$cret";
	private static String jdbcDatabase = "takenote10";
	private static String jdbcURL = "jdbc:postgresql://localhost:5432/" + jdbcDatabase;
*/
//Heroku	
	private static String serverURL = "jdbc:postgresql://ec2-23-23-36-227.compute-1.amazonaws.com:5432/";
	private static String jdbcUsername = "aspzclogozgxnj";
	private static String jdbcPassword = "33e1562e91d7f8f0a92b3e7a86f0f1350d7d2c58a81c34be12560ec7240e40af";
	private static String jdbcDatabase = "ddkvne5drekhag";
	private static String jdbcURL = "jdbc:postgresql://ec2-23-23-36-227.compute-1.amazonaws.com:5432/" + jdbcDatabase;
	
	public Connection getConnection() {
		
		String internal_method_name = Thread.currentThread() 
				.getStackTrace()[1] 
				.getMethodName(); 			


		Connection connection = null;

		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager
					.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
			connection.setAutoCommit(false);

		} catch (Exception e) {
			try { 	    		  
				
				System.out.println("Error connecting to database " + e.getMessage());

				Class.forName("org.postgresql.Driver");
				// (re) connect to the server (not a specific database) to establish a connection with which we can create a database
				connection = DriverManager.getConnection(serverURL, jdbcUsername, jdbcPassword);
				// set auto-commit to TRUE in order to avoid creating a transaction block in which the database cannot be created
				connection.setAutoCommit(true);
				// we are now actually connected to the default database; 'postgres' itself. Use this connection to create the database    	          
				createDatabase(connection, jdbcDatabase);
				
				//try to connect again but then to the database that was just created. Old connection is already closed in createDatabase method 
				try {
					Class.forName("org.postgresql.Driver");
					connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
					connection.setAutoCommit(false);
					System.out.println("Connection to database closed? " + String.valueOf(connection.isClosed()));
				} catch (Exception e1) {
					// this is no good. We cannot connect to the new database or something else went wrong
					System.out.println("it's no use because " + e1.getMessage());
					throw new CustomException(String.format("%s|%s", e1.getMessage(), internal_method_name));
				}

				System.out.println("database could be created, look for" + jdbcDatabase);
				
			} catch (Exception e2) {
				System.out.println("Something went wrong creating or connecting to the database " + e2.getMessage());
				throw new CustomException(String.format("%s|%s", e2.getMessage(), internal_method_name));
			}
		}
		// return the connection to the created and/or correct database  
		return connection;
	}	 

	private void createDatabase(Connection connection, String databasename) {
		
		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 	
		
		try {
			Statement statement = connection.createStatement();
			statement.execute("CREATE DATABASE " + databasename);
			// close the connection now. We need to reconnect to the just created database and reset auto-commit to false
			closeConnection(connection);
		} catch(Exception e) {
			System.out.println("Exception " + e.getMessage());
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}		
	}
	
	public boolean testConnection() {

		boolean connectSuccess;
		
		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 	
		
		try {
			Connection testConnection = getConnection();
			connectSuccess = (testConnection!=null);
			closeConnection(testConnection);
			return(connectSuccess);
		} catch(Exception e) {
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}
	}

	public void closeConnection(Connection c) {
		
		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 			
		
		try {
			c.close();
		} catch (SQLException e) {
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}
	}
	
	private String getTableDefinition(String tableName) {

		//PostgreSQL
		switch(tableName) {
		case TABLE_LOG:
			return String.format("CREATE TABLE IF NOT EXISTS %s (%s VARCHAR(100) NOT NULL, " + 
					"%s SERIAL, %s VARCHAR(255) NOT NULL, "+ 
					"%s TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, %s VARCHAR(10) NOT NULL, " +
					"%s VARCHAR(100), %s VARCHAR(255), CONSTRAINT %s PRIMARY KEY (%s,%s));", 
					TABLE_LOG, LOG_ID, LOG_SEQ, LOG_GUID, LOG_CREATED, LOG_TYPE, LOG_METHOD, LOG_VALUE, 
					P_KEY_LOG, LOG_ID, LOG_SEQ);
		case TABLE_PRF:
			return String.format("CREATE TABLE IF NOT EXISTS %s (%s VARCHAR(100) NOT NULL, " + 
					"%s VARCHAR(40) NOT NULL, %s VARCHAR(40) NOT NULL, %s VARCHAR(40), %s TIMESTAMP NOT " + 
					"NULL DEFAULT CURRENT_TIMESTAMP, %s TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " + 
					"CONSTRAINT %s PRIMARY KEY (%s,%s));", 
					TABLE_PRF, PRF_ID, PRF_NAME, PRF_VALUE, PRF_DTYPE, PRF_CREATED, PRF_UPDATED, 
					P_KEY_PRF, PRF_ID, PRF_NAME);
		case TABLE_NTS:
			return String.format("CREATE TABLE IF NOT EXISTS %s (%s VARCHAR(100) NOT NULL, %s VARCHAR(50) NOT NULL, " + 
					"%s VARCHAR(40) NOT NULL, %s BYTEA, %s VARCHAR(50), %s VARCHAR(30), %s VARCHAR(30), %s TIMESTAMP NOT " + 
					"NULL DEFAULT CURRENT_TIMESTAMP, %s TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " + 
					"CONSTRAINT %s PRIMARY KEY (%s,%s));", 
					TABLE_NTS, NTS_ID, NTS_NOTE_ID, NTS_NAME, NTS_VALUE, NTS_DTYPE, NTS_NOTE_CREATED, NTS_NOTE_UPDATED, NTS_CREATED, NTS_UPDATED, 
					P_KEY_NTS, NTS_ID, NTS_NAME);
		case TABLE_PPI:
			return String.format("CREATE TABLE IF NOT EXISTS %s (%s VARCHAR(100) NOT NULL, %s VARCHAR(50) NOT NULL, " + 
					"%s VARCHAR(40) NOT NULL, %s BYTEA, %s VARCHAR(50), %s VARCHAR(30), %s VARCHAR(30), %s TIMESTAMP NOT " + 
					"NULL DEFAULT CURRENT_TIMESTAMP, %s TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " + 
					"CONSTRAINT %s PRIMARY KEY (%s,%s));", 
					TABLE_PPI, PPI_ID, PPI_IMAGE_ID, PPI_NAME, PPI_VALUE, PPI_DTYPE, PPI_IMG_CREATED, PPI_IMG_UPDATED, PPI_CREATED, PPI_UPDATED, 
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
			closeConnection(connection);
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


