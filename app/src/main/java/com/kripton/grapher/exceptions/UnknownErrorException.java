package com.kripton.grapher.exceptions;

public class UnknownErrorException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public UnknownErrorException(String exp) {
		super(exp);
	}

}
