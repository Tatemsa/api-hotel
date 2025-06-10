package com.api.hotel.domain.user.service;


import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.hotel.domain.user.model.User;
import com.api.hotel.domain.user.model.Validation;
import com.api.hotel.domain.user.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final ValidationService validationService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, ValidationService validationService) {
		this.validationService = validationService;
		this.userRepository = userRepository;
	}

	public void save(User user) {
		if (!user.getEmail().contains("@") || !user.getEmail().contains(".")) {
			throw new RuntimeException("Invalid mail");
		}

		boolean isExistEmail = this.userRepository.existsByEmail(user.getEmail());
		if (isExistEmail) {
			throw new RuntimeException("Email deja utilise");
		}
		boolean isExistUsername = this.userRepository.existsByUsername(user.getUsername());
		if (isExistUsername) {
			throw new RuntimeException("Username deja utilise");
		}
		String encryptPwd = this.passwordEncoder.encode(user.getPwd());
		user.setPwd(encryptPwd);

		User userSaved = this.userRepository.save(user);
		this.validationService.save(userSaved);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public void activation(Map<String, String> activation) {
		Validation validation = this.validationService.readWithCode(activation.get("code"));
		if (Instant.now().isAfter(validation.getExpirationDate())) {
			throw new RuntimeException("Your code has expired");
		}
		User user= this.userRepository.findById(validation.getUser().getId()).orElseThrow(() -> new RuntimeException("User not found"));
		user.setActive(true);
		this.userRepository.save(user);

	}

	/**
	 * @param username
	 * @return
	 * @throws UsernameNotFoundException
	 */
	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("Le user " + username + " tente une connexion");
		return this.userRepository.findByEmailOrUsername(username, username).orElseThrow(() -> new UsernameNotFoundException("User nor found"));
	}
}
