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
import static org.mockito.ArgumentMatchers.any;
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
    void testGuardar() {
        RutaEntrega ruta = new RutaEntrega(1, "Origen", 10f, null);
        OpcionEnvio opcion = new OpcionEnvio(1, TipoEnvio.EXPRESS, "Rápido", 1000f, 2);
        Envio envio = new Envio(0, 10, 5, "Destino", null, TipoDestinatario.CLIENTE, null, null, null, ruta, opcion);
        Envio envioGuardado = new Envio(1, 10, 5, "Destino", EstadoEnvio.PENDIENTE, TipoDestinatario.CLIENTE,
                                        LocalDate.now(), null, null, ruta, opcion);

        when(rutaEntregaRepository.findById(1)).thenReturn(Optional.of(ruta));
        when(opcionEnvioRepository.findById(1)).thenReturn(Optional.of(opcion));
        when(envioRepository.save(any(Envio.class))).thenReturn(envioGuardado);

        Envio resultado = envioService.guardar(envio);

        assertThat(resultado.getIdEnvio()).isEqualTo(1);
        assertThat(resultado.getEstadoEnvio()).isEqualTo(EstadoEnvio.PENDIENTE);
        assertThat(resultado.getFechaSalida()).isEqualTo(LocalDate.now());
    }

    @Test
    void testActualizarEstado() {
        Envio envio = new Envio();
        envio.setIdEnvio(1);
        envio.setEstadoEnvio(EstadoEnvio.PENDIENTE);

        when(envioRepository.findById(1)).thenReturn(Optional.of(envio));
        when(envioRepository.save(envio)).thenReturn(envio);

        Envio resultado = envioService.actualizarEstado(1, EstadoEnvio.PENDIENTE);

        assertThat(resultado.getEstadoEnvio()).isEqualTo(EstadoEnvio.PENDIENTE);
    }

    @Test
    void testListarTodos() {
        Envio envio1 = new Envio();
        Envio envio2 = new Envio();

        when(envioRepository.findAll()).thenReturn(List.of(envio1, envio2));

        List<Envio> resultado = envioService.listarTodos();

        assertThat(resultado.size()).isEqualTo(2);
    }

    @Test
    void testBuscarPorId() {
        Envio envio = new Envio();
        envio.setIdEnvio(1);

        when(envioRepository.findById(1)).thenReturn(Optional.of(envio));

        Envio resultado = envioService.buscarPorId(1);

        assertThat(resultado.getIdEnvio()).isEqualTo(1);
    }

    @Test
    void testActualizarEstadoConFecha() {
        Envio envio = new Envio();
        envio.setIdEnvio(1);
        envio.setEstadoEnvio(EstadoEnvio.PENDIENTE);

        LocalDate fecha = LocalDate.now();

        when(envioRepository.findById(1)).thenReturn(Optional.of(envio));
        when(envioRepository.save(envio)).thenReturn(envio);

        Envio resultado = envioService.actualizarEstadoConFecha(1, EstadoEnvio.ENTREGADO, fecha);

        assertThat(resultado.getEstadoEnvio()).isEqualTo(EstadoEnvio.ENTREGADO);
        assertThat(resultado.getFechaEntregaReal()).isEqualTo(fecha);
    }

    @Test
    void testEliminar() {
        Envio envio = new Envio();
        envio.setIdEnvio(1);

        when(envioRepository.findById(1)).thenReturn(Optional.of(envio));
        doNothing().when(envioRepository).deleteById(1);

        boolean resultado = envioService.eliminar(1);

        assertThat(resultado).isTrue();
    }

    @Test
    void testModificar() {
        RutaEntrega ruta = new RutaEntrega(1, "Origen", 10f, null);
        OpcionEnvio opcion = new OpcionEnvio(1, TipoEnvio.EXPRESS, "Rápido", 1000f, 2);
        Envio envioModificado = new Envio(1, 10, 5, "Destino", EstadoEnvio.PENDIENTE, TipoDestinatario.CLIENTE,
                null, null, null, ruta, opcion);

        when(envioRepository.findById(1)).thenReturn(Optional.of(envioModificado));
        when(rutaEntregaRepository.findById(1)).thenReturn(Optional.of(ruta));
        when(opcionEnvioRepository.findById(1)).thenReturn(Optional.of(opcion));
        when(envioRepository.save(envioModificado)).thenReturn(envioModificado);

        Envio resultado = envioService.modificar(envioModificado);

        assertThat(resultado.getIdEnvio()).isEqualTo(1);
    }
}
