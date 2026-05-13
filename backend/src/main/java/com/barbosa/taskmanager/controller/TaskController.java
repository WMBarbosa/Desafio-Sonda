package com.barbosa.taskmanager.controller;


import com.barbosa.taskmanager.dto.request.TaskRequestDTO;
import com.barbosa.taskmanager.dto.response.TaskResponseDTO;
import com.barbosa.taskmanager.model.enums.Prioridade;
import com.barbosa.taskmanager.model.enums.Status;
import com.barbosa.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tarefas")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;


    @GetMapping
    @Operation(summary = "Lista todas as tarefas ou filtra por status, prioridade ou título")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<List<TaskResponseDTO>> listar(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Prioridade prioridade,
            @RequestParam(required = false) String titulo) {

        if (status != null) {
            return ResponseEntity.ok(taskService.findByStatus(status));
        }
        if (prioridade != null) {
            return ResponseEntity.ok(taskService.listByPriority(prioridade));
        }
        if (titulo != null && !titulo.isBlank()) {
            return ResponseEntity.ok(taskService.searchByTitle(titulo));
        }

        return ResponseEntity.ok(taskService.findAll());
    }


    @GetMapping("/{id}")
    @Operation(summary = "Busca uma tarefa por ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<TaskResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }


    @PostMapping
    @Operation(summary = "Cria uma nova tarefa")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<TaskResponseDTO> criar(@RequestBody @Valid TaskRequestDTO dto) {
        TaskResponseDTO criada = taskService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Atualiza uma tarefa existente")
    public ResponseEntity<TaskResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid TaskRequestDTO dto) {

        return ResponseEntity.ok(taskService.update(id, dto));
    }


    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualiza o status de uma tarefa existente")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<TaskResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String statusStr = body.get("status");
        if (statusStr == null || statusStr.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Status novoStatus;
        try {
            novoStatus = Status.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(taskService.updateStatus(id, novoStatus));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta uma tarefa existente")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
