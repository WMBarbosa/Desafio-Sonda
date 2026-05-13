import api from "../lib/api";
import type { Page, Role, RoleAuthority, UserResponseDTO } from "../types";

/**
 * Spring returns `roles` as `string[]` (see UserResponseDTO.fromEntity).
 * The UI expects `Role[]` with `authority`; without this, `role.authority` is
 * undefined on each string and every badge renders as "Employee".
 */
function normalizeRoles(raw: unknown): Role[] {
  if (!Array.isArray(raw)) return [];
  const seen = new Set<string>();
  const roles: Role[] = [];
  let syntheticId = 0;
  for (const item of raw as (string | Role)[]) {
    const authority =
      typeof item === "string" ? item : item?.authority;
    if (!authority || seen.has(authority)) continue;
    seen.add(authority);
    const id =
      typeof item === "object" && item != null && typeof item.id === "number"
        ? item.id
        : ++syntheticId;
    roles.push({ id, authority: authority as RoleAuthority });
  }
  return roles;
}

function normalizeUser(user: UserResponseDTO): UserResponseDTO {
  const rawRoles = user.roles as unknown;
  return { ...user, roles: normalizeRoles(rawRoles) };
}

export const userService = {
  async findAll(page = 0, size = 10): Promise<Page<UserResponseDTO>> {
    const { data } = await api.get<Page<UserResponseDTO>>("/users", {
      params: { page, size },
    });
    return {
      ...data,
      content: data.content.map(normalizeUser),
    };
  },

  async findById(id: number): Promise<UserResponseDTO> {
    const { data } = await api.get<UserResponseDTO>(`/users/${id}`);
    return normalizeUser(data);
  },

  async getMe(): Promise<UserResponseDTO> {
    const { data } = await api.get<UserResponseDTO>("/users/me");
    return normalizeUser(data);
  },
};