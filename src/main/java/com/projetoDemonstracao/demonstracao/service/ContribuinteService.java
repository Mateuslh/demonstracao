package com.projetoDemonstracao.demonstracao.service;

import com.projetoDemonstracao.demonstracao.domain.Contribuinte;
import com.projetoDemonstracao.demonstracao.domain.Debito;
import com.projetoDemonstracao.demonstracao.domain.Divida;
import com.projetoDemonstracao.demonstracao.exception.EntidadeNaoEncontradaException;
import com.projetoDemonstracao.demonstracao.repository.ContribuinteRepository;
import com.projetoDemonstracao.demonstracao.repository.DebitoRepository;
import com.projetoDemonstracao.demonstracao.repository.DividaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContribuinteService {

    @Autowired
    private ContribuinteRepository contribuinteRepository;
    @Autowired
    private DebitoRepository debitoRepository;
    @Autowired
    private DividaRepository dividaRepository;

    public List<Contribuinte> findAll() {
        return contribuinteRepository.findAll();
    }

    public Contribuinte findById(Long id) {
        return contribuinteRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Contribuinte não encontrado com o id: " + id));
    }

    public Contribuinte save(Contribuinte contribuinte) {
        if (contribuinte == null) {
            throw new NullPointerException("Contribuinte não pode ser nulo");
        }
        return contribuinteRepository.save(contribuinte);
    }

    public void delete(Contribuinte contribuinte) {
        if (contribuinte == null) {
            throw new NullPointerException("Contribuinte não pode ser nulo");
        }
        contribuinteRepository.delete(contribuinte);
    }

    public List<Debito> findAllDebitos(Contribuinte contribuinte) {
        return debitoRepository.findDebitosByContribuinteId(contribuinte.getId());
    }

    public List<Divida> findAllDividas(Contribuinte contribuinte) {
        return dividaRepository.findAllByDebito_Contribuinte_Id(contribuinte.getId());
    }
}
