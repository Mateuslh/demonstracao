package com.projetoDemonstracao.demonstracao.service;

import com.projetoDemonstracao.demonstracao.domain.Contribuinte;
import com.projetoDemonstracao.demonstracao.domain.Debito;
import com.projetoDemonstracao.demonstracao.domain.Divida;
import com.projetoDemonstracao.demonstracao.enums.SituacaoGuia;
import com.projetoDemonstracao.demonstracao.exception.EntidadeNaoEncontradaException;
import com.projetoDemonstracao.demonstracao.exception.GuiaNaoAbertaParaPagamentoException;
import com.projetoDemonstracao.demonstracao.exception.GuiaSemSaldoAbaterException;
import com.projetoDemonstracao.demonstracao.exception.GuiaValorInferiorValorPagoException;
import com.projetoDemonstracao.demonstracao.repository.DebitoRepository;
import com.projetoDemonstracao.demonstracao.repository.DividaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    void testSaveWithNull() {
        assertThrows(NullPointerException.class, () -> {
            dividaService.save(null);
        });

        verify(dividaRepository, never()).save(any());
    }

    @Test
    void testDelete() {
        Divida divida = new Divida();
        doNothing().when(dividaRepository).delete(divida);

        dividaService.delete(divida);
        verify(dividaRepository, times(1)).delete(divida);
    }

    @Test
    void testDeleteWithNull() {
        assertThrows(NullPointerException.class, () -> {
            dividaService.delete(null);
        });

        verify(dividaRepository, never()).delete(any());
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
    void testFindDebitoByDividaNull() {
        Divida divida = new Divida();
        Debito result = dividaService.findDebitoByDivida(divida);
        assertNull(result);
        verify(debitoRepository, never()).getReferenceById(anyLong());
    }

    @Test
    void testFindDebitoByDividaDebitoNotFound() {
        Divida divida = new Divida();
        Debito debito = new Debito();
        debito.setId(1L);
        divida.setDebitoOrigem(debito);

        when(debitoRepository.getReferenceById(1L)).thenThrow(new EntidadeNaoEncontradaException("Débito não encontrado"));

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            dividaService.findDebitoByDivida(divida);
        });

        verify(debitoRepository, times(1)).getReferenceById(1L);
    }

    @Test
    void testGetValorTotal() {
        Divida divida = new Divida();
        divida.setValorLancado(new BigDecimal("200.00"));
        divida.setValorAcrescimo(new BigDecimal("50.00"));
        divida.setValorDesconto(new BigDecimal("20.00"));

        BigDecimal valorTotal = dividaService.getValorTotal(divida);
        assertEquals(new BigDecimal("230.00"), valorTotal);
    }

    @Test
    void testGetValorTotalWithNulls() {
        Divida divida = new Divida();
        divida.setValorLancado(null);
        divida.setValorAcrescimo(null);
        divida.setValorDesconto(null);

        BigDecimal valorTotal = dividaService.getValorTotal(divida);
        assertEquals(BigDecimal.ZERO, valorTotal);
    }

    @Test
    void testGetValorAberto() {
        Divida divida = new Divida();
        divida.setValorLancado(new BigDecimal("200.00"));
        divida.setValorAcrescimo(new BigDecimal("50.00"));
        divida.setValorDesconto(new BigDecimal("20.00"));
        divida.setValorPago(new BigDecimal("50.00"));

        BigDecimal valorAberto = dividaService.getValorAberto(divida);
        assertEquals(new BigDecimal("180.00"), valorAberto);
    }

    @Test
    void testGetValorAbertoWithNulls() {
        Divida divida = new Divida();
        divida.setValorLancado(null);
        divida.setValorAcrescimo(null);
        divida.setValorDesconto(null);
        divida.setValorPago(null);

        BigDecimal valorAberto = dividaService.getValorAberto(divida);
        assertEquals(BigDecimal.ZERO, valorAberto);
    }

    @Test
    void testFindAllByContribuinteId() {
        List<Divida> dividas = Collections.singletonList(new Divida());
        when(dividaRepository.findAllByDebitoOrigem_Contribuinte_Id(1L)).thenReturn(dividas);

        List<Divida> result = dividaService.findAllByContribuinteId(1L);
        assertEquals(dividas, result);
        verify(dividaRepository, times(1)).findAllByDebitoOrigem_Contribuinte_Id(1L);
    }

    @Test
    void testFindAllByContribuinteIdEmpty() {
        when(dividaRepository.findAllByDebitoOrigem_Contribuinte_Id(1L)).thenReturn(Collections.emptyList());

        List<Divida> result = dividaService.findAllByContribuinteId(1L);
        assertTrue(result.isEmpty());
        verify(dividaRepository, times(1)).findAllByDebitoOrigem_Contribuinte_Id(1L);
    }

    @Test
    void testPagarDivida() {
        BigDecimal valorPago = new BigDecimal("230.00");

        Contribuinte contribuinte = new Contribuinte();

        Debito debito = new Debito();
        debito.setContribuinte(contribuinte);

        Divida divida = new Divida();
        divida.setDebitoOrigem(debito);
        divida.setSituacaoGuia(SituacaoGuia.ABERTA);
        divida.setValorLancado(new BigDecimal("200.00"));
        divida.setValorAcrescimo(new BigDecimal("50.00"));
        divida.setValorDesconto(new BigDecimal("20.00"));
        divida.setValorPago(new BigDecimal("0.00"));

        when(dividaRepository.save(any(Divida.class))).thenReturn(divida);

        dividaService.pagarDivida(divida, valorPago);

        assertEquals(dividaService.getValorTotal(divida), divida.getValorPago());
        assertEquals(SituacaoGuia.PAGA, divida.getSituacaoGuia());

        verify(dividaRepository, times(1)).save(divida);
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

    @Test
    void testPagarDividaComValorSuperior() {
        Divida divida = new Divida();
        divida.setSituacaoGuia(SituacaoGuia.ABERTA);
        divida.setValorLancado(new BigDecimal("200.00"));
        divida.setValorAcrescimo(new BigDecimal("50.00"));
        divida.setValorDesconto(new BigDecimal("20.00"));
        divida.setValorPago(new BigDecimal("0.00"));

        BigDecimal valorPago = new BigDecimal("300.00");

        assertThrows(GuiaValorInferiorValorPagoException.class, () -> {
            dividaService.pagarDivida(divida, valorPago);
        });
    }

    @Test
    void testPagarDividaComValorExato() {
        Divida divida = new Divida();
        divida.setSituacaoGuia(SituacaoGuia.ABERTA);
        divida.setValorLancado(new BigDecimal("200.00"));
        divida.setValorAcrescimo(new BigDecimal("50.00"));
        divida.setValorDesconto(new BigDecimal("20.00"));
        divida.setValorPago(new BigDecimal("0.00"));

        BigDecimal valorPago = new BigDecimal("230.00");

        when(dividaRepository.save(any(Divida.class))).thenReturn(divida);

        dividaService.pagarDivida(divida, valorPago);

        assertEquals(SituacaoGuia.PAGA, divida.getSituacaoGuia());
        assertEquals(valorPago, divida.getValorPago());

        verify(dividaRepository, times(1)).save(divida);
    }
}
