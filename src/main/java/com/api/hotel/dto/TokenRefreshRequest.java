package com.api.hotel.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requette de refraichissement du token")
public class TokenRefreshRequest {
	private String refreshToken;

	public String getRefreshToken() { return refreshToken; }
	public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}