package com.micro10.micro10g3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro10.micro10g3.model.OpcionEnvio;
import com.micro10.micro10g3.model.TipoEnvio;
import com.micro10.micro10g3.service.OpcionEnvioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OpcionEnvioController.class)
class OpcionEnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OpcionEnvioService opcionEnvioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListar_conDatos() throws Exception {
        OpcionEnvio opcion1 = new OpcionEnvio(1, TipoEnvio.ESTANDAR, "Entrega normal", 3000.0f, 5);
        OpcionEnvio opcion2 = new OpcionEnvio(2, TipoEnvio.EXPRESS, "Entrega rápida", 5000.0f, 2);

        Mockito.when(opcionEnvioService.listarTodas()).thenReturn(List.of(opcion1, opcion2));

        mockMvc.perform(get("/opciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descripcion").value("Entrega normal"))
                .andExpect(jsonPath("$[1].tipoEnvio").value("EXPRESS"));
    }

    @Test
    void testListar_sinDatos() throws Exception {
        Mockito.when(opcionEnvioService.listarTodas()).thenReturn(List.of());

        mockMvc.perform(get("/opciones"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCrear_ok() throws Exception {
        OpcionEnvio entrada = new OpcionEnvio(0, TipoEnvio.PICKUP, "Retiro en tienda", 0.0f, 0);
        OpcionEnvio guardado = new OpcionEnvio(10, TipoEnvio.PICKUP, "Retiro en tienda", 0.0f, 0);

        Mockito.when(opcionEnvioService.crear(any(OpcionEnvio.class))).thenReturn(guardado);

        mockMvc.perform(post("/opciones/envio/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.tipoEnvio").value("PICKUP"))
                .andExpect(jsonPath("$.descripcion").value("Retiro en tienda"));
    }
}
