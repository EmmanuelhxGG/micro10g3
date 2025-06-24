package com.micro10.micro10g3.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RutaEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRuta;

    private String origen;
    private float distanciaKm;

    @OneToOne(mappedBy = "ruta")
    @JsonBackReference
    private Envio envio;
}
