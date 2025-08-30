package com.accenture.assessment.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.accenture.assessment.domain.model.Fornecedor;
import com.accenture.assessment.domain.repository.FornecedorRepository;
import com.accenture.assessment.domain.repository.specification.GenericSpecification;
import com.accenture.assessment.exception.BusinessRuleException;

@Service
public class FornecedorService {

	@Autowired
	private FornecedorRepository fornecedorRepository;

	/*
	 * Verifica se já há um fornecedor com o documento informado cadastrado no
	 * sistema.
	 */
	public Fornecedor criar(Fornecedor novoFornecedor) {
		if (fornecedorRepository.existsByDocumento(novoFornecedor.getDocumento())) {
			throw new BusinessRuleException("Fornecedor já cadastrado no sistema.");
		}

		return fornecedorRepository.save(novoFornecedor);
	}

	/*
	 * Impede que um documento seja usado para mais de um fornecedor cadastrado no
	 * sistema.
	 */
	public Fornecedor salvar(Fornecedor fornecedor) {
		Optional<Fornecedor> fornecedorExistente = fornecedorRepository.findByDocumento(fornecedor.getDocumento());

		if (fornecedorExistente.isPresent() && !fornecedorExistente.get().getId().equals(fornecedor.getId())) {
			throw new BusinessRuleException("Documento já cadastrado para outro fornecedor.");
		}

		return fornecedorRepository.save(fornecedor);
	}

	public Page<Fornecedor> listarPaginado(String nome, String documento, Pageable pageable) {

		// Inicializa um spec vazio, porém true.
		Specification<Fornecedor> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

		// Se o parâmetro 'nome' for fornecido, adiciona o filtro de nome.
		if (nome != null && !nome.isBlank()) {
			spec = spec.and(GenericSpecification.like("nome", nome));
		}

		// Se o parâmetro 'documento' for fornecido, adiciona o filtro de documento.
		if (documento != null && !documento.isBlank()) {
			spec = spec.and(GenericSpecification.like("documento", documento));
		}

		return fornecedorRepository.findAll(spec, pageable);
	}
}
