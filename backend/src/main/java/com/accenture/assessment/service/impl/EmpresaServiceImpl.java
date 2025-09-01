package com.accenture.assessment.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.accenture.assessment.domain.model.Empresa;
import com.accenture.assessment.domain.model.Endereco;
import com.accenture.assessment.domain.model.Fornecedor;
import com.accenture.assessment.domain.repository.EmpresaRepository;
import com.accenture.assessment.domain.repository.FornecedorRepository;
import com.accenture.assessment.domain.repository.specification.GenericSpecification;
import com.accenture.assessment.dto.EmpresaRequestDTO;
import com.accenture.assessment.exception.BusinessRuleException;
import com.accenture.assessment.service.EmpresaService;
import com.accenture.assessment.service.result.VinculoFornecedorResult;

import jakarta.transaction.Transactional;

@Service
public class EmpresaServiceImpl implements EmpresaService {

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private FornecedorRepository fornecedorRepository;

	@Autowired
	private EnderecoServiceImpl enderecoService;

	@Override
	public Page<Empresa> listarEmpresas(String nomeFantasia, String cnpj, Pageable pageable) {
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

	@Override
	public Empresa buscarEmpresaPorId(UUID id) {
		return empresaRepository.findById(id).orElseThrow(NoSuchElementException::new);
	}

	@Override
	@Transactional
	public Empresa cadastrarEmpresa(EmpresaRequestDTO empresaDTO) {
		if (empresaRepository.existsByCnpj(empresaDTO.getCnpj())) {
			throw new BusinessRuleException("CNPJ já cadastrado para outra empresa.");
		}

		Empresa novaEmpresa = new Empresa();
		novaEmpresa.setCnpj(empresaDTO.getCnpj());
		novaEmpresa.setNomeFantasia(empresaDTO.getNomeFantasia());

		Endereco endereco = new Endereco();

		endereco.setCep(enderecoService.validarCep(empresaDTO.getEndereco().getCep()));
		endereco.setComplemento(empresaDTO.getEndereco().getComplemento());
		endereco.setLogradouro(empresaDTO.getEndereco().getLogradouro());
		endereco.setBairro(empresaDTO.getEndereco().getBairro());
		endereco.setLocalidade(empresaDTO.getEndereco().getLocalidade());
		endereco.setUf(empresaDTO.getEndereco().getUf());

		novaEmpresa.setEndereco(endereco);

		return empresaRepository.save(novaEmpresa);
	}

	@Override
	@Transactional
	public Empresa atualizarEmpresa(UUID empresaId, EmpresaRequestDTO empresaDTO) {
		Empresa empresaExistente = empresaRepository.findById(empresaId)
				.orElseThrow(() -> new NoSuchElementException("Essa empresa não está cadastrada no sistema."));

		empresaRepository.findByCnpj(empresaDTO.getCnpj()).ifPresent(empresaEncontrada -> {
			if (!empresaEncontrada.getId().equals(empresaId)) {
				throw new BusinessRuleException("CNPJ já cadastrado para outra empresa.");
			}
		});

		empresaExistente.setCnpj(empresaDTO.getCnpj());
		empresaExistente.setNomeFantasia(empresaDTO.getNomeFantasia());

		Endereco endereco = empresaExistente.getEndereco();

		endereco.setCep(enderecoService.validarCep(empresaDTO.getEndereco().getCep()));
		endereco.setLogradouro(empresaDTO.getEndereco().getLogradouro());
		endereco.setComplemento(empresaDTO.getEndereco().getComplemento());
		endereco.setBairro(empresaDTO.getEndereco().getBairro());
		endereco.setLocalidade(empresaDTO.getEndereco().getLocalidade());
		endereco.setUf(empresaDTO.getEndereco().getUf());

		return empresaRepository.save(empresaExistente);
	}

	@Override
	public void removerEmpresa(UUID empresaId) {
		empresaRepository.deleteById(empresaId);
	}

	@Override
	@Transactional
	public VinculoFornecedorResult vincularFornecedor(UUID empresaId, Fornecedor... fornecedores) {
		Empresa empresa = empresaRepository.findById(empresaId).orElseThrow(NoSuchElementException::new);
		List<String> avisos = new ArrayList<>();

		for (Fornecedor fornecedor : fornecedores) {
			// Checa se fornecedor já vinculado
			if (empresa.getFornecedores().contains(fornecedor)) {
				avisos.add("O fornecedor '" + fornecedor.getNome() + "' (Documento: " + fornecedor.getDocumento()
						+ ") já está vinculado à empresa.");
				continue;
			}

			// Verificação para empresas do PR
			if ("PR".equals(empresa.getEndereco().getUf()) && fornecedor.fornecedorEhMenorDeIdade()) {
				avisos.add("O fornecedor '" + fornecedor.getNome() + "' (Documento: " + fornecedor.getDocumento()
						+ ") não foi vinculado por ser menor de idade.");
				continue;
			}
			empresa.vincularFornecedor(fornecedor);
		}

		Empresa empresaSalva = empresaRepository.save(empresa);

		return new VinculoFornecedorResult(empresaSalva, avisos);
	}

	@Override
	@Transactional
	public Empresa desvincularFornecedor(UUID empresaId, List<UUID> fornecedorIds) {
		Empresa empresa = empresaRepository.findById(empresaId).orElseThrow(NoSuchElementException::new);

		List<Fornecedor> fornecedores = fornecedorRepository.findAllById(fornecedorIds);

		for (Fornecedor fornecedor : fornecedores) {
			empresa.desvincularFornecedor(fornecedor);
		}

		return empresaRepository.save(empresa);
	}

}
