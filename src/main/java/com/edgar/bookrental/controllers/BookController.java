package com.edgar.bookrental.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.edgar.bookrental.repositories.BookRepository;
import com.edgar.bookrental.services.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/book")
public class BookController {

	@Autowired
	private BookService bookServices;
	
	
	@Autowired
	private BookRepository bookRepository;	
	

	/*create book**/
	
	@PostMapping("/new")
	public ResponseEntity<Book> create(@Valid @RequestBody Book book) {
		return ResponseEntity.ok(bookServices.newBook(book));

	}
	
	
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
	
	
	@PutMapping("/book/{id}")
	public ResponseEntity<Book> updateById(@PathVariable Long id ,@RequestBody Book book){
		return ResponseEntity.ok(bookServices.updateBook(id, book));
	}
	
	
	@DeleteMapping("/book/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id){
		return bookRepository.findById(id)
		           .map(record -> {
		               bookRepository.deleteById(id);
		               return ResponseEntity.ok().build();
		           }).orElse(ResponseEntity.notFound().build());
	}
	


}
