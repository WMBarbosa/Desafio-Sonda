import api from "../lib/api";
import type { Page, UserResponseDTO } from "../types";

export const userService = {
  async findAll(page = 0, size = 10): Promise<Page<UserResponseDTO>> {
    const { data } = await api.get<Page<UserResponseDTO>>("/users", {
      params: { page, size },
    });
    return data;
  },

  async findById(id: number): Promise<UserResponseDTO> {
    const { data } = await api.get<UserResponseDTO>(`/users/${id}`);
    return data;
  },

  async getMe(): Promise<UserResponseDTO> {
    const { data } = await api.get<UserResponseDTO>("/users/me");
    return data;
  },
};