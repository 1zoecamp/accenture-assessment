package com.accenture.assessment.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessRuleException.class)
	public ResponseEntity<ApiError> handleBusinessRuleException(BusinessRuleException ex, HttpServletRequest request) {
		HttpStatus status = HttpStatus.CONFLICT;

		// Cria o objeto de erro estruturado
		ApiError apiError = new ApiError(status.value(), "Business Rule Violation", ex.getMessage(),
				request.getRequestURI());

		return new ResponseEntity<>(apiError, status);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;

		// Gerencia os erros de validação encontrados
		Map<String, String> validationErrors = new HashMap<>();
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			String fieldName;
			
			// Campo específico
			if (error instanceof FieldError) {
				fieldName = ((FieldError) error).getField();
			} else {
				// Objeto como um todo
				fieldName = error.getObjectName();
			}
			String errorMessage = error.getDefaultMessage();
			validationErrors.put(fieldName, errorMessage);
		}

		// Cria o objeto de erro estruturado com detalhes da validação
		ApiError apiError = new ApiError(status.value(), "Validation Error",
				"Um ou mais campos possuem erros de validação.", request.getRequestURI(), validationErrors);

		return new ResponseEntity<>(apiError, status);
	}
}
