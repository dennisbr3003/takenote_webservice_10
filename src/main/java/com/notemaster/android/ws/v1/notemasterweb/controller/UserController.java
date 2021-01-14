package com.notemaster.android.ws.v1.notemasterweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.notemaster.android.ws.v1.notemasterweb.database.DAOFactory;
import com.notemaster.android.ws.v1.notemasterweb.database.IDatabaseBusinessObject;
import com.notemaster.android.ws.v1.notemasterweb.exceptions.AuthenticationException;
import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;
import com.notemaster.android.ws.v1.notemasterweb.exceptions.RequestException;
import com.notemaster.android.ws.v1.notemasterweb.payload.WebUser;
import com.notemaster.android.ws.v1.notemasterweb.resource.Authentication;
import com.notemaster.android.ws.v1.notemasterweb.resource.Encryption;
import com.notemaster.android.ws.v1.notemasterweb.resource.LoggerTakeNote;
import com.notemaster.android.ws.v1.notemasterweb.resource.Session;
import com.notemaster.android.ws.v1.notemasterweb.response.DefaultResponse;

@RestController
@RequestMapping("notemaster/user") 
public class UserController {
	
	private DAOFactory factory = DAOFactory.getFactory(DAOFactory.PSQL);
	
	private IDatabaseBusinessObject databaseBusinessObject = factory.getDatabaseBusinessObject();
	
	@PostMapping(path = "/add", consumes = {MediaType.APPLICATION_JSON_VALUE,
		     	                            MediaType.APPLICATION_XML_VALUE },
			                    produces = {MediaType.APPLICATION_JSON_VALUE, 
					                        MediaType.APPLICATION_XML_VALUE })

	// translates to http://192.168.178.69:8080/notemaster/user/add

	public ResponseEntity<DefaultResponse> addUser(@RequestBody WebUser webuser,
			                                       @RequestParam (required=false, defaultValue="false") boolean OverrideEncryption) {

		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 	

		// password and device_id are send encrypted. The name and remark are not 
		String device_id="";

		LoggerTakeNote logger = new LoggerTakeNote();	
		DefaultResponse defaultResponse = new DefaultResponse();	
		Encryption encryption = new Encryption();
		
		device_id = webuser.getDevice_id();

		if(!OverrideEncryption) {
			Authentication authentication = new Authentication();
			device_id = authentication.authenticate(webuser.getDevice_id()); //override initial value if it is encrypted with the decrypted value
			// the password has to be decrypted so it can be 
			// re-encrypted using a encryption method supported by the webservice (BCrypt). 			
			try {
				webuser.setPassword(encryption.decrypt(webuser.getPassword()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new AuthenticationException("Authentication failed, decryption failed");
			}
			webuser.setDevice_id(device_id);
		}

		try {

			Session.getInstance().setLastCallTimeStamp(System.currentTimeMillis());

			logger.setDevice_id(device_id);
			logger.setTransaction_id();			

			databaseBusinessObject.setLogger(logger);

			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));				
			
			databaseBusinessObject.addWebUser(webuser);

			// produce answer for client -->
			defaultResponse.setStatus("1");
			defaultResponse.setEntity("adduser (post)");
			defaultResponse.setKey("");
			defaultResponse.setRemark("success");

			logger.createInfoLogEntry(internal_method_name, "Completed");

			return new ResponseEntity<DefaultResponse>(defaultResponse,HttpStatus.OK);

		}
		catch (RequestException e) {
			throw new RequestException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}
		catch (Exception e) {			
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));		
		}

	}		

	@PostMapping(path = "/authenticate", consumes = {MediaType.APPLICATION_JSON_VALUE,
			                                         MediaType.APPLICATION_XML_VALUE },
			                             produces = {MediaType.APPLICATION_JSON_VALUE, 
					                                 MediaType.APPLICATION_XML_VALUE })

	// translates to http://192.168.178.69:8080/notemaster/user/authenticate
	
	@Autowired(required = false)
	public ResponseEntity<DefaultResponse> authenticateUser(@RequestBody WebUser webuser,
	          		                                        @RequestParam (required=false, defaultValue="false") boolean OverrideEncryption) {

		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 	

		// password and device_id are send encrypted. The name and remark are not 
		String device_id="";
		String name="";

		LoggerTakeNote logger = new LoggerTakeNote();	
		DefaultResponse defaultResponse = new DefaultResponse();	
        Encryption encryption = new Encryption();
        
		device_id = webuser.getDevice_id();
		name = webuser.getName();

		if(!OverrideEncryption) {
			Authentication authentication = new Authentication();
			device_id = authentication.authenticate(webuser.getDevice_id()); //override initial value if it is encrypted with the decrypted value
			
			System.out.println(device_id);
			
			// the password has to be decrypted so it can be 
			// re-encrypted using a encryption method supported by the webservice (BCrypt). 
			
			try {
				webuser.setPassword(encryption.decrypt(webuser.getPassword()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new AuthenticationException("Authentication failed, decryption failed");
			}
			webuser.setDevice_id(device_id);
		}

		try {

			Session.getInstance().setLastCallTimeStamp(System.currentTimeMillis());

			logger.setDevice_id(device_id);
			logger.setTransaction_id();			

			databaseBusinessObject.setLogger(logger);

			logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));				

			// first get the web user from the database
			WebUser webuser_db = databaseBusinessObject.getWebUser(name);

			// compare decoded parameter password to encoded and saved password in database --> 
			if (!(passwordEncoder().matches(webuser.getPassword(), webuser_db.getPassword()))) {
				logger.createErrorLogEntry(internal_method_name, "Authentication failed");
				throw new AuthenticationException("Authentication failed, incorrect credentials");
			}

			// comparison succeeded, produce answer for client -->
			defaultResponse.setStatus("1");
			defaultResponse.setEntity("authenticateUser (post)");
			defaultResponse.setKey("");
			defaultResponse.setRemark("success");

			logger.createInfoLogEntry(internal_method_name, "Completed");

			return new ResponseEntity<DefaultResponse>(defaultResponse,HttpStatus.OK);

		}catch(AuthenticationException ae)	{
			throw new AuthenticationException(String.format("%s|%s", ae.getMessage(), internal_method_name));
		} catch (Exception e) {								
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));		
		}

	}			

	@Bean
	private PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	} 

}
