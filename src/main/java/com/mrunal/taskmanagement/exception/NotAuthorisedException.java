package com.mrunal.taskmanagement.exception;


public class NotAuthorisedException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public NotAuthorisedException(String message) {
		super(message);
	}

}
