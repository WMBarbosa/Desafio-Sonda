package com.barbosa.taskmanager.model.entities;

import com.barbosa.taskmanager.model.enums.Prioridade;
import com.barbosa.taskmanager.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 150)
        private String titulo;

        @Column(length = 500)
        private String descricao;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Status status;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Prioridade prioridade;

        @Column(name = "criado", nullable = false, updatable = false)
        private LocalDateTime criado;

        @Column(name = "atualizado")
        private LocalDateTime atualizado;

        @PrePersist
        public void prePersist() {
            this.criado = LocalDateTime.now();
            this.atualizado = LocalDateTime.now();
            if (this.status == null) {
                this.status = Status.PENDENTE;
            }
        }

        @PreUpdate
        public void preUpdate() {
            this.atualizado = LocalDateTime.now();
        }

}
