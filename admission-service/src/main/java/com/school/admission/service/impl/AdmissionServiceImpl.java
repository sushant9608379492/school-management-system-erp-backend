package com.school.admission.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.school.admission.dto.AdmissionResponse;
import com.school.admission.dto.ApplyAdmissionRequest;
import com.school.admission.entity.Admission;
import com.school.admission.enums.AdmissionStatus;
import com.school.admission.repository.AdmissionRepository;
import com.school.admission.service.AdmissionService;

@Service
public class AdmissionServiceImpl implements AdmissionService {

	private final AdmissionRepository admissionRepository;

	public AdmissionServiceImpl(AdmissionRepository admissionRepository) {
		this.admissionRepository = admissionRepository;
	}

	@Override
	public AdmissionResponse apply(ApplyAdmissionRequest request) {

		Admission admission = new Admission();
		admission.setFirstName(request.getFirstName());
		admission.setLastName(request.getLastName());
		admission.setDob(request.getDob());
		admission.setGender(request.getGender());
		admission.setEmail(request.getEmail());
		admission.setMobile(request.getMobile());

		// ✅ lifecycle initialization
		admission.setStatus(AdmissionStatus.APPLIED);
		admission.setCreatedAt(LocalDateTime.now());

		Admission saved = admissionRepository.save(admission);
		return mapToResponse(saved);
	}

	private AdmissionResponse mapToResponse(Admission admission) {
		AdmissionResponse response = new AdmissionResponse();
		response.setId(admission.getId());
		response.setFirstName(admission.getFirstName());
		response.setLastName(admission.getLastName());
		response.setDob(admission.getDob());
		response.setEmail(admission.getEmail());
		response.setMobile(admission.getMobile());
		response.setStatus(admission.getStatus());
		return response;
	}

	@Override
	public void approveAdmission(Long admissionId) {

		Admission admission = admissionRepository.findById(admissionId)
				.orElseThrow(() -> new RuntimeException("Admission not found"));

		if (admission.getStatus() != AdmissionStatus.APPLIED) {
			throw new RuntimeException(
					"Only APPLIED admission can be approved. Current status: " + admission.getStatus());
		}

		admission.setStatus(AdmissionStatus.APPROVED);
		admission.setUpdatedAt(LocalDateTime.now());

		admissionRepository.save(admission);
	}

}
