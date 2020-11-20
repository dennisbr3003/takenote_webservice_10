package com.notemaster.android.ws.v1.notemasterweb.resource;

import com.notemaster.android.ws.v1.notemasterweb.exceptions.AuthenticationException;

public class Authentication implements resourceConstants {

	public Authentication() {
		super();
		
	}

	public String authenticate(String encrypted_key) {

	    String internal_method_name = Thread.currentThread() 
				.getStackTrace()[1] 
						.getMethodName(); 			
		
	    Encryption encryption = new Encryption();
		
	    String timestamp;
	    String device_id;
	    String decrypted_key;
	    String[] parts;
	    
		try {
			decrypted_key = encryption.decrypt(encrypted_key);
			parts = decrypted_key.split("-");			
			timestamp = parts[0];
			device_id = parts[1];
		} catch (Exception e) {
			throw new AuthenticationException(String.format("%s|%s", String.format("%s (%s)", "Authentication error: invalid key", e.getMessage()), internal_method_name));	
		}
						
		System.out.println("Request timestamp " + timestamp);
		System.out.println("Current timestamp " + String.valueOf((System.currentTimeMillis() / 1000L)));
		System.out.println("Elapsed time      " + String.valueOf((System.currentTimeMillis() / 1000L) - Long.parseLong(timestamp)));		
		
		// check on time a request is valid for ... seconds minutes.
		if((System.currentTimeMillis() / 1000L) - Long.parseLong(timestamp) > ALLOWED_INTERVAL) {
			throw new AuthenticationException(String.format("%s|%s", "Time-out error, the request is too old", internal_method_name));
		}		
		
		return device_id;
		
	}
	
}
