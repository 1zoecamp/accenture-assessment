package com.accenture.assessment.domain.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DocumentoValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDocumento {
    String message() default "CPF/CNPJ inválido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}