/**
* Define a estrutura do objeto Endereco, usado tanto em Fornecedor quanto em Empresa
*/
export interface Endereco {
  cep: string;
  logradouro: string;
  complemento: string | null;
  bairro: string;
  localidade: string;
  uf: string;
}
