package com.projetoDemonstracao.demonstracao.dto.entityCreate;

import com.projetoDemonstracao.demonstracao.domain.Contribuinte;
import com.projetoDemonstracao.demonstracao.domain.Debito;
import com.projetoDemonstracao.demonstracao.dto.entityId.ContribuinteIdDTO;
import com.projetoDemonstracao.demonstracao.enums.SituacaoGuia;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DebitoCreateDTO {

    private ContribuinteIdDTO contribuinte;

    @Enumerated(EnumType.STRING)
    private SituacaoGuia situacaoGuia;

    private BigDecimal valorLancado;

    private BigDecimal valorDesconto;

    private BigDecimal valorAcrescimo;

    private BigDecimal valorPago;

    public Debito toDebito() {
        Debito debito = new Debito();
        if (contribuinte != null && contribuinte.getId() != null) {
            debito.setContribuinte(new Contribuinte(contribuinte.getId()));
        }
        debito.setSituacaoGuia(situacaoGuia);
        debito.setValorLancado(valorLancado);
        debito.setValorDesconto(valorDesconto);
        debito.setValorAcrescimo(valorAcrescimo);
        debito.setValorPago(valorPago);
        return debito;
    }
}
