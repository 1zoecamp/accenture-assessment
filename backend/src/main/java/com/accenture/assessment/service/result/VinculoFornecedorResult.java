package com.accenture.assessment.service.result;

import java.util.List;

import com.accenture.assessment.domain.model.Empresa;

/*
 * Disponibiliza o resultado da operação de vínculo de fornecedor.
 */
public class VinculoFornecedorResult {
	private Empresa empresa;
	private List<String> avisos;

	// Constructor
	public VinculoFornecedorResult(Empresa empresa, List<String> avisos) {
		this.empresa = empresa;
		this.avisos = avisos;
	}

	// getters
	public Empresa getEmpresa() {
		return empresa;
	}

	public List<String> getAvisos() {
		return avisos;
	}

}