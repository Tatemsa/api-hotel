package com.api.hotel.Exception.advice;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.api.hotel.dto.ErrorEntity;

@ControllerAdvice
public class ApplicationControllerAdvice {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ EntityNotFoundException.class})
	public @ResponseBody ErrorEntity handleException(EntityNotFoundException exception) {
		return new ErrorEntity(null, exception.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ RuntimeException.class})
	public @ResponseBody ErrorEntity handleRuntimeException(RuntimeException exception) {
		return new ErrorEntity(null, exception.getMessage());
	}
}
