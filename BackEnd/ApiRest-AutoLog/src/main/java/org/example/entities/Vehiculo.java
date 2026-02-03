package org.example.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehiculos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo")
    private Long idVehiculo;

    @Column(nullable = false, unique = true)
    private String matricula;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private Integer año;

    @Column
    private String color;

    @Column
    private Integer kilometraje;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "medidas_tomadas", columnDefinition = "TEXT")
    private String medidasTomadas;

    @Column(name = "estado_revision")
    private String estadoRevision;

    // IMAGEN COMO BLOB
    @Lob
    @Column(name = "imagen", columnDefinition = "LONGBLOB")
    private byte[] imagen;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_mecanico")
    private Mecanico mecanico;

    @Column(name = "fecha_ingreso")
    private LocalDateTime fechaIngreso;

    @PrePersist
    protected void onCreate() {
        fechaIngreso = LocalDateTime.now();
        if (estadoRevision == null) {
            estadoRevision = "pendiente";
        }
    }

}
