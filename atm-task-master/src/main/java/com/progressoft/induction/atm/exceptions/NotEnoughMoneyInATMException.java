package com.progressoft.induction.atm.exceptions;

public class NotEnoughMoneyInATMException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NotEnoughMoneyInATMException(String message) {
		super(message);	
	}
}
