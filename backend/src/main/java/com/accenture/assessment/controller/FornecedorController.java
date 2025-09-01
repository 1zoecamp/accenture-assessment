package com.accenture.assessment.controller;

import java.net.URI;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.accenture.assessment.domain.model.Fornecedor;
import com.accenture.assessment.dto.FornecedorRequestDTO;
import com.accenture.assessment.dto.FornecedorResponseDTO;
import com.accenture.assessment.dto.PageResponseDTO;
import com.accenture.assessment.service.FornecedorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/fornecedores")
@Tag(name = "Fornecedores", description = "Endpoints para gerenciar fornecedores")
public class FornecedorController {

	@Autowired
	private FornecedorService fornecedorService;

	@GetMapping
	@Operation(summary = "Listar fornecedores", description = "Exibe uma lista paginada de fornecedores cadastrados, com filtros opcionais.")
	public ResponseEntity<PageResponseDTO<FornecedorResponseDTO>> listarFornecedores(
			@RequestParam(required = false) String nome, @RequestParam(required = false) String documento,
			@ParameterObject Pageable pageable) {

		Page<Fornecedor> fornecedoresPage = fornecedorService.listarFornecedores(nome, documento, pageable);

		// Converte cada Fornecedor em um FornecedorResponseDTO
		Page<FornecedorResponseDTO> fornecedoresResponsePage = fornecedoresPage.map(FornecedorResponseDTO::new);
		PageResponseDTO<FornecedorResponseDTO> pageResponse = new PageResponseDTO<>(fornecedoresResponsePage);

		return ResponseEntity.ok(pageResponse);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar fornecedor por ID", description = "Busca um único fornecedor pelo seu ID.")
	public ResponseEntity<FornecedorResponseDTO> buscarFornecedorPorId(@PathVariable UUID id) {
		Fornecedor fornecedor = fornecedorService.buscarFornecedorPorId(id);

		return ResponseEntity.ok(new FornecedorResponseDTO(fornecedor));
	}

	@PostMapping
	@Operation(summary = "Cadastrar fornecedor", description = "Cria um novo registro de fornecedor no sistema.")
	public ResponseEntity<FornecedorResponseDTO> cadastrarFornecedor(
			@Valid @RequestBody FornecedorRequestDTO fornecedorDTO) {
		Fornecedor novoFornecedor = fornecedorService.cadastrarFornecedor(fornecedorDTO);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(novoFornecedor.getId()).toUri();

		FornecedorResponseDTO responseDTO = new FornecedorResponseDTO(novoFornecedor);

		return ResponseEntity.created(location).body(responseDTO);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar fornecedor", description = "Atualiza os dados de um fornecedor existente com base no seu ID.")
	public ResponseEntity<FornecedorResponseDTO> atualizarFornecedor(@PathVariable UUID id,
			@Valid @RequestBody FornecedorRequestDTO fornecedorDTO) {

		Fornecedor fornecedorAtualizado = fornecedorService.atualizarFornecedor(id, fornecedorDTO);
		FornecedorResponseDTO responseDTO = new FornecedorResponseDTO(fornecedorAtualizado);

		return ResponseEntity.ok(responseDTO);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir fornecedor", description = "Exclui (logicamente) um fornecedor com base no seu ID.")
	public ResponseEntity<Void> removerFornecedor(@PathVariable UUID id) {
		fornecedorService.removerFornecedor(id);

		return ResponseEntity.noContent().build();
	}
}