package com.projetoDemonstracao.demonstracao.exception;

public class PagamentoSemDestinoException extends RuntimeException {
    public PagamentoSemDestinoException(String message) {
        super(message);
    }
}