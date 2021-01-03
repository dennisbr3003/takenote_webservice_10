package com.notemaster.android.ws.v1.notemasterweb;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.notemaster.android.ws.v1.notemasterweb.database.DAOFactory;
import com.notemaster.android.ws.v1.notemasterweb.database.IDatabaseBusinessObject;
import com.notemaster.android.ws.v1.notemasterweb.payload.WebUser;

public class DatabaseUserDetailsService implements UserDetailsService {

	DAOFactory factory = DAOFactory.getFactory(DAOFactory.PSQL);
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		WebUser webuser;

		IDatabaseBusinessObject databaseBusinessObject = factory.getDatabaseBusinessObject();
		webuser = databaseBusinessObject.getWebUser(username);
	
		if (webuser == null){
			throw new UsernameNotFoundException(username);
		}
		
		UserDetails user = User.withUsername(webuser.getName()).password(webuser.getPassword()).authorities("USER").build();
        return user;

	}
	
}