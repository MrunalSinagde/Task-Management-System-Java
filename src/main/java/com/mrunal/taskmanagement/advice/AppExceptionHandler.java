package com.mrunal.taskmanagement.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mrunal.taskmanagement.exception.CommentNotFoundException;
import com.mrunal.taskmanagement.exception.NotAuthorisedException;
import com.mrunal.taskmanagement.exception.TaskNotFoundException;
import com.mrunal.taskmanagement.exception.UserNotFoundException;

@RestControllerAdvice
public class AppExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> invalidArgumentExceptionHandler(MethodArgumentNotValidException exception) {
		Map<String, String> errorMap = new HashMap<>();

		exception.getBindingResult().getFieldErrors().forEach(error -> {
			errorMap.put(error.getField(), error.getDefaultMessage());
		});
		return errorMap;
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(UserNotFoundException.class)
	public Map<String, String> userNotFoundExceptionHandler(UserNotFoundException exception) {
		Map<String, String> errorMap = new HashMap<>();

		errorMap.put("errorMessage", exception.getMessage());

		return errorMap;
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NotAuthorisedException.class)
	public Map<String, String> notAuthorisedExceptionHandler(NotAuthorisedException exception){
		Map<String, String> errorMap = new HashMap<>();

		errorMap.put("errorMessage", exception.getMessage());

		return errorMap;
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(TaskNotFoundException.class)
	public Map<String, String> taskNotFoundExceptionHandler(TaskNotFoundException exception){
		Map<String, String> errorMap = new HashMap<>();

		errorMap.put("errorMessage", exception.getMessage());

		return errorMap;
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(CommentNotFoundException.class)
	public Map<String, String> commentNotFoundExceptionHandler(CommentNotFoundException exception){
		Map<String, String> errorMap = new HashMap<>();

		errorMap.put("errorMessage", exception.getMessage());

		return errorMap;
	}
	
	

}
