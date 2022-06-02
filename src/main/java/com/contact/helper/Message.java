package com.contact.helper;

import lombok.Data;

@Data
public class Message {

	private String content;
	private String type;
	public Message(String content, String type) {
		super();
		this.content = content;
		this.type = type;
	}
	public Message() {
		super();
		
	}

		
		
	
	

}
