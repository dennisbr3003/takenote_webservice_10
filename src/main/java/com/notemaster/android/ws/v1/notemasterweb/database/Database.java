package com.notemaster.android.ws.v1.notemasterweb.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;

public abstract class Database {
	
	public Connection connection;
	
	public void disconnect() throws SQLException {
		
		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName();				
		
		try {
			this.connection.close();
		} catch (Exception e) {
			if (e.getMessage() == null) {
				System.out.println("Error while trying to disconnect, database may not have been connected");
			} else {
				System.out.println("Error while trying to disconnect" + e.getMessage());
				throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
			}
		}		

		System.out.println("Disconnected from database");		
	}
	

	public Connection getConnection() {
		
		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName();

		if (connection != null) { 
			return this.connection;
		}
		else {
			try {
				connect();
				return this.connection;
			}catch(Exception e) {
				System.out.println("No connection possible at this point");
				throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
			}
		}		
	}

	protected abstract void connect();


	public boolean verifyConnection() {
		try {
			return this.connection.isValid(0);
		} catch (Exception e) {
			return false;
		}
	}
	
}
