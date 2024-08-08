package com.projetoDemonstracao.demonstracao.domain.configsAplica;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfiguracaoSaldo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Max(value = 3600, message = "A quantidade de dias para o vencimento precisa ser inferior a 3600.")
    @Positive
    private int qtDiasVencimento;

    public ConfiguracaoSaldo(int qtDiasVencimento) {
        this.qtDiasVencimento = qtDiasVencimento;
    }
}
