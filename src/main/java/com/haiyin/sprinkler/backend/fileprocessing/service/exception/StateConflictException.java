package com.haiyin.sprinkler.backend.fileprocessing.service.exception;

public class StateConflictException extends RuntimeException {
    public StateConflictException(String message) {
        super(message);
    }
}
