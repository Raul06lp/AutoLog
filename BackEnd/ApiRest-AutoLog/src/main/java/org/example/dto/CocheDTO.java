package org.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CocheDTO {

    private Long id;
    private String uidUsuario;
    private String marca;
    private String modelo;
    private Integer año;
    private String matricula;
    private String color;
    private Integer kilometraje;
    private String notas;
    private String urlImagen;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
