package com.barbosa.taskmanager.api.controller;

import com.barbosa.taskmanager.application.dto.request.TaskRequestDTO;
import com.barbosa.taskmanager.application.dto.response.TaskResponseDTO;
import com.barbosa.taskmanager.application.dto.service.TaskService;
import com.barbosa.taskmanager.domain.model.enums.Prioridade;
import com.barbosa.taskmanager.domain.model.enums.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    void listar_withoutParams_callsFindAll() throws Exception {
        TaskResponseDTO dto = TaskResponseDTO.builder().id(1L).titulo("t").status(Status.PENDENTE).prioridade(Prioridade.BAIXA).build();
        when(taskService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/tarefas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titulo").value("t"));

        verify(taskService).findAll();
    }

    @Test
    void listar_withStatus_callsFindByStatus() throws Exception {
        when(taskService.findByStatus(Status.CONCLUIDA)).thenReturn(List.of());

        mockMvc.perform(get("/api/tarefas").param("status", "CONCLUIDA"))
                .andExpect(status().isOk());

        verify(taskService).findByStatus(Status.CONCLUIDA);
    }

    @Test
    void buscarPorId_returnsOk() throws Exception {
        TaskResponseDTO dto = TaskResponseDTO.builder().id(2L).titulo("x").status(Status.PENDENTE).prioridade(Prioridade.MEDIA).build();
        when(taskService.findById(2L)).thenReturn(dto);

        mockMvc.perform(get("/api/tarefas/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void criar_returnsCreated() throws Exception {
        TaskRequestDTO request = TaskRequestDTO.builder()
                .titulo("Nova tarefa")
                .descricao("d")
                .prioridade(Prioridade.ALTA)
                .build();
        TaskResponseDTO response = TaskResponseDTO.builder()
                .id(10L)
                .titulo("Nova tarefa")
                .descricao("d")
                .status(Status.PENDENTE)
                .prioridade(Prioridade.ALTA)
                .build();
        when(taskService.create(any(TaskRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void atualizarStatus_invalidStatus_returnsBadRequest() throws Exception {
        mockMvc.perform(patch("/api/tarefas/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", "INVALIDO"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void atualizarStatus_missingStatus_returnsBadRequest() throws Exception {
        mockMvc.perform(patch("/api/tarefas/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void atualizarStatus_valid_callsService() throws Exception {
        TaskResponseDTO dto = TaskResponseDTO.builder()
                .id(1L)
                .titulo("t")
                .status(Status.CONCLUIDA)
                .prioridade(Prioridade.BAIXA)
                .build();
        when(taskService.updateStatus(eq(1L), eq(Status.CONCLUIDA))).thenReturn(dto);

        mockMvc.perform(patch("/api/tarefas/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", "CONCLUIDA"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONCLUIDA"));
    }

    @Test
    void deletar_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/tarefas/5"))
                .andExpect(status().isNoContent());

        verify(taskService).delete(5L);
    }

    @Test
    void atualizar_returnsOk() throws Exception {
        TaskRequestDTO request = TaskRequestDTO.builder()
                .titulo("Atualizado")
                .prioridade(Prioridade.URGENTE)
                .build();
        TaskResponseDTO response = TaskResponseDTO.builder()
                .id(3L)
                .titulo("Atualizado")
                .status(Status.EM_ANDAMENTO)
                .prioridade(Prioridade.URGENTE)
                .build();
        when(taskService.update(eq(3L), any(TaskRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/tarefas/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Atualizado"));
    }
}
