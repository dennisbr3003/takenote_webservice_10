package com.notemaster.android.ws.v1.notemasterweb.database;

import java.sql.Connection;
import java.sql.DriverManager;

import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;

public class H2Database extends Database {
	// connection parameters
	//private static String jdbcURL = "jdbc:h2:file:/data/takenote10db;AUTO_SERVER=true"; //file and multiple connections 
	private static String jdbcURL = "jdbc:h2:mem:takenote10";         //(in memmory) 
	private static String jdbcUsername = "sa";
	private static String jdbcPassword = "";

	// this will make this class a singleton. This means one connection each time this program is run
	private static H2Database instance = new H2Database();

	// private constructor makes it a singleton
	private H2Database() {

	}	
	
	// static method can be used without instantiating the class
	public static H2Database getInstance() {
		return instance;
	}
	
	public void connect() {

		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName();				

		Connection connection = null;

		try {
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
			System.out.println("Connected to database " + jdbcURL);
			this.connection = connection;

		} catch (Exception e) {
			System.out.println("Error connecting to database " + e.getMessage());
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}		
	}
}
