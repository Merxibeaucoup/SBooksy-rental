package com.edgar.bookrental.exceptions;

public class BookReturnUnSuccessfulException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public BookReturnUnSuccessfulException(String message) {
		super(message);
	}

}
