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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DebitoServiceTest {

    @InjectMocks
    private DebitoService debitoService;

    @Mock
    private DebitoRepository debitoRepository;

    @Mock
    private DividaRepository dividaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        Debito debito = new Debito();
        when(debitoRepository.findById(1L)).thenReturn(Optional.of(debito));

        Debito result = debitoService.findById(1L);
        assertNotNull(result);
        verify(debitoRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(debitoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            debitoService.findById(1L);
        });

        verify(debitoRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        Debito debito = new Debito();
        when(debitoRepository.save(debito)).thenReturn(debito);

        Debito result = debitoService.save(debito);
        assertNotNull(result);
        verify(debitoRepository, times(1)).save(debito);
    }

    @Test
    void testDelete() {
        Debito debito = new Debito();
        doNothing().when(debitoRepository).delete(debito);

        debitoService.delete(debito);
        verify(debitoRepository, times(1)).delete(debito);
    }

    @Test
    void testFindByContribuinteId() {
        Contribuinte contribuinte = new Contribuinte();
        contribuinte.setId(1L);

        when(debitoRepository.findDebitosByContribuinteId(contribuinte.getId())).thenReturn(Arrays.asList(new Debito(), new Debito()));

        List<Debito> result = debitoService.findAllByContribuinteId(contribuinte.getId());
        assertEquals(2, result.size());
        verify(debitoRepository, times(1)).findDebitosByContribuinteId(contribuinte.getId());
    }

    @Test
    void testInscreverDebitoComSucesso() {
        Debito debito = new Debito();
        debito.setSituacaoGuia(SituacaoGuia.ABERTA);
        debito.setValorLancado(BigDecimal.valueOf(1000.0));
        debito.setValorDesconto(BigDecimal.valueOf(100.0));
        debito.setValorAcrescimo(BigDecimal.valueOf(50.0));

        Divida dividaSalva = new Divida();
        when(debitoRepository.save(any(Debito.class))).thenReturn(debito);
        when(dividaRepository.save(any(Divida.class))).thenReturn(dividaSalva);

        Divida resultado = debitoService.inscreverDebito(debito);

        assertNotNull(resultado);
        verify(debitoRepository, times(1)).save(debito);
        verify(dividaRepository, times(1)).save(any(Divida.class));
    }

    @Test
    void testInscreverDebitoComSituacaoNaoAberta() {
        Debito debito = new Debito();
        debito.setSituacaoGuia(SituacaoGuia.PAGA);

        DebitoNaoAbertoInscreverException exception = assertThrows(DebitoNaoAbertoInscreverException.class, () -> {
            debitoService.inscreverDebito(debito);
        });

        assertEquals("A guia deve estar aberta para que seja inscrita.", exception.getMessage());
        verify(debitoRepository, never()).save(any(Debito.class));
        verify(dividaRepository, never()).save(any(Divida.class));
    }

    @Test
    void testInscreverDebitoComErroNoSaveDebito() {
        Debito debito = new Debito();
        debito.setSituacaoGuia(SituacaoGuia.ABERTA);
        debito.setValorLancado(BigDecimal.valueOf(1000.0));
        debito.setValorDesconto(BigDecimal.valueOf(100.0));
        debito.setValorAcrescimo(BigDecimal.valueOf(50.0));

        when(debitoRepository.save(any(Debito.class))).thenThrow(new RuntimeException("Erro no save debito"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            debitoService.inscreverDebito(debito);
        });

        assertEquals("Erro no save debito", exception.getMessage());
        verify(debitoRepository, times(1)).save(debito);
        verify(dividaRepository, never()).save(any(Divida.class));
    }

    @Test
    void testInscreverDebitoComErroNoSaveDivida() {
        Debito debito = new Debito();
        debito.setSituacaoGuia(SituacaoGuia.ABERTA);
        debito.setValorLancado(BigDecimal.valueOf(1000.0));
        debito.setValorDesconto(BigDecimal.valueOf(100.0));
        debito.setValorAcrescimo(BigDecimal.valueOf(50.0));

        when(debitoRepository.save(any(Debito.class))).thenReturn(debito);
        when(dividaRepository.save(any(Divida.class))).thenThrow(new RuntimeException("Erro no save divida"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            debitoService.inscreverDebito(debito);
        });

        assertEquals("Erro no save divida", exception.getMessage());
        verify(debitoRepository, times(1)).save(debito);
        verify(dividaRepository, times(1)).save(any(Divida.class));
    }

    @Test
    void testPagarDebitoComSucesso() {
        Contribuinte contribuinte = new Contribuinte();
        contribuinte.setSaldo(new BigDecimal("100.00"));

        Debito debito = new Debito();
        debito.setContribuinte(contribuinte);
        debito.setSituacaoGuia(SituacaoGuia.ABERTA);
        debito.setValorLancado(new BigDecimal("200.00"));
        debito.setValorAcrescimo(new BigDecimal("50.00"));
        debito.setValorDesconto(new BigDecimal("20.00"));
        debito.setValorPago(new BigDecimal("0.00"));

        BigDecimal valorPago = new BigDecimal("230.00");
        BigDecimal valorTotalDebito = debitoService.getValorTotal(debito);
        BigDecimal valorAberto = debitoService.getValorAberto(debito);

        when(debitoRepository.save(any(Debito.class))).thenReturn(debito);

        debitoService.pagarDebito(debito, valorPago);

        BigDecimal valorRestante = valorPago.subtract(valorAberto);
        BigDecimal saldoEsperado = contribuinte.getSaldo();
        if (valorRestante.compareTo(BigDecimal.ZERO) > 0) {
            saldoEsperado = saldoEsperado.add(valorRestante);
        }

        assertEquals(saldoEsperado, contribuinte.getSaldo());
        assertEquals(valorPago, debito.getValorPago());
        assertEquals(SituacaoGuia.PAGA, debito.getSituacaoGuia());

        verify(debitoRepository, times(1)).save(debito);
    }


    @Test
    void testPagarDebitoNaoAberta() {
        Debito debito = new Debito();
        debito.setSituacaoGuia(SituacaoGuia.PAGA);

        BigDecimal valorPago = new BigDecimal("50.00");

        assertThrows(GuiaNaoAbertaParaPagamentoException.class, () -> {
            debitoService.pagarDebito(debito, valorPago);
        });
    }

    @Test
    void testPagarDebitoSemSaldoAbater() {
        Debito debito = new Debito();
        debito.setSituacaoGuia(SituacaoGuia.ABERTA);
        debito.setValorLancado(new BigDecimal("200.00"));
        debito.setValorAcrescimo(new BigDecimal("50.00"));
        debito.setValorDesconto(new BigDecimal("20.00"));
        debito.setValorPago(new BigDecimal("230.00"));

        BigDecimal valorPago = new BigDecimal("50.00");

        assertThrows(GuiaSemSaldoAbaterException.class, () -> {
            debitoService.pagarDebito(debito, valorPago);
        });
    }
}
