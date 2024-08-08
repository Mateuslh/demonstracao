package com.projetoDemonstracao.demonstracao.service;

import com.projetoDemonstracao.demonstracao.domain.Contribuinte;
import com.projetoDemonstracao.demonstracao.domain.Debito;
import com.projetoDemonstracao.demonstracao.domain.Divida;
import com.projetoDemonstracao.demonstracao.exception.EntidadeNaoEncontradaException;
import com.projetoDemonstracao.demonstracao.repository.ContribuinteRepository;
import com.projetoDemonstracao.demonstracao.repository.DebitoRepository;
import com.projetoDemonstracao.demonstracao.repository.DividaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContribuinteServiceTest {

    @Mock
    private ContribuinteRepository contribuinteRepository;

    @Mock
    private DebitoRepository debitoRepository;

    @Mock
    private DividaRepository dividaRepository;

    @InjectMocks
    private ContribuinteService contribuinteService;

    private Contribuinte contribuinte;

    @BeforeEach
    void setUp() {
        contribuinte = new Contribuinte();
        contribuinte.setId(1L);
    }

    @Test
    void testFindAll() {
        List<Contribuinte> contribuintes = Collections.singletonList(contribuinte);
        when(contribuinteRepository.findAll()).thenReturn(contribuintes);

        List<Contribuinte> result = contribuinteService.findAll();

        assertEquals(contribuintes, result);
        verify(contribuinteRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(contribuinteRepository.findById(1L)).thenReturn(Optional.of(contribuinte));

        Contribuinte result = contribuinteService.findById(1L);

        assertEquals(contribuinte, result);
        verify(contribuinteRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(contribuinteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            contribuinteService.findById(1L);
        });

        verify(contribuinteRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        when(contribuinteRepository.save(contribuinte)).thenReturn(contribuinte);

        Contribuinte result = contribuinteService.save(contribuinte);

        assertEquals(contribuinte, result);
        verify(contribuinteRepository, times(1)).save(contribuinte);
    }

    @Test
    void testSaveWithNull() {
        assertThrows(NullPointerException.class, () -> {
            contribuinteService.save(null);
        });

        verify(contribuinteRepository, never()).save(any());
    }

    @Test
    void testDelete() {
        doNothing().when(contribuinteRepository).delete(contribuinte);

        contribuinteService.delete(contribuinte);

        verify(contribuinteRepository, times(1)).delete(contribuinte);
    }

    @Test
    void testDeleteWithNull() {
        assertThrows(NullPointerException.class, () -> {
            contribuinteService.delete(null);
        });

        verify(contribuinteRepository, never()).delete(any());
    }

    @Test
    void testDeleteNonExistentContribuinte() {
        doThrow(new EntidadeNaoEncontradaException("Contribuinte não encontrado")).when(contribuinteRepository).delete(any());

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            contribuinteService.delete(new Contribuinte());
        });

        verify(contribuinteRepository, times(1)).delete(any());
    }

    @Test
    void testFindAllDebitos() {
        List<Debito> debitos = Collections.singletonList(new Debito());
        when(debitoRepository.findDebitosByContribuinteId(1L)).thenReturn(debitos);

        List<Debito> result = contribuinteService.findAllDebitos(contribuinte);

        assertEquals(debitos, result);
        verify(debitoRepository, times(1)).findDebitosByContribuinteId(1L);
    }

    @Test
    void testFindAllDebitosEmpty() {
        when(debitoRepository.findDebitosByContribuinteId(1L)).thenReturn(Collections.emptyList());

        List<Debito> result = contribuinteService.findAllDebitos(contribuinte);

        assertTrue(result.isEmpty());
        verify(debitoRepository, times(1)).findDebitosByContribuinteId(1L);
    }

    @Test
    void testFindAllDividas() {
        List<Divida> dividas = Collections.singletonList(new Divida());
        when(dividaRepository.findAllByDebitoOrigem_Contribuinte_Id(1L)).thenReturn(dividas);

        List<Divida> result = contribuinteService.findAllDividas(contribuinte);

        assertEquals(dividas, result);
        verify(dividaRepository, times(1)).findAllByDebitoOrigem_Contribuinte_Id(1L);
    }

    @Test
    void testFindAllDividasEmpty() {
        when(dividaRepository.findAllByDebitoOrigem_Contribuinte_Id(1L)).thenReturn(Collections.emptyList());

        List<Divida> result = contribuinteService.findAllDividas(contribuinte);

        assertTrue(result.isEmpty());
        verify(dividaRepository, times(1)).findAllByDebitoOrigem_Contribuinte_Id(1L);
    }
}
