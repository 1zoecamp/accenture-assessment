package com.accenture.assessment.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.accenture.assessment.domain.model.Endereco;
import com.accenture.assessment.domain.model.Fornecedor;
import com.accenture.assessment.domain.model.TipoPessoa;
import com.accenture.assessment.domain.repository.FornecedorRepository;
import com.accenture.assessment.domain.repository.specification.GenericSpecification;
import com.accenture.assessment.dto.FornecedorRequestDTO;
import com.accenture.assessment.exception.BusinessRuleException;
import com.accenture.assessment.service.EnderecoService;
import com.accenture.assessment.service.FornecedorService;

import jakarta.transaction.Transactional;

@Service
public class FornecedorServiceImpl implements FornecedorService {

	@Autowired
	private FornecedorRepository fornecedorRepository;

	@Autowired
	private EnderecoService enderecoService;

	@Override
	public Page<Fornecedor> listarFornecedores(String nome, String documento, Pageable pageable) {

		// Inicializa um spec vazio, porém true.
		Specification<Fornecedor> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

		// Se o parâmetro 'nome' for fornecido, adiciona o filtro de nome.
		if (nome != null && !nome.isBlank()) {
			spec = spec.and(GenericSpecification.like("nome", nome));
		}

		// Se o parâmetro 'documento' for fornecido, adiciona o filtro de documento.
		if (documento != null && !documento.isBlank()) {
			spec = spec.and(GenericSpecification.like("documento", documento));
		}

		return fornecedorRepository.findAll(spec, pageable);
	}

	@Override
	public Fornecedor buscarFornecedorPorId(UUID id) {
		return fornecedorRepository.findById(id).orElseThrow(NoSuchElementException::new);
	}

	@Override
	public List<Fornecedor> buscarFornecedoresPorId(List<UUID> fornecedoresId) {
		return fornecedorRepository.findAllById(fornecedoresId);
	}

	@Override
	public TipoPessoa definirTipoPessoa(String documento) {
		if (documento.length() == 11) {
			return TipoPessoa.PESSOA_FISICA;
		}
		if (documento.length() == 14) {
			return TipoPessoa.PESSOA_JURIDICA;
		} else {
			throw new BusinessRuleException("Formato do documento inválido.");
		}

	}

	@Override
	@Transactional
	public Fornecedor cadastrarFornecedor(FornecedorRequestDTO fornecedorDTO) {
		if (fornecedorRepository.existsByDocumento(fornecedorDTO.getDocumento())) {
			throw new BusinessRuleException("Fornecedor já cadastrado no sistema.");
		}

		Fornecedor novoFornecedor = new Fornecedor();

		novoFornecedor.setDocumento(fornecedorDTO.getDocumento());
		novoFornecedor.setNome(fornecedorDTO.getNome());
		novoFornecedor.setEmail(fornecedorDTO.getEmail());

		// Validação de documento e definição do tipoPessoa
		TipoPessoa tipoPessoa = definirTipoPessoa(fornecedorDTO.getDocumento());
		novoFornecedor.setTipoPessoa(tipoPessoa);

		if (tipoPessoa == TipoPessoa.PESSOA_FISICA) {
			String rg = fornecedorDTO.getRg();
			LocalDate dataNascimento = fornecedorDTO.getDataNascimento();

			if (rg == null || dataNascimento == null) {
				throw new BusinessRuleException("Para Pessoa Física, RG e Data de Nascimento são obrigatórios.");
			}
			novoFornecedor.setRg(rg);
			novoFornecedor.setDataNascimento(dataNascimento);
		}

		// Definição do endereço
		Endereco endereco = new Endereco();

		endereco.setCep(enderecoService.validarCep(fornecedorDTO.getEndereco().getCep()));
		endereco.setComplemento(fornecedorDTO.getEndereco().getComplemento());
		endereco.setLogradouro(fornecedorDTO.getEndereco().getLogradouro());
		endereco.setBairro(fornecedorDTO.getEndereco().getBairro());
		endereco.setLocalidade(fornecedorDTO.getEndereco().getLocalidade());
		endereco.setUf(fornecedorDTO.getEndereco().getUf());

		novoFornecedor.setEndereco(endereco);

		return fornecedorRepository.save(novoFornecedor);
	}

	@Override
	@Transactional
	public Fornecedor atualizarFornecedor(UUID fornecedorId, FornecedorRequestDTO fornecedorDTO) {
		// Verificações iniciais
		Fornecedor fornecedorExistente = fornecedorRepository.findById(fornecedorId)
				.orElseThrow(() -> new NoSuchElementException("Esse fornecedor não está cadastrado no sistema"));

		fornecedorRepository.findByDocumento(fornecedorDTO.getDocumento()).ifPresent(fornecedorEncontrado -> {
			if (!fornecedorEncontrado.getId().equals(fornecedorId)) {
				throw new BusinessRuleException("Documento já cadastrado para outro fornecedor.");
			}
		});

		// Atualização das informações
		fornecedorExistente.setDocumento(fornecedorDTO.getDocumento());
		fornecedorExistente.setNome(fornecedorDTO.getNome());
		fornecedorExistente.setEmail(fornecedorDTO.getEmail());

		TipoPessoa tipoPessoa = definirTipoPessoa(fornecedorDTO.getDocumento());
		fornecedorExistente.setTipoPessoa(tipoPessoa);

		if (tipoPessoa == TipoPessoa.PESSOA_FISICA) {
			String rg = fornecedorDTO.getRg();
			LocalDate dataNascimento = fornecedorDTO.getDataNascimento();

			if (rg == null || dataNascimento == null) {
				throw new BusinessRuleException("Para Pessoa Física, RG e Data de Nascimento são obrigatórios.");
			}
			fornecedorExistente.setRg(rg);
			fornecedorExistente.setDataNascimento(dataNascimento);
		} else {
			// Altera os campos de PF caso o tipoPessoa seja alterado
			fornecedorExistente.setRg(null);
			fornecedorExistente.setDataNascimento(null);
		}

		// Atualização do endereço
		Endereco endereco = fornecedorExistente.getEndereco();
		endereco.setCep(enderecoService.validarCep(fornecedorDTO.getEndereco().getCep()));
		endereco.setComplemento(fornecedorDTO.getEndereco().getComplemento());
		endereco.setLogradouro(fornecedorDTO.getEndereco().getLogradouro());
		endereco.setBairro(fornecedorDTO.getEndereco().getBairro());
		endereco.setLocalidade(fornecedorDTO.getEndereco().getLocalidade());
		endereco.setUf(fornecedorDTO.getEndereco().getUf());

		return fornecedorRepository.save(fornecedorExistente);
	}

	@Override
	public void removerFornecedor(UUID fornecedorId) {
		fornecedorRepository.deleteById(fornecedorId);
	}

}
