package com.accenture.assessment.domain.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;

@Entity
@Table(name = "endereco")
public class Endereco extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@NotNull(message = "O CEP não pode ser nulo.")
	@Column(nullable = false)
	private String cep;

	@NotNull(message = "O logradouro não pode ser nulo.")
	@Column(nullable = false)
	private String logradouro;

	private String complemento;

	@NotNull(message = "O bairro não pode ser nulo.")
	@Column(nullable = false)
	private String bairro;

	@NotNull(message = "A localidade não pode ser nula.")
	@Column(nullable = false)
	private String localidade;

	@NotNull(message = "A UF não pode ser nula.")
	@Column(nullable = false)
	private String uf;

	// Constructor
	public Endereco() {
	}

	// Getters and Setters
	public UUID getId() {
		return id;
	}

	protected void setId(UUID id) {
		this.id = id;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
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
