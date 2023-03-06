package com.edgar.bookrental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edgar.bookrental.models.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

}
