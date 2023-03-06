package com.edgar.bookrental.exceptions;

public class BookDoesntExistException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BookDoesntExistException(String message) {
		super(message);
	}

}
