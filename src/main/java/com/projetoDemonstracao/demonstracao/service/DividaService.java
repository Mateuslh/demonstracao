package com.projetoDemonstracao.demonstracao.service;

import com.projetoDemonstracao.demonstracao.domain.Contribuinte;
import com.projetoDemonstracao.demonstracao.domain.Debito;
import com.projetoDemonstracao.demonstracao.domain.Divida;
import com.projetoDemonstracao.demonstracao.enums.SituacaoGuia;
import com.projetoDemonstracao.demonstracao.exception.EntidadeNaoEncontradaException;
import com.projetoDemonstracao.demonstracao.exception.GuiaNaoAbertaParaPagamentoException;
import com.projetoDemonstracao.demonstracao.exception.GuiaSemSaldoAbaterException;
import com.projetoDemonstracao.demonstracao.repository.DebitoRepository;
import com.projetoDemonstracao.demonstracao.repository.DividaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.projetoDemonstracao.demonstracao.utils.BigDecimalUtils.nullToZero;

@Service
public class DividaService {
    @Autowired
    private DividaRepository dividaRepository;
    @Autowired
    private DebitoRepository debitoRepository;
    @Autowired
    private ContribuinteService contribuinteService;

    public List<Divida> findAll() {
        return dividaRepository.findAll();
    }

    public Divida findById(Long id) {
        return dividaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Divida não encontrado com o id:" + id));
    }

    public Divida save(Divida divida) {
        return dividaRepository.save(divida);
    }

    public void delete(Divida divida) {
        dividaRepository.delete(divida);
    }

    public Debito findDebitoByDivida(Divida divida) {
        if (divida.getDebitoOrigem() != null) {
            return debitoRepository.getReferenceById(divida.getDebitoOrigem().getId());
        }
        return null;
    }

    public BigDecimal getValorTotal(Divida divida) {
        return nullToZero(divida.getValorLancado())
                .add(nullToZero(divida.getValorAcrescimo()))
                .subtract(nullToZero(divida.getValorDesconto()));
    }

    public BigDecimal getValorAberto(Divida divida) {
        return getValorTotal(divida).subtract(nullToZero(divida.getValorPago()));
    }

    @Transactional
    public void pagarDivida(Divida divida, BigDecimal valorPago) {
        BigDecimal valorTotalDebito = getValorTotal(divida);
        BigDecimal valorAberto = getValorAberto(divida);

        if (divida.getSituacaoGuia() != SituacaoGuia.ABERTA) {
            throw new GuiaNaoAbertaParaPagamentoException("A dívida precisa estar aberta para realizar o pagamento.");
        }

        if (valorAberto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new GuiaSemSaldoAbaterException("Dívida sem saldo para abater.");
        }

        BigDecimal valorRestante = valorPago.subtract(valorAberto);
        if (valorRestante.compareTo(BigDecimal.ZERO) > 0) {
            Contribuinte contribuinte = divida.getDebitoOrigem().getContribuinte();
            BigDecimal saldoAtual = nullToZero(contribuinte.getSaldo());
            BigDecimal novoSaldo = saldoAtual.add(valorRestante);
            contribuinte.setSaldo(novoSaldo);
            contribuinteService.save(contribuinte); 
        }

        BigDecimal novoValorPago = nullToZero(divida.getValorPago()).add(valorPago);
        if (novoValorPago.compareTo(valorTotalDebito) > 0) {
            novoValorPago = valorTotalDebito;
        }
        divida.setValorPago(novoValorPago);

        if (getValorAberto(divida).compareTo(BigDecimal.ZERO) == 0) {
            divida.setSituacaoGuia(SituacaoGuia.PAGA);
        }

        dividaRepository.save(divida);
    }

}
