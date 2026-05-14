package com.barbosa.taskmanager.infrastructure.config.repository;

import com.barbosa.taskmanager.domain.model.entities.Task;
import com.barbosa.taskmanager.domain.model.enums.Prioridade;
import com.barbosa.taskmanager.domain.model.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(
        properties = {
                "spring.profiles.active=test",
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "spring.sql.init.mode=never"
        }
)
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        taskRepository.save(Task.builder()
                .titulo("Alpha task")
                .descricao("d1")
                .status(Status.PENDENTE)
                .prioridade(Prioridade.ALTA)
                .build());
        taskRepository.save(Task.builder()
                .titulo("Beta study")
                .descricao("d2")
                .status(Status.EM_ANDAMENTO)
                .prioridade(Prioridade.ALTA)
                .build());
        taskRepository.save(Task.builder()
                .titulo("Gamma")
                .descricao("d3")
                .status(Status.PENDENTE)
                .prioridade(Prioridade.MEDIA)
                .build());
    }

    @Test
    void findByStatus_returnsMatchingTasks() {
        List<Task> result = taskRepository.findByStatus(Status.PENDENTE);
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Task::getTitulo).containsExactlyInAnyOrder("Alpha task", "Gamma");
    }

    @Test
    void findByPrioridade_returnsMatchingTasks() {
        List<Task> result = taskRepository.findByPrioridade(Prioridade.ALTA);
        assertThat(result).hasSize(2);
    }

    @Test
    void findByStatusAndPrioridade_returnsIntersection() {
        List<Task> result = taskRepository.findByStatusAndPrioridade(Status.PENDENTE, Prioridade.ALTA);
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getTitulo()).isEqualTo("Alpha task");
    }

    @Test
    void findByTituloContainingIgnoreCase_isCaseInsensitive() {
        List<Task> result = taskRepository.findByTituloContainingIgnoreCase("alpha");
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getTitulo()).isEqualTo("Alpha task");
    }
}
