package com.edgar.bookrental.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edgar.bookrental.exceptions.BookDoesntExistException;
import com.edgar.bookrental.exceptions.BookWithTitleAlreadyExistsException;
import com.edgar.bookrental.models.Book;
import com.edgar.bookrental.repositories.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	/* create book */
	public Book newBook(Book book) {
		if (!isExists(book.getTitle())) {
			
			
			/* flat rate for every rental less than 30 days **/
			book.setPrice(new BigDecimal("10.00"));
			book.setBorrowed(false);

			return bookRepository.save(book);

		}

		else
			throw new BookWithTitleAlreadyExistsException("A book with that title already exists");

	}

	/* get all books by author **/
	public List<Book> getAllBooksByAuthor(String author) {
		return bookRepository.findByAuthor(author);

	}

	/* get book by title **/
	public Book getBookByTitle(String book_title) {
		Optional<Book> book = bookRepository.findByTitle(book_title);
		return book.orElseThrow(() -> new BookDoesntExistException(
				"Book with the title ::" + book_title + "doesnt Exist in the system"));
	}

	/* get all books in the system **/
	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	/*
	 * update book by title ---> for now , later i will write queries to update
	 * individual fields in Book object
	 **/
	public Book updateBook(Long id, Book book) {

		if (isExists(id)) {
			

			return bookRepository.save(book);

		}

		else
			throw new BookDoesntExistException("Book you are trying to update doesnt exist, check name and try again");

	}

	/* delete by id **/
	public void deleteById(Long id) {

		if (isExists(id)) {
			bookRepository.deleteById(id);
		}
		throw new BookDoesntExistException("Book not found with id :: " + id);
	}

	/* utils **/

	/* check exists */
	public boolean isExists(String book_title) {

		if (bookRepository.existsByTitle(book_title)) {
			return true;
		} else
			return false;

	}

	public boolean isExists(Long id) {

		if (bookRepository.existsById(id)) {
			return true;
		} else
			return false;

	}

}
