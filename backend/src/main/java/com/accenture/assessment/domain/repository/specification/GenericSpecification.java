package com.accenture.assessment.domain.repository.specification;

import org.springframework.data.jpa.domain.Specification;

public class GenericSpecification {

	/**
	 * Cria uma Specification que filtra uma entidade por um campo de texto usando a
	 * cláusula LIKE (case-insensitive).
	 *
	 * @param <T>           O tipo da entidade.
	 * @param attributeName O nome do atributo/campo na entidade (ex: "nome",
	 *                      "documento").
	 * @param value         O valor a ser procurado.
	 * @return Uma Specification que pode ser usada nos métodos do repositório.
	 */
	public static <T> Specification<T> like(String attributeName, String value) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get(attributeName)),
				"%" + value.toLowerCase() + "%");
	}
}
