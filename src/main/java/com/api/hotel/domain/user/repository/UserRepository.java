package com.api.hotel.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.hotel.domain.user.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

	Optional<User> findByEmailOrUsername(String username, String username1);
}
