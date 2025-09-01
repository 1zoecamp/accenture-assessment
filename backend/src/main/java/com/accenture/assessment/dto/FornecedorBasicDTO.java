package com.accenture.assessment.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.accenture.assessment.domain.model.Fornecedor;

/*
 * Utilizada para exibição de dados básicos do Forncedor em listagens de Empresa.
 * Contém um EnderecoDTO para garantir o desacoplamento da API com a entidade.
 */
public class FornecedorBasicDTO {
	private final UUID id;
	private final String nome;
	private final String documento;
	private final String email;
	private final String rg;
	private final LocalDate dataNascimento;
	private final EnderecoDTO endereco;

	// Construtor principal
	public FornecedorBasicDTO(UUID id, String documento, String nome, String email, String rg, LocalDate dataNascimento,
			EnderecoDTO endereco) {
		this.id = id;
		this.nome = nome;
		this.documento = documento;
		this.email = email;
		this.rg = rg;
		this.dataNascimento = dataNascimento;
		this.endereco = endereco;
	}

	// Construtor de conveniência
	public FornecedorBasicDTO(Fornecedor fornecedor) {
		this.id = fornecedor.getId();
		this.nome = fornecedor.getNome();
		this.documento = fornecedor.getDocumento();
		this.email = fornecedor.getEmail();
		this.rg = fornecedor.getRg();
		this.dataNascimento = fornecedor.getDataNascimento();
		this.endereco = new EnderecoDTO(fornecedor.getEndereco().getCep(), fornecedor.getEndereco().getLogradouro(),
				fornecedor.getEndereco().getComplemento(), fornecedor.getEndereco().getBairro(),
				fornecedor.getEndereco().getLocalidade(), fornecedor.getEndereco().getUf()

		);
	}

	// Getters
	public UUID getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getDocumento() {
		return documento;
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
