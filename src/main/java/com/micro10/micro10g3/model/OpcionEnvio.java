package com.micro10.micro10g3.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpcionEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private TipoEnvio tipoEnvio;

    private String descripcion;
    private float costo;
    private int tiempoEstimadoDias;
}
