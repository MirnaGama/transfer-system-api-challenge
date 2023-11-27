package com.mirna.transferapi.exceptions;

public class UserEmailException extends Exception {

	public UserEmailException() { 
		super("Email already exists");
	} 
}
