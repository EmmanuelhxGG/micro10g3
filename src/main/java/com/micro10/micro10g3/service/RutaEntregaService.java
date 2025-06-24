package com.micro10.micro10g3.service;

import com.micro10.micro10g3.model.RutaEntrega;
import com.micro10.micro10g3.repository.RutaEntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class RutaEntregaService {

    @Autowired
    private RutaEntregaRepository rutaEntregaRepository;

    public RutaEntrega planificarRuta(RutaEntrega ruta) {
        return rutaEntregaRepository.save(ruta);
    }

    // Actualiza solo la distancia
    public RutaEntrega optimizarRuta(int idRuta, float nuevaDistanciaKm) {
        Optional<RutaEntrega> rutaOpt = rutaEntregaRepository.findById(idRuta);
        if (rutaOpt.isEmpty()) {
            return null;
        }

        RutaEntrega ruta = rutaOpt.get();
        ruta.setDistanciaKm(nuevaDistanciaKm);

        return rutaEntregaRepository.save(ruta);
    }

    // Modifica toda la ruta si existe
    public RutaEntrega modificarRuta(RutaEntrega rutaModificada) {
        Optional<RutaEntrega> rutaOpt = rutaEntregaRepository.findById(rutaModificada.getIdRuta());
        if (rutaOpt.isEmpty()) {
            return null;
        }
        return rutaEntregaRepository.save(rutaModificada);
    }

    // Elimina la ruta por ID
    public boolean eliminarRuta(int idRuta) {
        Optional<RutaEntrega> rutaOpt = rutaEntregaRepository.findById(idRuta);
        if (rutaOpt.isEmpty()) {
            return false;
        }
        rutaEntregaRepository.deleteById(idRuta);
        return true;
    }
}
