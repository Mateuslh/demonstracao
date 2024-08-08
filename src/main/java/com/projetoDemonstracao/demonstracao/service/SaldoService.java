package com.projetoDemonstracao.demonstracao.service;

import com.projetoDemonstracao.demonstracao.domain.Saldo;
import com.projetoDemonstracao.demonstracao.exception.EntidadeNaoEncontradaException;
import com.projetoDemonstracao.demonstracao.repository.SaldoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaldoService {

    @Autowired
    private SaldoRepository saldoRepository;

    public Saldo save(Saldo saldo) {
        return saldoRepository.save(saldo);
    }

    public Saldo findById(Long id) {
        return saldoRepository.findById(id).orElseThrow((() -> new EntidadeNaoEncontradaException("Saldo não encontrado com o id:" + id)));
    }

    public List<Saldo> findAll() {
        return saldoRepository.findAll();
    }

    public void delete(Long id) {
        saldoRepository.deleteById(id);
    }

}
