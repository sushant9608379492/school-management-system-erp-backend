package com.school.student.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStudentRequest {

	@NotBlank
	public String firstName;

	@NotBlank
	public String lastName;

	@NotNull
	public LocalDate dob;

	@NotBlank
	public String gender;

	@Email
	@NotBlank
	public String email;

	@NotBlank
	public String mobile;
}
