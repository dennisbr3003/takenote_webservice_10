package com.notemaster.android.ws.v1.notemasterweb.database.tables;

import org.springframework.security.crypto.password.PasswordEncoder;
import com.notemaster.android.ws.v1.notemasterweb.payload.WebUser;
import com.notemaster.android.ws.v1.notemasterweb.resource.LoggerTakeNote;

public interface IUserTable {

	LoggerTakeNote getLogger();

	void setLogger(LoggerTakeNote logger);

	WebUser getWebUser(WebUser webuser);
	
	PasswordEncoder passwordEncoder();

	void addWebUser(WebUser webuser);
	
	void deleteWebUser(WebUser webuser);

}