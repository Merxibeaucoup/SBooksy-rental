package com.edgar.bookrental.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edgar.bookrental.models.Book;
import com.edgar.bookrental.models.user.Role;
import com.edgar.bookrental.repositories.BookRepository;
import com.edgar.bookrental.services.BookService;
import com.edgar.bookrental.services.UserService;

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

	/** Admin can assign roles to other users  --> figuring it out !  **/
	@PostMapping(path = "/user/{id}/role")
	public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestBody String role) {

		try {

			Role parsedRole = Role.valueOf(role.toUpperCase().trim());
			userService.assignRole(id, parsedRole);			
			System.out.println(parsedRole);

			return new ResponseEntity<>(parsedRole,HttpStatus.ACCEPTED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage().toString(), HttpStatus.BAD_REQUEST);
		}

	}

}
