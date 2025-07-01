package com.micro10.micro10g3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro10.micro10g3.model.*;
import com.micro10.micro10g3.service.EnvioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnvioController.class)
class EnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnvioService envioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetEnvios_conDatos() throws Exception {
        Envio envio1 = new Envio(1, 100, 200, "Santiago", EstadoEnvio.PENDIENTE, TipoDestinatario.CLIENTE,
                LocalDate.now(), LocalDate.now().plusDays(3), null, null, null);

        Mockito.when(envioService.listarTodos()).thenReturn(List.of(envio1));

        mockMvc.perform(get("/envios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEnvio").value(1));
    }

    @Test
    void testGetEnvios_sinDatos() throws Exception {
        Mockito.when(envioService.listarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/envios"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testPostEnvio_crearOk() throws Exception {
        Envio envioEntrada = new Envio(0, 101, 201, "Viña", EstadoEnvio.PENDIENTE, TipoDestinatario.CLIENTE,
                null, null, null,
                new RutaEntrega(1, "Santiago", 100, null),
                new OpcionEnvio(1, TipoEnvio.ESTANDAR, "Normal", 5.0f, 3));

        Envio envioGuardado = new Envio(10, 101, 201, "Viña", EstadoEnvio.PENDIENTE, TipoDestinatario.CLIENTE,
                LocalDate.now(), LocalDate.now().plusDays(3), null,
                envioEntrada.getRuta(), envioEntrada.getEnvio());

        Mockito.when(envioService.guardar(any(Envio.class))).thenReturn(envioGuardado);

        mockMvc.perform(post("/envios/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(envioEntrada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idEnvio").value(10));
    }

    @Test
    void testGetEnvioById_ok() throws Exception {
        Envio envio = new Envio(2, 102, 202, "La Serena", EstadoEnvio.PENDIENTE, TipoDestinatario.TIENDA,
                LocalDate.now(), null, null, null, null);

        Mockito.when(envioService.buscarPorId(2)).thenReturn(envio);

        mockMvc.perform(get("/envios/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.destino").value("La Serena"));
    }

    @Test
    void testGetEnvioById_notFound() throws Exception {
        Mockito.when(envioService.buscarPorId(99)).thenReturn(null);

        mockMvc.perform(get("/envios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPutEstadoEnvio_ok() throws Exception {
        Envio envioActualizado = new Envio(3, 103, 203, "Valpo", EstadoEnvio.ENTREGADO, TipoDestinatario.CLIENTE,
                LocalDate.now(), null, LocalDate.now(),
                null, null);

        Mockito.when(envioService.actualizarEstadoConFecha(eq(3), eq(EstadoEnvio.ENTREGADO), eq(LocalDate.parse("2024-01-01"))))
                .thenReturn(envioActualizado);

        mockMvc.perform(put("/envios/3/estado")
                        .param("estado", "ENTREGADO")
                        .param("fechaEntregaReal", "2024-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoEnvio").value("ENTREGADO"));
    }

    @Test
    void testPutEstadoEnvio_notFound() throws Exception {
        Mockito.when(envioService.actualizarEstadoConFecha(eq(4), eq(EstadoEnvio.RETRASADO), eq(null)))
                .thenReturn(null);

        mockMvc.perform(put("/envios/4/estado")
                        .param("estado", "RETRASADO"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPutModificarEnvio_ok() throws Exception {
        Envio modificado = new Envio(5, 105, 205, "Rancagua", EstadoEnvio.EN_TRANSITO, TipoDestinatario.TIENDA,
                LocalDate.now(), LocalDate.now().plusDays(4), null,
                null, null);

        Mockito.when(envioService.modificar(any(Envio.class))).thenReturn(modificado);

        mockMvc.perform(put("/envios/modificar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modificado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.destino").value("Rancagua"));
    }

    @Test
    void testPutModificarEnvio_notFound() throws Exception {
        Envio modificado = new Envio(6, 106, 206, "Talca", EstadoEnvio.PENDIENTE, TipoDestinatario.CLIENTE,
                null, null, null,
                null, null);

        Mockito.when(envioService.modificar(any(Envio.class))).thenReturn(null);

        mockMvc.perform(put("/envios/modificar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modificado)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteEnvio_ok() throws Exception {
        Mockito.when(envioService.eliminar(7)).thenReturn(true);

        mockMvc.perform(delete("/envios/7"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteEnvio_notFound() throws Exception {
        Mockito.when(envioService.eliminar(8)).thenReturn(false);

        mockMvc.perform(delete("/envios/8"))
                .andExpect(status().isNotFound());
    }
}
