package com.accenture.assessment.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "empresa")
public class Empresa extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@NotNull(message = "O CNPJ não pode ser nulo.")
	@Size(min = 11, max = 14, message = "O CNPJ deve ter 14 caracteres.")
	@Column(nullable = false, unique = true, length = 14)
	private String cnpj;

	@NotNull(message = "O nome fantasia não pode ser nulo.")
	@Column(name = "nome_fantasia", nullable = false)
	private String nomeFantasia;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "endereco_id", referencedColumnName = "id", nullable = false)
	private Endereco endereco;
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(
		name = "empresa_fornecedor",
		joinColumns = @JoinColumn(name = "id_empresa"),
		inverseJoinColumns = @JoinColumn(name = "id_fornecedor")
	)
	private List<Fornecedor> fornecedores = new ArrayList<>();
	
	// Constructor
	public Empresa() {
	}

	// Getters and Setters
	public UUID getId() {
		return id;
	}

	protected void setId(UUID id) {
		this.id = id;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public List<Fornecedor> getFornecedores() {
		return fornecedores;
	}

	// Extra methods
	public void vincularFornecedor(Fornecedor fornecedor) {
		this.fornecedores.add(fornecedor);
		fornecedor.getEmpresas().add(this);
	}

	public void desvincularFornecedor(Fornecedor fornecedor) {
		this.fornecedores.remove(fornecedor);
		fornecedor.getEmpresas().remove(this);
	}
}
