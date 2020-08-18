package com.fico.blaze.model;

import java.util.HashMap;

public class Message {

	private HashMap<String,String> metadata;
	
	private String message;

	public Message() {
		metadata = new HashMap<>();
	}
	
	public Message(String message2) {
		this();
		this.message=message2;
		
	}

	public HashMap<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(HashMap<String, String> metadata) {
		this.metadata = metadata;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
