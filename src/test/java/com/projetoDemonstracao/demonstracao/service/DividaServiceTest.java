package com.projetoDemonstracao.demonstracao.service;

import com.projetoDemonstracao.demonstracao.domain.Debito;
import com.projetoDemonstracao.demonstracao.domain.Divida;
import com.projetoDemonstracao.demonstracao.exception.EntidadeNaoEncontradaException;
import com.projetoDemonstracao.demonstracao.repository.DebitoRepository;
import com.projetoDemonstracao.demonstracao.repository.DividaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
}
