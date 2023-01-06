package com.dscatalog.main.controller.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.dscatalog.main.services.exceptions.ControllerNotFoundException;
import com.dscatalog.main.services.exceptions.DatabaseException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(ControllerNotFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public StandardError entityNotFound(ControllerNotFoundException e, HttpServletRequest request) {
		
		StandardError err = new StandardError(
				Instant.now(),
				HttpStatus.NOT_FOUND.value(),
				"Controller not found",
				e.getMessage(),
				request.getRequestURI());
		
		return err;
		
	}
	
	@ExceptionHandler(DatabaseException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public StandardError database(DatabaseException e, HttpServletRequest request) {
		
		StandardError err = new StandardError(
				Instant.now(),
				HttpStatus.BAD_REQUEST.value(),
				"Database exception",
				e.getMessage(),
				request.getRequestURI());
		
		return err;
		
	}
}
