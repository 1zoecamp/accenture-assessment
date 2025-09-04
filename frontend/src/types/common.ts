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
 * Props padrão do formulário
 */
export type CustomTableFormProps<T> = {
  error: AxiosError | null
  registros: Array<T>
  loading: boolean
  response: PaginatedResponse<T> | null
  onPageChange: (event: DataTablePageEvent) => void
  refetch: (page?: number) => void
  filters?: DataTableFilterMeta
}

/**
 *  Retorno da API ViaCep
 */
export type ViaCepResponse = {
  cep: string
  logradouro: string
  complemento: string
  bairro: string
  localidade: string
  uf: string
  ibge: string
  gia: string
  ddd: string
  siafi: string
  erro?: boolean
}
