package com.accenture.assessment.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.assessment.domain.model.Empresa;
import com.accenture.assessment.dto.PageResponse;
import com.accenture.assessment.service.EmpresaService;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

	@Autowired
	private EmpresaService empresaService;

	@GetMapping
	public ResponseEntity<PageResponse<Empresa>> listar(
			@Parameter(description = "Texto para busca no nome fantasia da empresa.") @RequestParam(required = false, name = "nome") String nomeFantasia,
			@RequestParam(required = false) String cnpj, @ParameterObject Pageable pageable) {

		Page<Empresa> empresas = empresaService.listarPaginado(nomeFantasia, cnpj, pageable);

		// Converte Page para PageResponse
		PageResponse<Empresa> empresasResponse = new PageResponse<>(empresas);

		return ResponseEntity.ok(empresasResponse);
	}

	// ... outros endpoints para Empresa ...
}
