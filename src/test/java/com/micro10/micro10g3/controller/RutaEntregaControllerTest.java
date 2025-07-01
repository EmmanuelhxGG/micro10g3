package com.micro10.micro10g3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro10.micro10g3.model.RutaEntrega;
import com.micro10.micro10g3.service.RutaEntregaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RutaEntregaController.class)
class RutaEntregaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RutaEntregaService rutaEntregaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testPlanificar_crearOk() throws Exception {
        RutaEntrega entrada = new RutaEntrega(0, "Santiago", 120f, null);
        RutaEntrega guardada = new RutaEntrega(5, "Santiago", 120f, null);

        Mockito.when(rutaEntregaService.planificarRuta(any(RutaEntrega.class))).thenReturn(guardada);

        mockMvc.perform(post("/rutas/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idRuta").value(5))
                .andExpect(jsonPath("$.origen").value("Santiago"));
    }

    @Test
    void testPlanificar_error() throws Exception {
        RutaEntrega entrada = new RutaEntrega(0, "Valparaíso", 100f, null);

        Mockito.when(rutaEntregaService.planificarRuta(any(RutaEntrega.class))).thenReturn(null);

        mockMvc.perform(post("/rutas/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isConflict());
    }

    @Test
    void testOptimizar_ok() throws Exception {
        RutaEntrega optimizada = new RutaEntrega(2, "Arica", 50f, null);

        Mockito.when(rutaEntregaService.optimizarRuta(eq(2), eq(50f))).thenReturn(optimizada);

        mockMvc.perform(put("/rutas/2/optimizar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(optimizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.distanciaKm").value(50f));
    }

    @Test
    void testOptimizar_notFound() throws Exception {
        RutaEntrega datos = new RutaEntrega(2, "Arica", 50f, null);

        Mockito.when(rutaEntregaService.optimizarRuta(eq(2), eq(50f))).thenReturn(null);

        mockMvc.perform(put("/rutas/2/optimizar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datos)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testModificar_ok() throws Exception {
        RutaEntrega modificada = new RutaEntrega(3, "Copiapó", 200f, null);

        Mockito.when(rutaEntregaService.modificarRuta(any(RutaEntrega.class))).thenReturn(modificada);

        mockMvc.perform(put("/rutas/modificar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modificada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.origen").value("Copiapó"));
    }

    @Test
    void testModificar_notFound() throws Exception {
        RutaEntrega modificada = new RutaEntrega(3, "Copiapó", 200f, null);

        Mockito.when(rutaEntregaService.modificarRuta(any(RutaEntrega.class))).thenReturn(null);

        mockMvc.perform(put("/rutas/modificar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modificada)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminar_ok() throws Exception {
        Mockito.when(rutaEntregaService.eliminarRuta(4)).thenReturn(true);

        mockMvc.perform(delete("/rutas/4"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminar_notFound() throws Exception {
        Mockito.when(rutaEntregaService.eliminarRuta(99)).thenReturn(false);

        mockMvc.perform(delete("/rutas/99"))
                .andExpect(status().isNotFound());
    }
}
