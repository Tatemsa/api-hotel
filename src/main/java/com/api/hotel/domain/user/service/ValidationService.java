package com.api.hotel.domain.user.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.api.hotel.domain.user.model.User;
import com.api.hotel.domain.user.model.Validation;
import com.api.hotel.domain.user.repository.ValidationRepository;

@Service
public class ValidationService {

	private final ValidationRepository validationRepository;
	private final NotificationService notificationService;

	public ValidationService(ValidationRepository validationRepository, NotificationService notificationService) {
		this.validationRepository = validationRepository;
		this.notificationService = notificationService;
	}

	public void save(User user) {
		Validation validation = new Validation();
		validation.setUser(user);
		Instant creation = Instant.now();
		validation.setCreationDate(creation);
		Instant expiration = creation.plus(10, ChronoUnit.MINUTES);
		validation.setExpirationDate(expiration);
		Random random = new Random();
		int randomInt = random.nextInt(99999);
		String code = String.format("%06d", randomInt);
		validation.setCode(code);

		Validation validationSaved = this.validationRepository.save(validation);
		this.notificationService.sendValidationNotification(validationSaved);
	}

	public Validation readWithCode(String code) {
		return this.validationRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Invalid code"));
	}
}
