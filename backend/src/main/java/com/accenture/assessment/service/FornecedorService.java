package com.accenture.assessment.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.accenture.assessment.domain.model.Fornecedor;
import com.accenture.assessment.domain.model.TipoPessoa;
import com.accenture.assessment.dto.FornecedorRequestDTO;

public interface FornecedorService {

	Page<Fornecedor> listarFornecedores(String nome, String documento, Pageable pageable);

	Fornecedor buscarFornecedorPorId(UUID id);
	
	List<Fornecedor> buscarFornecedoresPorId(List<UUID> fornecedoresId);

	Fornecedor cadastrarFornecedor(FornecedorRequestDTO fornecedorDTO);

	Fornecedor atualizarFornecedor(UUID fornecedorId, FornecedorRequestDTO fornecedorDTO);
	
	TipoPessoa definirTipoPessoa(String documento);

	void removerFornecedor(UUID fornecedorId);
}
