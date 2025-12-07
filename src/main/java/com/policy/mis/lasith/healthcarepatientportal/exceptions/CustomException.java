package com.policy.mis.lasith.healthcarepatientportal.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@SuppressWarnings("unused")
public class CustomException extends RuntimeException{

    @Getter
    private final HttpStatus status;

    public CustomException(HttpStatus status) {
        this.status = status;
    }

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public CustomException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

}
