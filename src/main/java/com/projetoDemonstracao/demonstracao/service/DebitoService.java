package com.projetoDemonstracao.demonstracao.service;

import com.projetoDemonstracao.demonstracao.domain.Contribuinte;
import com.projetoDemonstracao.demonstracao.domain.Debito;
import com.projetoDemonstracao.demonstracao.domain.Divida;
import com.projetoDemonstracao.demonstracao.enums.SituacaoGuia;
import com.projetoDemonstracao.demonstracao.exception.DebitoNaoAbertoInscreverException;
import com.projetoDemonstracao.demonstracao.exception.EntidadeNaoEncontradaException;
import com.projetoDemonstracao.demonstracao.exception.GuiaNaoAbertaParaPagamentoException;
import com.projetoDemonstracao.demonstracao.exception.GuiaSemSaldoAbaterException;
import com.projetoDemonstracao.demonstracao.repository.DebitoRepository;
import com.projetoDemonstracao.demonstracao.repository.DividaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.projetoDemonstracao.demonstracao.utils.BigDecimalUtils.nullToZero;

@Service
public class DebitoService {

    @Autowired
    private DebitoRepository debitoRepository;
    @Autowired
    private DividaRepository dividaRepository;

    public List<Debito> findAll() {
        return debitoRepository.findAll();
    }

    public Debito findById(Long id) {
        return debitoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Debito não encontrado com o id:" + id));
    }

    public Debito save(Debito debito) {
        return debitoRepository.save(debito);
    }

    public void delete(Debito debito) {
        debitoRepository.delete(debito);
    }

    public List<Debito> findByContribuinteId(Long contribuinteId) {
        return debitoRepository.findDebitosByContribuinteId(contribuinteId);
    }

    public BigDecimal getValorTotal(Debito debito) {
        return nullToZero(debito.getValorLancado())
                .add(nullToZero(debito.getValorAcrescimo()))
                .subtract(nullToZero(debito.getValorDesconto()));
    }

    public BigDecimal getValorAberto(Debito debito) {
        return getValorTotal(debito).subtract(nullToZero(debito.getValorPago()));
    }

    @Transactional
    public Divida inscreverDebito(Debito debito) {

        if (debito.getSituacaoGuia() != SituacaoGuia.ABERTA) {
            throw new DebitoNaoAbertoInscreverException("A guia deve estar aberta para que seja inscrita.");
        }

        Divida divida = new Divida();
        divida.setDebitoOrigem(debito);
        divida.setSituacaoGuia(SituacaoGuia.ABERTA);
        divida.setValorLancado(debito.getValorLancado());
        divida.setValorDesconto(debito.getValorDesconto());
        divida.setValorAcrescimo(debito.getValorAcrescimo());

        debito.setSituacaoGuia(SituacaoGuia.INSCRITA);
        debitoRepository.save(debito);

        return dividaRepository.save(divida);
    }

    public void pagarDebito(Debito debito, BigDecimal valorPago) {
        BigDecimal valorTotalDebito = getValorTotal(debito);
        BigDecimal valorAberto = getValorAberto(debito);

        if (debito.getSituacaoGuia() != SituacaoGuia.ABERTA) {
            throw new GuiaNaoAbertaParaPagamentoException("O débito precisa estar aberto para realizar o pagamento.");
        }

        if (valorAberto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new GuiaSemSaldoAbaterException("Débito sem saldo para abater.");
        }

        if (valorPago.compareTo(valorAberto) > 0) {
            BigDecimal valorRestante = valorPago.subtract(valorAberto);
            Contribuinte contribuinte = debito.getContribuinte();
            BigDecimal saldoAtual = nullToZero(contribuinte.getSaldo());
            BigDecimal novoSaldo = saldoAtual.add(valorRestante);
            contribuinte.setSaldo(novoSaldo);
        }

        BigDecimal novoValorPago = nullToZero(debito.getValorPago()).add(valorPago);
        if (novoValorPago.compareTo(valorTotalDebito) > 0) {
            novoValorPago = valorTotalDebito;
        }
        debito.setValorPago(novoValorPago);

        if (getValorAberto(debito).compareTo(BigDecimal.ZERO) == 0) {
            debito.setSituacaoGuia(SituacaoGuia.PAGA);
        }

        debitoRepository.save(debito);
    }

}
