package com.micro10.micro10g3.repository;

import com.micro10.micro10g3.model.OpcionEnvio;
import com.micro10.micro10g3.model.TipoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpcionEnvioRepository extends JpaRepository<OpcionEnvio, Integer> {
    OpcionEnvio findByTipoEnvio(TipoEnvio tipoEnvio);
}
