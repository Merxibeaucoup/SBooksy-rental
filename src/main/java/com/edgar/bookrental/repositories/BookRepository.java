package com.edgar.bookrental.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edgar.bookrental.models.Book;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	

	boolean existsByTitle(String title);

	Optional<Book> findByTitle(String title);

	List<Book> findByAuthor(String author);

}
