package com.accenture.assessment.domain.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.accenture.assessment.domain.model.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, UUID>, JpaSpecificationExecutor<Empresa> {
	
	boolean existsByCnpj(String cnpj);

	Optional<Empresa> findByCnpj(String cnpj);
}
