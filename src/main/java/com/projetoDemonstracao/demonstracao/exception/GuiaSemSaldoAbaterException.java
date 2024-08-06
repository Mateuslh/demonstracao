package com.projetoDemonstracao.demonstracao.exception;

public class GuiaSemSaldoAbaterException extends RuntimeException {
    public GuiaSemSaldoAbaterException(String message) {
        super(message);
    }
}