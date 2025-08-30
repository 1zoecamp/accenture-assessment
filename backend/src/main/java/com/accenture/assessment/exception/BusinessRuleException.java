package com.accenture.assessment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção a ser lançada quando uma regra de negócio é violada.
 *
 * A anotação @ResponseStatus(HttpStatus.CONFLICT) é um fallback. Se esta exceção
 * não for capturada por um @ExceptionHandler, o Spring a converterá em uma
 * resposta HTTP 409 (Conflict).
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class BusinessRuleException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Construtor que aceita uma mensagem de erro.
     * @param message A mensagem detalhando a violação da regra de negócio.
     */
    public BusinessRuleException(String message) {
        super(message);
    }
}
