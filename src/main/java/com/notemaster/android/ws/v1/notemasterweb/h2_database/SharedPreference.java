package com.notemaster.android.ws.v1.notemasterweb.h2_database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SharedPreference {

	Database h2db = new Database();

	public void readTable() {
		Connection connection = h2db.getConnection();
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement("SELECT * FROM SHARED_PREFERENCE");
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				System.out.println("Id " + rs.getString("ID") + " Name " + rs.getString("PREFERENCE") + " Value " + rs.getString("VALUE"));
			}
			preparedStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean RecordExists() {
		Connection connection = h2db.getConnection();
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = '%s';","SHARED_PREFERENCE", "ID","1" ));
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL ERROR " + e.getLocalizedMessage());
			return false;
		}	
		finally {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void insertRecord() {

		if(RecordExists()) {
			return;
		}
		
		String sql = String.format("INSERT INTO %s (%s, %s, %s) VALUES (?,?,?);", "SHARED_PREFERENCE", "ID", "PREFERENCE", "VALUE" );

		try (Connection connection = h2db.getConnection();
				// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, "1");
			preparedStatement.setString(2, "backgroundcolor");
			preparedStatement.setString(3, "-1");


			System.out.println(preparedStatement);
			// Step 3: Execute the query or update query
			preparedStatement.executeUpdate();
			preparedStatement.close();
			connection.commit();
			
			readTable();

		} catch (SQLException e) {

			// print SQL exception information
			System.out.println("SQL ERROR " + e.getLocalizedMessage());
		}		
		

	}	
	
}
