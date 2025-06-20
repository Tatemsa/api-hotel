package com.api.hotel.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class AuthDto {

	@Schema(description = "Nom d'utilisateur de l'utilisateur", example = "Levai1")
	private String username;
	@Schema(description = "Mot de passe", example = "password123")
	private String pwd;

	public AuthDto(String username, String pwd) {
		this.username = username;
		this.pwd = pwd;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
