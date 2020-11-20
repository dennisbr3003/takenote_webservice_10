package com.notemaster.android.ws.v1.notemasterweb.exceptions;

public class AuthenticationException extends RuntimeException {

	private static final long serialVersionUID = 5800968934044555456L;

	public AuthenticationException(String customMessage) {
		super(customMessage);
	}

}
