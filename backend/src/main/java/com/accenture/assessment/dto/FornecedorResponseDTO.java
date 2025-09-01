package com.accenture.assessment.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.accenture.assessment.domain.model.Fornecedor;
import com.accenture.assessment.domain.model.TipoPessoa;

public final class FornecedorResponseDTO {

	private final UUID id;
	private final String documento;
	private final TipoPessoa tipoPessoa;
	private final String nome;
	private final String email;
	private final String rg;
	private final LocalDate dataNascimento;
	private final EnderecoDTO endereco;
	private final List<EmpresaBasicDTO> empresas;

	// Construtor principal
	public FornecedorResponseDTO(UUID id, String documento, TipoPessoa tipoPessoa, String nome, String email, String rg,
			LocalDate dataNascimento, EnderecoDTO endereco, List<EmpresaBasicDTO> empresas) {
		this.id = id;
		this.documento = documento;
		this.tipoPessoa = tipoPessoa;
		this.nome = nome;
		this.email = email;
		this.rg = rg;
		this.dataNascimento = dataNascimento;
		this.endereco = endereco;
		this.empresas = empresas;
	}

	// Construtor de conveniência
	public FornecedorResponseDTO(Fornecedor fornecedor) {
		this.id = fornecedor.getId();
		this.nome = fornecedor.getNome();
		this.documento = fornecedor.getDocumento();
		this.tipoPessoa = fornecedor.getTipoPessoa();
		this.email = fornecedor.getEmail();
		this.rg = fornecedor.getRg();
		this.dataNascimento = fornecedor.getDataNascimento();
		this.endereco = new EnderecoDTO(fornecedor.getEndereco().getCep(), fornecedor.getEndereco().getLogradouro(),
				fornecedor.getEndereco().getComplemento(), fornecedor.getEndereco().getBairro(),
				fornecedor.getEndereco().getLocalidade(), fornecedor.getEndereco().getUf()

		);
		this.empresas = fornecedor.getEmpresas().stream().map(EmpresaBasicDTO::new).collect(Collectors.toList());
	}

	// Getters
	public UUID getId() {
		return id;
	}

	public String getDocumento() {
		return documento;
	}

	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
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

	public List<EmpresaBasicDTO> getEmpresas() {
		return empresas;
	}
}