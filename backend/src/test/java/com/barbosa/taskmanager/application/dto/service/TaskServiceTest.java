package com.barbosa.taskmanager.application.dto.service;

import com.barbosa.taskmanager.application.dto.request.TaskRequestDTO;
import com.barbosa.taskmanager.application.dto.response.TaskResponseDTO;
import com.barbosa.taskmanager.application.dto.service.exception.ResourceNotFoundException;
import com.barbosa.taskmanager.domain.model.entities.Task;
import com.barbosa.taskmanager.domain.model.enums.Prioridade;
import com.barbosa.taskmanager.domain.model.enums.Status;
import com.barbosa.taskmanager.infrastructure.config.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void findAll_mapsEntitiesToDto() {
        Task task = Task.builder()
                .id(1L)
                .titulo("t")
                .descricao("d")
                .status(Status.PENDENTE)
                .prioridade(Prioridade.BAIXA)
                .build();
        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<TaskResponseDTO> result = taskService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getTitulo()).isEqualTo("t");
    }

    @Test
    void findById_whenMissing_throwsResourceNotFound() {
        when(taskRepository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.findById(5L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void findById_whenPresent_returnsDto() {
        Task task = Task.builder()
                .id(2L)
                .titulo("x")
                .descricao("y")
                .status(Status.CONCLUIDA)
                .prioridade(Prioridade.MEDIA)
                .build();
        when(taskRepository.findById(2L)).thenReturn(Optional.of(task));

        TaskResponseDTO dto = taskService.findById(2L);

        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getStatus()).isEqualTo(Status.CONCLUIDA);
    }

    @Test
    void create_defaultsStatusToPendenteWhenNull() {
        TaskRequestDTO request = TaskRequestDTO.builder()
                .titulo("Nova")
                .descricao("desc")
                .prioridade(Prioridade.ALTA)
                .status(null)
                .build();
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task t = invocation.getArgument(0);
            t.setId(10L);
            return t;
        });

        TaskResponseDTO dto = taskService.create(request);

        assertThat(dto.getTitulo()).isEqualTo("Nova");
        assertThat(dto.getStatus()).isEqualTo(Status.PENDENTE);
    }

    @Test
    void update_whenMissing_throws() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.update(1L, TaskRequestDTO.builder()
                .titulo("a")
                .prioridade(Prioridade.BAIXA)
                .build()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateStatus_persistsNewStatus() {
        Task task = Task.builder()
                .id(3L)
                .titulo("t")
                .descricao("d")
                .status(Status.PENDENTE)
                .prioridade(Prioridade.BAIXA)
                .build();
        when(taskRepository.findById(3L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponseDTO dto = taskService.updateStatus(3L, Status.EM_ANDAMENTO);

        assertThat(dto.getStatus()).isEqualTo(Status.EM_ANDAMENTO);
        verify(taskRepository).save(task);
    }

    @Test
    void delete_whenMissing_throws() {
        when(taskRepository.existsById(9L)).thenReturn(false);

        assertThatThrownBy(() -> taskService.delete(9L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_whenPresent_callsRepository() {
        when(taskRepository.existsById(9L)).thenReturn(true);

        taskService.delete(9L);

        verify(taskRepository).deleteById(9L);
    }
}
