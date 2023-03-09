package com.edgar.bookrental.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edgar.bookrental.models.Book;
import com.edgar.bookrental.services.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/book")
public class BookController {

	@Autowired
	private BookService bookServices;
	
	
//	@Autowired
//	private BookRepository bookRepository;	
	

	
	
	
	@GetMapping("/all/books/by")
	public ResponseEntity<List<Book>> allBooksByAuthor(@Valid @RequestParam String author){
		return ResponseEntity.ok(bookServices.getAllBooksByAuthor(author));
		
	}
	
	@GetMapping("/{book_title}")
	public ResponseEntity<Book> bookByTitle(@PathVariable String book_title){
		return ResponseEntity.ok(bookServices.getBookByTitle(book_title));
	}
	
	@GetMapping("/all/books")
	public ResponseEntity<List<Book>> allBooks(){
		return ResponseEntity.ok(bookServices.getAllBooks());
		
	}
	
	

	


}
