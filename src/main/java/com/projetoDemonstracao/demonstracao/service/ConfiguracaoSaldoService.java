package com.projetoDemonstracao.demonstracao.service;

import com.projetoDemonstracao.demonstracao.domain.configsAplica.ConfiguracaoSaldo;
import com.projetoDemonstracao.demonstracao.exception.EntidadeNaoEncontradaException;
import com.projetoDemonstracao.demonstracao.repository.ConfiguracaoSaldoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfiguracaoSaldoService {

    @Value("${saldo.validade.dias}")
    private int qtDiasVencimento;

    @Autowired
    private ConfiguracaoSaldoRepository configuracaoSaldoRepository;

    public ConfiguracaoSaldo save(ConfiguracaoSaldo configuracaoSaldo) {
        return configuracaoSaldoRepository.save(configuracaoSaldo);
    }

    public List<ConfiguracaoSaldo> findAll() {
        return configuracaoSaldoRepository.findAll();
    }

    public ConfiguracaoSaldo findById(Long id) {
        return configuracaoSaldoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Configuração de saldo não entrada para o id:" + id));
    }

    public ConfiguracaoSaldo findFirst() {
        ConfiguracaoSaldo configuracaoSaldo = configuracaoSaldoRepository.findFirstByOrderByIdDesc();
        if (configuracaoSaldo == null) {
            throw new EntidadeNaoEncontradaException("Configuração de saldo não entrada");
        }
        return configuracaoSaldo;
    }

    public ConfiguracaoSaldo getDefault() {
        return new ConfiguracaoSaldo(qtDiasVencimento);
    }
}
