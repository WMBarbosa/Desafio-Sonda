package com.barbosa.taskmanager.application.dto.service.exception;

public class DatabaseException extends RuntimeException {

    public DatabaseException(String message) {
        super(message);
    }
}
