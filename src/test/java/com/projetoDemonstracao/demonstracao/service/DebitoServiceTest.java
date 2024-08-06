package com.projetoDemonstracao.demonstracao.service;

import com.projetoDemonstracao.demonstracao.domain.Debito;
import com.projetoDemonstracao.demonstracao.domain.Divida;
import com.projetoDemonstracao.demonstracao.enums.SituacaoGuia;
import com.projetoDemonstracao.demonstracao.exception.DebitoNaoAbertoInscreverException;
import com.projetoDemonstracao.demonstracao.repository.DebitoRepository;
import com.projetoDemonstracao.demonstracao.repository.DividaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

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
}
