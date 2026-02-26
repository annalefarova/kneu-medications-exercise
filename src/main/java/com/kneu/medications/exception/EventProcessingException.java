package com.kneu.medications.exception;

import lombok.Getter;

@Getter
public class EventProcessingException extends RuntimeException {
    private final int statusCode;

    public EventProcessingException(String message, int status) {
        super(message);
        this.statusCode = status;
    }
}