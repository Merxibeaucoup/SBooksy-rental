package com.edgar.bookrental.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edgar.bookrental.exceptions.BookIsNotAvailableAlreadyRentedOutException;
import com.edgar.bookrental.exceptions.BookRentalUnsuccessful;
import com.edgar.bookrental.exceptions.BookReturnUnSuccessfulException;
import com.edgar.bookrental.exceptions.RentedUnAvailableException;
import com.edgar.bookrental.models.Book;
import com.edgar.bookrental.models.Rental;
import com.edgar.bookrental.models.user.User;
import com.edgar.bookrental.repositories.BookRepository;
import com.edgar.bookrental.repositories.RentalRepository;
import com.edgar.bookrental.requests.ReturnRequest;

@Service
public class BookRentalService {

	@Autowired
	private RentalRepository rentalRepository;

	@Autowired
	private BookRepository bookRepository;

	private final int RENT_PERIOD = 30;

	private final BigDecimal LATE_RETURN_FEE = new BigDecimal("5.00");

	/* Rent a book **/
	public Rental rent(Rental rental, Book book_title, User user) {

		LocalDateTime rentdate_now = LocalDateTime.now();

		if (isExists(book_title.getTitle())) {

			book_title.setTitle(rental.getBookTitle());
			if (book_title.getBorrowed() == true)
				throw new BookIsNotAvailableAlreadyRentedOutException(
						"Book with title :: " + rental.getBookTitle() + " is not available to be rented");

			book_title.setBorrowed(true);
			rental.setUser(user);
			rental.setRentDate(rentdate_now);
			rental.setReturnDate(rentdate_now.plusDays(RENT_PERIOD));
			rental.setReturned(false);
			rental.setPenalty(new BigDecimal("0.00"));

			return rentalRepository.save(rental);

		}

		else
			throw new BookRentalUnsuccessful("Book rental unsuccessful");

	}

	/* return rented book **/
	public void returnBook(Rental rental, ReturnRequest request) {

		Optional<Rental> rented_book = rentalRepository.findByBookTitle(request.getBookTitle());
		Rental book_in_System = rented_book.orElseThrow(
				() -> new RentedUnAvailableException("This book hasnt been rented yet , or isnt in our system "));

		if (isExistsRented(request.getBookTitle())) {

			Book book = bookRepository.findByTitle(request.getBookTitle()).get();

			book.setBorrowed(false);

			rental.setReturned(true);

			LocalDateTime today = LocalDateTime.now();

			rental.setReturnDate((today));

			BigDecimal penalty = new BigDecimal("0.00"); // --> will use later  , change penalty field in Rental to total

			
			
			/* if return is late , user gets charged a late fee **/
			
			
			if (today.isAfter(book_in_System.getReturnDate())) {

				rental.setPenalty(book.getPrice().add(LATE_RETURN_FEE));
			} else {
				rental.setPenalty(book.getPrice());

			}

			bookRepository.save(book);

			rentalRepository.save(rental);

		}

		else
			throw new BookReturnUnSuccessfulException("failed to submit book return request");
	}

	/* util **/

	/* check if book exists ---> by title **/
	public boolean isExists(String book_title) {
		if (bookRepository.existsByTitle(book_title)) {
			return true;
		} else
			return false;
	}

	/* check if book title has been rented ---> by title **/
	public boolean isExistsRented(String book_title) {
		if (rentalRepository.existsByBookTitle(book_title)) {
			return true;
		} else
			return false;
	}

}
