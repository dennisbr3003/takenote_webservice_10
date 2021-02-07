package com.notemaster.android.ws.v1.notemasterweb;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextListener;

import com.notemaster.android.ws.v1.notemasterweb.database.DAOFactory;
import com.notemaster.android.ws.v1.notemasterweb.database.IDatabaseBusinessObject;
import com.notemaster.android.ws.v1.notemasterweb.payload.WebUser;

public class DatabaseUserDetailsService implements UserDetailsService {

	DAOFactory factory = DAOFactory.getFactory(DAOFactory.PSQL);

	@Autowired
	private HttpServletRequest request;
	
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

		System.out.println(String.format("(!) %s", userId));
		
		WebUser webuser = new WebUser(); 
		webuser.setUser_id(userId); // unique key		
		
		System.out.println(request.getServletPath());
		
		IDatabaseBusinessObject databaseBusinessObject = factory.getDatabaseBusinessObject();
		webuser = databaseBusinessObject.getWebUser(webuser);
	
		if (webuser == null){
			throw new UsernameNotFoundException(userId);
		}
		
		UserDetails user = User.withUsername(webuser.getName()).password(webuser.getPassword()).authorities("USER").build();
        return user;

	}
	
	public RequestContextListener requestContextListener(){
	    return new RequestContextListener();
	}	
	
}