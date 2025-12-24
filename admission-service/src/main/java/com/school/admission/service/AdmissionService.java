package com.school.admission.service;

import com.school.admission.dto.AdmissionResponse;
import com.school.admission.dto.ApplyAdmissionRequest;

public interface AdmissionService {

	AdmissionResponse apply(ApplyAdmissionRequest request);

	void approveAdmission(Long admissionId);
}
