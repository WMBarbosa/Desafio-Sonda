package com.barbosa.taskmanager.application.dto.service;

import com.barbosa.taskmanager.application.dto.request.TaskRequestDTO;
import com.barbosa.taskmanager.application.dto.response.TaskResponseDTO;
import com.barbosa.taskmanager.application.dto.service.exception.ResourceNotFoundException;
import com.barbosa.taskmanager.domain.model.entities.Task;
import com.barbosa.taskmanager.domain.model.enums.Prioridade;
import com.barbosa.taskmanager.domain.model.enums.Status;
import com.barbosa.taskmanager.infrastructure.config.repository.TaskRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> findAll() {
        return taskRepository.findAll()
                .stream()
                .map(TaskResponseDTO::fromEntity)
                .toList();
    }


    @Transactional(readOnly = true)
    public TaskResponseDTO findById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return TaskResponseDTO.fromEntity(task);
    }


    @Transactional(readOnly = true)
    public List<TaskResponseDTO> findByStatus(Status status) {
        return taskRepository.findByStatus(status)
                .stream()
                .map(TaskResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
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


    @Transactional
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


    @Transactional
    public TaskResponseDTO update(Long id, TaskRequestDTO dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        task.setTitulo(dto.getTitulo());
        task.setDescricao(dto.getDescricao());
        task.setPrioridade(dto.getPrioridade());

        if (dto.getStatus() != null) {
            task.setStatus(dto.getStatus());
        }

        Task atualizada = taskRepository.save(task);
        return TaskResponseDTO.fromEntity(atualizada);
    }


    @Transactional
    public TaskResponseDTO updateStatus(Long id, Status novoStatus) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        task.setStatus(novoStatus);
        Task atualizada = taskRepository.save(task);
        return TaskResponseDTO.fromEntity(atualizada);
    }


    @Transactional
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }
}
