import { useState, type ReactNode } from "react";
import { jwtDecode } from "jwt-decode";
import api from "../lib/api";
import type { LoginRequest } from "../types";
import { AuthContext, parseRoles, type JwtPayload } from "./AuthTypes";

function initFromStorage() {
  const t = localStorage.getItem("access_token");
  if (!t) return { token: null, roles: [] as ReturnType<typeof parseRoles> };
  try {
    const payload = jwtDecode<JwtPayload>(t);
    if (payload.exp * 1000 < Date.now()) {
      localStorage.removeItem("access_token");
      return { token: null, roles: [] as ReturnType<typeof parseRoles> };
    }
    return { token: t, roles: parseRoles(payload) };
  } catch {
    localStorage.removeItem("access_token");
    return { token: null, roles: [] as ReturnType<typeof parseRoles> };
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(() => initFromStorage().token);
  const [roles, setRoles] = useState(() => initFromStorage().roles);

  async function login({ username, password }: LoginRequest) {
    const clientId = import.meta.env.VITE_OAUTH_CLIENT_ID ?? "myclientid";
    const clientSecret = import.meta.env.VITE_OAUTH_CLIENT_SECRET ?? "myclientsecret";
    const basic = btoa(`${clientId}:${clientSecret}`);

    const params = new URLSearchParams();
    params.append("grant_type", "password");
    params.append("username", username);
    params.append("password", password);
    params.append("scope", "read write");

    const { data } = await api.post("/oauth2/token", params, {
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
        Authorization: `Basic ${basic}`,
      },
    });

    const accessToken: string = data.access_token ?? data.token ?? data;
    const payload = jwtDecode<JwtPayload>(accessToken);

    localStorage.setItem("access_token", accessToken);
    setToken(accessToken);
    setRoles(parseRoles(payload));
  }

  function logout() {
    localStorage.removeItem("access_token");
    setToken(null);
    setRoles([]);
  }

  return (
    <AuthContext.Provider
      value={{
        token,
        roles,
        isAdmin: roles.includes("ROLE_ADMIN"),
        isAuthenticated: !!token,
        login,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}