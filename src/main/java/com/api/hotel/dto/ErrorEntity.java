package com.api.hotel.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Entite de gestion des erreurs")
public record ErrorEntity(String code, String message) {

}
