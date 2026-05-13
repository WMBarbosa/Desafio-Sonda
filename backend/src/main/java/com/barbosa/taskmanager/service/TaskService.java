package com.barbosa.taskmanager.service;

import com.barbosa.taskmanager.dto.TaskRequestDTO;
import com.barbosa.taskmanager.dto.TaskResponseDTO;
import com.barbosa.taskmanager.exception.TaskNotFoundException;
import com.barbosa.taskmanager.model.entities.Task;
import com.barbosa.taskmanager.model.enums.Prioridade;
import com.barbosa.taskmanager.model.enums.Status;
import com.barbosa.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;


    public List<TaskResponseDTO> findAll() {
        return taskRepository.findAll()
                .stream()
                .map(TaskResponseDTO::fromEntity)
                .toList();
    }


    public TaskResponseDTO findById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return TaskResponseDTO.fromEntity(task);
    }


    public List<TaskResponseDTO> findByStatus(Status status) {
        return taskRepository.findByStatus(status)
                .stream()
                .map(TaskResponseDTO::fromEntity)
                .toList();
    }


    public List<TaskResponseDTO> listByPriority(Prioridade prioridade) {
        return taskRepository.findByPrioridade(prioridade)
                .stream()
                .map(TaskResponseDTO::fromEntity)
                .toList();
    }

    public List<TaskResponseDTO> searchByTitle(String titulo) {
        return taskRepository.findByTituloContainingIgnoreCase(titulo)
                .stream()
                .map(TaskResponseDTO::fromEntity)
                .toList();
    }


    public TaskResponseDTO create(TaskRequestDTO dto) {
        Task task = Task.builder()
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .prioridade(dto.getPrioridade())
                .status(dto.getStatus() != null ? dto.getStatus() : Status.PENDENTE)
                .build();

        Task salva = taskRepository.save(task);
        return TaskResponseDTO.fromEntity(salva);
    }


    public TaskResponseDTO update(Long id, TaskRequestDTO dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitulo(dto.getTitulo());
        task.setDescricao(dto.getDescricao());
        task.setPrioridade(dto.getPrioridade());

        if (dto.getStatus() != null) {
            task.setStatus(dto.getStatus());
        }

        Task atualizada = taskRepository.save(task);
        return TaskResponseDTO.fromEntity(atualizada);
    }


    public TaskResponseDTO updateStatus(Long id, Status novoStatus) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setStatus(novoStatus);
        Task atualizada = taskRepository.save(task);
        return TaskResponseDTO.fromEntity(atualizada);
    }


    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }
}
