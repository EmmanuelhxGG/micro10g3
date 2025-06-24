package com.micro10.micro10g3.controller;

import com.micro10.micro10g3.model.Envio;
import com.micro10.micro10g3.model.EstadoEnvio;
import com.micro10.micro10g3.service.EnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/envios")
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @GetMapping
    public ResponseEntity<List<Envio>> getEnvios() {
        List<Envio> envios = envioService.listarTodos();
        if (!envios.isEmpty()) {
            return new ResponseEntity<>(envios, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/crear")
    public ResponseEntity<Envio> postEnvio(@RequestBody Envio envio) {
        Envio nuevo = envioService.guardar(envio);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Envio> getEnvioById(@PathVariable int id) {
        Envio envio = envioService.buscarPorId(id);
        if (envio != null) {
            return new ResponseEntity<>(envio, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Envio> putEstadoEnvio(
            @PathVariable int id,
            @RequestParam EstadoEnvio estado,
            @RequestParam(required = false) String fechaEntregaReal
    ) {
        LocalDate fechaEntrega = (fechaEntregaReal != null) ? LocalDate.parse(fechaEntregaReal) : null;
        Envio actualizado = envioService.actualizarEstadoConFecha(id, estado, fechaEntrega);
        if (actualizado != null) {
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/modificar")
    public ResponseEntity<Envio> putModificarEnvio(@RequestBody Envio envio) {
        Envio modificado = envioService.modificar(envio);
        if (modificado != null) {
            return new ResponseEntity<>(modificado, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnvio(@PathVariable int id) {
        boolean eliminado = envioService.eliminar(id);
        if (eliminado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
