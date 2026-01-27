package org.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CocheRequestDTO {

    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 50, message = "La marca no puede exceder 50 caracteres")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    @Size(max = 50, message = "El modelo no puede exceder 50 caracteres")
    private String modelo;

    @NotNull(message = "El año es obligatorio")
    @Min(value = 1900, message = "El año debe ser mayor a 1900")
    @Max(value = 2027, message = "El año debe ser menor a 2027")
    private Integer año;

    @Size(max = 20, message = "La matrícula no puede exceder 20 caracteres")
    private String matricula;

    @Size(max = 30, message = "El color no puede exceder 30 caracteres")
    private String color;

    @Min(value = 0, message = "El kilometraje no puede ser negativo")
    private Integer kilometraje;

    private String notas;

    @Size(max = 500, message = "La URL de la imagen no puede exceder 500 caracteres")
    private String urlImagen;
}