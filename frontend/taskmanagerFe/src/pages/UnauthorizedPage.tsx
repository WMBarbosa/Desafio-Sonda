import { useNavigate } from "react-router-dom";
import { ShieldOff } from "lucide-react";

export function UnauthorizedPage() {
  const navigate = useNavigate();
  return (
    <div className="min-h-screen bg-slate-950 flex items-center justify-center">
      <div className="text-center space-y-4">
        <div className="flex justify-center">
          <ShieldOff size={48} className="text-red-400" />
        </div>
        <h1 className="text-2xl font-bold text-white">Acesso Negado</h1>
        <p className="text-slate-400 text-sm">
          Você não tem permissão para acessar esta página.
        </p>
        <button
          onClick={() => navigate("/dashboard")}
          className="px-4 py-2 bg-indigo-600 hover:bg-indigo-500 text-white text-sm font-semibold rounded-lg transition-colors"
        >
          Voltar ao Dashboard
        </button>
      </div>
    </div>
  );
}