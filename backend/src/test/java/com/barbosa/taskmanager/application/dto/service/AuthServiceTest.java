package com.barbosa.taskmanager.application.dto.service;

import com.barbosa.taskmanager.application.dto.service.exception.ForbiddenException;
import com.barbosa.taskmanager.domain.model.entities.Role;
import com.barbosa.taskmanager.domain.model.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userService);
    }

    @Test
    void validateSelfOrAdmin_whenAdmin_doesNotThrow() {
        User admin = new User();
        admin.setId(1L);
        admin.addRoles(new Role(2L, "ROLE_ADMIN"));
        when(userService.authenticate()).thenReturn(admin);

        assertThatCode(() -> authService.validateSelfOrAdmin(999L)).doesNotThrowAnyException();
    }

    @Test
    void validateSelfOrAdmin_whenSelf_doesNotThrow() {
        User user = new User();
        user.setId(7L);
        user.addRoles(new Role(1L, "ROLE_EMPLOYEE"));
        when(userService.authenticate()).thenReturn(user);

        assertThatCode(() -> authService.validateSelfOrAdmin(7L)).doesNotThrowAnyException();
    }

    @Test
    void validateSelfOrAdmin_whenOtherUserAndNotAdmin_throwsForbidden() {
        User user = new User();
        user.setId(7L);
        user.addRoles(new Role(1L, "ROLE_EMPLOYEE"));
        when(userService.authenticate()).thenReturn(user);

        assertThatThrownBy(() -> authService.validateSelfOrAdmin(8L))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("Access denied");
    }
}
