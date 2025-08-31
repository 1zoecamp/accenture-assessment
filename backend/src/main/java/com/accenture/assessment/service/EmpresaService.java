package com.accenture.assessment.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.accenture.assessment.domain.model.Empresa;
import com.accenture.assessment.domain.model.Fornecedor;
import com.accenture.assessment.dto.EmpresaRequestDTO;
import com.accenture.assessment.service.result.VinculoFornecedorResult;

public interface EmpresaService {

	Page<Empresa> listarEmpresas(String nomeFantasia, String cnpj, Pageable pageable);

	Empresa buscarEmpresaPorId(UUID id);

	Empresa cadastrarEmpresa(EmpresaRequestDTO empresaDTO);

	Empresa atualizarEmpresa(UUID empresaId, EmpresaRequestDTO empresaDTO);

	void removerEmpresa(UUID empresaId);

	VinculoFornecedorResult vincularFornecedor(UUID empresaId, Fornecedor... fornecedores);

	Empresa desvincularFornecedor(UUID empresaId, List<UUID> fornecedorIds);
}