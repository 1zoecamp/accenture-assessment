package com.accenture.assessment.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public final class EmpresaRequestDTO {

	@NotBlank(message = "O CNPJ não pode ser nulo.")
	@Size(min = 14, max = 14, message = "O CNPJ deve ter 14 dígitos.")
	private final String cnpj;

	@NotBlank(message = "O nome fantasia não pode ser nulo.")
	private final String nomeFantasia;

	@NotNull(message = "Endereço é obrigatório.")
	@Valid
	private final EnderecoDTO endereco;

	// Constructor
	public EmpresaRequestDTO(String cnpj, String nomeFantasia, EnderecoDTO endereco) {
		this.cnpj = cnpj;
		this.nomeFantasia = nomeFantasia;
		this.endereco = endereco;
	}

	// Getters
	public String getCnpj() {
		return cnpj;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public EnderecoDTO getEndereco() {
		return endereco;
	}
}