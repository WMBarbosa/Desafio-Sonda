export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  access_token: string;
  token_type: string;
}


export type RoleAuthority = "ROLE_ADMIN" | "ROLE_EMPLOYEE";

export interface Role {
  id: number;
  authority: RoleAuthority;
}


export interface UserResponseDTO {
  id: number;
  name: string;
  email: string;
  roles: Role[];
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}


export type Status =
  | "PENDENTE"
  | "EM_ANDAMENTO"
  | "CONCLUIDA"
  | "CANCELADA";

export type Prioridade = "BAIXA" | "MEDIA" | "ALTA" | "URGENTE";

export interface TaskResponseDTO {
  id: number;
  titulo: string;
  descricao: string;
  status: Status;
  prioridade: Prioridade;
  criado: string;
  atualizado: string | null;
}

export interface TaskRequestDTO {
  titulo: string;
  descricao?: string;
  status: Status;
  prioridade: Prioridade;
}