package com.barbosa.taskmanager.infrastructure.config.repository;

import com.barbosa.taskmanager.domain.model.entities.Task;
import com.barbosa.taskmanager.domain.model.enums.Prioridade;
import com.barbosa.taskmanager.domain.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatus(Status status);

    List<Task> findByPrioridade(Prioridade prioridade);

    List<Task> findByStatusAndPrioridade(Status status, Prioridade prioridade);

    List<Task> findByTituloContainingIgnoreCase(String titulo);
}
