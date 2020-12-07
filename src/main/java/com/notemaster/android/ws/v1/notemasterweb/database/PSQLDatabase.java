package com.notemaster.android.ws.v1.notemasterweb.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;

public class PSQLDatabase extends Database {
	
	// connection parameters
	private static String jdbcUsername = "aspzclogozgxnj";
	private static String jdbcPassword = "33e1562e91d7f8f0a92b3e7a86f0f1350d7d2c58a81c34be12560ec7240e40af";
	private static String jdbcDatabase = "ddkvne5drekhag";
	private static String serverURL = "jdbc:postgresql://ec2-23-23-36-227.compute-1.amazonaws.com:5432/";
	private static String jdbcURL = "jdbc:postgresql://ec2-23-23-36-227.compute-1.amazonaws.com:5432/" + jdbcDatabase;
	
	// this will make this class a singleton. This means one connection each time this program is run
	private static PSQLDatabase instance = new PSQLDatabase();

	// private constructor makes it a singleton
	private PSQLDatabase() {

	}	
	
	// static method can be used without instantiating the class
	public static PSQLDatabase getInstance() {
		return instance;
	}
	
	public void connect() {

		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName();				

		Connection connection = null;

		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
			connection.setAutoCommit(false);
			System.out.println("Connected to database " + jdbcURL);
			this.connection = connection;

		} catch (Exception e) {
			
			try { 	    		  
				
				System.out.println("Error connecting to database " + e.getMessage());

				Class.forName("org.postgresql.Driver");
				// (re) connect to the server (not a specific database) to establish a connection with which we can create a database
				connection = DriverManager.getConnection(serverURL, jdbcUsername, jdbcPassword);
				// set auto-commit to TRUE in order to avoid creating a transaction block in which the database cannot be created
				connection.setAutoCommit(true);
				// we are now actually connected to the default database; 'postgres' itself. Use this connection to create the database    	          
				createDatabase(jdbcDatabase);
				// close the connection now. We need to reconnect to the just created database and reset auto-commit to false
				disconnect();				
				//try to connect again but then to the database that was just created. Old connection is already closed in createDatabase method 
				try {
					Class.forName("org.postgresql.Driver");
					connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
					connection.setAutoCommit(false);
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
	}
	
	private void createDatabase(String databasename) {
		
		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 	
		
		try {
			Statement statement = connection.createStatement();
			statement.execute("CREATE DATABASE " + databasename);
		} catch(Exception e) {
			System.out.println("Exception " + e.getMessage());
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}		
	}	

}
