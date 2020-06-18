package com.notemaster.android.ws.v1.notemasterweb.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;
import com.notemaster.android.ws.v1.notemasterweb.h2_database.Database;
import com.notemaster.android.ws.v1.notemasterweb.response.DefaultResponse;

@RestController
@RequestMapping("notemaster") // translates to http://192.168.178.69:8080/notemaster
public class NoteMasterController {

	private Database h2db = new Database();	
	private DefaultResponse defaultResponse = new DefaultResponse();
	
	@GetMapping(path = "/{argument}", produces = {MediaType.APPLICATION_JSON_VALUE, 
			                                      MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<DefaultResponse> executeAction(
			@PathVariable String argument) {
		
		String internal_method_name = Thread.currentThread() 
		        .getStackTrace()[1] 
				.getMethodName(); 	
		
		switch(argument) {
		case "test":  // translates to http://192.168.178.69:8080/notemaster/test					
			if(h2db.testConnection()) {
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

	
}
