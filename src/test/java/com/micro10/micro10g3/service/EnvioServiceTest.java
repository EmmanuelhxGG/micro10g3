package com.micro10.micro10g3.service;

import com.micro10.micro10g3.model.*;
import com.micro10.micro10g3.repository.EnvioRepository;
import com.micro10.micro10g3.repository.RutaEntregaRepository;
import com.micro10.micro10g3.repository.OpcionEnvioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class EnvioServiceTest {

    @Mock
    private EnvioRepository envioRepository;

    @Mock
    private RutaEntregaRepository rutaEntregaRepository;

    @Mock
    private OpcionEnvioRepository opcionEnvioRepository;

    @InjectMocks
    private EnvioService envioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }    

@Test
void testGuardarRutaNoExiste() {
    OpcionEnvio opcion = new OpcionEnvio(1, TipoEnvio.EXPRESS, "Rápido", 1000f, 2);
    Envio envio = new Envio(0, 10, 5, "Destino", null, TipoDestinatario.CLIENTE, null, null, null, new RutaEntrega(1, "Origen", 10f, null), opcion);

    when(rutaEntregaRepository.findById(1)).thenReturn(Optional.empty());
    when(opcionEnvioRepository.findById(1)).thenReturn(Optional.of(opcion));

    Envio resultado = envioService.guardar(envio);

    assertThat(resultado).isNull();
}

@Test
void testGuardarOpcionNoExiste() {
    RutaEntrega ruta = new RutaEntrega(1, "Origen", 10f, null);
    Envio envio = new Envio(0, 10, 5, "Destino", null, TipoDestinatario.CLIENTE, null, null, null, ruta, new OpcionEnvio(1, TipoEnvio.EXPRESS, "Rapido", 1000f, 2));

    when(rutaEntregaRepository.findById(1)).thenReturn(Optional.of(ruta));
    when(opcionEnvioRepository.findById(1)).thenReturn(Optional.empty());

    Envio resultado = envioService.guardar(envio);

    assertThat(resultado).isNull();
}

@Test
void testActualizarEstadoNoExiste() {
    when(envioRepository.findById(99)).thenReturn(Optional.empty());

    Envio resultado = envioService.actualizarEstado(99, EstadoEnvio.PENDIENTE);

    assertThat(resultado).isNull();
}

@Test
void testActualizarEstadoConFechaNoExiste() {
    when(envioRepository.findById(99)).thenReturn(Optional.empty());

    Envio resultado = envioService.actualizarEstadoConFecha(99, EstadoEnvio.ENTREGADO, LocalDate.now());

    assertThat(resultado).isNull();
}

@Test
void testBuscarPorIdNoExiste() {
    when(envioRepository.findById(99)).thenReturn(Optional.empty());

    Envio resultado = envioService.buscarPorId(99);

    assertThat(resultado).isNull();
}

@Test
void testEliminarNoExiste() {
    when(envioRepository.findById(99)).thenReturn(Optional.empty());

    boolean resultado = envioService.eliminar(99);

    assertThat(resultado).isFalse();
}

@Test
void testModificarEnvioNoExiste() {
    RutaEntrega ruta = new RutaEntrega(1, "Origen", 10f, null);
    OpcionEnvio opcion = new OpcionEnvio(1, TipoEnvio.EXPRESS, "Rápido", 1000f, 2);
    Envio envioModificado = new Envio(1, 10, 5, "Destino", EstadoEnvio.PENDIENTE, TipoDestinatario.CLIENTE,
            null, null, null, ruta, opcion);

    when(envioRepository.findById(1)).thenReturn(Optional.empty());

    Envio resultado = envioService.modificar(envioModificado);

    assertThat(resultado).isNull();
}

@Test
void testModificarRutaNoExiste() {
    RutaEntrega ruta = new RutaEntrega(1, "Origen", 10f, null);
    OpcionEnvio opcion = new OpcionEnvio(1, TipoEnvio.EXPRESS, "Rápido", 1000f, 2);
    Envio envioModificado = new Envio(1, 10, 5, "Destino", EstadoEnvio.PENDIENTE, TipoDestinatario.CLIENTE,
            null, null, null, ruta, opcion);

    when(envioRepository.findById(1)).thenReturn(Optional.of(envioModificado));
    when(rutaEntregaRepository.findById(1)).thenReturn(Optional.empty());

    Envio resultado = envioService.modificar(envioModificado);

    assertThat(resultado).isNull();
}

@Test
void testModificarOpcionNoExiste() {
    RutaEntrega ruta = new RutaEntrega(1, "Origen", 10f, null);
    OpcionEnvio opcion = new OpcionEnvio(1, TipoEnvio.EXPRESS, "Rápido", 1000f, 2);
    Envio envioModificado = new Envio(1, 10, 5, "Destino", EstadoEnvio.PENDIENTE, TipoDestinatario.CLIENTE,
            null, null, null, ruta, opcion);

    when(envioRepository.findById(1)).thenReturn(Optional.of(envioModificado));
    when(rutaEntregaRepository.findById(1)).thenReturn(Optional.of(ruta));
    when(opcionEnvioRepository.findById(1)).thenReturn(Optional.empty());

    Envio resultado = envioService.modificar(envioModificado);

    assertThat(resultado).isNull();
}
}