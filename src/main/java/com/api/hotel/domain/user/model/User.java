package com.api.hotel.domain.user.model;

import java.util.Collection;
import java.util.Collections;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column
	private String username;

	@Column
	private String firstName;

	@Column
	private String secondName;

	@Column
	private String email;

	@Column
	private String pwd;

	@Column(nullable = false)
	private boolean active = false;

	@Enumerated(EnumType.STRING)
	@Column(name = "US_ROLE")
	private Role  role;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Role of user
	 * @return
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_"  + this.role));
	}

	/**
	 * Get the user password
	 * @return
	 */
	@Override
	public String getPassword() {
		return this.pwd;
	}

	/**
	 * Get the user username
	 * @return
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * If user account has expired
	 * @return
	 */
	@Override
	public boolean isAccountNonExpired() {
		return this.active;
	}

	/**
	 * If user account in blocked/locked
	 * @return
	 */
	@Override
	public boolean isAccountNonLocked() {
		return this.active;
	}

	/**
	 * If  credentials are expired
	 * @return
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return UserDetails.super.isCredentialsNonExpired();
	}
	/**
	 * If user account is active
	 * @return
	 */
	@Override
	public boolean isEnabled() {
		return this.active;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
