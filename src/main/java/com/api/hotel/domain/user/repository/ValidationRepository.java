package com.api.hotel.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.hotel.domain.user.model.Validation;

public interface ValidationRepository extends JpaRepository<Validation, Integer> {

	Optional<Validation> findByCode(String code);
}
