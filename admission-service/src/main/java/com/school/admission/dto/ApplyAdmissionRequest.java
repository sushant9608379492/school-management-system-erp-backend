package com.school.admission.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplyAdmissionRequest {

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	@NotNull
	private LocalDate dob;

	@NotBlank
	private String gender;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	private String mobile;

	// getters and setters
}
