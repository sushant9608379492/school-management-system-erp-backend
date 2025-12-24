package com.school.student.dto;

import jakarta.validation.constraints.NotNull;

public class PromoteStudentRequest {

	@NotNull
	public Long nextClassId;

	@NotNull
	public Long nextSectionId;

	@NotNull
	public String nextAcademicYear;
}
