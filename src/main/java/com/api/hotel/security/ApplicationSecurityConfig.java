package com.api.hotel.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return
			httpSecurity
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(
					authorize ->
						authorize.requestMatchers(HttpMethod.POST,"/auth").permitAll()
							.requestMatchers("/admin").hasRole("ADMIN")
							.requestMatchers("/users").hasRole("ADMIN")
							.requestMatchers(HttpMethod.POST, "/blog/posts").hasAnyRole("ADMIN", "EMPLOYEE")
							.anyRequest().permitAll()
				).build();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
