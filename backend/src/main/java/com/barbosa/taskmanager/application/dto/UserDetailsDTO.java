package com.barbosa.taskmanager.application.dto;

public record UserDetailsDTO(String username, String password, Long roleId, String authority) {
}
