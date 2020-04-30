package com.notemaster.android.ws.v1.notemasterweb.response;

import java.util.Date;

public class ErrorObject {
	
	private Date timestamp;
	private String message;
	private String entity;
	
	public ErrorObject() {
		super();
	}

	public ErrorObject(Date timestamp, String message) {
		super();
		this.timestamp = timestamp;
		this.message = message;
	}

	public ErrorObject(Date timestamp, String message, String entity) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.entity = entity;
	}	
	
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}
}
