package com.notemaster.android.ws.v1.notemasterweb.controller;

import com.notemaster.android.ws.v1.notemasterweb.response.DefaultResponse;

public abstract class DataController {
	
	public DefaultResponse createSuccessResponse(String methodName) {
		
		DefaultResponse defaultResponse = new DefaultResponse();
		
		defaultResponse.setStatus("1");
		defaultResponse.setEntity(methodName);
		defaultResponse.setKey("");
		defaultResponse.setRemark("success");		
		
		return defaultResponse;
		
	}
	
}
