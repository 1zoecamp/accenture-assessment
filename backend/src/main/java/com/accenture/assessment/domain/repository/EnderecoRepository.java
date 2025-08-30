package com.accenture.assessment.domain.repository;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

import com.accenture.assessment.domain.model.Endereco;

public interface EnderecoRepository extends CrudRepository<Endereco, UUID> {}
