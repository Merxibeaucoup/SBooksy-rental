package com.edgar.bookrental.exceptions;

public class BookIsNotAvailableAlreadyRentedOutException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public BookIsNotAvailableAlreadyRentedOutException(String message) {
		super(message);
	}

}
