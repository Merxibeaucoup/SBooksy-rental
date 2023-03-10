package com.edgar.bookrental.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.edgar.bookrental.models.user.Role;
import com.edgar.bookrental.models.user.User;

import jakarta.transaction.Transactional;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	
	Optional<User> findByEmail(String email);
	
	
	@Transactional
    @Modifying(clearAutomatically = true)
    @Query("update User user set user.role = :role where user.id = :id")
    void assignRole(@Param("id") long id, @Param("role") Role role);
	
	

	
}
