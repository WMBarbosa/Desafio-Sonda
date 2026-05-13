import { createContext } from "react";
import type { LoginRequest, RoleAuthority } from "../types";
import { jwtDecode } from "jwt-decode";

export interface JwtPayload {
  sub: string;
  roles?: RoleAuthority[];
  authorities?: string[];
  exp: number;
}

export interface AuthContextData {
  token: string | null;
  roles: RoleAuthority[];
  isAdmin: boolean;
  isAuthenticated: boolean;
  login: (data: LoginRequest) => Promise<void>;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextData>({} as AuthContextData);

export function parseRoles(payload: JwtPayload): RoleAuthority[] {
  const raw: string[] = payload.roles ?? payload.authorities ?? [];
  return raw.filter(
    (r): r is RoleAuthority => r === "ROLE_ADMIN" || r === "ROLE_EMPLOYEE"
  );
}

export function initFromStorage(): { token: string | null; roles: RoleAuthority[] } {
  const t = localStorage.getItem("access_token");
  if (!t) return { token: null, roles: [] };
  try {
    const payload = jwtDecode<JwtPayload>(t);
    if (payload.exp * 1000 < Date.now()) {
      localStorage.removeItem("access_token");
      return { token: null, roles: [] };
    }
    return { token: t, roles: parseRoles(payload) };
  } catch {
    localStorage.removeItem("access_token");
    return { token: null, roles: [] };
  }
}