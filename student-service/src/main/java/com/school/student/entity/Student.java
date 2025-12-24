package com.school.student.entity;

import com.school.student.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "student")
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admission_number", unique = true, nullable = false)
    private String admissionNumber;

    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String gender;
    private String email;
    private String mobile;

    @Enumerated(EnumType.STRING)
    private StudentStatus status;

    // getters and setters
}
