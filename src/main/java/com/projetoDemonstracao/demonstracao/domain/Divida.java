package com.projetoDemonstracao.demonstracao.domain;

import com.projetoDemonstracao.demonstracao.enums.SituacaoGuia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Divida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @OneToOne
    @JoinColumn(nullable = false, unique = true)
    private Debito debito;

    @Enumerated(EnumType.STRING)
    private SituacaoGuia situacaoGuia;

    private BigDecimal valorLancado;

    private BigDecimal valorDesconto;

    private BigDecimal valorAcrescimo;

    private BigDecimal valorPago;

    public Divida(Long id) {
    }
}
