package com.accenture.assessment.dto;

import java.util.List;

public final class VincularFornecedorResponseDTO {

	private final EmpresaResponseDTO empresa;
	private final List<String> avisos;

	// Constructor
	public VincularFornecedorResponseDTO(EmpresaResponseDTO empresa, List<String> avisos) {
		this.empresa = empresa;
		this.avisos = avisos;
	}

	// Getters
	public EmpresaResponseDTO getEmpresa() {
		return empresa;
	}

	public List<String> getAvisos() {
		return avisos;
	}
}