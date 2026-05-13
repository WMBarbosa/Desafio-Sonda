package com.barbosa.taskmanager.application.dto.service;


import com.barbosa.taskmanager.domain.model.entities.User;
import com.barbosa.taskmanager.application.dto.service.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService UserService;

    public void validateSelfOrAdmin(Long userId){
        User user = UserService.authenticate();
        if (!user.hasRole("ROLE_ADMIN") && !user.getId().equals(userId)) {
            throw new ForbiddenException("Access denied");
        }
    }

}
