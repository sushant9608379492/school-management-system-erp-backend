package com.school.admission.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.school.admission.enums.AdmissionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "admissions")
@Getter
@Setter
public class Admission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Student basic details (copied here intentionally)
	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false)
	private LocalDate dob;

	@Column(nullable = false)
	private String gender;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String mobile;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AdmissionStatus status;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.status = AdmissionStatus.APPLIED;
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	// getters and setters
}
