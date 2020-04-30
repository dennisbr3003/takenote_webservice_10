package com.notemaster.android.ws.v1.notemasterweb.exceptions;

public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 5800968934044555456L;

	public CustomException(String customMessage) {
		super(customMessage);
	}

}
