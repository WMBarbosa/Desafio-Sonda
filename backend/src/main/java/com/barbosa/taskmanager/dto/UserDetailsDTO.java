package com.barbosa.taskmanager.dto;

public record UserDetailsDTO(String username, String password, Long roleId, String authority) {
}
