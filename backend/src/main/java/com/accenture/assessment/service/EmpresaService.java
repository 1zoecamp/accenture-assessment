package com.accenture.assessment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.accenture.assessment.domain.model.Empresa;
import com.accenture.assessment.domain.repository.EmpresaRepository;
import com.accenture.assessment.domain.repository.specification.GenericSpecification;

@Service
public class EmpresaService {

	@Autowired
	private EmpresaRepository empresaRepository;

	public Page<Empresa> listarPaginado(String nomeFantasia, String cnpj, Pageable pageable) {
		// Inicializa um spec vazio, porém true.
		Specification<Empresa> spec = (root, query, cb) -> cb.conjunction();

		// Se o parâmetro 'nomeFantasia' for fornecido, adiciona o filtro de
		// nomeFantasia.
		if (nomeFantasia != null && !nomeFantasia.isBlank()) {
			spec = spec.and(GenericSpecification.like("nomeFantasia", nomeFantasia));
		}

		// Se o parâmetro 'cnpj for fornecido, adiciona o filtro de cnpj.
		if (cnpj != null && !cnpj.isBlank()) {
			spec = spec.and(GenericSpecification.like("cnpj", cnpj));
		}

		return empresaRepository.findAll(spec, pageable);
	}
}
