package com.barbosa.taskmanager.api.controller.controllerException;

import lombok.Getter;

import java.time.Instant;

@Getter
public class CustomError {

    private final Instant timestamp;
    private final Integer status;
    private final String error;
    private final String path;


    public CustomError(Instant timestamp, Integer status, String error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
    }

}

