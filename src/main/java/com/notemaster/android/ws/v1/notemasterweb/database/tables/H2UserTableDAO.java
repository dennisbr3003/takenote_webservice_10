package com.notemaster.android.ws.v1.notemasterweb.database.tables;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.notemaster.android.ws.v1.notemasterweb.database.H2Database;
import com.notemaster.android.ws.v1.notemasterweb.database.constants.UserTableConstants;
import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;
import com.notemaster.android.ws.v1.notemasterweb.payload.WebUser;
import com.notemaster.android.ws.v1.notemasterweb.resource.LoggerTakeNote;

public class H2UserTableDAO implements UserTableConstants, IUserTable {

	private LoggerTakeNote logger;

	public LoggerTakeNote getLogger() {
		return logger;
	}

	public void setLogger(LoggerTakeNote logger) {
		this.logger = logger;
	}	
	

	@Override
	public WebUser getWebUser(WebUser webuser) {
		
		WebUser verifiedWebUser = null;
		PreparedStatement preparedStatement = null;
		
		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 
				
		if(logger != null) {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
		}
		
		try {
			preparedStatement = H2Database.getInstance().getConnection().prepareStatement(
					            String.format("SELECT * FROM %s WHERE %s = '%s';", 
					                          TABLE_USR, USR_GUID,  webuser.getUser_id()), 
					            ResultSet.TYPE_SCROLL_INSENSITIVE, // so I can move forward and backward in this ResultSet 
	                            ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				verifiedWebUser = new WebUser(rs);				
		        break; /* just get the first */ 
			}			
			preparedStatement.close();
			
		} catch (SQLException e) {
			if(logger != null) {
				logger.createErrorLogEntry(internal_method_name, e.getMessage());
			}
			System.out.println(String.format("%s %s", internal_method_name, e.getMessage()));
			throw new CustomException(e.getMessage());
		}
		
		if(logger != null) {
			if (verifiedWebUser != null) {
				logger.createInfoLogEntry(internal_method_name, String.format("%s %s (%s)", "Retrieved user", verifiedWebUser.getName(), verifiedWebUser.getDevice_id()));
			} else {
				logger.createInfoLogEntry(internal_method_name, String.format("User with name %s could not be found", webuser.getName()));
			}
			
		}
		return verifiedWebUser;
		
	}

	@Override
	@Bean
	public PasswordEncoder passwordEncoder() {
		 return new BCryptPasswordEncoder();
	}

	@Override
	@Autowired
	public void addWebUser(WebUser webuser) {

		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = H2Database.getInstance().getConnection().prepareStatement(
					String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,?);", 
							       TABLE_USR, USR_NAME, USR_PASSWRD, USR_DID, USR_REMARK, USR_GUID, USR_ROLES));
			
			preparedStatement.setString(1, webuser.getName());
			preparedStatement.setString(2, passwordEncoder().encode(webuser.getPassword()));
			preparedStatement.setString(3, webuser.getDevice_id());
			preparedStatement.setString(4, webuser.getRemark());
			preparedStatement.setString(5, webuser.getUser_id());
			preparedStatement.setString(6, webuser.getRoles());
			preparedStatement.executeUpdate();		
			
			preparedStatement.close();
			H2Database.getInstance().getConnection().commit();
			
		} catch (SQLException e) {
			if(logger != null) {
				logger.createErrorLogEntry(internal_method_name, e.getMessage());
			}
			System.out.println(String.format("%s %s", internal_method_name, e.getMessage()));
			throw new CustomException(e.getMessage());			
		} catch (Exception e) {
			if(logger != null) {
				logger.createErrorLogEntry(internal_method_name, e.getMessage());
			}
			System.out.println(String.format("%s %s", internal_method_name, e.getMessage()));
			throw new CustomException(e.getMessage());
		}	

	}	

    @Override
    public void deleteWebUser(WebUser webuser) {
		
    	String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		
    	/* first check if the user has the correct credentials */
    	WebUser webuser_db;
    	webuser_db = getWebUser(webuser);
    	
    	System.out.println(String.format("%s Looking for user %s", internal_method_name, webuser.getUser_id()));
    	
    	if (webuser_db != null) { 
    	
    		System.out.println(String.format("%s Found user %s", internal_method_name, webuser_db.getUser_id()));
    		
    		Statement stmt = null; 
	    	
			try {			
				stmt = H2Database.getInstance().getConnection().createStatement();
				stmt.execute(String.format("DELETE FROM %s WHERE %s = '%s';",
							                TABLE_USR, USR_GUID, webuser.getUser_id()));			
				stmt.close();
				if(logger != null) {
					logger.createInfoLogEntry(internal_method_name, String.format("Removed cloud registration for %s on device %s", webuser.getName(), webuser.getDevice_id()));
				}				
			} catch (SQLException e) {
				if(logger != null) {
					logger.createErrorLogEntry(internal_method_name, e.getMessage());
				}
				System.out.println(String.format("%s %s", internal_method_name, e.getMessage()));
				throw new CustomException(e.getMessage());
			} catch (Exception e) {
				if(logger != null) {					
					logger.createErrorLogEntry(internal_method_name, e.getMessage());
				}
				System.out.println(String.format("%s %s", internal_method_name, e.getMessage()));
				throw new CustomException(e.getMessage());	
			}
    	} else {
    		System.out.println(String.format("%s Could not find user to unregister", internal_method_name));
    		throw new CustomException("User cannot be unregistered. App data does not match service data for this device.");	
    	}
    }

}
