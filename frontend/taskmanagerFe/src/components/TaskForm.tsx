import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { X, Loader2 } from "lucide-react";
import { taskSchema } from "../schemas";
import type { TaskFormData } from "../schemas";
import type{ TaskResponseDTO } from "../types";

interface TaskFormProps {
  task?: TaskResponseDTO | null;
  onClose: () => void;
  onSubmit: (data: TaskFormData) => Promise<void>;
}

export function TaskForm({ task, onClose, onSubmit }: TaskFormProps) {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors, isSubmitting },
  } = useForm<TaskFormData>({
    resolver: zodResolver(taskSchema),
  });

  useEffect(() => {
    if (task) {
      reset({
        titulo: task.titulo,
        descricao: task.descricao ?? "",
        status: task.status,
        prioridade: task.prioridade,
      });
    } else {
      reset({ titulo: "", descricao: "", status: "PENDENTE", prioridade: "MEDIA" });
    }
  }, [task, reset]);

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/70">
      <div className="w-full max-w-lg bg-slate-900 border border-slate-800 rounded-2xl shadow-2xl">
        {/* Header */}
        <div className="flex items-center justify-between px-6 py-4 border-b border-slate-800">
          <h2 className="text-base font-semibold text-white">
            {task ? "Editar Tarefa" : "Nova Tarefa"}
          </h2>
          <button
            onClick={onClose}
            className="text-slate-400 hover:text-white transition-colors"
          >
            <X size={20} />
          </button>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit(onSubmit)} className="p-6 space-y-5">
          {/* Título */}
          <Field label="Título" error={errors.titulo?.message}>
            <input
              {...register("titulo")}
              className={inputClass}
              placeholder="Título da tarefa"
            />
          </Field>

          {/* Descrição */}
          <Field label="Descrição" error={errors.descricao?.message}>
            <textarea
              {...register("descricao")}
              rows={3}
              className={`${inputClass} resize-none`}
              placeholder="Descrição opcional..."
            />
          </Field>

          <div className="grid grid-cols-2 gap-4">
            {/* Status */}
            <Field label="Status" error={errors.status?.message}>
              <select {...register("status")} className={inputClass}>
                <option value="PENDENTE">Pendente</option>
                <option value="EM_ANDAMENTO">Em Andamento</option>
                <option value="CONCLUIDA">Concluída</option>
                <option value="CANCELADA">Cancelada</option>
              </select>
            </Field>

            {/* Prioridade */}
            <Field label="Prioridade" error={errors.prioridade?.message}>
              <select {...register("prioridade")} className={inputClass}>
                <option value="BAIXA">Baixa</option>
                <option value="MEDIA">Média</option>
                <option value="ALTA">Alta</option>
                <option value="CRITICA">Crítica</option>
              </select>
            </Field>
          </div>

          {/* Actions */}
          <div className="flex justify-end gap-3 pt-2">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 text-sm font-medium text-slate-300 hover:text-white border border-slate-700 rounded-lg hover:border-slate-600 transition-colors"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={isSubmitting}
              className="flex items-center gap-2 px-4 py-2 text-sm font-semibold bg-indigo-600 hover:bg-indigo-500 disabled:opacity-60 rounded-lg text-white transition-colors"
            >
              {isSubmitting && <Loader2 size={14} className="animate-spin" />}
              {task ? "Salvar" : "Criar"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

function Field({
  label,
  error,
  children,
}: {
  label: string;
  error?: string;
  children: React.ReactNode;
}) {
  return (
    <div>
      <label className="block text-sm font-medium text-slate-300 mb-1.5">{label}</label>
      {children}
      {error && <p className="mt-1.5 text-xs text-red-400">{error}</p>}
    </div>
  );
}

const inputClass =
  "w-full px-3 py-2.5 bg-slate-800 border border-slate-700 rounded-lg text-white text-sm placeholder-slate-500 focus:outline-none focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 transition-colors";
