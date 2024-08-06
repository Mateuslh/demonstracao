package com.projetoDemonstracao.demonstracao.service;

import com.projetoDemonstracao.demonstracao.domain.Debito;
import com.projetoDemonstracao.demonstracao.domain.Divida;
import com.projetoDemonstracao.demonstracao.enums.SituacaoGuia;
import com.projetoDemonstracao.demonstracao.exception.DebitoNaoAbertoInscreverException;
import com.projetoDemonstracao.demonstracao.exception.EntidadeNaoEncontradaException;
import com.projetoDemonstracao.demonstracao.repository.DebitoRepository;
import com.projetoDemonstracao.demonstracao.repository.DividaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
