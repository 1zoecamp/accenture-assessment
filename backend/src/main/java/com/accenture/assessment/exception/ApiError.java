package com.accenture.assessment.exception;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

/*
 * Estrutura padrão para exceptions tratadas
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

	private LocalDateTime timestamp;
	private Integer status;
	private String error;
	private String message;
	private String path;
	private Map<String, String> validationErrors;

	// Construtor padrão
	public ApiError(Integer status, String error, String message, String path) {
		this.timestamp = LocalDateTime.now();
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}

	// Construtor para erros de validação
	public ApiError(Integer status, String error, String message, String path, Map<String, String> validationErrors) {
		this(status, error, message, path);
		this.validationErrors = validationErrors;
	}

	// Getters e Setters
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public Map<String, String> getValidationErrors() {
		return validationErrors;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
