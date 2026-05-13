import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./contexts/Authprovider";
import { ProtectedRoute } from "./components/ProtectedRoute";
import { Layout } from "./components/Layout";
import { LoginPage } from "./pages/LoginPage";
import { DashboardPage } from "./pages/DashboardPage";
import { TarefasPage } from "./pages/TarefasPage";
import { UsuariosPage } from "./pages/UsuariosPage";
import { UnauthorizedPage } from "./pages/UnauthorizedPage";

export function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          {/* Public */}
          <Route path="/login" element={<LoginPage />} />
          <Route path="/unauthorized" element={<UnauthorizedPage />} />

          {/* Protected — any authenticated user */}
          <Route
            element={
              <ProtectedRoute
                allowedRoles={["ROLE_ADMIN", "ROLE_EMPLOYEE"]}
              />
            }
          >
            <Route element={<Layout />}>
              <Route path="/dashboard" element={<DashboardPage />} />
              <Route path="/tarefas" element={<TarefasPage />} />

              {/* Admin only */}
              <Route
                element={<ProtectedRoute allowedRoles={["ROLE_ADMIN"]} />}
              >
                <Route path="/usuarios" element={<UsuariosPage />} />
              </Route>
            </Route>
          </Route>

          {/* Fallback */}
          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}