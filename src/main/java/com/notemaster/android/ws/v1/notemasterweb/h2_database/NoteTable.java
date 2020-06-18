package com.notemaster.android.ws.v1.notemasterweb.h2_database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.UUID;

import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;
import com.notemaster.android.ws.v1.notemasterweb.payload.Note;
import com.notemaster.android.ws.v1.notemasterweb.payload.UserDataPayload;
import com.notemaster.android.ws.v1.notemasterweb.response.UserDataResponse;

public class NoteTable implements NoteTableConstants {

	private Database h2db = new Database();
	private Connection connection;
	public LoggingTable dlt;
	private String device_id; 

	UUID uuid = UUID.randomUUID(); 
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public NoteTable() {
		super();
		try {
			connection = h2db.getConnection(); // create once and use for all methods
		} catch(Exception e) {
			throw new CustomException(String.format("%s %s|%s", "Connection could not be established:", e.getMessage(), "Constructor: DatabaseNoteTable()"));
		}
	}

	public boolean RecordExists(String id, String key) {

		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 
		
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connection.prepareStatement(
					String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s';",
							TABLE_NTS, NTS_ID, id, NTS_NAME, key ));
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
	
	public void saveNote(UserDataPayload udp) {

		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 
		
		// extra safety -->
		if(dlt == null) {
			dlt = new LoggingTable();
			dlt.setGlobal_id();
			dlt.setDevice_id(udp.getDevice_id());
		}

		clearNoteDeviceData(udp);
		
		this.device_id = udp.getDevice_id();
		
		Note note = new Note();

		dlt.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
		
		for(int i=0;i<udp.getNoteListSize();i++) {
			note = udp.getNoteListElement(i);			
			if(note!=null) {
				try {
					// Check if the record exists -->
					if(RecordExists(udp.getDevice_id(), note.getName())) {
						// If the record exists then update -->	
						updateRecord(note);
					}else {
						// If the record does not exist then create and insert -->
						insertRecord(note);
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
				dlt.createErrorLogEntry(internal_method_name, "Note array item object is empty or null");
				//throw new CustomException(String.format("%s|%s", "Note array item object is null", internal_method_name));

			}
		}		
	    dlt.createInfoLogEntry(internal_method_name, String.format("%s %s %s", "Processed", String.valueOf(udp.getNoteListSize()), " notes"));				
	}	
	
	public void insertRecord(Note note) {

		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 
		
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connection.prepareStatement(
					String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,?,?);", 
							TABLE_NTS, NTS_ID, NTS_NOTE_ID, NTS_NAME, NTS_VALUE, NTS_DTYPE, NTS_NOTE_CREATED, NTS_NOTE_UPDATED));

			preparedStatement.setString(1, this.device_id);
			preparedStatement.setString(2, note.getId());
			preparedStatement.setString(3, note.getName());
			preparedStatement.setBytes(4, note.getFile());
			preparedStatement.setString(5, uuid.toString());
			preparedStatement.setString(6, note.getCreated());
			preparedStatement.setString(7, note.getUpdated());
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
	
	public void updateRecord(Note note) {

		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 
		
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connection.prepareStatement(
					String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = '%s' AND %s = '%s';", 
							TABLE_NTS, NTS_VALUE, NTS_UPDATED, NTS_NOTE_UPDATED, NTS_ID, this.device_id, NTS_NAME, note.getName()));

			preparedStatement.setBytes(1, note.getFile());
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, note.getUpdated());

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
	
	
	public UserDataResponse getNoteResponse(UserDataResponse udr) {
		
		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 

		String device_id = udr.getDevice_id();
		
		// extra safety -->
		if(dlt == null) {
			dlt = new LoggingTable();
			dlt.setGlobal_id();
			dlt.setDevice_id(device_id);
		}
				
		PreparedStatement preparedStatement = null;
		
		dlt.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
		
		// extra safety -->
		if(dlt == null) {
			dlt = new LoggingTable();
			dlt.setGlobal_id();
			dlt.setDevice_id(device_id);
		}
		
		int i=0;
		
		try {
			preparedStatement = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = '%s';", 
					                                                TABLE_NTS, NTS_ID, device_id));
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
		        udr.addNoteArrayElement(rs.getString(NTS_NOTE_ID), rs.getString(NTS_NAME), rs.getString(NTS_NOTE_CREATED), rs.getString(NTS_NOTE_UPDATED), rs.getBytes(NTS_VALUE));
		        i++;
			}
			preparedStatement.close();
		} catch (SQLException e) {
			dlt.createErrorLogEntry(internal_method_name, e.getMessage());
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}
		
		dlt.createInfoLogEntry(internal_method_name, String.format("%s %s %s", "Retreived", String.valueOf(i), "notes"));
		
		return udr;
		
	}
	
    private void clearNoteDeviceData(UserDataPayload udp) {
		
    	String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 
		
    	Statement stmt = null; 
    	
		try {			
			stmt = connection.createStatement();
			stmt.execute(String.format("DELETE FROM %s WHERE %s = '%s';",
							TABLE_NTS, NTS_ID, udp.getDevice_id()));			

		} catch (SQLException e) {			
			dlt.createErrorLogEntry(internal_method_name, e.getMessage());
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}	
		finally {
			try {
				stmt.close();
				dlt.createInfoLogEntry(internal_method_name, String.format("%s %s", "Cleared note device data for id ", udp.getDevice_id()));
			} catch (SQLException e) {
				dlt.createErrorLogEntry(internal_method_name, e.getMessage());
				throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));			}
		}
    	
    }	
	
}
