package com.micro10.micro10g3.service;

import com.micro10.micro10g3.model.RutaEntrega;
import com.micro10.micro10g3.repository.RutaEntregaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RutaEntregaServiceTest {

    @Mock
    private RutaEntregaRepository rutaEntregaRepository;

    @InjectMocks
    private RutaEntregaService rutaEntregaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlanificarRuta() {
        RutaEntrega ruta = new RutaEntrega(0, "Origen A", 10.5f, null);
        RutaEntrega rutaGuardada = new RutaEntrega(1, "Origen A", 10.5f, null);

        when(rutaEntregaRepository.save(ruta)).thenReturn(rutaGuardada);

        RutaEntrega resultado = rutaEntregaService.planificarRuta(ruta);

        assertThat(resultado.getIdRuta()).isEqualTo(1);
        verify(rutaEntregaRepository).save(ruta);
    }

    @Test
    void testOptimizarRuta_existente() {
        RutaEntrega rutaExistente = new RutaEntrega(1, "Origen A", 10.5f, null);
        RutaEntrega rutaActualizada = new RutaEntrega(1, "Origen A", 7.0f, null);

        when(rutaEntregaRepository.findById(1)).thenReturn(Optional.of(rutaExistente));
        when(rutaEntregaRepository.save(rutaExistente)).thenReturn(rutaActualizada);

        RutaEntrega resultado = rutaEntregaService.optimizarRuta(1, 7.0f);

        assertThat(resultado.getDistanciaKm()).isEqualTo(7.0f);
        verify(rutaEntregaRepository).findById(1);
        verify(rutaEntregaRepository).save(rutaExistente);
    }

    @Test
    void testOptimizarRuta_noExiste() {
        when(rutaEntregaRepository.findById(99)).thenReturn(Optional.empty());

        RutaEntrega resultado = rutaEntregaService.optimizarRuta(99, 5.0f);

        assertThat(resultado).isNull();
        verify(rutaEntregaRepository).findById(99);
    }

    @Test
    void testModificarRuta_existente() {
        RutaEntrega rutaModificada = new RutaEntrega(1, "Origen B", 15.0f, null);

        when(rutaEntregaRepository.findById(1)).thenReturn(Optional.of(rutaModificada));
        when(rutaEntregaRepository.save(rutaModificada)).thenReturn(rutaModificada);

        RutaEntrega resultado = rutaEntregaService.modificarRuta(rutaModificada);

        assertThat(resultado.getOrigen()).isEqualTo("Origen B");
        verify(rutaEntregaRepository).findById(1);
        verify(rutaEntregaRepository).save(rutaModificada);
    }

    @Test
    void testModificarRuta_noExiste() {
        RutaEntrega rutaModificada = new RutaEntrega(99, "Origen B", 15.0f, null);

        when(rutaEntregaRepository.findById(99)).thenReturn(Optional.empty());

        RutaEntrega resultado = rutaEntregaService.modificarRuta(rutaModificada);

        assertThat(resultado).isNull();
        verify(rutaEntregaRepository).findById(99);
    }

    @Test
    void testEliminarRuta_existente() {
        RutaEntrega rutaExistente = new RutaEntrega(1, "Origen A", 10.5f, null);

        when(rutaEntregaRepository.findById(1)).thenReturn(Optional.of(rutaExistente));
        doNothing().when(rutaEntregaRepository).deleteById(1);

        boolean resultado = rutaEntregaService.eliminarRuta(1);

        assertThat(resultado).isTrue();
        verify(rutaEntregaRepository).findById(1);
        verify(rutaEntregaRepository).deleteById(1);
    }

    @Test
    void testEliminarRuta_noExiste() {
        when(rutaEntregaRepository.findById(99)).thenReturn(Optional.empty());

        boolean resultado = rutaEntregaService.eliminarRuta(99);

        assertThat(resultado).isFalse();
        verify(rutaEntregaRepository).findById(99);
    }
}
