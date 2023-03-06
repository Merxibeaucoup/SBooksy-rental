package com.edgar.bookrental.advice;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.edgar.bookrental.exceptions.BookDoesntExistException;
import com.edgar.bookrental.exceptions.BookWithTitleAlreadyExistsException;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BookWithTitleAlreadyExistsException.class)
	public ResponseEntity<Object> handleBookAlreadyExists(BookWithTitleAlreadyExistsException ex, WebRequest request) {

		return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.CONFLICT, LocalDateTime.now()),
				HttpStatus.CONFLICT);
	}
	
	
	@ExceptionHandler(BookDoesntExistException.class)
	public ResponseEntity<Object> handleBookDoesntExist(BookDoesntExistException ex, WebRequest request) {

		return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()),
				HttpStatus.NOT_FOUND);
	}

}
