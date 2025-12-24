package com.school.student.dto;

import com.school.student.enums.StudentStatus;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentResponse {

    private Long id;
    private String admissionNumber;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String gender;
    private String email;
    private String mobile;
    private StudentStatus status;

    // getters and setters
}
