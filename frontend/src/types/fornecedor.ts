import type { Empresa } from "./empresa";
import type { Endereco } from "./endereco";

/**
 * Define a estrutura principal do Fornecedor
 */
export interface Fornecedor {
  id: string;
  documento: string;
  nome: string;
  email: string;
  rg: string | null;
  dataNascimento: Date | null;
  endereco: Endereco;
  empresas?: Empresa[];
}
