package com.school.student.entity;

import java.time.LocalDate;

import com.school.student.enums.ClassStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "student_class_history")
@Getter
@Setter
public class StudentClassHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;

	private Long classId;
	private Long sectionId;
	private String academicYear;
	private Integer rollNumber;

	@Enumerated(EnumType.STRING)
	private ClassStatus status;

	private LocalDate startDate;
	private LocalDate endDate;

	// getters and setters
}
