package com.notemaster.android.ws.v1.notemasterweb.exceptions;

public class RequestException extends RuntimeException {

	private static final long serialVersionUID = 1337162564713603954L;

	public RequestException(String customMessage) {
		super(customMessage);
	}

}
