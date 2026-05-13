package com.barbosa.taskmanager.application.dto.exceptionsDto;
import com.barbosa.taskmanager.api.controller.controllerException.CustomError;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationError extends CustomError {

    private final List<FieldMessage> fieldErrors = new ArrayList<>();

    public ValidationError(Instant timestamp, Integer status, String error, String path) {
        super(timestamp, status, error, path);
    }

    public void addError(String fieldName, String message) {
        fieldErrors.removeIf(e -> e.getFieldName().equals(fieldName));
        fieldErrors.add(new FieldMessage(fieldName, message));
    }
}
