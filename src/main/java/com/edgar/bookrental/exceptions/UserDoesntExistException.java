package com.edgar.bookrental.exceptions;

public class UserDoesntExistException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserDoesntExistException(String message) {
		super(message);
	}

}
