import type { Endereco } from "./endereco";

/**
* Define a estrutura do objeto Empresa, que pode estar associado a um fornecedor
*/
export interface Empresa {
  id: string;
  cnpj: string;
  nomeFantasia: string;
  endereco: Endereco;
}
