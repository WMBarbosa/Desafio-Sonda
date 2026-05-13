import { useEffect, useState } from "react";
import {
  Plus,
  Pencil,
  Trash2,
  RefreshCw,
  Loader2,
  Search,
} from "lucide-react";

import { taskService } from "../services/taskService";

import type { TaskResponseDTO, Status } from "../types";
import type { TaskFormData } from "../schemas";

import { TaskForm } from "../components/TaskForm";
import { StatusBadge, PrioridadeBadge } from "../components/Badges";
import { SortableHeader } from "../components/SortableHeader";

import { useSortableTable } from "../hooks/useSortableTable";
import { useAuth } from "../contexts/UseAuth";

function formatCriado(criado: string | number[] | undefined): string {
  if (criado == null) return "—";
  if (Array.isArray(criado)) {
    const [y, mo = 1, d = 1, h = 0, mi = 0, s = 0, ns = 0] = criado;
    const dt = new Date(y, mo - 1, d, h, mi, s, Math.floor(ns / 1_000_000));
    return Number.isNaN(dt.getTime()) ? "—" : dt.toLocaleDateString("pt-BR");
  }
  const dt = new Date(criado);
  return Number.isNaN(dt.getTime()) ? "—" : dt.toLocaleDateString("pt-BR");
}

