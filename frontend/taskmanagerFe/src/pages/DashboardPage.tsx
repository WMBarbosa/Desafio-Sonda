import { useEffect, useState } from "react";
import { CheckSquare, Clock, AlertCircle, Loader2 } from "lucide-react";
import { taskService } from "../services/taskService";
import { userService } from "../services/userService";
import type { TaskResponseDTO, UserResponseDTO } from "../types";
import { useAuth } from "../contexts/UseAuth";

export function DashboardPage() {
  const { isAdmin } = useAuth();
  const [tasks, setTasks] = useState<TaskResponseDTO[]>([]);
  const [me, setMe] = useState<UserResponseDTO | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function load() {
      const [tasksData, meData] = await Promise.all([
        taskService.findAll(),
        userService.getMe(),
      ]);
      setTasks(tasksData);
      setMe(meData);
      setLoading(false);
    }
    load();
  }, []);

  const stats = {
    total: tasks.length,
    pendente: tasks.filter((t) => t.status === "PENDENTE").length,
    emAndamento: tasks.filter((t) => t.status === "EM_ANDAMENTO").length,
    concluida: tasks.filter((t) => t.status === "CONCLUIDA").length,
    cancelada: tasks.filter((t) => t.status === "CANCELADA").length,
    critica: tasks.filter((t) => t.prioridade === "CRITICA").length,
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <Loader2 className="animate-spin text-indigo-400" size={32} />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Welcome */}
      <div>
        <h1 className="text-2xl font-bold text-white">
          Olá, {me?.name?.split(" ")[0]} 👋
        </h1>
        <p className="text-slate-400 text-sm mt-1">
          {isAdmin ? "Administrador" : "Funcionário"} · {me?.email}
        </p>
      </div>

      {/* Stat cards */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        <StatCard
          icon={<CheckSquare size={20} />}
          label="Total de Tarefas"
          value={stats.total}
          color="indigo"
        />
        <StatCard
          icon={<Clock size={20} />}
          label="Em Andamento"
          value={stats.emAndamento}
          color="blue"
        />
        <StatCard
          icon={<CheckSquare size={20} />}
          label="Concluídas"
          value={stats.concluida}
          color="green"
        />
        <StatCard
          icon={<AlertCircle size={20} />}
          label="Críticas"
          value={stats.critica}
          color="red"
        />
      </div>

      {/* Recent tasks */}
      <div className="bg-slate-900 border border-slate-800 rounded-xl p-5">
        <h2 className="text-sm font-semibold text-slate-300 mb-4">Tarefas Recentes</h2>
        <div className="space-y-3">
          {tasks.slice(0, 5).map((task) => (
            <div
              key={task.id}
              className="flex items-center justify-between py-2 border-b border-slate-800 last:border-0"
            >
              <span className="text-sm text-white truncate max-w-xs">{task.titulo}</span>
              <span className="text-xs text-slate-500">
                {new Date(task.criado).toLocaleDateString("pt-BR")}
              </span>
            </div>
          ))}
          {tasks.length === 0 && (
            <p className="text-slate-500 text-sm">Nenhuma tarefa encontrada.</p>
          )}
        </div>
      </div>
    </div>
  );
}

function StatCard({
  icon,
  label,
  value,
  color,
}: {
  icon: React.ReactNode;
  label: string;
  value: number;
  color: "indigo" | "blue" | "green" | "red";
}) {
  const colors = {
    indigo: "bg-indigo-500/10 text-indigo-400 border-indigo-500/20",
    blue: "bg-blue-500/10 text-blue-400 border-blue-500/20",
    green: "bg-green-500/10 text-green-400 border-green-500/20",
    red: "bg-red-500/10 text-red-400 border-red-500/20",
  };

  return (
    <div className="bg-slate-900 border border-slate-800 rounded-xl p-4">
      <div className={`inline-flex p-2 rounded-lg border ${colors[color]} mb-3`}>
        {icon}
      </div>
      <div className="text-2xl font-bold text-white">{value}</div>
      <div className="text-xs text-slate-400 mt-0.5">{label}</div>
    </div>
  );
}