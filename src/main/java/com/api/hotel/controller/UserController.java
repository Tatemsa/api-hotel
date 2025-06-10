package com.api.hotel.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.hotel.domain.user.model.User;
import com.api.hotel.domain.user.service.UserService;
import com.api.hotel.dto.AuthDto;
import com.api.hotel.security.JwtService;

@RestController
@RequestMapping
public class UserController {

	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public UserController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.jwtService  = jwtService;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/auth/register")
	public void register(@RequestBody @Validated User user) {
		this.userService.save(user);
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping("/auth/activation")
	public void userAccountActivation(@RequestBody Map<String, String> activation) {
		this.userService.activation(activation);
	}


	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/auth/login")
	public Map<String, String> login(@RequestBody AuthDto authDto) {
		System.out.println("Le user " + authDto.getUsername() + " tente une connexion");
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPwd())
		);

		if (authentication.isAuthenticated()) {
			return this.jwtService.generate(authDto.getUsername());
		}
		return null;
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("users")
	public List<User> getAll() {
		return this.userService.findAll();
	}
}
