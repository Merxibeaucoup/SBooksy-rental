package com.edgar.bookrental.exceptions;

public class UserIsAdminException extends Exception {

	private static final long serialVersionUID = 1L;
	
	
	public UserIsAdminException(String message) {
		super(message);
	}

}
