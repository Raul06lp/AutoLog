package org.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MecanicoDTO {
    private Long idMecanico;
    private String nombre;
    private String email;
    private Integer cantidadVehiculos;
}
