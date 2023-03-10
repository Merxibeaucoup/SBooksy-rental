package com.edgar.bookrental.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.edgar.bookrental.models.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="rentals")
@Entity
public class Rental {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String bookTitle;

	@CreationTimestamp
	@Column(name = "rent_date")
	private LocalDateTime rentDate;

	@Column(name = "return_date")
	private LocalDateTime returnDate;

	@Column(name = "book_returned")
	private Boolean returned;
	
	
	@PositiveOrZero
	private BigDecimal total;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

}
