package com.barbosa.taskmanager.dto;

import com.barbosa.taskmanager.model.entities.Task;
import com.barbosa.taskmanager.model.enums.Prioridade;
import com.barbosa.taskmanager.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponseDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private Status status;
    private Prioridade prioridade;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public static TaskResponseDTO fromEntity(Task task) {
        return TaskResponseDTO.builder()
                .id(task.getId())
                .titulo(task.getTitulo())
                .descricao(task.getDescricao())
                .status(task.getStatus())
                .prioridade(task.getPrioridade())
                .criadoEm(task.getCriadoEm())
                .atualizadoEm(task.getAtualizadoEm())
                .build();
    }
}
