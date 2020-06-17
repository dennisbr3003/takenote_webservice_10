package com.notemaster.android.ws.v1.notemasterweb.h2_database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;
import com.notemaster.android.ws.v1.notemasterweb.payload.ArrayItemObject;
import com.notemaster.android.ws.v1.notemasterweb.payload.UserDataPayload;
import com.notemaster.android.ws.v1.notemasterweb.response.SharedPreferenceResponse;

public class SharedPreferenceTable implements SharedPreferenceTableConstants {

	private Database h2db = new Database();
	private Connection connection;
	public LoggingTable dlt;

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public SharedPreferenceTable() {
		super();
		try {
			connection = h2db.getConnection(); // create once and use for all methods
		} catch(Exception e) {
			throw new CustomException(String.format("%s %s|%s", "Connection could not be established:", e.getMessage(), "Constructor: SharedPreference()"));
		}
	}

	public SharedPreferenceResponse getSharedPreferenceResponse(String device_id) {

		String internal_method_name = Thread.currentThread() 
				        .getStackTrace()[1] 
						.getMethodName(); 
		
		PreparedStatement preparedStatement = null;
		SharedPreferenceResponse spr = new SharedPreferenceResponse();	

		dlt.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
		
		// extra safety -->
		if(dlt == null) {
			dlt = new LoggingTable();
			dlt.setGlobal_id();
			dlt.setDevice_id(device_id);
		}
		
		int i=0;
	    
		spr.setDevice_id(device_id);

		try {
			preparedStatement = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = '%s';", 
					                                                TABLE_PRF, PRF_ID, device_id));
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				// System.out.println("Id " + rs.getString("ID") + " Name " + rs.getString("PREFERENCE") + " Value " + rs.getString("VALUE"));
                spr.addArrayElement(rs.getString(PRF_ID), rs.getString(PRF_NAME), rs.getString(PRF_VALUE), rs.getString(PRF_DTYPE));
                i++;
			}
			preparedStatement.close();
		} catch (SQLException e) {
			dlt.createErrorLogEntry(internal_method_name, e.getMessage());
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}
		dlt.createInfoLogEntry(internal_method_name, String.format("%s %s %s", "Retreived", String.valueOf(i), "shared preference values"));
		return spr;
	}
	
	public boolean RecordExists(String id, String key) {

		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 
		
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
			dlt.createErrorLogEntry(internal_method_name, e.getMessage());
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}	
		finally {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				dlt.createErrorLogEntry(internal_method_name, e.getMessage());
				throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));			}
		}
	}	

	public void insertRecord(ArrayItemObject aio) {

		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 
		
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connection.prepareStatement(
					String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?,?,?,?);", 
							TABLE_PRF, PRF_ID, PRF_NAME, PRF_VALUE, PRF_DTYPE));

			preparedStatement.setString(1, aio.getItemId());
			preparedStatement.setString(2, aio.getItemName());
			preparedStatement.setString(3, aio.getItemValue());
			preparedStatement.setString(4, aio.getItemDatatype());

			preparedStatement.executeUpdate();		

		} catch (Exception e) {
			dlt.createErrorLogEntry(internal_method_name, e.getMessage());
			throw new CustomException(String.format("%s|%s", e.getMessage(), "insertRecord()"));
		}	
		finally {
			try {
				preparedStatement.close();
				connection.commit();
			} catch (SQLException e) {
				dlt.createErrorLogEntry(internal_method_name, e.getMessage());
				throw new CustomException(String.format("%s|%s", e.getMessage(), "insertRecord()"));			}
		}		
	}	


	public void updateRecord(ArrayItemObject aio) {

		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 
		
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connection.prepareStatement(
					String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = '%s' AND %s = '%s';", 
							TABLE_PRF, PRF_VALUE, PRF_DTYPE, PRF_UPDATED, PRF_ID, aio.getItemId(), PRF_NAME, aio.getItemName()));

			preparedStatement.setString(1, aio.getItemValue());
			preparedStatement.setString(2, aio.getItemDatatype());
			preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

			preparedStatement.executeUpdate();

		} catch (Exception e) {
			dlt.createErrorLogEntry(internal_method_name, e.getMessage());			
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}	
		finally {
			try {
				preparedStatement.close();
				connection.commit();
			} catch (SQLException e) {
				dlt.createErrorLogEntry(internal_method_name, e.getMessage());	
				throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));			}
		}				
	}

	public void saveSharedPreferences(UserDataPayload udp) {

		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 
		
		// extra safety -->
		if(dlt == null) {
			dlt = new LoggingTable();
			dlt.setGlobal_id();
			dlt.setDevice_id(udp.getDevice_id());
		}

		clearSharedPreferenceDeviceData(udp);
		
		ArrayItemObject aio = new ArrayItemObject();

		dlt.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
		
		for(int i=0;i<udp.getShared_preferenceSize();i++) {
			aio = udp.getShared_preferenceElement(i);
			if(aio!=null) {
				try {
					// Check if the record exists -->
					if(RecordExists(aio.getItemId(), aio.getItemName())) {
						// If the record exists then update -->	
						updateRecord(aio);
					}else {
						// If the record does not exist then create and insert -->
						insertRecord(aio);
					}
				} catch(Exception e) {
					if(e.getMessage() != null) {
						dlt.createErrorLogEntry(internal_method_name, e.getMessage());
						throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));		
					} else {
						dlt.createErrorLogEntry(internal_method_name, "Insert or Update went wrong. Exception = null");
						throw new CustomException(String.format("%s|%s", "Insert or Update went wrong. Exception = null", internal_method_name));			
					}
				}
			}else {
				dlt.createErrorLogEntry(internal_method_name, "Sharedpreference array item object is null");
				throw new CustomException(String.format("%s|%s", "Sharedpreference array item object is null", internal_method_name));

			}
		}		
	    dlt.createInfoLogEntry(internal_method_name, String.format("%s %s %s", "Processed", String.valueOf(udp.getShared_preferenceSize()), "shared preference values"));				
	}
	
    private void clearSharedPreferenceDeviceData(UserDataPayload udp) {
		
    	String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 
		
    	Statement stmt = null; 
    	
		try {			
			stmt = connection.createStatement();
			stmt.execute(String.format("DELETE FROM %s WHERE %s = '%s';",
							TABLE_PRF, PRF_ID, udp.getDevice_id()));			

		} catch (SQLException e) {			
			dlt.createErrorLogEntry(internal_method_name, e.getMessage());
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}	
		finally {
			try {
				stmt.close();
				dlt.createInfoLogEntry(internal_method_name, String.format("%s %s", "Cleared shared preference device data for id ", udp.getDevice_id()));
			} catch (SQLException e) {
				dlt.createErrorLogEntry(internal_method_name, e.getMessage());
				throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));			}
		}
    	
    }
	
}


