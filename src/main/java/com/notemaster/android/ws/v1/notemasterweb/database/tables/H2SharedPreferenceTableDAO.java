package com.notemaster.android.ws.v1.notemasterweb.database.tables;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.notemaster.android.ws.v1.notemasterweb.database.H2Database;
import com.notemaster.android.ws.v1.notemasterweb.database.constants.SharedPreferenceTableConstants;
import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;
import com.notemaster.android.ws.v1.notemasterweb.payload.ArrayItemObject;
import com.notemaster.android.ws.v1.notemasterweb.payload.UserDataPayload;
import com.notemaster.android.ws.v1.notemasterweb.resource.LoggerTakeNote;
import com.notemaster.android.ws.v1.notemasterweb.response.UserDataResponse;

public class H2SharedPreferenceTableDAO implements SharedPreferenceTableConstants, ISharedPreferenceTable{

	private LoggerTakeNote logger;

	public LoggerTakeNote getLogger() {
		return logger;
	}

	public void setLogger(LoggerTakeNote logger) {
		this.logger = logger;
	}		
	
	public H2SharedPreferenceTableDAO() {
		super();
	}

	@Override
	public UserDataResponse getSharedPreferenceResponse(UserDataResponse udr) {
		
		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 

		String device_id = udr.getDevice_id();
		
		PreparedStatement preparedStatement = null;

		if(logger != null) {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
		}						
		
		int i=0;

		try {
			preparedStatement = H2Database.getInstance().getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s = '%s';", 
					                                                TABLE_PRF, PRF_ID, device_id));
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				// System.out.println("Id " + rs.getString("ID") + " Name " + rs.getString("PREFERENCE") + " Value " + rs.getString("VALUE"));
                udr.addSharedPreferenceArrayElement(rs.getString(PRF_ID), rs.getString(PRF_NAME), rs.getString(PRF_VALUE), rs.getString(PRF_DTYPE));
                i++;
			}
			preparedStatement.close();
		} catch (SQLException e) {
			if(logger != null) {
				logger.createErrorLogEntry(internal_method_name, e.getMessage());
			}
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}
		if(logger != null) {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s %s", "Retreived", String.valueOf(i), "shared preference values"));
		}
		return udr;
		
	}
	
	@Override
	public boolean RecordExists(String id, String key) {

		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = H2Database.getInstance().getConnection().prepareStatement(
					String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s';",
							TABLE_PRF, PRF_ID, id, PRF_NAME, key ));
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {	
			if(logger != null) {
				logger.createErrorLogEntry(internal_method_name, e.getMessage());
			}
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}	
		finally {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				if(logger != null) {
					logger.createErrorLogEntry(internal_method_name, e.getMessage());
				}
				throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));			}
		}
	}	

	@Override
	public void insertRecord(ArrayItemObject aio) {

		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = H2Database.getInstance().getConnection().prepareStatement(
					String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?,?,?,?);", 
							TABLE_PRF, PRF_ID, PRF_NAME, PRF_VALUE, PRF_DTYPE));

			preparedStatement.setString(1, aio.getItemId());
			preparedStatement.setString(2, aio.getItemName());
			preparedStatement.setString(3, aio.getItemValue());
			preparedStatement.setString(4, aio.getItemDatatype());

			preparedStatement.executeUpdate();		

		} catch (Exception e) {
			if(logger != null) {
				logger.createErrorLogEntry(internal_method_name, e.getMessage());
			}
			throw new CustomException(String.format("%s|%s", e.getMessage(), "insertRecord()"));
		}	
		finally {
			try {
				preparedStatement.close();
				H2Database.getInstance().getConnection().commit();
			} catch (SQLException e) {
				if(logger != null) {
					logger.createErrorLogEntry(internal_method_name, e.getMessage());
				}
				throw new CustomException(String.format("%s|%s", e.getMessage(), "insertRecord()"));			}
		}		
	}	

    @Override
	public boolean isEmpty(String device_id) {
		
		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = H2Database.getInstance().getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s = '%s';", 
					                                                TABLE_PRF, PRF_ID, device_id));
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {				
				return false; // not empty
			} else {
				return true; // empty
			}
			
		} catch (SQLException e) {
			if(logger != null) {
				logger.createErrorLogEntry(internal_method_name, e.getMessage());
			}
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}
		finally {
	 		try {
				preparedStatement.close();
				if(logger != null) {
					logger.createInfoLogEntry(internal_method_name, String.format("%s", "completed"));
				}
			} catch (SQLException e) {	
				if(logger != null) {
					logger.createErrorLogEntry(internal_method_name, e.getMessage());
				}
				throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
			}
		}
    }		
    
	@Override
	public void updateRecord(ArrayItemObject aio) {

		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = H2Database.getInstance().getConnection().prepareStatement(
					String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = '%s' AND %s = '%s';", 
							TABLE_PRF, PRF_VALUE, PRF_DTYPE, PRF_UPDATED, PRF_ID, aio.getItemId(), PRF_NAME, aio.getItemName()));

			preparedStatement.setString(1, aio.getItemValue());
			preparedStatement.setString(2, aio.getItemDatatype());
			preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

			preparedStatement.executeUpdate();

		} catch (Exception e) {
			if(logger != null) {
				logger.createErrorLogEntry(internal_method_name, e.getMessage());
			}
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}	
		finally {
			try {
				preparedStatement.close();
				H2Database.getInstance().getConnection().commit();
			} catch (SQLException e) {
				if(logger != null) {
					logger.createErrorLogEntry(internal_method_name, e.getMessage());
				}
				throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));			}
		}				
	}

	@Override
	public void saveSharedPreferences(UserDataPayload udp) {

		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		
		clearSharedPreferenceDeviceData(udp);
		
		ArrayItemObject aio = new ArrayItemObject();
		if(logger != null) {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
		}
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
						if(logger != null) {
							logger.createErrorLogEntry(internal_method_name, e.getMessage());
						}
						throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));		
					} else {
						if(logger != null) {
							logger.createErrorLogEntry(internal_method_name, "Insert or Update went wrong. Exception = null");
						}
						throw new CustomException(String.format("%s|%s", "Insert or Update went wrong. Exception = null", internal_method_name));			
					}
				}
			}else {
				if(logger != null) {
					logger.createErrorLogEntry(internal_method_name, "Sharedpreference array item object is null");
				}
				throw new CustomException(String.format("%s|%s", "Sharedpreference array item object is null", internal_method_name));

			}
		}
		if(logger != null) {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s %s", "Processed", String.valueOf(udp.getShared_preferenceSize()), "shared preference values"));
		}
	}
	
    private void clearSharedPreferenceDeviceData(UserDataPayload udp) {
		
    	String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		
    	Statement stmt = null; 
    	
		try {			
			stmt = H2Database.getInstance().getConnection().createStatement();
			stmt.execute(String.format("DELETE FROM %s WHERE %s = '%s';",
							TABLE_PRF, PRF_ID, udp.getDevice_id()));			

		} catch (SQLException e) {
			if(logger != null) {
				logger.createErrorLogEntry(internal_method_name, e.getMessage());
			}
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}	
		finally {
			try {
				stmt.close();
				if(logger != null) {
					logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Cleared shared preference device data for id ", udp.getDevice_id()));
				}
			} catch (SQLException e) {
				if(logger != null) {
					logger.createErrorLogEntry(internal_method_name, e.getMessage());
				}
				throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));			
			}
		}    	
    }

}
