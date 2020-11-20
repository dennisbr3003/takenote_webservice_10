package com.notemaster.android.ws.v1.notemasterweb.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notemaster.android.ws.v1.notemasterweb.database.DatabaseBusinessObject;
import com.notemaster.android.ws.v1.notemasterweb.database.tables.LoggingTable;
import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;
import com.notemaster.android.ws.v1.notemasterweb.payload.UserDataPayload;
import com.notemaster.android.ws.v1.notemasterweb.resource.Authentication;
import com.notemaster.android.ws.v1.notemasterweb.response.DefaultResponse;
import com.notemaster.android.ws.v1.notemasterweb.response.UserDataResponse;

@RestController
@RequestMapping("notemaster/userdata") // translates to http://192.168.178.69:8080/notemaster/userdata
public class UserDataController {

	private DatabaseBusinessObject databaseBusinessObject = new DatabaseBusinessObject();
	private LoggingTable logger;
	
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,
			                 MediaType.APPLICATION_XML_VALUE }, 
			     produces = {MediaType.APPLICATION_JSON_VALUE, 
					         MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<DefaultResponse> setUserData(@RequestBody UserDataPayload udp) {

		
		Boolean success = false;
		DefaultResponse defaultResponse = new DefaultResponse();		
		
		String internal_method_name = Thread.currentThread() 
				        .getStackTrace()[1] 
						.getMethodName(); 

		try {
			if (udp != null) {

				try {

					// Initialise logging -->
					databaseBusinessObject.setConnection();
					databaseBusinessObject.setLogger(udp.getDevice_id());
					logger = databaseBusinessObject.getLogger();

					// create first log entry -->
					logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));

					success = true; // <-- for finally
					databaseBusinessObject.processUserDataPayload(udp);

					// produce answer for client -->
					defaultResponse.setStatus("1");
					defaultResponse.setEntity("notemaster/userdata/post");
					defaultResponse.setKey(UUID.randomUUID().toString());
					defaultResponse.setRemark("JSON payload was consumed by method setSharedPreference");

				}catch(Exception e) {
					logger.createErrorLogEntry(internal_method_name, e.getMessage());
					throw new CustomException(e.getMessage());
				}
				logger.createInfoLogEntry(internal_method_name, "Completed");
				return new ResponseEntity<DefaultResponse>(defaultResponse,HttpStatus.OK);
			} else {
				return new ResponseEntity<DefaultResponse>(HttpStatus.BAD_REQUEST);
			}
		} finally {
			databaseBusinessObject.closeConnection();
			if (!success) {
				return new ResponseEntity<DefaultResponse>(HttpStatus.BAD_REQUEST);
			}
		}		
	}

	@GetMapping(path = "/{device_id}", 
			    produces = {MediaType.APPLICATION_JSON_VALUE, 
				      	    MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserDataResponse> getUserData(@PathVariable String device_id) {

		String internal_method_name = Thread.currentThread() 
				        .getStackTrace()[1] 
						.getMethodName(); 	

		
		Authentication authentication = new Authentication();
	    device_id = authentication.authenticate(device_id);
	    
		Boolean success = false;
		UserDataResponse udr = new UserDataResponse();
		
		// Initialise logging -->
		databaseBusinessObject.setConnection();
		databaseBusinessObject.setLogger(device_id);
		logger = databaseBusinessObject.getLogger();

		try {
			try {
				logger.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
				success = true; // for finally
				udr = databaseBusinessObject.getUserDataResponse(device_id);

			}catch(Exception e) {
				logger.createErrorLogEntry(internal_method_name, e.getMessage());
				throw new CustomException(e.getMessage());
			}
			logger.createInfoLogEntry(internal_method_name, "Completed");
			return new ResponseEntity<UserDataResponse>(udr,HttpStatus.OK);
		} finally {
			databaseBusinessObject.closeConnection();
			if (!success) {
				return new ResponseEntity<UserDataResponse>(HttpStatus.BAD_REQUEST);
			}
		}		
	}	

	
}
