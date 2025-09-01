package com.accenture.assessment.dto;

import com.accenture.assessment.domain.model.Empresa;

import java.util.UUID;

/*
 * Utilizada para exibição de dados básicos da Empresa em listagens de Fornecedor.
 * Contém um EnderecoDTO para garantir o desacoplamento da API com a entidade.
 */
public final class EmpresaBasicDTO {
	private final UUID id;
	private final String cnpj;
	private final String nomeFantasia;
	private final EnderecoDTO endereco;

	// Construtor principal
	public EmpresaBasicDTO(UUID id, String cnpj, String nomeFantasia, EnderecoDTO endereco) {
		this.id = id;
		this.cnpj = cnpj;
		this.nomeFantasia = nomeFantasia;
		this.endereco = endereco;
	}

	// Construtor de conveniência
	public EmpresaBasicDTO(Empresa empresa) {
		this.id = empresa.getId();
		this.cnpj = empresa.getCnpj();
		this.nomeFantasia = empresa.getNomeFantasia();
		this.endereco = new EnderecoDTO(empresa.getEndereco().getCep(), empresa.getEndereco().getLogradouro(),
				empresa.getEndereco().getComplemento(), empresa.getEndereco().getBairro(),
				empresa.getEndereco().getLocalidade(), empresa.getEndereco().getUf()

		);
	}

	// Getters
	public UUID getId() {
		return id;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public String getCnpj() {
		return cnpj;
	}

	public EnderecoDTO getEndereco() {
		return endereco;
	}
}