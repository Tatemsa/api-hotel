package com.api.hotel.domain.user.service;

import java.util.List;
import java.util.Objects;
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

	public UserResponseDto createUser(CreateUserDto createUserDto) {

		if (userRepository.existsByUsername(createUserDto.getUsername())) {
			throw new BadRequestException("Nom d'utilisateur déjà existant");
		}

		if (userRepository.existsByEmail(createUserDto.getEmail())) {
			throw new com.api.hotel.Exception.BadRequestException("Email déjà utilisé");
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

	public List<UserResponseDto> getAllUsers(User user) {
		if (user.getRole() == Role.ADMIN) {
			return getAllUsers();
		} else if (user.getRole() == Role.EMPLOYEE) {
			return getClientUsers(user.getRole());
		} else {
			throw new BadRequestException("You don't authorisation to get all user");
		}
	}

	public List<UserResponseDto> getAllUsers() {
		return userRepository.findAll().stream()
			.map(this::convertToResponseDto)
			.collect(Collectors.toList());
	}

	public List<UserResponseDto> getClientUsers(Role role) {
		return userRepository.findByRole(role).stream()
			.map(this::convertToResponseDto)
			.collect(Collectors.toList());
	}

	public UserResponseDto getUserById(Long id, User user) {
		if (user.getRole() == Role.ADMIN) {
			return getUserById(id);
		} else if (user.getRole() == Role.EMPLOYEE) {
			return getUserById(id);
		} else if (user.getRole() == Role.CLIENT) {
			if (Objects.equals(id, user.getId()))
				getUserById(id);
			else
				throw new BadRequestException("Vous n'avez pas l'autorisation de modifier cet utilisateur");
		}
		throw new ResourceNotFoundException("Utilisateur non trouvé");
	}

	public UserResponseDto getUserById(Long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
		return convertToResponseDto(user);
	}

	public UserResponseDto findUserByIdAndRole(Long id, Role role) {
		User user = userRepository.findByIdAndRole(id, role)
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

		if (currentUser.getRole() == Role.EMPLOYEE && targetUser.getRole() == Role.CLIENT) {
			return true;
		}

		return targetUser.getId().equals(currentUser.getId());
	}

	private boolean canDeleteUser(User targetUser, User currentUser) {

		if (currentUser.getRole() == Role.ADMIN) {
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
