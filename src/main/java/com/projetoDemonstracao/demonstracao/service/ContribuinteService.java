package com.projetoDemonstracao.demonstracao.service;

import com.projetoDemonstracao.demonstracao.domain.Contribuinte;
import com.projetoDemonstracao.demonstracao.domain.Debito;
import com.projetoDemonstracao.demonstracao.exception.EntidadeNaoEncontradaException;
import com.projetoDemonstracao.demonstracao.repository.ContribuinteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContribuinteService {

    @Autowired
    private ContribuinteRepository contribuinteRepository;

    @Autowired
    private DebitoService debitoService;

    public List<Contribuinte> findAll() {
        return contribuinteRepository.findAll();
    }

    public Contribuinte findById(Long id) {
        return contribuinteRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Contribuinte não encontrado com o id: " + id));
    }

    public Contribuinte save(Contribuinte contribuinte) {
        return contribuinteRepository.save(contribuinte);
    }

    public void delete(Contribuinte contribuinte) {
        contribuinteRepository.delete(contribuinte);
    }

    public List<Debito> findAllDebitos(Contribuinte contribuinte) {
        return debitoService.findByContribuinteId(contribuinte.getId());
    }

}

