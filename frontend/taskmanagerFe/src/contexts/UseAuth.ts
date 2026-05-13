import { useContext } from "react";
import { AuthContext } from "./AuthTypes";

export function useAuth() {
  return useContext(AuthContext);
}