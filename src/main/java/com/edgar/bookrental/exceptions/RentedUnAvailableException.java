package com.edgar.bookrental.exceptions;

public class RentedUnAvailableException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	
	public RentedUnAvailableException(String message) {
		super(message);
	}

}
