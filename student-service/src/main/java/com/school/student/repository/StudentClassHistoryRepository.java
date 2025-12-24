package com.school.student.repository;

import com.school.student.entity.StudentClassHistory;
import com.school.student.enums.ClassStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentClassHistoryRepository
        extends JpaRepository<StudentClassHistory, Long> {

    Optional<StudentClassHistory>
    findByStudentIdAndStatus(Long studentId, ClassStatus status);
}
