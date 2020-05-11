package com.notemaster.android.ws.v1.notemasterweb.payload;


public class DefaultPayload {

	private String device_id;
	private String action;

	public DefaultPayload() {
		super();
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
}
