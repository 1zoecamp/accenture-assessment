import type { AxiosError } from "axios";
import type { DataTableFilterMeta, DataTablePageEvent } from "primevue";

/**
 * Estrutura padrão da resposta paginada da API
 */
export type PaginatedResponse<T> = {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
}

/**
 * Estrutura padrão de erro do formulário
 */
export type FormError = Record<string, Error[]>


/** Props padrão do formulário */
export type CustomTableFormProps<T> = {
  error: AxiosError | null
  registros: Array<T>
  loading: boolean
  response: PaginatedResponse<T> | null
  onPageChange: (event: DataTablePageEvent) => void
  refetch: (page?: number) => void
  filters?: DataTableFilterMeta
}
