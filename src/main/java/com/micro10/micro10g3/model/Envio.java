package com.micro10.micro10g3.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEnvio;

    private int idPedido;

    private int destinatarioId;

    @Column(nullable = false)
    private String destino;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnvio estadoEnvio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDestinatario tipoDestinatario;

    private LocalDate fechaSalida;
    private LocalDate entregaEstimada;
    private LocalDate fechaEntregaReal;

    @OneToOne
    @JoinColumn(name = "idRuta")
    private RutaEntrega ruta;

    @ManyToOne
    @JoinColumn(name = "opcionEnvio_id")
    private OpcionEnvio envio;
}

