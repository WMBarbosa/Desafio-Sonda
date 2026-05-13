package com.barbosa.taskmanager.dto.response;

import com.barbosa.taskmanager.model.entities.User;
import com.barbosa.taskmanager.model.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private List<String> roles = new ArrayList<>();

    public static UserResponseDTO fromEntity(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getAuthority).toList())
                .build();
    }
}
