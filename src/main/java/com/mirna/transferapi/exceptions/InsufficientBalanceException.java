package com.mirna.transferapi.exceptions;

public class InsufficientBalanceException extends Exception {

	public InsufficientBalanceException() {
	super("Insufficient balance for transaction");
	}
}
