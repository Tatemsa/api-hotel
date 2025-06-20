package com.api.hotel.controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/users")
@Tag(name = "Gestion des Utilisateurs", description = "CRUD des utilisateurs du système hôtelier")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	@Operation(
		summary = "Créer un nouvel utilisateur",
		description = "Crée un nouvel utilisateur dans le système"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "Utilisateur créé avec succès",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = UserResponseDto.class)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "Données invalides"
		),
		@ApiResponse(
			responseCode = "409",
			description = "Email déjà utilisé"
		)
	})
	public ResponseEntity<UserResponseDto> createUser(
		@Validated @RequestBody CreateUserDto createUserDto,
		@AuthenticationPrincipal User currentUser) {
		UserResponseDto createdUser = userService.createUser(createUserDto, currentUser);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
	}

	@GetMapping
	@Operation(
		summary = "Lister tous les utilisateurs",
		description = "Récupère la liste paginée de tous les utilisateurs"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Liste des utilisateurs récupérée avec succès",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = List.class)
			)
		),
		@ApiResponse(
			responseCode = "401",
			description = "Token d'authentification invalide"
		),
		@ApiResponse(
			responseCode = "403",
			description = "Accès non autorisé"
		)
	})
	public ResponseEntity<List<UserResponseDto>> getAllUsers() {
		List<UserResponseDto> users = userService.getAllUsers();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/{id}")
	@Operation(
		summary = "Récupérer un utilisateur par ID",
		description = "Récupère les détails d'un utilisateur spécifique"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Utilisateur trouvé",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = UserResponseDto.class)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "Utilisateur non trouvé"
		),
		@ApiResponse(
			responseCode = "401",
			description = "Token d'authentification invalide"
		)
	})
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
	@Operation(
		summary = "Mettre à jour un utilisateur",
		description = "Met à jour les informations d'un utilisateur existant"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Utilisateur mis à jour avec succès",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = UserResponseDto.class)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "Utilisateur non trouvé"
		),
		@ApiResponse(
			responseCode = "400",
			description = "Données invalides"
		)
	})
	public ResponseEntity<UserResponseDto> updateUser(
		@PathVariable Long id,
		@Validated @RequestBody UpdateUserDto updateUserDto,
		@AuthenticationPrincipal User currentUser) {
		UserResponseDto updatedUser = userService.updateUser(id, updateUserDto, currentUser);
		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("/{id}")
	@Operation(
		summary = "Supprimer un utilisateur",
		description = "Supprime un utilisateur du système"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "204",
			description = "Utilisateur supprimé avec succès"
		),
		@ApiResponse(
			responseCode = "404",
			description = "Utilisateur non trouvé"
		),
		@ApiResponse(
			responseCode = "409",
			description = "Impossible de supprimer cet utilisateur"
		)
	})
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
