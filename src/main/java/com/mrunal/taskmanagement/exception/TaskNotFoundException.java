package com.mrunal.taskmanagement.exception;


public class TaskNotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public TaskNotFoundException(String message) {
		super(message);
	}

}
