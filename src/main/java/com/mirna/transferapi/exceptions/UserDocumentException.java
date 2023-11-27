package com.mirna.transferapi.exceptions;

public class UserDocumentException extends Exception {

	public UserDocumentException() { 
		super("Document number already exists");
	} 
  
}
