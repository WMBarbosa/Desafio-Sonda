import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../contexts/UseAuth";
import type{ RoleAuthority } from "../types";

interface ProtectedRouteProps {
  allowedRoles?: RoleAuthority[];
}

export function ProtectedRoute({ allowedRoles }: ProtectedRouteProps) {
  const { isAuthenticated, roles } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && !allowedRoles.some((r) => roles.includes(r))) {
    return <Navigate to="/unauthorized" replace />;
  }

  return <Outlet />;
}
