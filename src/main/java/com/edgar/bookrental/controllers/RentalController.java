package com.edgar.bookrental.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edgar.bookrental.exceptions.BookDoesntExistException;
import com.edgar.bookrental.models.Book;
import com.edgar.bookrental.models.Rental;
import com.edgar.bookrental.models.user.User;
import com.edgar.bookrental.repositories.BookRepository;
import com.edgar.bookrental.repositories.RentalRepository;
import com.edgar.bookrental.requests.ReturnRequest;
import com.edgar.bookrental.services.BookRentalService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/rent")
public class RentalController {
	
	
	
	@Autowired
	private BookRentalService bookRentalService;
	
	@Autowired
	private BookRepository bookRepository;
	
	
	@Autowired
	private RentalRepository rentalRepository;

	
	
	
	@PostMapping("/new")
	public ResponseEntity<?> rentBook(@Valid @RequestBody Rental rent, @AuthenticationPrincipal User user){
		
		Optional<Book> book_title = bookRepository.findByTitle(rent.getBookTitle());
		
		Book book = book_title.orElseThrow(()-> new BookDoesntExistException(
				"Book with the title ::" + rent.getBookTitle() + "doesnt Exist in the system"));
		
		bookRentalService.rent(rent, book, user);	
		
		
		bookRepository.save(book);		
		rentalRepository.save(rent);
		
		 return new ResponseEntity<>(rent, HttpStatus.CREATED); 
	}
	
	@Transactional
	@PutMapping("/return-book")
	public ResponseEntity<?> returnBook(@Valid @RequestBody ReturnRequest request, @AuthenticationPrincipal User user){
				
		Rental rented = rentalRepository.findByBookTitle(request.getBookTitle()).get();		
		bookRentalService.returnBook(rented, request);
			
		rentalRepository.save(rented);
		
		 return new ResponseEntity<>(rented, HttpStatus.CREATED); 
		
	}
}
