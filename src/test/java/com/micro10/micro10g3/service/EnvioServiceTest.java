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
import java.util.List;
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
    void testGuardar_existe() {
        RutaEntrega ruta = new RutaEntrega(1, "Origen", 10f, null);
        OpcionEnvio opcion = new OpcionEnvio(1, TipoEnvio.EXPRESS, "Rapido", 1000f, 2);
        Envio envio = new Envio(0, 10, 5, "Destino", null, TipoDestinatario.CLIENTE, null, null, null, ruta, opcion);

        when(rutaEntregaRepository.findById(1)).thenReturn(Optional.of(ruta));
        when(opcionEnvioRepository.findById(1)).thenReturn(Optional.of(opcion));
        when(envioRepository.save(any(Envio.class))).thenReturn(envio);

        Envio resultado = envioService.guardar(envio);

        assertThat(resultado).isNotNull();
        verify(envioRepository).save(envio);
    }

    @Test
    void testGuardar_noExisteRuta() {
        OpcionEnvio opcion = new OpcionEnvio(1, TipoEnvio.EXPRESS, "Rapido", 1000f, 2);
        Envio envio = new Envio(0, 10, 5, "Destino", null, TipoDestinatario.CLIENTE, null, null, null, new RutaEntrega(1, "Origen", 10f, null), opcion);

        when(rutaEntregaRepository.findById(1)).thenReturn(Optional.empty());
        when(opcionEnvioRepository.findById(1)).thenReturn(Optional.of(opcion));

        Envio resultado = envioService.guardar(envio);

        assertThat(resultado).isNull();
    }

    @Test
    void testGuardar_noExisteOpcion() {
        RutaEntrega ruta = new RutaEntrega(1, "Origen", 10f, null);
        Envio envio = new Envio(0, 10, 5, "Destino", null, TipoDestinatario.CLIENTE, null, null, null, ruta,
                new OpcionEnvio(1, TipoEnvio.EXPRESS, "Rapido", 1000f, 2));

        when(rutaEntregaRepository.findById(1)).thenReturn(Optional.of(ruta));
        when(opcionEnvioRepository.findById(1)).thenReturn(Optional.empty());

        Envio resultado = envioService.guardar(envio);

        assertThat(resultado).isNull();
    }

    @Test
    void testActualizarEstado_existe() {
        Envio envio = new Envio(1, 10, 5, "Destino", EstadoEnvio.PENDIENTE, TipoDestinatario.CLIENTE,
                null, null, null, null, null);

        when(envioRepository.findById(1)).thenReturn(Optional.of(envio));
        when(envioRepository.save(any(Envio.class))).thenReturn(envio);

        Envio resultado = envioService.actualizarEstado(1, EstadoEnvio.ENTREGADO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getEstadoEnvio()).isEqualTo(EstadoEnvio.ENTREGADO);
    }

    @Test
    void testActualizarEstado_noExiste() {
        when(envioRepository.findById(99)).thenReturn(Optional.empty());

        Envio resultado = envioService.actualizarEstado(99, EstadoEnvio.ENTREGADO);

        assertThat(resultado).isNull();
    }

    @Test
    void testListarTodos() {
        when(envioRepository.findAll()).thenReturn(List.of());
        List<Envio> resultado = envioService.listarTodos();
        assertThat(resultado).isEmpty();
    }

    // buscar por id
    @Test
    void testBuscarPorId_existe() {
        Envio envio = new Envio(1, 10, 5, "Destino", EstadoEnvio.PENDIENTE, TipoDestinatario.CLIENTE,
                null, null, null, null, null);

        when(envioRepository.findById(1)).thenReturn(Optional.of(envio));

        Envio resultado = envioService.buscarPorId(1);

        assertThat(resultado).isNotNull();
    }

    @Test
    void testBuscarPorId_noExiste() {
        when(envioRepository.findById(99)).thenReturn(Optional.empty());

        Envio resultado = envioService.buscarPorId(99);

        assertThat(resultado).isNull();
    }

    @Test
    void testActualizarEstadoConFecha_existe() {
        Envio envio = new Envio(1, 10, 5, "Destino", EstadoEnvio.PENDIENTE, TipoDestinatario.CLIENTE,
                null, null, null, null, null);

        when(envioRepository.findById(1)).thenReturn(Optional.of(envio));
        when(envioRepository.save(any(Envio.class))).thenReturn(envio);

        LocalDate fecha = LocalDate.now();
        Envio resultado = envioService.actualizarEstadoConFecha(1, EstadoEnvio.ENTREGADO, fecha);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getFechaEntregaReal()).isEqualTo(fecha);
    }

    @Test
    void testActualizarEstadoConFecha_noExiste() {
        when(envioRepository.findById(99)).thenReturn(Optional.empty());

        Envio resultado = envioService.actualizarEstadoConFecha(99, EstadoEnvio.ENTREGADO, LocalDate.now());

        assertThat(resultado).isNull();
    }

    // eliminar por id
    @Test
    void testEliminar_existe() {
        Envio envio = new Envio(1, 10, 5, "Destino", EstadoEnvio.PENDIENTE, TipoDestinatario.CLIENTE,
                null, null, null, null, null);

        when(envioRepository.findById(1)).thenReturn(Optional.of(envio));
        doNothing().when(envioRepository).deleteById(1);

        boolean resultado = envioService.eliminar(1);

        assertThat(resultado).isTrue();
    }

    @Test
    void testEliminar_noExiste() {
        when(envioRepository.findById(99)).thenReturn(Optional.empty());

        boolean resultado = envioService.eliminar(99);

        assertThat(resultado).isFalse();
    }

    @Test
    void testModificar_existe() {
        RutaEntrega ruta = new RutaEntrega(1, "Origen", 10f, null);
        OpcionEnvio opcion = new OpcionEnvio(1, TipoEnvio.EXPRESS, "Rapido", 1000f, 2);
        Envio envioModificado = new Envio(1, 10, 5, "Destino", EstadoEnvio.PENDIENTE,
                TipoDestinatario.CLIENTE, null, null, null, ruta, opcion);

        when(envioRepository.findById(1)).thenReturn(Optional.of(envioModificado));
        when(rutaEntregaRepository.findById(1)).thenReturn(Optional.of(ruta));
        when(opcionEnvioRepository.findById(1)).thenReturn(Optional.of(opcion));
        when(envioRepository.save(any(Envio.class))).thenReturn(envioModificado);

        Envio resultado = envioService.modificar(envioModificado);

        assertThat(resultado).isNotNull();
    }

    @Test
    void testModificar_noExisteEnvio() {
        RutaEntrega ruta = new RutaEntrega(1, "Origen", 10f, null);
        OpcionEnvio opcion = new OpcionEnvio(1, TipoEnvio.EXPRESS, "Rapido", 1000f, 2);
        Envio envioModificado = new Envio(1, 10, 5, "Destino", EstadoEnvio.PENDIENTE,
                TipoDestinatario.CLIENTE, null, null, null, ruta, opcion);

        when(envioRepository.findById(1)).thenReturn(Optional.empty());

        Envio resultado = envioService.modificar(envioModificado);

        assertThat(resultado).isNull();
    }

    @Test
    void testModificar_noExisteRuta() {
        RutaEntrega ruta = new RutaEntrega(1, "Origen", 10f, null);
        OpcionEnvio opcion = new OpcionEnvio(1, TipoEnvio.EXPRESS, "Rapido", 1000f, 2);
        Envio envioModificado = new Envio(1, 10, 5, "Destino", EstadoEnvio.PENDIENTE,
                TipoDestinatario.CLIENTE, null, null, null, ruta, opcion);

        when(envioRepository.findById(1)).thenReturn(Optional.of(envioModificado));
        when(rutaEntregaRepository.findById(1)).thenReturn(Optional.empty());

        Envio resultado = envioService.modificar(envioModificado);

        assertThat(resultado).isNull();
    }

    @Test
    void testModificar_noExisteOpcion() {
        RutaEntrega ruta = new RutaEntrega(1, "Origen", 10f, null);
        OpcionEnvio opcion = new OpcionEnvio(1, TipoEnvio.EXPRESS, "Rapido", 1000f, 2);
        Envio envioModificado = new Envio(1, 10, 5, "Destino", EstadoEnvio.PENDIENTE,
                TipoDestinatario.CLIENTE, null, null, null, ruta, opcion);

        when(envioRepository.findById(1)).thenReturn(Optional.of(envioModificado));
        when(rutaEntregaRepository.findById(1)).thenReturn(Optional.of(ruta));
        when(opcionEnvioRepository.findById(1)).thenReturn(Optional.empty());

        Envio resultado = envioService.modificar(envioModificado);

        assertThat(resultado).isNull();
    }
}
