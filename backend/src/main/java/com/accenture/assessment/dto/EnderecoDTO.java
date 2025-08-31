package com.accenture.assessment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class EnderecoDTO {

	@NotBlank
	@Size(min = 8, max = 8)
	private final String cep;

	@NotBlank
	private final String logradouro;

	private final String complemento;

	@NotBlank
	private final String bairro;

	@NotBlank
	private final String localidade;

	@NotBlank
	@Size(min = 2, max = 2)
	private final String uf;

	// Constructor
	public EnderecoDTO(String cep, String logradouro, String complemento, String bairro, String localidade, String uf) {
		this.cep = cep;
		this.logradouro = logradouro;
		this.complemento = complemento;
		this.bairro = bairro;
		this.localidade = localidade;
		this.uf = uf;
	}

	// Getters
	public String getCep() {
		return cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public String getComplemento() {
		return complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public String getLocalidade() {
		return localidade;
	}

	public String getUf() {
		return uf;
	}
}
