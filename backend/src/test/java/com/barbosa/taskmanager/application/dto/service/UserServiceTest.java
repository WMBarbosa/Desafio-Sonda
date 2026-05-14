package com.barbosa.taskmanager.application.dto.service;

import com.barbosa.taskmanager.application.dto.UserDetailsDTO;
import com.barbosa.taskmanager.application.dto.response.UserResponseDTO;
import com.barbosa.taskmanager.application.dto.service.exception.ResourceNotFoundException;
import com.barbosa.taskmanager.domain.model.entities.Role;
import com.barbosa.taskmanager.domain.model.entities.User;
import com.barbosa.taskmanager.infrastructure.config.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    @Test
    void loadUserByUsername_whenNoRows_throwsUsernameNotFound() {
        when(repository.searchUserAndRolesByEmail("a@b.com")).thenReturn(List.of());

        assertThatThrownBy(() -> userService.loadUserByUsername("a@b.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Email not found");
    }

    @Test
    void loadUserByUsername_buildsUserWithRoles() {
        when(repository.searchUserAndRolesByEmail("user@test.com")).thenReturn(List.of(
                new UserDetailsDTO("user@test.com", "pwd", 1L, "ROLE_EMPLOYEE"),
                new UserDetailsDTO("user@test.com", "pwd", 2L, "ROLE_ADMIN")
        ));

        UserDetails details = userService.loadUserByUsername("user@test.com");

        assertThat(details.getUsername()).isEqualTo("user@test.com");
        assertThat(details.getPassword()).isEqualTo("pwd");
        assertThat(details.getAuthorities())
                .extracting(a -> a.getAuthority())
                .containsExactlyInAnyOrder("ROLE_EMPLOYEE", "ROLE_ADMIN");
    }

    @Test
    void getMe_withJwtPrincipal_returnsUserDto() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("username")).thenReturn("jwt@test.com");

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(jwt);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        User user = new User();
        user.setId(5L);
        user.setEmail("jwt@test.com");
        user.setName("Jwt User");
        user.addRoles(new Role(1L, "ROLE_EMPLOYEE"));
        when(repository.findByEmail("jwt@test.com")).thenReturn(Optional.of(user));

        try (MockedStatic<SecurityContextHolder> holder = org.mockito.Mockito.mockStatic(SecurityContextHolder.class)) {
            holder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            UserResponseDTO dto = userService.getMe();

            assertThat(dto.getEmail()).isEqualTo("jwt@test.com");
            assertThat(dto.getRoles()).containsExactly("ROLE_EMPLOYEE");
        }
    }

    @Test
    void getMe_whenUserMissing_throwsUsernameNotFound() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("username")).thenReturn("missing@test.com");

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(jwt);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(repository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

        try (MockedStatic<SecurityContextHolder> holder = org.mockito.Mockito.mockStatic(SecurityContextHolder.class)) {
            holder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            assertThatThrownBy(() -> userService.getMe())
                    .isInstanceOf(UsernameNotFoundException.class);
        }
    }

    @Test
    void findAll_mapsPage() {
        User u = new User();
        u.setId(1L);
        u.setEmail("e@e.com");
        Page<User> page = new PageImpl<>(List.of(u), PageRequest.of(0, 10), 1);
        when(repository.findAll(any(org.springframework.data.domain.Pageable.class))).thenReturn(page);

        Page<UserResponseDTO> result = userService.findAll(PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getEmail()).isEqualTo("e@e.com");
    }

    @Test
    void findById_whenMissing_throwsResourceNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
