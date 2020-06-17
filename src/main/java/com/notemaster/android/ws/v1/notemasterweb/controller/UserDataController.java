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

import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;
import com.notemaster.android.ws.v1.notemasterweb.h2_database.DatabaseBusinessObject;
import com.notemaster.android.ws.v1.notemasterweb.h2_database.LoggingTable;
import com.notemaster.android.ws.v1.notemasterweb.h2_database.SharedPreferenceTable;
import com.notemaster.android.ws.v1.notemasterweb.payload.UserDataPayload;
import com.notemaster.android.ws.v1.notemasterweb.response.DefaultResponse;
import com.notemaster.android.ws.v1.notemasterweb.response.SharedPreferenceResponse;

@RestController
@RequestMapping("notemaster/userdata") // translates to http://192.168.178.69:8080/notemaster/userdata
public class UserDataController {

	private SharedPreferenceTable sp = new SharedPreferenceTable();
	private DatabaseBusinessObject databaseBusinessObject = new DatabaseBusinessObject();
	
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
					initLoggingObject(databaseBusinessObject, udp.getDevice_id());

					// create first log entry -->
					databaseBusinessObject.dlt.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));

					success = true; // <-- for finally
					databaseBusinessObject.processUserDataPayload(udp);

					// produce answer for client -->
					defaultResponse.setStatus("1");
					defaultResponse.setEntity("notemaster/userdata/post");
					defaultResponse.setKey(UUID.randomUUID().toString());
					defaultResponse.setRemark("JSON payload was consumed by method setSharedPreference");

				}catch(Exception e) {
					databaseBusinessObject.dlt.createErrorLogEntry(internal_method_name, e.getMessage());
					throw new CustomException(e.getMessage());
				}
				databaseBusinessObject.dlt.createInfoLogEntry(internal_method_name, "Completed");
				return new ResponseEntity<DefaultResponse>(defaultResponse,HttpStatus.OK);
			} else {
				return new ResponseEntity<DefaultResponse>(HttpStatus.BAD_REQUEST);
			}
		} finally {
			if (!success) {
				return new ResponseEntity<DefaultResponse>(HttpStatus.BAD_REQUEST);
			}
		}		
	}

	@GetMapping(path = "/{device_id}", 
			    produces = {MediaType.APPLICATION_JSON_VALUE, 
				      	    MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<SharedPreferenceResponse> getSharedPreferences(@PathVariable String device_id) {

		String internal_method_name = Thread.currentThread() 
				        .getStackTrace()[1] 
						.getMethodName(); 	
		
		Boolean success = false;
		SharedPreferenceResponse spr = new SharedPreferenceResponse();
		
		// Initialise logging -->
		initLoggingObject(sp, device_id);

		try {
			try {
				sp.dlt.createInfoLogEntry(internal_method_name, String.format("%s %s", "Execute", internal_method_name));
				success = true; // for finally
				spr = sp.getSharedPreferenceResponse(device_id);

			}catch(Exception e) {
				sp.dlt.createErrorLogEntry(internal_method_name, e.getMessage());
				throw new CustomException(e.getMessage());
			}
			sp.dlt.createInfoLogEntry(internal_method_name, "Completed");
			return new ResponseEntity<SharedPreferenceResponse>(spr,HttpStatus.OK);
		} finally {
			if (!success) {
				return new ResponseEntity<SharedPreferenceResponse>(HttpStatus.BAD_REQUEST);
			}
		}		
	}	

	private void initLoggingObject(DatabaseBusinessObject databaseBusinessObject, String device_id) {
		
		databaseBusinessObject.dlt = new LoggingTable();
		databaseBusinessObject.dlt.setGlobal_id();
		databaseBusinessObject.dlt.setDevice_id(device_id);
		
	}

	private void initLoggingObject(SharedPreferenceTable sp, String device_id) {
		
		sp.dlt = new LoggingTable();
		sp.dlt.setGlobal_id();
		sp.dlt.setDevice_id(device_id);
		
	}	
	
}
