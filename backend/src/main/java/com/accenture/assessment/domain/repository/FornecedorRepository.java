package com.accenture.assessment.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.accenture.assessment.domain.model.Fornecedor;

import java.util.Optional;
import java.util.UUID;

public interface FornecedorRepository extends JpaRepository<Fornecedor, UUID>, JpaSpecificationExecutor<Fornecedor> {

	boolean existsByDocumento(String documento);

	Optional<Fornecedor> findByDocumento(String documento);
}