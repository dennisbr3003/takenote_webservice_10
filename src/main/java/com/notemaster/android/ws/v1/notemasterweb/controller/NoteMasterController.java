package com.notemaster.android.ws.v1.notemasterweb.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notemaster.android.ws.v1.notemasterweb.database.DatabaseBusinessObject;
import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;
import com.notemaster.android.ws.v1.notemasterweb.resource.Authentication;
import com.notemaster.android.ws.v1.notemasterweb.response.DefaultResponse;

@RestController
@RequestMapping("notemaster") // translates to http://192.168.178.69:8080/notemaster
public class NoteMasterController {

	//private Database h2db = new Database();	
	private DefaultResponse defaultResponse = new DefaultResponse();
	
	private DatabaseBusinessObject databaseBusinessObject = new DatabaseBusinessObject();
	
	@GetMapping(path = "/{argument}", produces = {MediaType.APPLICATION_JSON_VALUE, 
			                                      MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<DefaultResponse> executeAction(
			@PathVariable String argument) {
		
		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 	
		
		switch(argument) {
		case "ping":  // translates to http://192.168.178.69:8080/notemaster/ping					
			if(databaseBusinessObject.verifyConnection()) { //this method auto-closes the connection
				try {
					defaultResponse.setStatus("1");
					defaultResponse.setEntity("notemaster/test");
					defaultResponse.setKey(UUID.randomUUID().toString());
					defaultResponse.setRemark("Database is online and ready");
					return new ResponseEntity<DefaultResponse>(defaultResponse,HttpStatus.OK);
				} catch(Exception e) {
					throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
				}
			} else {
				throw new CustomException(String.format("%s|%s", "Database connection could not be established", internal_method_name));
			}							    
		default:
			throw new CustomException(String.format("%s|%s", String.format("Unknown argument %s", argument), internal_method_name));			
		}

	}

	@GetMapping(path = "/{argument}/{device_id}", produces = {MediaType.APPLICATION_JSON_VALUE, 
            												  MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<DefaultResponse> deviceHasData(@PathVariable String argument, @PathVariable String device_id) {

	    String internal_method_name = Thread.currentThread() 
				.getStackTrace()[1] 
						.getMethodName(); 		
		
		Authentication authentication = new Authentication();
	    device_id = authentication.authenticate(device_id);
	    
		// Initialise logging -->
		databaseBusinessObject.setConnection();
		databaseBusinessObject.setLogger(device_id);

		try {
			defaultResponse.setEntity("notemaster/devicehasdata/<device_id>");
			defaultResponse.setKey(device_id);

			if (databaseBusinessObject.deviceHasData(device_id)) {
				try {					
					defaultResponse.setStatus("1"); //has data
					defaultResponse.setRemark(String.format("%s %s %s", "Device", device_id, "has saved data"));
					return new ResponseEntity<DefaultResponse>(defaultResponse,HttpStatus.OK);
				} catch(Exception e) {
					throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
				}
			} else {
				try {					
					defaultResponse.setStatus("0"); //has no data
					defaultResponse.setRemark(String.format("%s %s %s", "Device", device_id, "has no saved data"));
					return new ResponseEntity<DefaultResponse>(defaultResponse,HttpStatus.OK);
				} catch(Exception e) {
					throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
				}
			}	

		}finally {
			databaseBusinessObject.closeConnection();
		}
	}


}
