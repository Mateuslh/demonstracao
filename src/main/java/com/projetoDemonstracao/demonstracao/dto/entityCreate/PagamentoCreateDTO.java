package com.projetoDemonstracao.demonstracao.dto.entityCreate;

import com.projetoDemonstracao.demonstracao.domain.Debito;
import com.projetoDemonstracao.demonstracao.domain.Divida;
import com.projetoDemonstracao.demonstracao.domain.Pagamento;
import com.projetoDemonstracao.demonstracao.dto.entityId.DebitoIdDTO;
import com.projetoDemonstracao.demonstracao.dto.entityId.DividaIdDTO;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PagamentoCreateDTO {
    @ManyToOne
    private DebitoIdDTO debito;
    @ManyToOne
    private DividaIdDTO divida;
    private BigDecimal valorPago;
    private LocalDate dtPagamento;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Pagamento.DestinoPagamento destinoPagamento;
    @Enumerated(EnumType.STRING)
    private Pagamento.SituacaoPagamento situacaoPagamento;
    @Enumerated(EnumType.STRING)
    private Pagamento.TipoBaixa tipoBaixa;

    public Pagamento toPagamento() {
        Pagamento pagamento = new Pagamento();

        if (this.debito != null && this.debito.getId() != null) {
            pagamento.setDebito(new Debito(this.debito.getId()));
        }
        if (this.divida != null && this.divida.getId() != null) {
            pagamento.setDivida(new Divida(this.divida.getId()));
        }
        pagamento.setValorPago(valorPago);
        pagamento.setDtPagamento(dtPagamento);
        pagamento.setDestinoPagamento(destinoPagamento);
        pagamento.setSituacaoPagamento(situacaoPagamento);
        pagamento.setTipoBaixa(tipoBaixa);
        return pagamento;
    }

}
