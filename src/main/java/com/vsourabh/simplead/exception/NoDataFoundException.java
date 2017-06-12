package com.vsourabh.simplead.exception;

public class NoDataFoundException extends RuntimeException{

	private static final long serialVersionUID = -8123015274837494289L;
	
	public NoDataFoundException(String message) {
		super(message);
	}
}
