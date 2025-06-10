package com.api.hotel.domain.user.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.api.hotel.domain.user.model.Validation;

@Service
public class NotificationService {


	JavaMailSender javaMailSender;

	public NotificationService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void sendValidationNotification(Validation validation) {
		SimpleMailMessage message  = new SimpleMailMessage();
		message.setFrom("tatemsabill@gmail.com");
		message.setTo(validation.getUser().getEmail());
		message.setSubject("Votre code d'activation");
		String text = String.format(
			"Bonjour %s, <br /> Votre code d'activation est  %s, a bientot",
			validation.getUser().getUsername(),
			validation.getCode()
		);
		message.setText(text);

		javaMailSender.send(message);
	}
}
