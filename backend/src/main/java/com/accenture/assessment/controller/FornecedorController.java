package com.accenture.assessment.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.assessment.domain.model.Fornecedor;
import com.accenture.assessment.dto.PageResponseDTO;
import com.accenture.assessment.service.impl.FornecedorServiceImpl;

import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

	@Autowired
	private FornecedorServiceImpl fornecedorService;

	@PostMapping
	public ResponseEntity<Fornecedor> criarFornecedor(@Valid @RequestBody Fornecedor fornecedor) {
		Fornecedor novoFornecedor = null;
		// Fornecedor novoFornecedor = fornecedorService.salvar(fornecedor);
		return ResponseEntity.status(HttpStatus.CREATED).body(novoFornecedor);
	}

	@GetMapping
	public ResponseEntity<PageResponseDTO<Fornecedor>> listar(
			@Parameter(description = "Texto para busca no nome do fornecedor.") @RequestParam(required = false) String nome,
			@Parameter(description = "Texto para busca no documento do fornecedor.") @RequestParam(required = false) String documento,
			@ParameterObject Pageable pageable) {

		Page<Fornecedor> fornecedores = fornecedorService.listarPaginado(nome, documento, pageable);

		// Converte Page para PageResponse
		PageResponseDTO<Fornecedor> fornecedoresResponse = new PageResponseDTO<>(fornecedores);

		return ResponseEntity.ok(fornecedoresResponse);
	}
}
