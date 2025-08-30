package com.accenture.assessment.controller;

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
import com.accenture.assessment.service.FornecedorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

	@Autowired
	private FornecedorService fornecedorService;

	@PostMapping
	public ResponseEntity<Fornecedor> criarFornecedor(@Valid @RequestBody Fornecedor fornecedor) {
		Fornecedor novoFornecedor = null;
		// Fornecedor novoFornecedor = fornecedorService.salvar(fornecedor);
		return ResponseEntity.status(HttpStatus.CREATED).body(novoFornecedor);
	}

	@GetMapping
	public ResponseEntity<Page<Fornecedor>> listar(@RequestParam(required = false) String nome,
			@RequestParam(required = false) String documento, Pageable pageable) {

		Page<Fornecedor> fornecedores = fornecedorService.listarPaginado(nome, documento, pageable);
		return ResponseEntity.ok(fornecedores);
	}
}
