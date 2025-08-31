package com.accenture.assessment.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

public final class VincularFornecedorRequestDTO {

	@NotEmpty(message = "A lista de IDs de fornecedores não pode estar vazia.")
	private final List<UUID> fornecedorIds;

	// Constrcutor
	public VincularFornecedorRequestDTO(List<UUID> fornecedorIds) {
		this.fornecedorIds = fornecedorIds;
	}

	// Getter
	public List<UUID> getFornecedorIds() {
		return fornecedorIds;
	}
}