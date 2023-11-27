package com.mirna.transferapi.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotPresentException extends Exception {

	public EntityNotPresentException() { 
		super("Entity is not present in database");
	} 
	
	public EntityNotPresentException(String message) {
		super(message);
	}
}
