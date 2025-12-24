package com.school.student.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.school.student.dto.AssignClassRequest;
import com.school.student.dto.CreateStudentRequest;
import com.school.student.dto.PromoteStudentRequest;
import com.school.student.dto.StudentResponse;
import com.school.student.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@PostMapping
	public StudentResponse createStudent(@Valid @RequestBody CreateStudentRequest request) {
		return studentService.createStudent(request);
	}

	@PostMapping("/{id}/assign-class")
	public void assignClass(@PathVariable Long id, @Valid @RequestBody AssignClassRequest request) {
		studentService.assignClass(id, request);
	}

	@PostMapping("/{id}/promote")
	public void promoteStudent(@PathVariable Long id, @Valid @RequestBody PromoteStudentRequest request) {

		studentService.promoteStudent(id, request);
	}

}
