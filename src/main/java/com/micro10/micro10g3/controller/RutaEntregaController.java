package com.micro10.micro10g3.controller;

import com.micro10.micro10g3.model.RutaEntrega;
import com.micro10.micro10g3.service.RutaEntregaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rutas")
public class RutaEntregaController {

    @Autowired
    private RutaEntregaService rutaEntregaService;

    @PostMapping("/crear")
    public ResponseEntity<RutaEntrega> planificar(@RequestBody RutaEntrega ruta) {
        RutaEntrega rutaCreada = rutaEntregaService.planificarRuta(ruta);
        if (rutaCreada != null) {
            return new ResponseEntity<>(rutaCreada, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/{id}/optimizar")
    public ResponseEntity<RutaEntrega> optimizar(@PathVariable int id, @RequestBody RutaEntrega datosRuta) {
        RutaEntrega rutaOpt = rutaEntregaService.optimizarRuta(id, datosRuta.getDistanciaKm());
        if (rutaOpt != null) {
            return new ResponseEntity<>(rutaOpt, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/modificar")
    public ResponseEntity<RutaEntrega> modificar(@RequestBody RutaEntrega ruta) {
        RutaEntrega modificada = rutaEntregaService.modificarRuta(ruta);
        if (modificada != null) {
            return new ResponseEntity<>(modificada, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        boolean eliminada = rutaEntregaService.eliminarRuta(id);
        if (eliminada) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
