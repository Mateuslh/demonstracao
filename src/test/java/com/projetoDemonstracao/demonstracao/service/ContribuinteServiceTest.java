package com.projetoDemonstracao.demonstracao.service;

import com.projetoDemonstracao.demonstracao.domain.Contribuinte;
import com.projetoDemonstracao.demonstracao.domain.Debito;
import com.projetoDemonstracao.demonstracao.domain.Divida;
import com.projetoDemonstracao.demonstracao.exception.EntidadeNaoEncontradaException;
import com.projetoDemonstracao.demonstracao.repository.ContribuinteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContribuinteServiceTest {

    @Mock
    private ContribuinteRepository contribuinteRepository;

    @Mock
    private DebitoService debitoService;

    @Mock
    private DividaService dividaService;

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
    void testDelete() {
        doNothing().when(contribuinteRepository).delete(contribuinte);

        contribuinteService.delete(contribuinte);

        verify(contribuinteRepository, times(1)).delete(contribuinte);
    }

    @Test
    void testFindAllDebitos() {
        List<Debito> debitos = Collections.singletonList(new Debito());
        when(debitoService.findAllByContribuinteId(1L)).thenReturn(debitos);

        List<Debito> result = contribuinteService.findAllDebitos(contribuinte);

        assertEquals(debitos, result);
        verify(debitoService, times(1)).findAllByContribuinteId(1L);
    }

    @Test
    void testFindAllDividas() {
        List<Divida> dividas = Collections.singletonList(new Divida());
        when(dividaService.findAllByContribuinteId(1L)).thenReturn(dividas);

        List<Divida> result = contribuinteService.findAllDividas(contribuinte);

        assertEquals(dividas, result);
        verify(dividaService, times(1)).findAllByContribuinteId(1L);
    }
}
