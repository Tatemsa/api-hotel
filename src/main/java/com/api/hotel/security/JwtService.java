package com.api.hotel.security;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.api.hotel.domain.user.model.User;
import com.api.hotel.domain.user.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final String ENCRYPTION_KEY = "fgdtrrd5OOfghjk0gfjhxfxhgccxkfxgchgxgxgxfxhgxhgfwghcjgxfgwgfgxghrurgdYTUYRYDCUYXGcutyduiXgddYxYt";
	private UserService userService;

	public Map<String, String> generate(String username) {
		User user = this.userService.loadUserByUsername(username);
		return this.generateJwt(user);
	}

	private Map<String, String> generateJwt(User user) {

		final long currentTime = System.currentTimeMillis();
		final long expirationTime = currentTime  + 30 * 60 * 1000;
		final Map<String, String> claims = Map.of(
			"name", user.getUsername(),
			"email", user.getEmail()
		);

		final String bearer = Jwts.builder()
								.setIssuedAt(new Date(currentTime))
								.setExpiration(new Date(expirationTime))
								.setSubject(user.getUsername())
								.setClaims(claims)
								.signWith(getKey(), SignatureAlgorithm.HS256)
								.compact();

		return Map.of("bearer", bearer);
	}

	private Key getKey() {
		final byte[] decoder = Decoders.BASE64.decode(ENCRYPTION_KEY);
		return Keys.hmacShaKeyFor(decoder);
	}
}
