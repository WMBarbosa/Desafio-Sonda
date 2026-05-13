package com.barbosa.taskmanager.controller;


import com.barbosa.taskmanager.dto.TaskRequestDTO;
import com.barbosa.taskmanager.dto.TaskResponseDTO;
import com.barbosa.taskmanager.model.entities.Task;
import com.barbosa.taskmanager.model.enums.Prioridade;
import com.barbosa.taskmanager.model.enums.Status;
import com.barbosa.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tarefas")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;


    @GetMapping
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
    public ResponseEntity<TaskResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }


    @PostMapping
    public ResponseEntity<TaskResponseDTO> criar(@RequestBody @Valid TaskRequestDTO dto) {
        TaskResponseDTO criada = taskService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }


    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid TaskRequestDTO dto) {

        return ResponseEntity.ok(taskService.update(id, dto));
    }


    @PatchMapping("/{id}/status")
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
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
