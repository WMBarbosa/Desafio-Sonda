package com.barbosa.taskmanager.api.controller;

import com.barbosa.taskmanager.application.dto.response.UserResponseDTO;
import com.barbosa.taskmanager.application.dto.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void findAll_returnsPage() throws Exception {
        UserResponseDTO dto = UserResponseDTO.builder()
                .id(1L)
                .name("n")
                .email("e@e.com")
                .roles(List.of("ROLE_EMPLOYEE"))
                .build();
        Page<UserResponseDTO> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 20), 1);
        when(userService.findAll(any(org.springframework.data.domain.Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/users").param("page", "0").param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").value("e@e.com"));

        verify(userService).findAll(any(org.springframework.data.domain.Pageable.class));
    }

    @Test
    void findById_returnsUser() throws Exception {
        UserResponseDTO dto = UserResponseDTO.builder()
                .id(2L)
                .name("n")
                .email("u@u.com")
                .roles(List.of())
                .build();
        when(userService.findById(2L)).thenReturn(dto);

        mockMvc.perform(get("/users/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.email").value("u@u.com"));
    }

    @Test
    void getMe_returnsCurrentUser() throws Exception {
        UserResponseDTO dto = UserResponseDTO.builder()
                .id(3L)
                .name("me")
                .email("me@me.com")
                .roles(List.of("ROLE_ADMIN"))
                .build();
        when(userService.getMe()).thenReturn(dto);

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("me@me.com"));

        verify(userService).getMe();
    }
}
