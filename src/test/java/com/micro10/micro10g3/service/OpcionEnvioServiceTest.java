package com.micro10.micro10g3.service;

import com.micro10.micro10g3.model.OpcionEnvio;
import com.micro10.micro10g3.model.TipoEnvio;
import com.micro10.micro10g3.repository.OpcionEnvioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OpcionEnvioServiceTest {

    @Mock
    private OpcionEnvioRepository opcionEnvioRepository;

    @InjectMocks
    private OpcionEnvioService opcionEnvioService;

    @Test
    void testListarTodas() {
        OpcionEnvio opcion1 = new OpcionEnvio(1, TipoEnvio.ESTANDAR, "Entrega normal", 5.0f, 3);
        OpcionEnvio opcion2 = new OpcionEnvio(2, TipoEnvio.EXPRESS,"Entrega rápida", 10.0f, 1);
        List<OpcionEnvio> opciones = Arrays.asList(opcion1, opcion2);

        when(opcionEnvioRepository.findAll()).thenReturn(opciones);

        List<OpcionEnvio> resultado = opcionEnvioService.listarTodas();

        assertThat(resultado).hasSize(2);
        verify(opcionEnvioRepository).findAll();
    }

    @Test
    void testCrear() {
        OpcionEnvio opcion = new OpcionEnvio(0, TipoEnvio.PICKUP, "Retiro en tienda", 0.0f, 0);
        OpcionEnvio opcionGuardada = new OpcionEnvio(3, TipoEnvio.PICKUP, "Retiro en tienda", 0.0f, 0);

        when(opcionEnvioRepository.save(opcion)).thenReturn(opcionGuardada);

        OpcionEnvio resultado = opcionEnvioService.crear(opcion);

        assertThat(resultado.getId()).isEqualTo(3);
        verify(opcionEnvioRepository).save(opcion);
    }
}
