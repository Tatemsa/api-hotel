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
				.csrf(AbstractHttpConfigurer::disable) //Desactivation de la  partie csrf
				.authorizeHttpRequests(
					authorize ->
						authorize.requestMatchers(HttpMethod.POST,"/inscription").permitAll()
							.anyRequest().authenticated()
				).build(); //Cette configuration permet de dire que si la requete est sur la route inscription et que le verbe http est POST, on authorise, et toute autre requete necessite l'authentification
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
