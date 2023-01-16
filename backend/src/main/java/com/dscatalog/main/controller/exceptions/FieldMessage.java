package com.dscatalog.main.controller.exceptions;

public class FieldMessage {
	
	private String fieldMessage;
	private String message;
	
	public FieldMessage(String fieldMessage, String message) {
		super();
		this.fieldMessage = fieldMessage;
		this.message = message;
	}
	
	public String getFieldMessage() {
		return fieldMessage;
	}
	
	public void setFieldMessage(String fieldMessage) {
		this.fieldMessage = fieldMessage;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
