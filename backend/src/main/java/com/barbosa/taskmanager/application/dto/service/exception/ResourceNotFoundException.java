package com.barbosa.taskmanager.application.dto.service.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Object id) {
        super("Resource not found. id: " + id);
    }
}
