import api from "../lib/api";
import { LoginRequest, LoginResponse } from "../types";

export const authService = {
  async login(credentials: LoginRequest): Promise<LoginResponse> {
    const params = new URLSearchParams();
    params.append("username", credentials.username);
    params.append("password", credentials.password);

    const { data } = await api.post<LoginResponse>("/login", params, {
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
    });
    return data;
  },
};