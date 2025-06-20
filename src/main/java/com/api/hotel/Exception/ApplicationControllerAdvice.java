package com.api.hotel.Exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.api.hotel.dto.ErrorEntity;

@ControllerAdvice
@RestControllerAdvice
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

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
		ErrorResponse error = new ErrorResponse(
			HttpStatus.NOT_FOUND.value(),
			ex.getMessage(),
			LocalDateTime.now()
		);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
		ErrorResponse error = new ErrorResponse(
			HttpStatus.BAD_REQUEST.value(),
			ex.getMessage(),
			LocalDateTime.now()
		);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		ValidationErrorResponse errorResponse = new ValidationErrorResponse(
			HttpStatus.BAD_REQUEST.value(),
			"Erreurs de validation",
			LocalDateTime.now(),
			errors
		);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	public static class ErrorResponse {
		private final int status;
		private final String message;
		private final LocalDateTime timestamp;

		public ErrorResponse(int status, String message, LocalDateTime timestamp) {
			this.status = status;
			this.message = message;
			this.timestamp = timestamp;
		}

		// Getters
		public int getStatus() { return status; }
		public String getMessage() { return message; }
		public LocalDateTime getTimestamp() { return timestamp; }
	}

	public static class ValidationErrorResponse extends ErrorResponse {
		private final Map<String, String> errors;

		public ValidationErrorResponse(int status, String message, LocalDateTime timestamp, Map<String, String> errors) {
			super(status, message, timestamp);
			this.errors = errors;
		}

		public Map<String, String> getErrors() { return errors; }
	}
}
