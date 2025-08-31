package com.accenture.assessment.domain.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.accenture.assessment.domain.validation.PessoaFisicaInfoRequired;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "fornecedor")
@PessoaFisicaInfoRequired
public class Fornecedor extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@NotNull(message = "O documento não pode ser nulo.")
	@Size(min = 11, max = 14, message = "O documento deve ter entre 11 e 14 caracteres.")
	@Column(nullable = false, unique = true)
	private String documento;

	@NotNull(message = "O tipo de pessoa não pode ser nulo.")
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_pessoa", nullable = false)
	private TipoPessoa tipoPessoa;

	@NotNull(message = "O nome não pode ser nulo.")
	@Column(nullable = false)
	private String nome;

	@Email(message = "O email deve ser válido.")
	@NotNull(message = "O email não pode ser nulo.")
	@Column(nullable = false)
	private String email;

	private String rg;

	@Column(name = "data_nascimento")
	private LocalDate dataNascimento;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "endereco_id", referencedColumnName = "id", nullable = false)
	private Endereco endereco;

	@ManyToMany(mappedBy = "fornecedores")
	private List<Empresa> empresas = new ArrayList<>();

	// Constructor
	public Fornecedor() {
	}

	// Getters and Setters
	public UUID getId() {
		return id;
	}

	protected void setId(UUID id) {
		this.id = id;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	// Extra methods
	public void vincularEmpresa(Empresa empresa) {
		this.empresas.add(empresa);
		empresa.getFornecedores().add(this);
	}

	public void desvincularFornecedor(Empresa empresa) {
		this.empresas.remove(empresa);
		empresa.getFornecedores().remove(this);
	}

	public boolean fornecedorEhMenorDeIdade() {
		LocalDate dataNascimento = this.dataNascimento;

		if (dataNascimento == null || this.tipoPessoa != TipoPessoa.PESSOA_FISICA) {
			return false;
		}

		Integer idade = Period.between(dataNascimento, LocalDate.now()).getYears();
		
		return idade < 18;
	}
}
