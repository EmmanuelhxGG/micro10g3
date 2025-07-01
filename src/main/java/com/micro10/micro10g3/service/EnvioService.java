package com.micro10.micro10g3.service;

import com.micro10.micro10g3.model.Envio;
import com.micro10.micro10g3.model.EstadoEnvio;
import com.micro10.micro10g3.model.RutaEntrega;
import com.micro10.micro10g3.model.OpcionEnvio;
import com.micro10.micro10g3.repository.EnvioRepository;
import com.micro10.micro10g3.repository.RutaEntregaRepository;
import com.micro10.micro10g3.repository.OpcionEnvioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EnvioService {

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private RutaEntregaRepository rutaEntregaRepository;

    @Autowired
    private OpcionEnvioRepository opcionEnvioRepository;

    // Crear nuevo envío
    public Envio guardar(Envio envio) {
        Optional<RutaEntrega> rutaOpt = rutaEntregaRepository.findById(envio.getRuta().getIdRuta());
        Optional<OpcionEnvio> opcionOpt = opcionEnvioRepository.findById(envio.getEnvio().getId());

        if (rutaOpt.isEmpty() || opcionOpt.isEmpty()) {
            return null;
        }

        envio.setEstadoEnvio(EstadoEnvio.PENDIENTE);
        envio.setFechaSalida(LocalDate.now());
        envio.setRuta(rutaOpt.get());
        envio.setEnvio(opcionOpt.get());

        return envioRepository.save(envio);
    }

    // Actualizar estado del envío
    public Envio actualizarEstado(int idEnvio, EstadoEnvio nuevoEstado) {
        Optional<Envio> envioOpt = envioRepository.findById(idEnvio);
        if (envioOpt.isEmpty()) {
            return null;
        }
        Envio envio = envioOpt.get();
        envio.setEstadoEnvio(nuevoEstado);
        return envioRepository.save(envio);
    }

    // Listar todos los envíos
    public List<Envio> listarTodos() {
        return envioRepository.findAll();
    }

    // Buscar envío por ID de envío
    public Envio buscarPorId(int idEnvio) {
        Optional<Envio> envioOpt = envioRepository.findById(idEnvio);
        return envioOpt.orElse(null);
    }

    // Actualizar estado y fecha real de entrega
    public Envio actualizarEstadoConFecha(int id, EstadoEnvio estado, LocalDate fechaEntregaReal) {
        Optional<Envio> envioOpt = envioRepository.findById(id);
        if (envioOpt.isEmpty()) {
            return null;
        }

        Envio envio = envioOpt.get();
        envio.setEstadoEnvio(estado);
        envio.setFechaEntregaReal(fechaEntregaReal);
        return envioRepository.save(envio);
    }

    // Eliminar envío por ID
    public boolean eliminar(int idEnvio) {
        Optional<Envio> envioOpt = envioRepository.findById(idEnvio);
        if (envioOpt.isEmpty()) {
            return false;
        }
        envioRepository.deleteById(idEnvio);
        return true;
    }

    // Modificar un envío completo si existe
    public Envio modificar(Envio envioModificado) {
        Optional<Envio> envioOpt = envioRepository.findById(envioModificado.getIdEnvio());
        if (envioOpt.isEmpty()) {
            return null;
        }

        Optional<RutaEntrega> rutaOpt = rutaEntregaRepository.findById(envioModificado.getRuta().getIdRuta());
        Optional<OpcionEnvio> opcionOpt = opcionEnvioRepository.findById(envioModificado.getEnvio().getId());

        if (rutaOpt.isEmpty() || opcionOpt.isEmpty()) {
            return null;
        }

        envioModificado.setRuta(rutaOpt.get());
        envioModificado.setEnvio(opcionOpt.get());

        return envioRepository.save(envioModificado);
    }
}
