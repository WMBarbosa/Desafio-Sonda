import { ChevronUp, ChevronDown, ChevronsUpDown } from "lucide-react";
import type { SortState } from "../hooks/useSortableTable";

interface SortableHeaderProps<T> {
  label: string;
  sortKey: keyof T;
  sort: SortState<T>;
  onSort: (key: keyof T) => void;
  className?: string;
}

export function SortableHeader<T>({
  label,
  sortKey,
  sort,
  onSort,
  className = "",
}: SortableHeaderProps<T>) {
  const isActive = sort.key === sortKey;

  return (
    <th
      className={`px-4 py-3 text-left text-xs font-semibold uppercase tracking-wider text-slate-400 cursor-pointer select-none hover:text-white transition-colors ${className}`}
      onClick={() => onSort(sortKey)}
    >
      <span className="flex items-center gap-1.5">
        {label}
        {isActive ? (
          sort.direction === "asc" ? (
            <ChevronUp size={14} className="text-indigo-400" />
          ) : (
            <ChevronDown size={14} className="text-indigo-400" />
          )
        ) : (
          <ChevronsUpDown size={14} className="opacity-40" />
        )}
      </span>
    </th>
  );
}
