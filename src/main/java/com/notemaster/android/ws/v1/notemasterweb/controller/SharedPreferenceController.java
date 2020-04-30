package com.notemaster.android.ws.v1.notemasterweb.controller;

import java.util.UUID;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notemaster.android.ws.v1.notemasterweb.h2_database.Database;
import com.notemaster.android.ws.v1.notemasterweb.h2_database.SharedPreference;
import com.notemaster.android.ws.v1.notemasterweb.response.DefaultResponse;

@RestController
@RequestMapping("notemaster/sharedpreference") // translates to http://192.168.178.69:8080/notemaster/sharedpreference
public class SharedPreferenceController {

	private Database h2db = new Database();
	private SharedPreference sp = new SharedPreference();
	
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,
                             MediaType.APPLICATION_XML_VALUE }, 
                 produces = {MediaType.APPLICATION_JSON_VALUE, 
		                     MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<DefaultResponse> setSharedPreference(@RequestBody String json_payload) {

		Boolean success = false;
		DefaultResponse defaultResponse = new DefaultResponse();

		try {
			if ((json_payload != "") && (json_payload != null)) {

				defaultResponse.setStatus("1");
				defaultResponse.setEntity("notemaster/sharedpreference/post");
				defaultResponse.setKey(UUID.randomUUID().toString());
				defaultResponse.setRemark("JSON payload was consumed by method setSharedPreference");

				JSONObject j_object = new JSONObject(json_payload);
				if(j_object.has("name")) {
					System.out.println(j_object.getString("name"));
				}
					
				success = true; // for finally
				
				try {
					
     				h2db.createTable("SHARED_PREFERENCE");
     				sp.insertRecord(); //<-- test, moeten parameters bij, een object.
				}catch(Exception e) {
					System.out.println(e.getLocalizedMessage());
				}
				
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
	
}
