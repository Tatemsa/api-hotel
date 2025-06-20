package com.api.hotel.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("API de Gestion Hôtelière")
				.version("1.0.0")
				.description("API REST pour la gestion d'hôtels avec authentification JWT")
				.contact(new Contact()
					.name("Équipe de développement")
					.email("dev@hotel-api.com")
					.url("https://hotel-api.com"))
				.license(new License()
					.name("Apache 2.0")
					.url("https://www.apache.org/licenses/LICENSE-2.0")))
			.servers(List.of(
				new Server()
					.url("http://localhost:8080/api")
					.description("Serveur de développement"),
				new Server()
					.url("https://api.hotel.com")
					.description("Serveur de production")))
			.addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
			.components(new Components()
				.addSecuritySchemes("Bearer Authentication",
					new SecurityScheme()
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")
						.description("Entrez votre token JWT dans le format: Bearer {token}")));
	}
}