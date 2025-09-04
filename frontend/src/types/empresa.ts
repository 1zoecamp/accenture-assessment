import type { Endereco } from "./endereco";
import type { Fornecedor } from "./fornecedor";

/**
* Define a estrutura do objeto Empresa, que pode estar associado a um fornecedor
*/
export interface Empresa {
  id: string;
  cnpj: string;
  nomeFantasia: string;
  endereco: Endereco;
  fornecedores?: Array<Fornecedor>
}
