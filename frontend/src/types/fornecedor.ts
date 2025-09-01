import type { Empresa } from "./empresa";
import type { Endereco } from "./endereco";

/**
 * Define a estrutura principal do Fornecedor
 */
export interface Fornecedor {
  id: string;
  documento: string;
  tipoPessoa: 'PESSOA_JURIDICA' | 'PESSOA_FISICA'; // Usando tipos literais para mais segurança
  nome: string;
  email: string;
  rg: string | null;
  dataNascimento: string | null;
  endereco: Endereco;
  empresas: Empresa[];
}
