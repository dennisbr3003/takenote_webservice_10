package com.notemaster.android.ws.v1.notemasterweb.exceptions;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.notemaster.android.ws.v1.notemasterweb.response.ErrorObject;

@ControllerAdvice
public class ServletExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { Exception.class }) // connect to all error no matter
	public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest webrequest) {

		ErrorObject error_object = new ErrorObject(new Date(), getErrorMessage(ex), "system");
		return new ResponseEntity<>(error_object, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	@ExceptionHandler(value={CustomException.class}) // connect to user defined exception class specifically
	public ResponseEntity<Object> handleException(CustomException ex, WebRequest webrequest){
		
		String localErrorMessage = getErrorMessage(ex);		
		
		String errMessage;
		String errEntity;
		System.out.println("Custom Exception");
		if(localErrorMessage.split("\\|").length == 2) {
			errMessage = localErrorMessage.split("\\|")[0];
			errEntity = localErrorMessage.split("\\|")[1];			
		}else {
			errMessage = localErrorMessage;
			errEntity = "unknown";
		}
		
		ErrorObject error_object = new ErrorObject(new Date(), errMessage, errEntity);
		
		return new ResponseEntity<>(error_object, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		
	}					
	
	@ExceptionHandler(value={AuthenticationException.class}) // connect to user defined exception class specifically
	public ResponseEntity<Object> handleException(AuthenticationException ex, WebRequest webrequest){
		
		String localErrorMessage = getErrorMessage(ex);		
		
		String errMessage;
		String errEntity;

		if(localErrorMessage.split("\\|").length == 2) {
			errMessage = localErrorMessage.split("\\|")[0];
			errEntity = localErrorMessage.split("\\|")[1];			
		}else {
			errMessage = localErrorMessage;
			errEntity = "unknown";
		}
		
		ErrorObject error_object = new ErrorObject(new Date(), errMessage, errEntity);
		
		return new ResponseEntity<>(error_object, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
		
	}				

	@ExceptionHandler(value={RequestException.class}) // connect to user defined exception class specifically
	public ResponseEntity<Object> handleException(RequestException ex, WebRequest webrequest){
		
		String localErrorMessage = getErrorMessage(ex);		
		
		String errMessage;
		String errEntity;

		if(localErrorMessage.split("\\|").length == 2) {
			errMessage = localErrorMessage.split("\\|")[0];
			errEntity = localErrorMessage.split("\\|")[1];			
		}else {
			errMessage = localErrorMessage;
			errEntity = "unknown";
		}
		
		ErrorObject error_object = new ErrorObject(new Date(), errMessage, errEntity);
		
		return new ResponseEntity<>(error_object, new HttpHeaders(), HttpStatus.BAD_REQUEST);
		
	}		
	
	
	private String getErrorMessage(Exception ex) {
		if (ex.getLocalizedMessage() == null) {
			return ex.toString();
		} else {
			return ex.getLocalizedMessage();
		}	
	}
	
}
