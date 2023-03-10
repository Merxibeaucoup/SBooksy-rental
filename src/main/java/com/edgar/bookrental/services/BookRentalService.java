package com.edgar.bookrental.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
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

	private final BigDecimal LATE_RETURN_FEE = new BigDecimal("7.00");
	
	
	
	/** find all rentals **/
	public List<Rental> getAllRentals(){
		return rentalRepository.findAll();
	}

	/* Rent a book **/
	public Rental rent(Rental rental, Book book_title, User user) {

		LocalDateTime rent_date_now = LocalDateTime.now();

		if (isExists(book_title.getTitle())) {

			book_title.setTitle(rental.getBookTitle());
			if (book_title.getBorrowed() == true)
				throw new BookIsNotAvailableAlreadyRentedOutException(
						"Book with title :: " + rental.getBookTitle() + " is not available to be rented");

			book_title.setBorrowed(true);
			rental.setUser(user);
			rental.setRentDate(rent_date_now);
			rental.setReturnDate(rent_date_now.plusDays(RENT_PERIOD));
			rental.setReturned(false);
			rental.setTotal(new BigDecimal("0.00"));

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

			/* if return is late , user gets charged a late fee **/

			LocalDateTime today = LocalDateTime.now();

			long days_late = ChronoUnit.DAYS.between(book_in_System.getReturnDate(), today);

			if (today.isAfter(book_in_System.getReturnDate())) {

				/* if days late is more than 15 days renter pays full price of the book **/
				if (days_late > 15) {
					rental.setTotal(book.getBookPrice());
				}

				else
					rental.setTotal(book.getRentPrice().add(LATE_RETURN_FEE));
			} else

			{
				rental.setTotal(book.getRentPrice());

			}

			rental.setReturnDate((today));
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
