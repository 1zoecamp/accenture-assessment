package com.accenture.assessment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accenture.assessment.domain.model.Endereco;
import com.accenture.assessment.domain.repository.EnderecoRepository;
import com.accenture.assessment.dto.ViaCepResponse;
import com.accenture.assessment.exception.BusinessRuleException;

@Service
public class EnderecoService {

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private ViaCepService viaCepService;

	public Endereco salvar(Endereco endereco) {

		String cep = endereco.getCep();

		// Valida o CEP informado a partir do ViaCepService
		ViaCepResponse cepInfo = viaCepService.consultarCep(cep)
				.orElseThrow(() -> new BusinessRuleException("CEP inválido ou não encontrado: " + cep));

		// Caso o CEP seja válido, atualiza as demais informações do endereço
		endereco.setLogradouro(cepInfo.getLogradouro());
		endereco.setBairro(cepInfo.getBairro());
		endereco.setLocalidade(cepInfo.getLocalidade());
		endereco.setUf(cepInfo.getUf());

		return enderecoRepository.save(endereco);
	}
}
