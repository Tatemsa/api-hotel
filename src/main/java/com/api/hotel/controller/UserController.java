package com.api.hotel.controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.hotel.domain.user.model.User;
import com.api.hotel.domain.user.service.UserService;
import com.api.hotel.dto.CreateUserDto;
import com.api.hotel.dto.UpdateUserDto;
import com.api.hotel.dto.UserResponseDto;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<UserResponseDto> createUser(
		@Validated @RequestBody CreateUserDto createUserDto,
		@AuthenticationPrincipal User currentUser) {
		UserResponseDto createdUser = userService.createUser(createUserDto, currentUser);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
	}

	@GetMapping
	public ResponseEntity<List<UserResponseDto>> getAllUsers() {
		List<UserResponseDto> users = userService.getAllUsers();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
		UserResponseDto user = userService.getUserById(id);
		return ResponseEntity.ok(user);
	}

	@GetMapping("/employees")
	public ResponseEntity<List<UserResponseDto>> getEmployees() {
		List<UserResponseDto> employees = userService.getEmployees();
		return ResponseEntity.ok(employees);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserResponseDto> updateUser(
		@PathVariable Long id,
		@Validated @RequestBody UpdateUserDto updateUserDto,
		@AuthenticationPrincipal User currentUser) {
		UserResponseDto updatedUser = userService.updateUser(id, updateUserDto, currentUser);
		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(
		@PathVariable Long id,
		@AuthenticationPrincipal User currentUser) {
		userService.deleteUser(id, currentUser);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}/deactivate")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserResponseDto> deactivateUser(
		@PathVariable Long id,
		@AuthenticationPrincipal User currentUser) {
		UserResponseDto user = userService.deactivateUser(id, currentUser);
		return ResponseEntity.ok(user);
	}

	@GetMapping("/profile")
	public ResponseEntity<UserResponseDto> getCurrentUserProfile(
		@AuthenticationPrincipal User currentUser) {
		UserResponseDto user = userService.getUserById(currentUser.getId());
		return ResponseEntity.ok(user);
	}
}
