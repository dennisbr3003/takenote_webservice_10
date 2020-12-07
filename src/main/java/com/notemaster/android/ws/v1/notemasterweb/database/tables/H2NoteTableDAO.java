package com.notemaster.android.ws.v1.notemasterweb.database.tables;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.notemaster.android.ws.v1.notemasterweb.database.H2Database;
import com.notemaster.android.ws.v1.notemasterweb.database.constants.NoteTableConstants;
import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;
import com.notemaster.android.ws.v1.notemasterweb.payload.Note;
import com.notemaster.android.ws.v1.notemasterweb.payload.UserDataPayload;
import com.notemaster.android.ws.v1.notemasterweb.resource.LoggerTakeNote;
import com.notemaster.android.ws.v1.notemasterweb.response.UserDataResponse;

public class H2NoteTableDAO implements NoteTableConstants, INoteTable {

	private LoggerTakeNote logger;

	public LoggerTakeNote getLogger() {
		return logger;
	}

	public void setLogger(LoggerTakeNote logger) {
		this.logger = logger;
	}			
	
	public H2NoteTableDAO() {
		super();
	}

	@Override
	public boolean RecordExists(String id, String key) {

		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = H2Database.getInstance().getConnection().prepareStatement(
					String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s';",
							TABLE_NTS, NTS_ID, id, NTS_NAME, key ));
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
	public void saveNote(UserDataPayload udp) {

		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 

		clearNoteDeviceData(udp);
		
		Note note = new Note();
		if(logger != null) {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
		}
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
					logger.createErrorLogEntry(internal_method_name, "Note array item object is empty or null");
				}
			}
		}		
		if(logger != null) {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s %s", "Processed", String.valueOf(udp.getNoteListSize()), " notes"));
		}
	}	
	
	@Override
	public void insertRecord(Note note) {

		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = H2Database.getInstance().getConnection().prepareStatement(
					String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,?,?);", 
							TABLE_NTS, NTS_ID, NTS_NOTE_ID, NTS_NAME, NTS_VALUE, NTS_DTYPE, NTS_NOTE_CREATED, NTS_NOTE_UPDATED));

			preparedStatement.setString(1, logger.getDevice_id());
			preparedStatement.setString(2, note.getId());
			preparedStatement.setString(3, note.getName());
			preparedStatement.setBytes(4, note.getFile());
			preparedStatement.setString(5, "byteArray");
			preparedStatement.setString(6, note.getCreated());
			preparedStatement.setString(7, note.getUpdated());
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
	public void updateRecord(Note note) {

		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = H2Database.getInstance().getConnection().prepareStatement(
					String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = '%s' AND %s = '%s';", 
							TABLE_NTS, NTS_VALUE, NTS_UPDATED, NTS_NOTE_UPDATED, NTS_ID, logger.getDevice_id(), NTS_NAME, note.getName()));

			preparedStatement.setBytes(1, note.getFile());
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, note.getUpdated());

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
	public UserDataResponse getNoteResponse(UserDataResponse udr) {
		
		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 

		String device_id = udr.getDevice_id();
				
		PreparedStatement preparedStatement = null;
		if(logger != null) {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
		}
				
		int i=0;
		
		try {
			preparedStatement = H2Database.getInstance().getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s = '%s';", 
					                                                TABLE_NTS, NTS_ID, device_id));
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
		        udr.addNoteArrayElement(rs.getString(NTS_NOTE_ID), rs.getString(NTS_NAME), rs.getString(NTS_NOTE_CREATED), rs.getString(NTS_NOTE_UPDATED), rs.getBytes(NTS_VALUE));
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
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s %s", "Retreived", String.valueOf(i), "notes"));
		}
		
		return udr;
		
	}

    @Override
	public boolean isEmpty(String device_id) {
		
		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 
				
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = H2Database.getInstance().getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s = '%s';", 
					                                                TABLE_NTS, NTS_ID, device_id));
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
	
    private void clearNoteDeviceData(UserDataPayload udp) {
		
    	String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		
    	Statement stmt = null; 
    	
		try {			
			stmt = H2Database.getInstance().getConnection().createStatement();
			stmt.execute(String.format("DELETE FROM %s WHERE %s = '%s';",
							TABLE_NTS, NTS_ID, udp.getDevice_id()));			

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
					logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Cleared note device data for id ", udp.getDevice_id()));
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
