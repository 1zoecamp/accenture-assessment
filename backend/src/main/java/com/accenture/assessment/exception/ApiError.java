package com.accenture.assessment.exception;

import java.time.LocalDateTime;

/*
 * Estrutura a resposta de erro para exceptions
 */
public class ApiError {

 private LocalDateTime timestamp;
 private Integer status;
 private String error;
 private String message;
 private String path;

 // Constructor
 public ApiError(Integer status, String error, String message, String path) {
     this.timestamp = LocalDateTime.now();
     this.status = status;
     this.error = error;
     this.message = message;
     this.path = path;
 }

 // Getters and Setters
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
