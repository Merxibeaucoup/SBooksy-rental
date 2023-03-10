package com.edgar.bookrental.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edgar.bookrental.models.Book;
import com.edgar.bookrental.models.Rental;
import com.edgar.bookrental.models.user.Role;
import com.edgar.bookrental.models.user.User;
import com.edgar.bookrental.repositories.BookRepository;
import com.edgar.bookrental.repositories.RentalRepository;
import com.edgar.bookrental.requests.ReturnRequest;
import com.edgar.bookrental.services.BookRentalService;
import com.edgar.bookrental.services.BookService;
import com.edgar.bookrental.services.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

	@Autowired
	private BookService bookServices;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private UserService userService;
	
	@Autowired
	private BookRentalService bookRentalService;
	
	@Autowired
	private RentalRepository rentalRepository;

	/** create book --> only admin can create book .... hence /admin/new **/
	@PostMapping("/book/new")
	public ResponseEntity<Book> create(@Valid @RequestBody Book book) {
		return ResponseEntity.ok(bookServices.newBook(book));

	}

	/** update book --> only admin can update book .... hence /admin/book **/
	@PutMapping("/book/{id}")
	public ResponseEntity<Book> updateById(@PathVariable Long id, @RequestBody Book book) {
		return ResponseEntity.ok(bookServices.updateBook(id, book));
	}

	/** delete book --> only admin can delete book .... hence /admin/book **/
	@DeleteMapping("/book/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id) {
		return bookRepository.findById(id).map(record -> {
			bookRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}

	/** Admin can assign roles to other users --> figuring it out ! **/
	@PostMapping("/user/{id}/role")
	public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestBody String role) {

		try {

			Role parsedRole = Role.valueOf(role);
			System.out.println(parsedRole);
			userService.assignRole(id, parsedRole);

			return new ResponseEntity<>(parsedRole, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage().toString(), HttpStatus.BAD_REQUEST);
		}

	}
	
	
	
		/** for rentals **/
	
	
	/** get all rentals made **/
	@GetMapping("/rentals")
	public ResponseEntity<List<Rental>> getAllRentalls(){
		return ResponseEntity.ok(bookRentalService.getAllRentals());
	}
	
	
	/** only  Admin can confirm book returned status **/
	@Transactional
	@PutMapping("/return-book")
	public ResponseEntity<?> returnBook(@Valid @RequestBody ReturnRequest request, @AuthenticationPrincipal User user){
				
		Rental rented = rentalRepository.findByBookTitle(request.getBookTitle()).get();		
		bookRentalService.returnBook(rented, request);
			
		rentalRepository.save(rented);
		
		 return new ResponseEntity<>(rented, HttpStatus.CREATED); 
		
	}
	
	
	
	

	/** Extras , regular endpoints for all users **/
	
	
	/** for Books **/
	
	/** Admin can fetch all books   -->  by author **/
	@GetMapping("/all/books/by")
	public ResponseEntity<List<Book>> allBooksByAuthor(@Valid @RequestParam String author) {
		return ResponseEntity.ok(bookServices.getAllBooksByAuthor(author));

	}

	/** Admin can fetch  book by title **/
	@GetMapping("/{book_title}")
	public ResponseEntity<Book> bookByTitle(@PathVariable String book_title) {
		return ResponseEntity.ok(bookServices.getBookByTitle(book_title));
	}

	
	/** Admin can fetch all books **/
	@GetMapping("/all/books")
	public ResponseEntity<List<Book>> allBooks() {
		return ResponseEntity.ok(bookServices.getAllBooks());

	}
	
	
	

}
