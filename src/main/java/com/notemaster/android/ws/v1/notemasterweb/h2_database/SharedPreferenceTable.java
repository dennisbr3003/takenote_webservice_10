package com.notemaster.android.ws.v1.notemasterweb.h2_database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;
import com.notemaster.android.ws.v1.notemasterweb.payload.ArrayItemObject;
import com.notemaster.android.ws.v1.notemasterweb.payload.SharedPreferencePayload;

public class SharedPreferenceTable implements SharedPreferenceConstants {

	private Database h2db = new Database();
	private Connection connection;

	public SharedPreferenceTable() {
		super();
		try {
			connection = h2db.getConnection(); // create once and use for all methods
		} catch(Exception e) {
			throw new CustomException(String.format("%s %s|%s", "Connection could not be established:", e.getMessage(), "Constructor: SharedPreference()"));
		}
	}

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


	public boolean RecordExists(String id, String key) {

		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connection.prepareStatement(
					String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s';",
							TABLE_PRF, PRF_ID, id, PRF_NAME, key ));
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			throw new CustomException(String.format("%s|%s", e.getMessage(), "RecordExists()"));
		}	
		finally {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				throw new CustomException(String.format("%s|%s", e.getMessage(), "RecordExists()"));			}
		}
	}	

	public void insertRecord(ArrayItemObject aio) {

		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connection.prepareStatement(
					String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?,?,?,?);", 
							TABLE_PRF, PRF_ID, PRF_NAME, PRF_VALUE, PRF_DTYPE));

			preparedStatement.setString(1, aio.getItem_id());
			preparedStatement.setString(2, aio.getItem_name());
			preparedStatement.setString(3, aio.getItem_value());
			preparedStatement.setString(4, aio.getItem_datatype());

			preparedStatement.executeUpdate();

		} catch (Exception e) {
			throw new CustomException(String.format("%s|%s", e.getMessage(), "insertRecord()"));
		}	
		finally {
			try {
				preparedStatement.close();
				connection.commit();
			} catch (SQLException e) {
				throw new CustomException(String.format("%s|%s", e.getMessage(), "insertRecord()"));			}
		}		
	}	


	public void updateRecord(ArrayItemObject aio) {
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connection.prepareStatement(
					String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = '%s' AND %s = '%s';", 
							TABLE_PRF, PRF_VALUE, PRF_DTYPE, PRF_UPDATED, PRF_ID, aio.getItem_id(), PRF_NAME, aio.getItem_name()));

			preparedStatement.setString(1, aio.getItem_value());
			preparedStatement.setString(2, aio.getItem_datatype());
			preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

			preparedStatement.executeUpdate();

		} catch (Exception e) {
			throw new CustomException(String.format("%s|%s", e.getMessage(), "updateRecord()"));
		}	
		finally {
			try {
				preparedStatement.close();
				connection.commit();
			} catch (SQLException e) {
				throw new CustomException(String.format("%s|%s", e.getMessage(), "updateRecord()"));			}
		}				
	}

	public void processSharedPreferencePayload(SharedPreferencePayload spp) {

		ArrayItemObject aio = new ArrayItemObject();

		for(int i=0;i<spp.getShared_preferenceSize();i++) {
			aio = spp.getShared_preferenceElement(i);
			if(aio!=null) {
				try {
					// Check if the record exists -->
					if(RecordExists(aio.getItem_id(), aio.getItem_name())) {
						// If the record exists then update -->	
						updateRecord(aio);
					}else {
						// If the record does not exist then create and insert -->
						insertRecord(aio);
					}
				} catch(Exception e) {
					if(e.getMessage() != null) {
						throw new CustomException(String.format("%s|%s", e.getMessage(), "processSharedPreferencePayload()"));		
					} else {
						throw new CustomException(String.format("%s|%s", "Insert or Update went wrong. Exception = null", "processSharedPreferencePayload()"));			
					}
				}
			}else {
				throw new CustomException(String.format("%s|%s", "Sharedpreference array item object is null", "processSharedPreferencePayload()"));

			}
		}
	}

}


