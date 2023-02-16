package com.sebastian.unobackend.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

// All exceptions response to the client will have this format
@Getter
@Setter
public class ExceptionResponse {
    private Date timestamp;
    private String message;
    private String details;

    public ExceptionResponse(Date timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

}
