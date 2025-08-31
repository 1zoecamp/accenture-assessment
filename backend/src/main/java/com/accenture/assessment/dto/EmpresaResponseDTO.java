package com.accenture.assessment.dto;

import java.util.UUID;

import com.accenture.assessment.domain.model.Empresa;

public final class EmpresaResponseDTO {

	private final UUID id;
	private final String cnpj;
	private final String nomeFantasia;
	private final EnderecoDTO endereco;

	// Construtor principal
	public EmpresaResponseDTO(UUID id, String cnpj, String nomeFantasia, EnderecoDTO endereco) {
		this.id = id;
		this.cnpj = cnpj;
		this.nomeFantasia = nomeFantasia;
		this.endereco = endereco;
	}

	// Construtor de conveniência (delega a chamada para o construtor principal)
	public EmpresaResponseDTO(Empresa empresa) {
		this(
				empresa.getId(), 
				empresa.getCnpj(), 
				empresa.getNomeFantasia(),
				new EnderecoDTO(
						empresa.getEndereco().getCep(), 
						empresa.getEndereco().getLogradouro(),
						empresa.getEndereco().getComplemento(), 
						empresa.getEndereco().getBairro(),
						empresa.getEndereco().getLocalidade(), 
						empresa.getEndereco().getUf()
						)
				);
	}

	// Getters
	public UUID getId() {
		return id;
	}

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