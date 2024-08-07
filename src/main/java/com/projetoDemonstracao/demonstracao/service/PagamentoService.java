package com.projetoDemonstracao.demonstracao.service;

import com.projetoDemonstracao.demonstracao.domain.Debito;
import com.projetoDemonstracao.demonstracao.domain.Divida;
import com.projetoDemonstracao.demonstracao.domain.Pagamento;
import com.projetoDemonstracao.demonstracao.exception.EntidadeNaoEncontradaException;
import com.projetoDemonstracao.demonstracao.exception.PagamentoSemDestinoException;
import com.projetoDemonstracao.demonstracao.repository.PagamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private DebitoService debitoService;

    @Autowired
    private DividaService dividaService;

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

        switch (pagamento.getDestinoPagamento()) {
            case DEBITO:
                Debito debito = debitoService.findById(pagamento.getDebito().getId());
                debitoService.pagarDebito(debito, pagamento.getValorPago());
                break;
            case DIVIDA:
                Divida divida = dividaService.findById(pagamento.getDivida().getId());
                dividaService.pagarDivida(divida, pagamento.getValorPago());
                break;
            default:
                throw new PagamentoSemDestinoException("Destino de pagamento inválido.");
        }
        return pagamentoRepository.save(pagamento);
    }

    public void delete(Pagamento pagamento) {
        pagamentoRepository.delete(pagamento);
    }

    private boolean pagamentoTemDestinoInformado(Pagamento pagamento) {
        if (pagamento.getDestinoPagamento() == Pagamento.DestinoPagamento.DIVIDA && pagamento.getDivida() == null) {
            return false;
        } else return pagamento.getDestinoPagamento() != Pagamento.DestinoPagamento.DEBITO || pagamento.getDebito() != null;
    }
}
