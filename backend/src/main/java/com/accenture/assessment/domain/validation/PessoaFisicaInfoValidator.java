package com.accenture.assessment.domain.validation;

import com.accenture.assessment.domain.model.Fornecedor;
import com.accenture.assessment.domain.model.TipoPessoa;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Cria uma validação personalizada para Pessoa Física. Caso o tipo_pessoa seja
 * pessoa física, o rg e a data de nascimento se tornam informações
 * obrigatórias.
 */
public class PessoaFisicaInfoValidator implements ConstraintValidator<PessoaFisicaInfoRequired, Fornecedor> {

	@Override
	public boolean isValid(Fornecedor fornecedor, ConstraintValidatorContext context) {

		if (fornecedor == null || fornecedor.getTipoPessoa() != TipoPessoa.PESSOA_FISICA) {
			return true;
		}

		boolean isRgValid = fornecedor.getRg() != null && !fornecedor.getRg().isBlank();
		boolean isDataNascimentoValid = fornecedor.getDataNascimento() != null;

		return isRgValid && isDataNascimentoValid;
	}
}
