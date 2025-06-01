package com.api.hotel.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("inscription")
	public void inscription(@RequestBody @Validated User user) {
		this.userService.save(user);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("users")
	public List<User> getAll() {
		return this.userService.findAll();
	}
}
