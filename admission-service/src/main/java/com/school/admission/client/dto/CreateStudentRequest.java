package com.school.admission.client.dto;

import java.time.LocalDate;

import com.school.admission.enums.Gender;

public class CreateStudentRequest {

	private String firstName;
	private String lastName;
	private LocalDate dob;
	private Gender gender;
	private String email;
	private String mobile;

	// getters & setters
}
