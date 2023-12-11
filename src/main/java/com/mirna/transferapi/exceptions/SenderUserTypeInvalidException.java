package com.mirna.transferapi.exceptions;

public class SenderUserTypeInvalidException extends Exception {

	public SenderUserTypeInvalidException() { 
		super("Sender user type is invalid for this transaction");
	} 
}
