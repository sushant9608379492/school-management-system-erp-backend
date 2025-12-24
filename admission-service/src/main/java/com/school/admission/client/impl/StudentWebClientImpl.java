package com.school.admission.client.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.school.admission.client.StudentClient;
import com.school.admission.client.dto.CreateStudentRequest;

@Component
public class StudentWebClientImpl implements StudentClient {

	private final WebClient webClient;
	private final String studentServiceBaseUrl;

	public StudentWebClientImpl(WebClient webClient,
			@Value("${student-service.base-url}") String studentServiceBaseUrl) {
		this.webClient = webClient;
		this.studentServiceBaseUrl = studentServiceBaseUrl;
	}

	@Override
	public void createStudent(CreateStudentRequest request) {

		webClient.post().uri(studentServiceBaseUrl + "/students").bodyValue(request).retrieve().toBodilessEntity()
				.block();
	}
}
