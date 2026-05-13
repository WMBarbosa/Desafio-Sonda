import api from "../lib/api";
import type {
  Prioridade,
  Status,
  TaskRequestDTO,
  TaskResponseDTO,
} from "../types";

function normalizeTaskList(payload: unknown): TaskResponseDTO[] {
  if (Array.isArray(payload)) {
    return payload as TaskResponseDTO[];
  }
  if (
    payload &&
    typeof payload === "object" &&
    "content" in payload &&
    Array.isArray((payload as { content: unknown }).content)
  ) {
    return (payload as { content: TaskResponseDTO[] }).content;
  }
  return [];
}

export const taskService = {
  async findAll(params?: {
    status?: Status;
    prioridade?: Prioridade;
    titulo?: string;
  }): Promise<TaskResponseDTO[]> {
    const { data } = await api.get<unknown>("/api/tarefas", {
      params,
    });
    return normalizeTaskList(data);
  },

  async findById(id: number): Promise<TaskResponseDTO> {
    const { data } = await api.get<TaskResponseDTO>(`/api/tarefas/${id}`);
    return data;
  },

  async create(dto: TaskRequestDTO): Promise<TaskResponseDTO> {
    const { data } = await api.post<TaskResponseDTO>("/api/tarefas", dto);
    return data;
  },

  async update(id: number, dto: TaskRequestDTO): Promise<TaskResponseDTO> {
    const { data } = await api.put<TaskResponseDTO>(`/api/tarefas/${id}`, dto);
    return data;
  },

  async updateStatus(id: number, status: Status): Promise<TaskResponseDTO> {
    const { data } = await api.patch<TaskResponseDTO>(
      `/api/tarefas/${id}/status`,
      { status }
    );
    return data;
  },

  async delete(id: number): Promise<void> {
    await api.delete(`/api/tarefas/${id}`);
  },
};