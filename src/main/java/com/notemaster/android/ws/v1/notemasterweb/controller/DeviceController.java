package com.notemaster.android.ws.v1.notemasterweb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.notemaster.android.ws.v1.notemasterweb.database.DAOFactory;
import com.notemaster.android.ws.v1.notemasterweb.database.IDatabaseBusinessObject;
import com.notemaster.android.ws.v1.notemasterweb.exceptions.CustomException;
import com.notemaster.android.ws.v1.notemasterweb.resource.Authentication;
import com.notemaster.android.ws.v1.notemasterweb.resource.Session;
import com.notemaster.android.ws.v1.notemasterweb.response.DefaultResponse;

@RestController
@RequestMapping("notemaster") // translates to http://192.168.178.69:8080/notemaster
public class DeviceController {

	private DefaultResponse defaultResponse = new DefaultResponse();
	private DAOFactory factory = DAOFactory.getFactory(DAOFactory.PSQL);	
	private IDatabaseBusinessObject databaseBusinessObject = factory.getDatabaseBusinessObject();
			
	@GetMapping(path = "device/{device_id}/hasdata", produces = {MediaType.APPLICATION_JSON_VALUE, 
		                                                   MediaType.APPLICATION_XML_VALUE })
	
	// translates to http://192.168.178.69:8080/notemaster/device/{device}/hasdata
	
	public ResponseEntity<DefaultResponse> deviceHasData(@PathVariable String device_id, 
			                                             @RequestParam (required=false, defaultValue="false") boolean OverrideEncryption) {
		
		String internal_method_name = Thread.currentThread().getStackTrace()[1].getMethodName(); 		
				
		if(!OverrideEncryption) {
			Authentication authentication = new Authentication();
			device_id = authentication.authenticate(device_id);
		}
		
		try {
			
			Session.getInstance().setLastCallTimeStamp(System.currentTimeMillis());
			
			defaultResponse.setEntity("hasdata");
			defaultResponse.setKey("");
			
			if (databaseBusinessObject.deviceHasData(device_id)) {
				defaultResponse.setStatus("1"); //has data
				defaultResponse.setRemark("Device has saved data");
				return new ResponseEntity<DefaultResponse>(defaultResponse,HttpStatus.OK);
			} else {
				defaultResponse.setStatus("0"); //has no data
				defaultResponse.setRemark("Device has no saved data");
				return new ResponseEntity<DefaultResponse>(defaultResponse,HttpStatus.OK);
			}
			
		}catch(Exception e) {
			throw new CustomException(String.format("%s|%s", e.getMessage(), internal_method_name));
		}
	}		

}
