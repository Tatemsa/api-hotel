package com.api.hotel.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Message d'erreur")
public class MessageResponse {
	private String message;

	public MessageResponse(String message) {
		this.message = message;
	}

	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
}