export function TarefasPage() {
  const { isAdmin } = useAuth();

  const [tasks, setTasks] = useState<TaskResponseDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [loadError, setLoadError] = useState<string | null>(null);

  const [search, setSearch] = useState("");
  const [filterStatus, setFilterStatus] = useState<Status | "">("");

  const [showForm, setShowForm] = useState(false);
  const [editing, setEditing] = useState<TaskResponseDTO | null>(null);

  const [deleting, setDeleting] = useState<number | null>(null);

  const { sortedData, sort, handleSort } = useSortableTable(tasks);

  const filtered = sortedData.filter((t) => {
    const matchSearch = (t.titulo ?? "")
      .toLowerCase()
      .includes(search.toLowerCase());

    const matchStatus = filterStatus
      ? t.status === filterStatus
      : true;

    return matchSearch && matchStatus;
  });

  async function loadTasks() {
    setLoading(true);
    setLoadError(null);

    try {
      const data = await taskService.findAll();
      setTasks(data);
    } catch (error) {
      console.error("Erro ao carregar tarefas:", error);
      setLoadError("Não foi possível carregar as tarefas. Verifique a API e o token.");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    let mounted = true;

    async function fetchTasks() {
      setLoadError(null);
      try {
        const data = await taskService.findAll();

        if (mounted) {
          setTasks(data);
        }
      } catch (error) {
        console.error(error);
        if (mounted) {
          setLoadError("Não foi possível carregar as tarefas. Verifique a API e o token.");
        }
      } finally {
        if (mounted) {
          setLoading(false);
        }
      }
    }

    void fetchTasks();

    return () => {
      mounted = false;
    };
  }, []);

  async function handleSubmit(data: TaskFormData) {
    try {
      if (editing) {
        await taskService.update(editing.id, data);
      } else {
        await taskService.create(data);
      }

      setShowForm(false);
      setEditing(null);

      await loadTasks();
    } catch (error) {
      console.error("Erro ao salvar tarefa:", error);
    }
  }

  async function handleDelete(id: number) {
    if (!confirm("Tem certeza que deseja excluir esta tarefa?")) {
      return;
    }

    try {
      setDeleting(id);

      await taskService.delete(id);

      await loadTasks();
    } catch (error) {
      console.error("Erro ao excluir tarefa:", error);
    } finally {
      setDeleting(null);
    }
  }

  function openEdit(task: TaskResponseDTO) {
    setEditing(task);
    setShowForm(true);
  }

  return (
    <div className="space-y-5">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-xl font-bold text-white">
            Tarefas
          </h1>

          <p className="text-slate-400 text-sm mt-0.5">
            {tasks.length} tarefas no total
          </p>
        </div>

        <div className="flex items-center gap-2">
          <button
            onClick={() => void loadTasks()}
            className="p-2 text-slate-400 hover:text-white border border-slate-700 rounded-lg transition-colors"
          >
            <RefreshCw size={16} />
          </button>

          {isAdmin && (
            <button
              onClick={() => {
                setEditing(null);
                setShowForm(true);
              }}
              className="flex items-center gap-2 px-4 py-2 bg-indigo-600 hover:bg-indigo-500 text-white text-sm font-semibold rounded-lg transition-colors"
            >
              <Plus size={16} />
              Nova Tarefa
            </button>
          )}
        </div>
      </div>

      <div className="flex flex-col sm:flex-row gap-3">
        <div className="relative flex-1">
          <Search
            size={16}
            className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-500"
          />

          <input
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder="Buscar por título..."
            className="w-full pl-9 pr-4 py-2.5 bg-slate-800 border border-slate-700 rounded-lg text-white text-sm placeholder-slate-500 focus:outline-none focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500"
          />
        </div>

        <select
          value={filterStatus}
          onChange={(e) =>
            setFilterStatus(e.target.value as Status | "")
          }
          className="px-3 py-2.5 bg-slate-800 border border-slate-700 rounded-lg text-white text-sm focus:outline-none focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500"
        >
          <option value="">Todos os status</option>

          <option value="PENDENTE">
            Pendente
          </option>

          <option value="EM_ANDAMENTO">
            Em Andamento
          </option>

          <option value="CONCLUIDA">
            Concluída
          </option>

          <option value="CANCELADA">
            Cancelada
          </option>
        </select>
      </div>

      {loadError && (
        <div className="px-4 py-3 rounded-lg text-sm bg-amber-500/10 border border-amber-500/30 text-amber-200">
          {loadError}
        </div>
      )}

      <div className="bg-slate-900 border border-slate-800 rounded-xl overflow-hidden">
        {loading ? (
          <div className="flex items-center justify-center h-48">
            <Loader2
              className="animate-spin text-indigo-400"
              size={28}
            />
          </div>
        ) : loadError && tasks.length === 0 ? (
          <div className="flex items-center justify-center h-48 text-amber-200/90 text-sm px-6 text-center">
            {loadError}
          </div>
        ) : filtered.length === 0 ? (
          <div className="flex items-center justify-center h-48 text-slate-500 text-sm">
            Nenhuma tarefa encontrada.
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-slate-800/50 border-b border-slate-800">
                <tr>
                  <SortableHeader<TaskResponseDTO>
                    label="Título"
                    sortKey="titulo"
                    sort={sort}
                    onSort={handleSort}
                    className="min-w-[200px]"
                  />

                  <SortableHeader<TaskResponseDTO>
                    label="Status"
                    sortKey="status"
                    sort={sort}
                    onSort={handleSort}
                  />

                  <SortableHeader<TaskResponseDTO>
                    label="Prioridade"
                    sortKey="prioridade"
                    sort={sort}
                    onSort={handleSort}
                  />

                  <SortableHeader<TaskResponseDTO>
                    label="Criado em"
                    sortKey="criado"
                    sort={sort}
                    onSort={handleSort}
                  />

                  {isAdmin && (
                    <th className="px-4 py-3 text-right text-xs font-semibold uppercase tracking-wider text-slate-400">
                      Ações
                    </th>
                  )}
                </tr>
              </thead>

              <tbody className="divide-y divide-slate-800">
                {filtered.map((task) => (
                  <tr
                    key={task.id}
                    className="hover:bg-slate-800/40 transition-colors"
                  >
                    <td className="px-4 py-3">
                      <div>
                        <p className="text-sm text-white font-medium">
                          {task.titulo}
                        </p>

                        {task.descricao && (
                          <p className="text-xs text-slate-500 mt-0.5 truncate max-w-xs">
                            {task.descricao}
                          </p>
                        )}
                      </div>
                    </td>

                    <td className="px-4 py-3">
                      <StatusBadge status={task.status} />
                    </td>

                    <td className="px-4 py-3">
                      <PrioridadeBadge
                        prioridade={task.prioridade}
                      />
                    </td>

                    <td className="px-4 py-3 text-sm text-slate-400">
                      {formatCriado(task.criado as string | number[] | undefined)}
                    </td>

                    {isAdmin && (
                      <td className="px-4 py-3 text-right">
                        <div className="flex items-center justify-end gap-2">
                          <button
                            onClick={() => openEdit(task)}
                            className="p-1.5 text-slate-400 hover:text-indigo-400 hover:bg-indigo-400/10 rounded-lg transition-colors"
                          >
                            <Pencil size={15} />
                          </button>

                          <button
                            onClick={() =>
                              void handleDelete(task.id)
                            }
                            disabled={deleting === task.id}
                            className="p-1.5 text-slate-400 hover:text-red-400 hover:bg-red-400/10 rounded-lg transition-colors disabled:opacity-50"
                          >
                            {deleting === task.id ? (
                              <Loader2
                                size={15}
                                className="animate-spin"
                              />
                            ) : (
                              <Trash2 size={15} />
                            )}
                          </button>
                        </div>
                      </td>
                    )}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {showForm && (
        <TaskForm
          task={editing}
          onClose={() => {
            setShowForm(false);
            setEditing(null);
          }}
          onSubmit={handleSubmit}
        />
      )}
    </div>
  );
}