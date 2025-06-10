package com.api.hotel.domain.user.model;

import java.time.Instant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "validation")
public class Validation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column
	private Instant creationDate;
	@Column
	private Instant expirationDate;
	@Column
	private Instant activationDate;
	@Column
	private String code;
	@JoinColumn
	@OneToOne(cascade = CascadeType.ALL)
	private User user;

	public Validation() {
	}

	public Validation(int id, Instant creationDate, Instant expirationDate, Instant activationDate, String code, User user) {
		this.id = id;
		this.creationDate = creationDate;
		this.expirationDate = expirationDate;
		this.activationDate = activationDate;
		this.code = code;
		this.user = user;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Instant getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Instant creationDate) {
		this.creationDate = creationDate;
	}
	public Instant getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Instant expirationDate) {
		this.expirationDate = expirationDate;
	}
	public Instant getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(Instant activationDate) {
		this.activationDate = activationDate;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
