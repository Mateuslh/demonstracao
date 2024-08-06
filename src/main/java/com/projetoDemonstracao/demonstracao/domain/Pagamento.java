package com.projetoDemonstracao.demonstracao.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = true, unique = true)
    private Long id;
    @ManyToOne
    private Debito debito;
    @ManyToOne
    @JoinColumn()
    private Divida divida;
    private BigDecimal valorPago;
    private Date dataPagamento;
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private DestinoPagamento destinoPagamento;
    @Enumerated(EnumType.STRING)
    private SituacaoPagamento situacaoPagamento;
    @Enumerated(EnumType.STRING)
    private TipoBaixa tipoBaixa;

    public enum SituacaoPagamento {PROCESSADO, ESTORNADO}
    public enum TipoBaixa {AUTOMATICA, MANUAL}
    public enum DestinoPagamento {DEBITO, DIVIDA}

}
