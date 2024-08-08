package com.projetoDemonstracao.demonstracao.service;

import com.projetoDemonstracao.demonstracao.domain.Debito;
import com.projetoDemonstracao.demonstracao.domain.Divida;
import com.projetoDemonstracao.demonstracao.domain.Pagamento;
import com.projetoDemonstracao.demonstracao.domain.Saldo;
import com.projetoDemonstracao.demonstracao.domain.configsAplica.ConfiguracaoSaldo;
import com.projetoDemonstracao.demonstracao.exception.EntidadeNaoEncontradaException;
import com.projetoDemonstracao.demonstracao.exception.PagamentoSemDestinoException;
import com.projetoDemonstracao.demonstracao.repository.PagamentoRepository;
import com.projetoDemonstracao.demonstracao.repository.SaldoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private DebitoService debitoService;

    @Autowired
    private DividaService dividaService;
    @Autowired
    private ConfiguracaoSaldoService configuracaoSaldoService;
    @Autowired
    private SaldoRepository saldoRepository;

    public List<Pagamento> findAll() {
        return pagamentoRepository.findAll();
    }

    public Pagamento findById(Long id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pagamento não encontrado com o id: " + id));
    }

    @Transactional
    public Pagamento save(Pagamento pagamento) {
        if (!pagamentoTemDestinoInformado(pagamento)) {
            throw new PagamentoSemDestinoException("O pagamento precisa ter o destino informado.");
        }

        BigDecimal valorPago = pagamento.getValorPago();
        BigDecimal valorAberto;

        switch (pagamento.getDestinoPagamento()) {
            case DEBITO:
                valorAberto = processarDebito(pagamento, valorPago);
                break;
            case DIVIDA:
                valorAberto = processarDivida(pagamento, valorPago);
                break;
            default:
                throw new PagamentoSemDestinoException("Destino de pagamento inválido.");
        }

        if (valorAberto.compareTo(valorPago) < 0) {
            BigDecimal valorSaldo = valorPago.subtract(valorAberto);
            gerarSaldoPagamento(pagamento, valorSaldo);
            valorPago = valorAberto;
        }

        return pagamentoRepository.save(pagamento);
    }

    private BigDecimal processarDebito(Pagamento pagamento, BigDecimal valorPago) {
        Debito debito = debitoService.findById(pagamento.getDebito().getId());
        BigDecimal valorAberto = debitoService.getValorAberto(debito);

        if (valorAberto.compareTo(valorPago) < 0) {
            valorPago = valorAberto;
        }

        debitoService.pagarDebito(debito, valorPago);
        return valorAberto;
    }

    private BigDecimal processarDivida(Pagamento pagamento, BigDecimal valorPago) {
        Divida divida = dividaService.findById(pagamento.getDivida().getId());
        BigDecimal valorAberto = dividaService.getValorAberto(divida);

        if (valorAberto.compareTo(valorPago) < 0) {
            valorPago = valorAberto;
        }

        dividaService.pagarDivida(divida, valorPago);
        return valorAberto;
    }

    public void delete(Pagamento pagamento) {
        pagamentoRepository.delete(pagamento);
    }

    private boolean pagamentoTemDestinoInformado(Pagamento pagamento) {
        if (pagamento.getDestinoPagamento() == Pagamento.DestinoPagamento.DIVIDA && pagamento.getDivida() == null) {
            return false;
        } else
            return pagamento.getDestinoPagamento() != Pagamento.DestinoPagamento.DEBITO || pagamento.getDebito() != null;
    }

    private void gerarSaldoPagamento(Pagamento pagamento, BigDecimal vlSaldo) {
        ConfiguracaoSaldo configuracaoSaldo;
        Saldo saldo = new Saldo();
        saldo.setValor(vlSaldo);
        saldo.setPagamento(pagamento);
        saldo.setMensagem("Saldo gerado automaticamente pelo pagamento id:" + pagamento.getId());
        try {
            configuracaoSaldo = configuracaoSaldoService.findFirst();
        } catch (EntidadeNaoEncontradaException e) {
            configuracaoSaldo = configuracaoSaldoService.getDefault();
        }
        LocalDate dataVencimento = LocalDate.now().plusDays(configuracaoSaldo.getQtDiasVencimento());
        saldo.setDtValidade(dataVencimento);
        saldoRepository.save(saldo);
    }

}
