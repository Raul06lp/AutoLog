package org.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehiculoDTO {
    private Long idVehiculo;
    private String matricula;
    private String marca;
    private String modelo;
    private Integer año;
    private String color;
    private Integer kilometraje;
    private String observaciones;
    private String medidasTomadas;
    private String estadoRevision;

    // Imagen en Base64 para enviar/recibir
    private String imagenBase64;
    
    private Long idCliente;
    private String nombreCliente;
    private String emailCliente;
    private Long idMecanico;
    private String nombreMecanico;
    private String emailMecanico;
    private LocalDateTime fechaIngreso;
}
