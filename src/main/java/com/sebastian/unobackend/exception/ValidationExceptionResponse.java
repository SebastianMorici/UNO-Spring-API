package com.sebastian.unobackend.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

// Since validation exceptions need another format, this class is created
@Getter
@Setter
public class ValidationExceptionResponse {
    private String errorMessage = "Fields validation error";
    private Map<String, String> fieldErrors;

    public ValidationExceptionResponse() {
        this.fieldErrors = new HashMap<>();
    }

    public void addFieldError(String field, String message) {
        fieldErrors.put(field, message);
    }
}
