package com.school.admission.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.school.admission.dto.AdmissionResponse;
import com.school.admission.dto.ApplyAdmissionRequest;
import com.school.admission.service.AdmissionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admissions")
public class AdmissionController {

	private final AdmissionService admissionService;

	public AdmissionController(AdmissionService admissionService) {
		this.admissionService = admissionService;
	}

	@PostMapping
	public AdmissionResponse apply(@Valid @RequestBody ApplyAdmissionRequest request) {
		return admissionService.apply(request);
	}
}
