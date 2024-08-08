package com.projetoDemonstracao.demonstracao.dto.entityCreate;

import com.projetoDemonstracao.demonstracao.domain.configsAplica.ConfiguracaoSaldo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConfiguracaoSaldoCreateDTO {

    private int qtDiasVencimento;

    public ConfiguracaoSaldo toEntity() {
        return new ConfiguracaoSaldo(qtDiasVencimento);
    }

}
