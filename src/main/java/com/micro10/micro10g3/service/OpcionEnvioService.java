package com.micro10.micro10g3.service;

import com.micro10.micro10g3.model.OpcionEnvio;
import com.micro10.micro10g3.repository.OpcionEnvioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpcionEnvioService {

    @Autowired
    private OpcionEnvioRepository opcionEnvioRepository;

    public List<OpcionEnvio> listarTodas() {
        return opcionEnvioRepository.findAll();
    }
    public OpcionEnvio crear(OpcionEnvio opcion) {
        return opcionEnvioRepository.save(opcion);
    }
}
