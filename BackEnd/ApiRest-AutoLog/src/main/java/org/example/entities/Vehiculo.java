package org.example.entities;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehiculo")
@Data
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uid_usuario", nullable = false)
    private String uidUsuario; // UID de Firebase

    @Column(nullable = false, length = 50)
    private String marca;

    @Column(nullable = false, length = 50)
    private String modelo;

    @Column(nullable = false)
    private Integer año;

    @Column(length = 20)
    private String matricula;

    @Column(length = 30)
    private String color;

    @Column(name = "kilometraje")
    private Integer kilometraje;

    @Column(length = 50)
    private String combustible; // Gasolina, Diesel, Eléctrico, Híbrido

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Column(name = "url_imagen")
    private String urlImagen;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
