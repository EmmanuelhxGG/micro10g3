package com.micro10.micro10g3.controller;

import com.micro10.micro10g3.model.OpcionEnvio;
import com.micro10.micro10g3.service.OpcionEnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/opciones")
public class OpcionEnvioController {

    @Autowired
    private OpcionEnvioService opcionEnvioService;

    @GetMapping
    public ResponseEntity<List<OpcionEnvio>> listar() {
        List<OpcionEnvio> lista = opcionEnvioService.listarTodas();
        if (!lista.isEmpty()) {
            return new ResponseEntity<>(lista, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/envio/crear")
    public ResponseEntity<OpcionEnvio> crear(@RequestBody OpcionEnvio opcion) {
        return new ResponseEntity<>(opcionEnvioService.crear(opcion), HttpStatus.CREATED);
    }
}
