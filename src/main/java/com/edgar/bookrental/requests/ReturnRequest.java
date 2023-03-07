package com.edgar.bookrental.requests;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnRequest {

	@NotBlank
	private String bookTitle;

	@Column(name = "return_date", nullable = true)
	private LocalDateTime returnDate;

}
