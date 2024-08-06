package com.projetoDemonstracao.demonstracao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> {
    private boolean sucesso;
    private String mensagem;
    private T retorno;

}
