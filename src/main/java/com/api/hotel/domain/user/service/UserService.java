package com.api.hotel.domain.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.hotel.Exception.BadRequestException;
import com.api.hotel.Exception.ResourceNotFoundException;
import com.api.hotel.domain.user.model.Role;
import com.api.hotel.domain.user.model.User;
import com.api.hotel.domain.user.repository.UserRepository;
import com.api.hotel.dto.CreateUserDto;
import com.api.hotel.dto.UpdateUserDto;
import com.api.hotel.dto.UserResponseDto;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder ) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public UserResponseDto createUser(CreateUserDto createUserDto, User currentUser) {

		if (userRepository.existsByUsername(createUserDto.getUsername())) {
			throw new BadRequestException("Nom d'utilisateur déjà existant");
		}

		if (userRepository.existsByEmail(createUserDto.getEmail())) {
			throw new com.api.hotel.Exception.BadRequestException("Email déjà utilisé");
		}

		if (createUserDto.getRole() == Role.EMPLOYEE && currentUser.getRole() != Role.ADMIN) {
			throw new BadRequestException("Seul l'administrateur peut créer des employés");
		}

		User user = new User(
			createUserDto.getUsername(),
			createUserDto.getEmail(),
			passwordEncoder.encode(createUserDto.getPassword()),
			createUserDto.getRole()
		);

		User savedUser = userRepository.save(user);
		return convertToResponseDto(savedUser);
	}

	public List<UserResponseDto> getAllUsers() {
		return userRepository.findAll().stream()
			.map(this::convertToResponseDto)
			.collect(Collectors.toList());
	}

	public UserResponseDto getUserById(Long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
		return convertToResponseDto(user);
	}

	public List<UserResponseDto> getEmployees() {
		return userRepository.findByRole(Role.EMPLOYEE).stream()
			.map(this::convertToResponseDto)
			.collect(Collectors.toList());
	}

	public UserResponseDto updateUser(Long id, UpdateUserDto updateUserDto, User currentUser) throws BadRequestException {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

		if (!canModifyUser(user, currentUser)) {
			throw new BadRequestException("Vous n'avez pas l'autorisation de modifier cet utilisateur");
		}

		if (updateUserDto.getEmail() != null && !updateUserDto.getEmail().equals(user.getEmail())) {
			if (userRepository.existsByEmail(updateUserDto.getEmail())) {
				throw new BadRequestException("Email déjà utilisé");
			}
			user.setEmail(updateUserDto.getEmail());
		}


		if (updateUserDto.getPassword() != null && !updateUserDto.getPassword().isEmpty()) {
			user.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
		}

		User updatedUser = userRepository.save(user);
		return convertToResponseDto(updatedUser);
	}

	public void deleteUser(Long id, User currentUser) throws BadRequestException {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

		// Vérifier les permissions
		if (!canDeleteUser(user, currentUser)) {
			throw new BadRequestException("Vous n'avez pas l'autorisation de supprimer cet utilisateur");
		}

		userRepository.delete(user);
	}

	public UserResponseDto deactivateUser(Long id, User currentUser) throws BadRequestException {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

		if (currentUser.getRole() != Role.ADMIN) {
			throw new BadRequestException("Seul l'administrateur peut désactiver un utilisateur");
		}

		user.setEnabled(false);
		User updatedUser = userRepository.save(user);
		return convertToResponseDto(updatedUser);
	}

	public UserResponseDto reactivateUser(Long id, User currentUser) throws BadRequestException {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

		if (currentUser.getRole() != Role.ADMIN) {
			throw new BadRequestException("Seul l'administrateur peut réactiver un utilisateur");
		}

		user.setEnabled(true);
		User updatedUser = userRepository.save(user);
		return convertToResponseDto(updatedUser);
	}

	private boolean canModifyUser(User targetUser, User currentUser) {

		if (currentUser.getRole() == Role.ADMIN) {
			return true;
		}

		return targetUser.getId().equals(currentUser.getId());
	}

	private boolean canDeleteUser(User targetUser, User currentUser) {

		if (currentUser.getRole() == Role.ADMIN && targetUser.getRole() == Role.EMPLOYEE) {
			return true;
		}

		return currentUser.getRole() == Role.CLIENT &&
			targetUser.getId().equals(currentUser.getId());
	}

	private UserResponseDto convertToResponseDto(User user) {
		UserResponseDto dto = new UserResponseDto();
		dto.setUsername(user.getUsername());
		dto.setEmail(user.getEmail());
		dto.setRole(user.getRole());
		dto.setEnabled(user.isEnabled());
		return dto;
	}
}
