package com.school.admission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.school.admission.entity.Admission;

public interface AdmissionRepository extends JpaRepository<Admission, Long> {
}
