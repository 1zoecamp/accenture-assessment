package com.accenture.assessment.service;

import java.util.Optional;

import com.accenture.assessment.dto.ViaCepResponseDTO;

public interface ViaCepService {
	
	public Optional<ViaCepResponseDTO> consultarCep(String cep);
}
