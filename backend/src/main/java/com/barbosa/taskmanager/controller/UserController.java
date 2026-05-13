package com.barbosa.taskmanager.controller;

import com.barbosa.taskmanager.dto.response.UserResponseDTO;
import com.barbosa.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Listar usuários",
            description = "Retorna uma lista paginada de usuários. Acesso permitido para administradores e funcionários.")
    public ResponseEntity<Page<UserResponseDTO>> findAll(Pageable pageable) {
        Page<UserResponseDTO> dtoList = userService.findAll(pageable);
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Buscar usuário por ID",
            description = "Retorna os detalhes de um usuário específico com base no ID fornecido. Acesso permitido para administradores e funcionários.")
    public ResponseEntity<UserResponseDTO> findById (@PathVariable Long id) {
        UserResponseDTO user = userService.findById(id);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping(value = "/me")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Obter informações do usuário autenticado",
            description = "Retorna os detalhes do usuário atualmente autenticado. Acesso permitido para administradores e funcionários.")
    public ResponseEntity<UserResponseDTO> getMe () {
        UserResponseDTO user = userService.getMe();
        return ResponseEntity.ok().body(user);
    }

}
