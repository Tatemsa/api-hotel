package com.api.hotel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtConfig {
	private String secret;
	private int expiration;
	private int refreshExpiration;

	// Getters et Setters
	public String getSecret() { return secret; }
	public void setSecret(String secret) { this.secret = secret; }

	public int getExpiration() { return expiration; }
	public void setExpiration(int expiration) { this.expiration = expiration; }

	public int getRefreshExpiration() { return refreshExpiration; }
	public void setRefreshExpiration(int refreshExpiration) { this.refreshExpiration = refreshExpiration; }
}
