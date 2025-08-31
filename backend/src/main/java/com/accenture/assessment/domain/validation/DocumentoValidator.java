package com.accenture.assessment.domain.validation;

import com.accenture.assessment.util.DocumentoValidatorUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DocumentoValidator implements ConstraintValidator<ValidDocumento, String> {

	@Override
	public boolean isValid(String documento, ConstraintValidatorContext context) {
		if (documento == null || documento.isBlank()) {
			return true; // @NotBlank garante validação para campos obrigatórios
		}
		return DocumentoValidatorUtils.isValid(documento);
	}
}
