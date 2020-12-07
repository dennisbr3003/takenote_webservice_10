package com.notemaster.android.ws.v1.notemasterweb.resource;

public class Session {
	
	private static Session instance = new Session();
	
	private long lastCallTimeStamp;
	
	// private constructor makes it a singleton
	private Session() {
	}	
	
	// static method can be used without instantiating the class
	public static Session getInstance() {
		return instance;
	}

	public long getLastCallTimeStamp() {
		return lastCallTimeStamp;
	}

	public void setLastCallTimeStamp(long lastCallTimeStamp) {
		this.lastCallTimeStamp = lastCallTimeStamp;
	}
	
}
