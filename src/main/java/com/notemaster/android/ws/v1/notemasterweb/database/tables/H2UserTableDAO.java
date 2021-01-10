package com.notemaster.android.ws.v1.notemasterweb.database.tables;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	public WebUser getWebUser(String webusername) {
		
		WebUser webuser = null;
		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 
				
		PreparedStatement preparedStatement = null;
		if(logger != null) {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
		}
		
		try {
			preparedStatement = H2Database.getInstance().getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s = '%s';", 
					                                                TABLE_USR, USR_NAME, webusername));
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				// public WebUser(String code, String name, String password, String remark) 
				webuser = new WebUser(rs.getString(USR_NAME), rs.getString(USR_PASSWRD), rs.getString(USR_DID), rs.getString(USR_REMARK));				
		        break; /* just get the first */ 
			}
			preparedStatement.close();
		} catch (SQLException e) {
			if(logger != null) {
				logger.createErrorLogEntry(internal_method_name, e.getMessage());
			}
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}
		if(logger != null) {
			logger.createInfoLogEntry(internal_method_name, String.format("%s %s (%s)", "Retrieved user", webuser.getName(), webuser.getDevice_id()));
		}
		
		return webuser;
		
	}

	@Override
	@Bean
	public PasswordEncoder passwordEncoder() {
		 return new BCryptPasswordEncoder();
	}

	@Override
	@Autowired
	public void insertWebUser(WebUser webuser) {
		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = H2Database.getInstance().getConnection().prepareStatement(
					String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?,?,?,?);", 
							       TABLE_USR, USR_NAME, USR_PASSWRD, USR_DID, USR_REMARK));
			
			preparedStatement.setString(1, webuser.getName());
			preparedStatement.setString(2, passwordEncoder().encode(webuser.getPassword()));
			preparedStatement.setString(3, webuser.getDevice_id());
			preparedStatement.setString(4, webuser.getRemark());
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
				throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));			
			}
		}
		
	}

}
