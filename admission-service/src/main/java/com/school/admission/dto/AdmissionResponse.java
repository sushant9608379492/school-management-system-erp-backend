package com.school.admission.dto;

import com.school.admission.enums.AdmissionStatus;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AdmissionResponse {

	private Long id;
	private String firstName;
	private String lastName;
	private LocalDate dob;
	private String email;
	private String mobile;
	private AdmissionStatus status;

	// getters and setters
}
