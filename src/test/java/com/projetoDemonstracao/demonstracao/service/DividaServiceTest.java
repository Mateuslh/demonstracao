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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DividaServiceTest {

    @InjectMocks
    private DividaService dividaService;

    @Mock
    private DividaRepository dividaRepository;

    @Mock
    private DebitoRepository debitoRepository;

    @Mock
    private ContribuinteService contribuinteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Divida divida1 = new Divida();
        Divida divida2 = new Divida();
        List<Divida> dividas = Arrays.asList(divida1, divida2);

        when(dividaRepository.findAll()).thenReturn(dividas);

        List<Divida> result = dividaService.findAll();
        assertEquals(2, result.size());
        verify(dividaRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Divida divida = new Divida();
        when(dividaRepository.findById(1L)).thenReturn(Optional.of(divida));

        Divida result = dividaService.findById(1L);
        assertNotNull(result);
        verify(dividaRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(dividaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            dividaService.findById(1L);
        });

        verify(dividaRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        Divida divida = new Divida();
        when(dividaRepository.save(divida)).thenReturn(divida);

        Divida result = dividaService.save(divida);
        assertNotNull(result);
        verify(dividaRepository, times(1)).save(divida);
    }

    @Test
    void testDelete() {
        Divida divida = new Divida();
        doNothing().when(dividaRepository).delete(divida);

        dividaService.delete(divida);
        verify(dividaRepository, times(1)).delete(divida);
    }

    @Test
    void testFindDebitoByDivida() {
        Debito debito = new Debito();
        debito.setId(1L);
        Divida divida = new Divida();
        divida.setDebitoOrigem(debito);
        when(debitoRepository.getReferenceById(1L)).thenReturn(debito);

        Debito result = dividaService.findDebitoByDivida(divida);
        assertNotNull(result);
        verify(debitoRepository, times(1)).getReferenceById(1L);
    }

    @Test
    void testPagarDivida() {
        BigDecimal valorPago = new BigDecimal("260.00");


        Contribuinte contribuinte = new Contribuinte();
        contribuinte.setSaldo(new BigDecimal("100.00"));

        Debito debito = new Debito();
        debito.setContribuinte(contribuinte);

        Divida divida = new Divida();
        divida.setDebitoOrigem(debito);
        divida.setSituacaoGuia(SituacaoGuia.ABERTA);
        divida.setValorLancado(new BigDecimal("200.00"));
        divida.setValorAcrescimo(new BigDecimal("50.00"));
        divida.setValorDesconto(new BigDecimal("20.00"));
        divida.setValorPago(new BigDecimal("0.00"));

        BigDecimal valorAberto = dividaService.getValorAberto(divida);
        BigDecimal valorRestante = valorPago.subtract(valorAberto);

        when(dividaRepository.save(any(Divida.class))).thenReturn(divida);

        dividaService.pagarDivida(divida, valorPago);


        BigDecimal saldoEsperado = new BigDecimal("130.00");

        assertEquals(saldoEsperado, contribuinte.getSaldo());
        assertEquals(dividaService.getValorTotal(divida), divida.getValorPago());
        assertEquals(SituacaoGuia.PAGA, divida.getSituacaoGuia());

        verify(dividaRepository, times(1)).save(divida);
        verify(contribuinteService, times(1)).save(contribuinte);
    }


    @Test
    void testPagarDividaNaoAberta() {
        Divida divida = new Divida();
        divida.setSituacaoGuia(SituacaoGuia.PAGA);

        BigDecimal valorPago = new BigDecimal("50.00");

        assertThrows(GuiaNaoAbertaParaPagamentoException.class, () -> {
            dividaService.pagarDivida(divida, valorPago);
        });
    }

    @Test
    void testPagarDividaSemSaldoAbater() {
        Divida divida = new Divida();
        divida.setSituacaoGuia(SituacaoGuia.ABERTA);
        divida.setValorLancado(new BigDecimal("200.00"));
        divida.setValorAcrescimo(new BigDecimal("50.00"));
        divida.setValorDesconto(new BigDecimal("20.00"));
        divida.setValorPago(new BigDecimal("230.00"));

        BigDecimal valorPago = new BigDecimal("50.00");

        assertThrows(GuiaSemSaldoAbaterException.class, () -> {
            dividaService.pagarDivida(divida, valorPago);
        });
    }
}
