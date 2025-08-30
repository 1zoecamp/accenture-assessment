package com.accenture.assessment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.assessment.domain.model.Empresa;
import com.accenture.assessment.service.EmpresaService;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

	@Autowired
	private EmpresaService empresaService;

	@GetMapping
	public ResponseEntity<Page<Empresa>> listar(@RequestParam(required = false, name = "nome") String nomeFantasia,
			@RequestParam(required = false) String cnpj, Pageable pageable) {

		Page<Empresa> empresas = empresaService.listarPaginado(nomeFantasia, cnpj, pageable);
		return ResponseEntity.ok(empresas);
	}

	// ... outros endpoints para Empresa ...
}
