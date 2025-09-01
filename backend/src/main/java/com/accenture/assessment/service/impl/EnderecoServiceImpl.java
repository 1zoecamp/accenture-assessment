package com.accenture.assessment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.accenture.assessment.domain.model.Endereco;
//import com.accenture.assessment.domain.repository.EnderecoRepository;
//import com.accenture.assessment.dto.ViaCepResponse;
import com.accenture.assessment.exception.BusinessRuleException;
import com.accenture.assessment.service.EnderecoService;

@Service
public class EnderecoServiceImpl implements EnderecoService {

	//@Autowired
	//private EnderecoRepository enderecoRepository;

	@Autowired
	private ViaCepServiceImpl viaCepService;

	public String validarCep(String cep) {

		// Valida o CEP informado a partir do ViaCepService
		viaCepService.consultarCep(cep)
				.orElseThrow(() -> new BusinessRuleException("CEP inválido ou não encontrado: " + cep));


		return cep;
	}
}
