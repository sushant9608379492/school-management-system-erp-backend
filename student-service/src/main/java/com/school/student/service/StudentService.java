package com.school.student.service;

import com.school.student.dto.AssignClassRequest;
import com.school.student.dto.CreateStudentRequest;
import com.school.student.dto.PromoteStudentRequest;
import com.school.student.dto.StudentResponse;

public interface StudentService {

	StudentResponse createStudent(CreateStudentRequest request);

	void assignClass(Long studentId, AssignClassRequest request);

	void promoteStudent(Long studentId, PromoteStudentRequest request);

}
