package com.api.hotel.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Réponse d'authentification")
public class JwtResponse {
	@Schema(description = "Token JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
	private String token;

	@Schema(description = "Type de token", example = "Bearer")
	private String type = "Bearer";

	@Schema(description = "Refresh token", example = "refresh_token_here")
	private String refreshToken;

	@Schema(description = "User id", example = "3600")
	private Long id;

	@Schema(description = "Nom d'utilisateur de l'utilisateur", example = "Levai1")
	private String username;

	@Schema(description = "Email de l'utilisateur", example = "admin@hotel.com")
	private String email;

	@Schema(description = "Role de l'utilisateur", example = "ADMIN")
	private String role;

	public JwtResponse(String accessToken, String refreshToken, Long id, String username, String email, String role) {
		this.token = accessToken;
		this.refreshToken = refreshToken;
		this.id = id;
		this.username = username;
		this.email = email;
		this.role = role;
	}

	public String getToken() { return token; }
	public void setToken(String token) { this.token = token; }

	public String getType() { return type; }
	public void setType(String type) { this.type = type; }

	public String getRefreshToken() { return refreshToken; }
	public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getRole() { return role; }
	public void setRole(String role) { this.role = role; }

}
