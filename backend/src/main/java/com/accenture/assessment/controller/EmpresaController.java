package com.accenture.assessment.controller;

import java.net.URI;
import java.util.List;
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

import com.accenture.assessment.domain.model.Empresa;
import com.accenture.assessment.domain.model.Fornecedor;
import com.accenture.assessment.dto.EmpresaRequestDTO;
import com.accenture.assessment.dto.EmpresaResponseDTO;
import com.accenture.assessment.dto.PageResponseDTO;
import com.accenture.assessment.dto.VincularFornecedorRequestDTO;
import com.accenture.assessment.dto.VincularFornecedorResponseDTO;
import com.accenture.assessment.service.impl.EmpresaServiceImpl;
import com.accenture.assessment.service.impl.FornecedorServiceImpl;
import com.accenture.assessment.service.result.VinculoFornecedorResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/empresas")
@Tag(name = "Empresas", description = "Endpoints para gerenciar empresas")
public class EmpresaController {

	@Autowired
	private EmpresaServiceImpl empresaService;

	@Autowired
	private FornecedorServiceImpl fornecedorService;

	@GetMapping
	@Operation(summary = "Listar empresas", description = "Exibe a lista de empresas cadastradas")
	public ResponseEntity<PageResponseDTO<EmpresaResponseDTO>> listarEmpresas(
			@RequestParam(required = false, name = "nome") String nomeFantasia,
			@RequestParam(required = false) String cnpj, @ParameterObject Pageable pageable) {

		Page<Empresa> empresasPage = empresaService.listarEmpresas(nomeFantasia, cnpj, pageable);

		// Converte cada Empresa em um EmpresaResponseDTO (construtor de conveniência)
		Page<EmpresaResponseDTO> empresasDtoPage = empresasPage.map(EmpresaResponseDTO::new);
		PageResponseDTO<EmpresaResponseDTO> responseFinal = new PageResponseDTO<>(empresasDtoPage);

		return ResponseEntity.ok(responseFinal);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar empresa", description = "Busca uma empresa pelo id")
	public ResponseEntity<EmpresaResponseDTO> buscarEmpresaPorId(@PathVariable("id") UUID id) {
		Empresa empresa = empresaService.buscarEmpresaPorId(id);

		// Converte a entidade salva para o DTO de resposta
		EmpresaResponseDTO responseDTO = new EmpresaResponseDTO(empresa);

		return ResponseEntity.ok(responseDTO);
	}

	@PostMapping
	@Operation(summary = "Cadastrar empresa", description = "Cria um novo registro de empresa no sistema.")
	public ResponseEntity<EmpresaResponseDTO> cadastrarEmpresa(@Valid @RequestBody EmpresaRequestDTO empresaDTO) {

		Empresa novaEmpresa = empresaService.cadastrarEmpresa(empresaDTO);

		// Cria a URI de localização do novo recurso criado
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(novaEmpresa.getId()).toUri();

		// Converte a entidade salva para o DTO de resposta
		EmpresaResponseDTO responseDTO = new EmpresaResponseDTO(novaEmpresa);

		return ResponseEntity.created(location).body(responseDTO);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar empresa", description = "Atualiza os dados de uma empresa com base no seu ID.")
	public ResponseEntity<EmpresaResponseDTO> atualizarEmpresa(
			@Parameter(description = "ID da empresa a ser atualizada") @PathVariable UUID id,
			@Valid @RequestBody EmpresaRequestDTO empresaDTO) {

		Empresa empresaAtualizada = empresaService.atualizarEmpresa(id, empresaDTO);

		// Converte a entidade atualizada para o DTO de resposta
		EmpresaResponseDTO responseDTO = new EmpresaResponseDTO(empresaAtualizada);

		return ResponseEntity.ok(responseDTO);
	}

	@PostMapping("/{empresaId}/fornecedores")
	@Operation(summary = "Vincular fornecedores", description = "Associa fornecedores existentes a uma empresa. Retorna a empresa atualizada e uma lista de avisos para fornecedores que não puderam ser vinculados.")
	public ResponseEntity<VincularFornecedorResponseDTO> vincularFornecedores(@PathVariable UUID empresaId,
			@Valid @RequestBody VincularFornecedorRequestDTO requestDTO) {

		// hydration
		List<Fornecedor> fornecedores = fornecedorService.buscarFornecedoresPorId(requestDTO.getFornecedorIds());

		VinculoFornecedorResult resultado = empresaService.vincularFornecedor(empresaId,
				fornecedores.toArray(new Fornecedor[0]));

		// Converte a entidade atualizada para o DTO de resposta
		VincularFornecedorResponseDTO responseDTO = new VincularFornecedorResponseDTO(
				new EmpresaResponseDTO(resultado.getEmpresa()), resultado.getAvisos());

		return ResponseEntity.ok(responseDTO);
	}

	@DeleteMapping("/{empresaId}/fornecedores")
	@Operation(summary = "Desvincular fornecedores", description = "Remove o vínculo entre uma empresa e os fornecedores especificados por seus IDs.")
	public ResponseEntity<Void> desvincularFornecedores(
			@Parameter(description = "ID da empresa da qual se deseja desvincular os fornecedores") @PathVariable UUID empresaId,

			@Parameter(description = "Lista de IDs de fornecedores a serem desvinculados, separados por vírgula") @RequestParam List<UUID> fornecedorIds) {

		empresaService.desvincularFornecedor(empresaId, fornecedorIds);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir empresa", description = "Exclui uma empresa a partir do id")
	public ResponseEntity<Void> removerEmpresa(@PathVariable UUID id) {
		empresaService.removerEmpresa(id);

		return ResponseEntity.ok().build();
	}
}
