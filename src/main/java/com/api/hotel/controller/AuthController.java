package com.api.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.api.hotel.Exception.ApplicationControllerAdvice;
import com.api.hotel.domain.user.model.Role;
import com.api.hotel.domain.user.model.User;
import com.api.hotel.domain.user.repository.UserRepository;
import com.api.hotel.dto.JwtResponse;
import com.api.hotel.dto.LoginRequest;
import com.api.hotel.dto.MessageResponse;
import com.api.hotel.dto.SignupRequest;
import com.api.hotel.dto.TokenRefreshRequest;
import com.api.hotel.dto.TokenRefreshResponse;
import com.api.hotel.security.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Authentification", description = "Endpoints pour l'authentification des utilisateurs")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtService jwtService;

	@PostMapping("/signin")
	@Operation(
		summary = "Connexion utilisateur",
		description = "Authentifie un utilisateur et retourne un token JWT"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Connexion réussie",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = JwtResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "401",
			description = "Identifiants invalides",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ApplicationControllerAdvice.ErrorResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "Données de requête invalides"
		)
	})
	public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		User userDetails = (User) authentication.getPrincipal();
		String jwt = jwtService.generateToken(userDetails);
		String refreshToken = jwtService.generateRefreshToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(jwt, refreshToken,
			userDetails.getId(),
			userDetails.getUsername(),
			userDetails.getEmail(),
			userDetails.getRole().name()));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Validated @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest()
				.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest()
				.body(new MessageResponse("Error: Email is already in use!"));
		}

		User user = new User(signUpRequest.getUsername(),
			signUpRequest.getEmail(),
			encoder.encode(signUpRequest.getPassword()),
			signUpRequest.getRole()
		);

		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/signout")
	@Operation(
		summary = "Déconnexion utilisateur",
		description = "Déconnecte l'utilisateur et invalide le token"
	)
	@SecurityRequirement(name = "Bearer Authentication")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Déconnexion réussie"
		),
		@ApiResponse(
			responseCode = "401",
			description = "Token invalide ou expiré"
		)
	})
	public ResponseEntity<?> logoutUser() {
		SecurityContextHolder.clearContext();
		return ResponseEntity.ok(new MessageResponse("User signed out successfully!"));
	}

	@PostMapping("/refresh")
	@Operation(
		summary = "Renouveler le token",
		description = "Génère un nouveau token JWT à partir d'un refresh token"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Token renouvelé avec succès",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = JwtResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "401",
			description = "Refresh token invalide"
		)
	})
	public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
		String requestRefreshToken = request.getRefreshToken();

		try {
			String username = jwtService.extractUsername(requestRefreshToken);
			User user = userRepository.findByUsername(username).orElse(null);

			if (user != null && jwtService.isTokenValid(requestRefreshToken, user)) {
				String token = jwtService.generateToken(user);
				return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Refresh token is not valid!"));
		}

		return ResponseEntity.badRequest().body(new MessageResponse("Refresh token is not valid!"));
	}
}
