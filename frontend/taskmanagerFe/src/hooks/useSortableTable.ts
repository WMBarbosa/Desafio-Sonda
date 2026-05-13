import { useState, useMemo } from "react";

export type SortDirection = "asc" | "desc" | null;

export interface SortState<T> {
  key: keyof T | null;
  direction: SortDirection;
}

export function useSortableTable<T>(data: T[]) {
  const [sort, setSort] = useState<SortState<T>>({
    key: null,
    direction: null,
  });

  function handleSort(key: keyof T) {
    setSort((prev) => {
      if (prev.key !== key) return { key, direction: "asc" };
      if (prev.direction === "asc") return { key, direction: "desc" };
      return { key: null, direction: null };
    });
  }

  const sortedData = useMemo(() => {
    const list = Array.isArray(data) ? data : [];
    if (!sort.key || !sort.direction) return list;

    return [...list].sort((a, b) => {
      const aVal = a[sort.key!];
      const bVal = b[sort.key!];

      if (aVal === null || aVal === undefined) return 1;
      if (bVal === null || bVal === undefined) return -1;

      if (typeof aVal === "string" && typeof bVal === "string") {
        return sort.direction === "asc"
          ? aVal.localeCompare(bVal)
          : bVal.localeCompare(aVal);
      }

      if (aVal < bVal) return sort.direction === "asc" ? -1 : 1;
      if (aVal > bVal) return sort.direction === "asc" ? 1 : -1;
      return 0;
    });
  }, [data, sort]);

  return { sortedData, sort, handleSort };
}