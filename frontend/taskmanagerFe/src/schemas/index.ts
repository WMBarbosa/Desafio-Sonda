import { z } from "zod";


export const loginSchema = z.object({
  username: z
    .string()
    .min(1, "E-mail é obrigatório")
    .email("E-mail inválido"),
  password: z.string().min(1, "Senha é obrigatória"),
});

export type LoginFormData = z.infer<typeof loginSchema>;


export const taskSchema = z.object({
  titulo: z
    .string()
    .min(1, "Título é obrigatório")
    .max(150, "Máximo de 150 caracteres"),
  descricao: z.string().max(500, "Máximo de 500 caracteres").optional(),
  status: z.enum(["PENDENTE", "EM_ANDAMENTO", "CONCLUIDA", "CANCELADA"], {
    required_error: "Status é obrigatório",
  }),
  prioridade: z.enum(["BAIXA", "MEDIA", "ALTA", "URGENTE"], {
    required_error: "Prioridade é obrigatória",
  }),
});

export type TaskFormData = z.infer<typeof taskSchema>;


export const statusSchema = z.object({
  status: z.enum(["PENDENTE", "EM_ANDAMENTO", "CONCLUIDA", "CANCELADA"]),
});

export type StatusFormData = z.infer<typeof statusSchema>;