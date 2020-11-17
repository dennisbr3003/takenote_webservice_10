package com.notemaster.android.ws.v1.notemasterweb.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;
import com.notemaster.android.ws.v1.notemasterweb.payload.Image;
import com.notemaster.android.ws.v1.notemasterweb.payload.UserDataPayload;
import com.notemaster.android.ws.v1.notemasterweb.response.UserDataResponse;

public class ImageTable implements ImageTableConstants {

	private Connection connection;
	public LoggingTable logger;
	private String device_id;  
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setLogger(LoggingTable logger) {
		this.logger = logger;
	}	
		
	public ImageTable() {
		super();
	}

	public boolean RecordExists(String id, String key) {

		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 
		
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connection.prepareStatement(
					String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s';",
							TABLE_PPI, PPI_ID, id, PPI_NAME, key ));
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {			
			logger.createErrorLogEntry(internal_method_name, e.getMessage());
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}	
		finally {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				logger.createErrorLogEntry(internal_method_name, e.getMessage());
				throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));			}
		}
	}
	
	public void saveImage(UserDataPayload udp) {

		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 
		
		clearImageDeviceData(udp);
		
		this.device_id = udp.getDevice_id();
		
		Image image = new Image();

		logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
		
		for(int i=0;i<udp.getPassPointImageListSize();i++) {
			image = udp.getImageListElement(i);			
			if(image!=null) {
				try {
					// Check if the record exists -->
					if(RecordExists(udp.getDevice_id(), image.getName())) {
						// If the record exists then update -->	
						updateRecord(image);
					}else {
						// If the record does not exist then create and insert -->
						insertRecord(image);
					}
				} catch(Exception e) {
					if(e.getMessage() != null) {
						logger.createErrorLogEntry(internal_method_name, e.getMessage());
						throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));		
					} else {
						logger.createErrorLogEntry(internal_method_name, "Insert or Update went wrong. Exception = null");
						throw new CustomException(String.format("%s|%s", "Insert or Update went wrong. Exception = null", internal_method_name));			
					}
				}
			}else {
				logger.createErrorLogEntry(internal_method_name, "Image array item object is empty or null");
				//throw new CustomException(String.format("%s|%s", "Image array item object is null", internal_method_name));

			}
		}		
		logger.createInfoLogEntry(internal_method_name, String.format("%s %s %s", "Processed", String.valueOf(udp.getPassPointImageListSize()), " image(s)"));				
	}	
	
    public boolean isEmpty(String device_id) {
		
		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 
				
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = '%s';", 
					                                                TABLE_PPI, PPI_ID, device_id));
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {				
				return false; // not empty
			} else {
				return true; // empty
			}
			
		} catch (SQLException e) {
			logger.createErrorLogEntry(internal_method_name, e.getMessage());
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}
		finally {
	 		try {
				preparedStatement.close();
				logger.createInfoLogEntry(internal_method_name, String.format("%s", "completed"));
			} catch (SQLException e) {			
				logger.createErrorLogEntry(internal_method_name, e.getMessage());
				throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
			}
		}
    }	
	
	public void insertRecord(Image image) {

		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 
		
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connection.prepareStatement(
					String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,?,?);", 
							TABLE_PPI, PPI_ID, PPI_IMAGE_ID, PPI_NAME, PPI_VALUE, PPI_DTYPE, PPI_IMG_CREATED, PPI_IMG_UPDATED));

			preparedStatement.setString(1, this.device_id);
			preparedStatement.setString(2, image.getId());
			preparedStatement.setString(3, image.getName());
			preparedStatement.setBytes(4, image.getFile());
			preparedStatement.setString(5, "byteArray");
			preparedStatement.setString(6, image.getCreated());
			preparedStatement.setString(7, image.getUpdated());			
			preparedStatement.executeUpdate();		

		} catch (Exception e) {
			logger.createErrorLogEntry(internal_method_name, e.getMessage());
			throw new CustomException(String.format("%s|%s", e.getMessage(), "insertRecord()"));
		}	
		finally {
			try {
				preparedStatement.close();
				connection.commit();
			} catch (SQLException e) {
				logger.createErrorLogEntry(internal_method_name, e.getMessage());
				throw new CustomException(String.format("%s|%s", e.getMessage(), "insertRecord()"));			}
		}		
	}	
	
	public void updateRecord(Image image) {

		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 
		
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connection.prepareStatement(
					String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = '%s' AND %s = '%s';", 
							TABLE_PPI, PPI_VALUE, PPI_UPDATED, PPI_IMG_UPDATED, PPI_ID, this.device_id, PPI_NAME, image.getName()));

			preparedStatement.setBytes(1, image.getFile());
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, image.getUpdated());
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			logger.createErrorLogEntry(internal_method_name, e.getMessage());			
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}	
		finally {
			try {
				preparedStatement.close();
				connection.commit();
			} catch (SQLException e) {
				logger.createErrorLogEntry(internal_method_name, e.getMessage());	
				throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));			}
		}				
	}	

	
	public UserDataResponse getImageResponse(UserDataResponse udr) {
		
		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 

		String device_id = udr.getDevice_id();
		
		PreparedStatement preparedStatement = null;
		
		logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
				
		int i=0;
		
		try {
			preparedStatement = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = '%s';", 
					                                                TABLE_PPI, PPI_ID, device_id));
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				udr.addImageArrayElement(rs.getString(PPI_IMAGE_ID), rs.getString(PPI_NAME), rs.getString(PPI_IMG_CREATED), rs.getString(PPI_IMG_UPDATED), rs.getBytes(PPI_VALUE));
		        i++;
			}
			preparedStatement.close();
		} catch (SQLException e) {
			logger.createErrorLogEntry(internal_method_name, e.getMessage());
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}
		
		logger.createInfoLogEntry(internal_method_name, String.format("%s %s %s", "Retreived", String.valueOf(i), "image(s)"));
		
		return udr;
		
	}
		
	
    private void clearImageDeviceData(UserDataPayload udp) {
		
    	String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 
		
    	Statement stmt = null; 
    	
		try {			
			stmt = connection.createStatement();
			stmt.execute(String.format("DELETE FROM %s WHERE %s = '%s';",
							TABLE_PPI, PPI_ID, udp.getDevice_id()));			

		} catch (SQLException e) {			
			logger.createErrorLogEntry(internal_method_name, e.getMessage());
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}	
		finally {
			try {
				stmt.close();
				logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Cleared image device data for id ", udp.getDevice_id()));
			} catch (SQLException e) {
				logger.createErrorLogEntry(internal_method_name, e.getMessage());
				throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));			}
		}
    	
    }		
	
}
