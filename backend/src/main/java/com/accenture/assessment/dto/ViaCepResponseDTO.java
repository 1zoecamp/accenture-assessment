package com.accenture.assessment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignora campos não mapeados
public class ViaCepResponseDTO {
	private String cep;
	private String logradouro;
	private String bairro;
	private String localidade;
	private String uf;
	private boolean erro; // ViaCEP retorna 'erro: true' para CEPs de formato válido mas inexistentes

	// Getters and Setters
	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public boolean isErro() {
		return erro;
	}

	public void setErro(boolean erro) {
		this.erro = erro;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

}
