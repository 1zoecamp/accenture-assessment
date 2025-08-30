package com.accenture.assessment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
