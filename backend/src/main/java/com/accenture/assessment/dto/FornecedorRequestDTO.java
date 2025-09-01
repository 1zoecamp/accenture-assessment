package com.accenture.assessment.dto;

import java.time.LocalDate;

import com.accenture.assessment.domain.validation.ValidDocumento;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FornecedorRequestDTO {

	@NotBlank(message = "O documento não pode ser nulo.")
	@ValidDocumento
	private final String documento;

	@NotBlank(message = "O nome não pode ser nulo.")
	@Column(nullable = false)
	private final String nome;

	@Email(message = "O email deve ser válido.")
	@NotBlank(message = "O email não pode ser nulo.")
	private final String email;

	private final String rg;

	private final LocalDate dataNascimento;

	@NotNull(message = "Endereço é obrigatório.")
	@Valid
	private final EnderecoDTO endereco;

	// Constructor
	public FornecedorRequestDTO(String documento, String nome, String email, String rg,
			LocalDate dataNascimento, EnderecoDTO endereco) {
		this.documento = documento;
		this.nome = nome;
		this.email = email;
		this.rg = rg;
		this.dataNascimento = dataNascimento;
		this.endereco = endereco;
	}

	// Getters
	public String getDocumento() {
		return documento;
	}

	public String getNome() {
		return nome;
	}

	public String getEmail() {
		return email;
	}

	public String getRg() {
		return rg;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public EnderecoDTO getEndereco() {
		return endereco;
	}

	

}
