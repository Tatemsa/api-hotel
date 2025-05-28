package com.api.hotel.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.hotel.domain.user.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
