package com.notemaster.android.ws.v1.notemasterweb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notemaster.android.ws.v1.notemasterweb.database.PSQLDatabase;
import com.notemaster.android.ws.v1.notemasterweb.resource.Session;
import com.notemaster.android.ws.v1.notemasterweb.response.DefaultResponse;

@RestController
@RequestMapping("notemaster") 
public class NoteMasterController {
	
	private DefaultResponse defaultResponse = new DefaultResponse();
	
	@GetMapping(path = "/ping", produces = {MediaType.APPLICATION_JSON_VALUE, 
			                                MediaType.APPLICATION_XML_VALUE })
	
	// translates to http://192.168.178.69:8080/notemaster/ping
	
	public ResponseEntity<DefaultResponse> ping() {
		
    	Session.getInstance().setLastCallTimeStamp(System.currentTimeMillis());
			
	    defaultResponse.setStatus("1");
		defaultResponse.setEntity("ping");
		defaultResponse.setKey("");

		if (PSQLDatabase.getInstance().verifyConnection()) {
			defaultResponse.setRemark("Database is online");
		} else {
			defaultResponse.setRemark("Database is offline");	
		}
			
		return new ResponseEntity<DefaultResponse>(defaultResponse,HttpStatus.OK);

	}

}
