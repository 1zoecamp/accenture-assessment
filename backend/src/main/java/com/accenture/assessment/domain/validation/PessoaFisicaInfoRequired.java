package com.accenture.assessment.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = PessoaFisicaInfoValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PessoaFisicaInfoRequired {
	String message() default "Para pessoa física, RG e data de nascimento são obrigatórios.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
