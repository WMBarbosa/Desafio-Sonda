import type { Status, Prioridade } from "../types";

const statusConfig: Record<Status, { label: string; className: string }> = {
  PENDENTE: {
    label: "Pendente",
    className: "bg-yellow-500/20 text-yellow-400 border border-yellow-500/30",
  },
  EM_ANDAMENTO: {
    label: "Em Andamento",
    className: "bg-blue-500/20 text-blue-400 border border-blue-500/30",
  },
  CONCLUIDA: {
    label: "Concluída",
    className: "bg-green-500/20 text-green-400 border border-green-500/30",
  },
  CANCELADA: {
    label: "Cancelada",
    className: "bg-red-500/20 text-red-400 border border-red-500/30",
  },
};

const prioridadeConfig: Record<Prioridade, { label: string; className: string }> = {
  BAIXA: {
    label: "Baixa",
    className: "bg-slate-500/20 text-slate-400 border border-slate-500/30",
  },
  MEDIA: {
    label: "Média",
    className: "bg-sky-500/20 text-sky-400 border border-sky-500/30",
  },
  ALTA: {
    label: "Alta",
    className: "bg-orange-500/20 text-orange-400 border border-orange-500/30",
  },
  URGENTE: {
    label: "Urgente",
    className: "bg-red-500/20 text-red-400 border border-red-500/30",
  },
};

export function StatusBadge({ status }: { status: Status }) {
  const config = statusConfig[status];
  if (!config) {
    return (
      <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-slate-600/30 text-slate-300 border border-slate-500/40">
        {String(status)}
      </span>
    );
  }
  return (
    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${config.className}`}>
      {config.label}
    </span>
  );
}

export function PrioridadeBadge({ prioridade }: { prioridade: Prioridade }) {
  const config = prioridadeConfig[prioridade];
  if (!config) {
    return (
      <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-slate-600/30 text-slate-300 border border-slate-500/40">
        {String(prioridade)}
      </span>
    );
  }
  return (
    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${config.className}`}>
      {config.label}
    </span>
  );
}

export function RoleBadge({ role }: { role: string }) {
  const isAdmin = role === "ROLE_ADMIN";
  return (
    <span
      className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
        isAdmin
          ? "bg-indigo-500/20 text-indigo-400 border border-indigo-500/30"
          : "bg-slate-500/20 text-slate-400 border border-slate-500/30"
      }`}
    >
      {isAdmin ? "Admin" : "Employee"}
    </span>
  );
}
