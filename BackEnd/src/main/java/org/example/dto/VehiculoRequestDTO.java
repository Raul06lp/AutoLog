package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoRequestDTO {
    
    @NotBlank(message = "La matrícula es obligatoria")
    @Size(max = 20, message = "La matrícula no puede exceder 20 caracteres")
    private String matricula;

    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 50, message = "La marca no puede exceder 50 caracteres")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    @Size(max = 50, message = "El modelo no puede exceder 50 caracteres")
    private String modelo;

    @NotNull(message = "El año es obligatorio")
    @Min(value = 1900, message = "El año debe ser mayor a 1900")
    @Max(value = 2027, message = "El año debe ser menor o igual a 2027")
    private Integer año;

    private String color;

    @Min(value = 0, message = "El kilometraje no puede ser negativo")
    private Integer kilometraje;

    private String observaciones;
    private String medidasTomadas;
    private String estadoRevision;
    
    // Imagen en Base64
    private String imagenBase64;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long idCliente;

    private Long idMecanico;
}
