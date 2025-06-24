package com.micro10.micro10g3.repository;

import com.micro10.micro10g3.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvioRepository extends JpaRepository<Envio, Integer> {
    Envio findByIdPedido(int idPedido);
}
