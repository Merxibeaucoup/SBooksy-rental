package com.edgar.bookrental.exceptions;

public class BookWithTitleAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BookWithTitleAlreadyExistsException(String message) {
		super(message);
	}
}
