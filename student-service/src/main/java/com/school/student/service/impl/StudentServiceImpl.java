package com.school.student.service.impl;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.school.student.dto.AssignClassRequest;
import com.school.student.dto.CreateStudentRequest;
import com.school.student.dto.PromoteStudentRequest;
import com.school.student.dto.StudentResponse;
import com.school.student.entity.Student;
import com.school.student.entity.StudentClassHistory;
import com.school.student.enums.ClassStatus;
import com.school.student.enums.StudentStatus;
import com.school.student.exception.BusinessException;
import com.school.student.exception.ResourceNotFoundException;
import com.school.student.repository.StudentClassHistoryRepository;
import com.school.student.repository.StudentRepository;
import com.school.student.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

	private final StudentRepository studentRepository;
	private final StudentClassHistoryRepository historyRepository;

	public StudentServiceImpl(StudentRepository studentRepository, StudentClassHistoryRepository historyRepository) {
		this.studentRepository = studentRepository;
		this.historyRepository = historyRepository;
	}

	@Override
	public StudentResponse createStudent(CreateStudentRequest request) {

		Student student = new Student();
		student.setAdmissionNumber(generateAdmissionNumber());
		student.setFirstName(request.firstName);
		student.setLastName(request.lastName);
		student.setDob(request.dob);
		student.setGender(request.gender);
		student.setEmail(request.email);
		student.setMobile(request.mobile);
		student.setStatus(StudentStatus.ACTIVE);

		Student savedStudent = studentRepository.save(student);
		return mapToResponse(savedStudent);
	}

	@Transactional
	@Override
	public void assignClass(Long studentId, AssignClassRequest request) {

		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found"));

		boolean hasActiveClass = historyRepository.findByStudentIdAndStatus(studentId, ClassStatus.ACTIVE).isPresent();

		if (hasActiveClass) {
			throw new BusinessException("Student already assigned to a class");
		}

		StudentClassHistory history = new StudentClassHistory();
		history.setStudent(student);
		history.setClassId(request.classId);
		history.setSectionId(request.sectionId);
		history.setAcademicYear(request.academicYear);
		history.setStatus(ClassStatus.ACTIVE);
		history.setStartDate(LocalDate.now());

		historyRepository.save(history);
	}

	private StudentResponse mapToResponse(Student student) {
		StudentResponse response = new StudentResponse();
		response.setId(student.getId());
		response.setAdmissionNumber(student.getAdmissionNumber());
		response.setFirstName(student.getFirstName());
		response.setLastName(student.getLastName());
		response.setDob(student.getDob());
		response.setGender(student.getGender());
		response.setEmail(student.getEmail());
		response.setMobile(student.getMobile());
		response.setStatus(student.getStatus());
		return response;
	}

	private String generateAdmissionNumber() {
		return "ADM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
	}

	@Transactional
	@Override
	public void promoteStudent(Long studentId, PromoteStudentRequest request) {

		// 1. Validate student
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found"));

		// 2. Find current ACTIVE class
		StudentClassHistory activeHistory = historyRepository.findByStudentIdAndStatus(studentId, ClassStatus.ACTIVE)
				.orElseThrow(() -> new BusinessException("No active class found for promotion"));

		// 3. Close current class
		activeHistory.setStatus(ClassStatus.PROMOTED);
		activeHistory.setEndDate(LocalDate.now());
		historyRepository.save(activeHistory);

		// 4. Create new class history
		StudentClassHistory newHistory = new StudentClassHistory();
		newHistory.setStudent(student);
		newHistory.setClassId(request.nextClassId);
		newHistory.setSectionId(request.nextSectionId);
		newHistory.setAcademicYear(request.nextAcademicYear);
		newHistory.setStatus(ClassStatus.ACTIVE);
		newHistory.setStartDate(LocalDate.now());

		historyRepository.save(newHistory);
	}

}
