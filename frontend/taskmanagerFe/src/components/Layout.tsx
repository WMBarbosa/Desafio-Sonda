import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/UseAuth";
import {
  LayoutDashboard,
  CheckSquare,
  Users,
  LogOut,
  Menu,
} from "lucide-react";
import { useState } from "react";

export function Layout() {
  const { logout, isAdmin } = useAuth();
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);

  function handleLogout() {
    logout();
    navigate("/login");
  }

  const navClass = ({ isActive }: { isActive: boolean }) =>
    `flex items-center gap-3 px-4 py-2.5 rounded-lg text-sm font-medium transition-colors ${
      isActive
        ? "bg-indigo-600 text-white"
        : "text-slate-300 hover:bg-slate-700 hover:text-white"
    }`;

  return (
    <div className="flex h-screen bg-slate-950 text-white">
      {/* Mobile overlay */}
      {open && (
        <div
          className="fixed inset-0 z-20 bg-black/60 md:hidden"
          onClick={() => setOpen(false)}
        />
      )}

      {/* Sidebar */}
      <aside
        className={`fixed inset-y-0 left-0 z-30 flex w-64 flex-col bg-slate-900 border-r border-slate-800 transition-transform duration-200
          ${open ? "translate-x-0" : "-translate-x-full"} md:relative md:translate-x-0`}
      >
        {/* Logo */}
        <div className="flex items-center gap-2 px-6 py-5 border-b border-slate-800">
          <LayoutDashboard className="text-indigo-400" size={22} />
          <span className="text-lg font-semibold tracking-tight">TaskManager</span>
        </div>

        {/* Nav */}
        <nav className="flex-1 overflow-y-auto px-3 py-4 space-y-1">
          <NavLink to="/dashboard" className={navClass} end>
            <LayoutDashboard size={18} />
            Dashboard
          </NavLink>

          <NavLink to="/tarefas" className={navClass}>
            <CheckSquare size={18} />
            Tarefas
          </NavLink>

          {isAdmin && (
            <NavLink to="/usuarios" className={navClass}>
              <Users size={18} />
              Usuários
            </NavLink>
          )}
        </nav>

        {/* Logout */}
        <div className="px-3 py-4 border-t border-slate-800">
          <button
            onClick={handleLogout}
            className="flex w-full items-center gap-3 px-4 py-2.5 rounded-lg text-sm font-medium text-slate-300 hover:bg-red-600/20 hover:text-red-400 transition-colors"
          >
            <LogOut size={18} />
            Sair
          </button>
        </div>
      </aside>

      {/* Main */}
      <div className="flex flex-1 flex-col overflow-hidden">
        {/* Top bar (mobile) */}
        <header className="flex items-center gap-4 px-4 py-3 bg-slate-900 border-b border-slate-800 md:hidden">
          <button onClick={() => setOpen(true)} className="text-slate-300">
            <Menu size={22} />
          </button>
          <span className="font-semibold">TaskManager</span>
        </header>

        <main className="flex-1 overflow-y-auto p-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
