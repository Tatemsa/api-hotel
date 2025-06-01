package com.api.hotel.domain.user.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.hotel.domain.user.model.User;
import com.api.hotel.domain.user.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void save(User user) {
		if(!user.getEmail().contains("@") && !user.getEmail().contains(".")) {
			throw new RuntimeException("Invalid mail");
		}
		String encryptPwd = this.passwordEncoder.encode(user.getPwd());
		user.setPwd(encryptPwd);
		this.userRepository.save(user);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}
}
