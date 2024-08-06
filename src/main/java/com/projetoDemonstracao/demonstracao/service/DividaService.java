package com.projetoDemonstracao.demonstracao.service;

import com.projetoDemonstracao.demonstracao.domain.Debito;
import com.projetoDemonstracao.demonstracao.domain.Divida;
import com.projetoDemonstracao.demonstracao.exception.EntidadeNaoEncontradaException;
import com.projetoDemonstracao.demonstracao.repository.DebitoRepository;
import com.projetoDemonstracao.demonstracao.repository.DividaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.projetoDemonstracao.demonstracao.utils.BigDecimalUtils.nullToZero;

@Service
public class DividaService {
    @Autowired
    private DividaRepository dividaRepository;
    @Autowired
    private DebitoRepository debitoRepository;

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

    public BigDecimal getValorAtualDebito(Debito debito) {
        return nullToZero(debito.getValorLancado())
                .add(nullToZero(debito.getValorAcrescimo()))
                .subtract(nullToZero(debito.getValorDesconto()));
    }
}
