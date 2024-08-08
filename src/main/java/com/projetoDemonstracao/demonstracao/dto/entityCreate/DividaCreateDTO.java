package com.projetoDemonstracao.demonstracao.dto.entityCreate;

import com.projetoDemonstracao.demonstracao.domain.Debito;
import com.projetoDemonstracao.demonstracao.domain.Divida;
import com.projetoDemonstracao.demonstracao.dto.entityId.DebitoIdDTO;
import com.projetoDemonstracao.demonstracao.enums.SituacaoGuia;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DividaCreateDTO {
    @OneToOne
    private DebitoIdDTO debito;

    @Enumerated(EnumType.STRING)
    private SituacaoGuia situacaoGuia;

    private BigDecimal valorLancado;

    private BigDecimal valorDesconto;

    private BigDecimal valorAcrescimo;

    private BigDecimal valorPago;

    public Divida toDivida() {
        Divida divida = new Divida();
        if (this.debito != null && this.debito.getId() != null) {
            divida.setDebito(new Debito(this.debito.getId()));
        }
        divida.setSituacaoGuia(situacaoGuia);
        divida.setValorLancado(valorLancado);
        divida.setValorDesconto(valorDesconto);
        divida.setValorAcrescimo(valorAcrescimo);
        divida.setValorPago(valorPago);
        return divida;
    }
}
