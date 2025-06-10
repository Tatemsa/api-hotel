package com.api.hotel.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.hotel.domain.user.model.User;
import com.api.hotel.domain.user.service.UserService;


@RestController
@RequestMapping
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
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
	public void login(@RequestBody @Validated String usernaame, @RequestBody @Validated String pwd) {
		System.out.println("L'utilisateur " + usernaame + " tente de se connecter.");
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("users")
	public List<User> getAll() {
		return this.userService.findAll();
	}
}
