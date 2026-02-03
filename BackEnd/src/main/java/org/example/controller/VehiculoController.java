package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.VehiculoDTO;
import org.example.dto.VehiculoRequestDTO;
import org.example.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehiculos")
@CrossOrigin(origins = "*")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    /**
     * Crear nuevo vehículo
     * POST /api/vehiculos
     */
    @PostMapping
    public ResponseEntity<VehiculoDTO> crearVehiculo(@Valid @RequestBody VehiculoRequestDTO requestDTO) {
        try {
            VehiculoDTO vehiculo = vehiculoService.crearVehiculo(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(vehiculo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Crear vehículo con imagen MultipartFile
     * POST /api/vehiculos/con-imagen
     */
    @PostMapping(value = "/con-imagen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VehiculoDTO> crearVehiculoConImagen(
            @RequestParam("matricula") String matricula,
            @RequestParam("marca") String marca,
            @RequestParam("modelo") String modelo,
            @RequestParam("año") Integer año,
            @RequestParam(value = "color", required = false) String color,
            @RequestParam(value = "kilometraje", required = false) Integer kilometraje,
            @RequestParam(value = "observaciones", required = false) String observaciones,
            @RequestParam(value = "medidasTomadas", required = false) String medidasTomadas,
            @RequestParam(value = "estadoRevision", required = false) String estadoRevision,
            @RequestParam("idCliente") Long idCliente,
            @RequestParam(value = "idMecanico", required = false) Long idMecanico,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen) {
        
        try {
            VehiculoRequestDTO requestDTO = new VehiculoRequestDTO();
            requestDTO.setMatricula(matricula);
            requestDTO.setMarca(marca);
            requestDTO.setModelo(modelo);
            requestDTO.setAño(año);
            requestDTO.setColor(color);
            requestDTO.setKilometraje(kilometraje);
            requestDTO.setObservaciones(observaciones);
            requestDTO.setMedidasTomadas(medidasTomadas);
            requestDTO.setEstadoRevision(estadoRevision);
            requestDTO.setIdCliente(idCliente);
            requestDTO.setIdMecanico(idMecanico);
            
            // Convertir MultipartFile a Base64
            if (imagen != null && !imagen.isEmpty()) {
                byte[] bytes = imagen.getBytes();
                String imagenBase64 = Base64.getEncoder().encodeToString(bytes);
                requestDTO.setImagenBase64(imagenBase64);
            }
            
            VehiculoDTO vehiculo = vehiculoService.crearVehiculo(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(vehiculo);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Obtener todos los vehículos
     * GET /api/vehiculos
     */
    @GetMapping
    public ResponseEntity<List<VehiculoDTO>> obtenerTodos() {
        List<VehiculoDTO> vehiculos = vehiculoService.obtenerTodos();
        return ResponseEntity.ok(vehiculos);
    }

    /**
     * Obtener vehículo por ID
     * GET /api/vehiculos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehiculoDTO> obtenerPorId(@PathVariable Long id) {
        return vehiculoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtener solo la imagen de un vehículo
     * GET /api/vehiculos/{id}/imagen
     */
    @GetMapping(value = "/{id}/imagen", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable Long id) {
        return vehiculoService.obtenerImagen(id)
                .map(imagen -> ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(imagen))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualizar solo la imagen
     * PUT /api/vehiculos/{id}/imagen
     */
    @PutMapping("/{id}/imagen")
    public ResponseEntity<VehiculoDTO> actualizarImagen(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        
        String imagenBase64 = body.get("imagenBase64");
        if (imagenBase64 == null || imagenBase64.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return vehiculoService.actualizarImagen(id, imagenBase64)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualizar imagen con MultipartFile
     * PUT /api/vehiculos/{id}/imagen-file
     */
    @PutMapping(value = "/{id}/imagen-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VehiculoDTO> actualizarImagenFile(
            @PathVariable Long id,
            @RequestParam("imagen") MultipartFile imagen) {
        
        try {
            if (imagen.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            byte[] bytes = imagen.getBytes();
            String imagenBase64 = Base64.getEncoder().encodeToString(bytes);

            return vehiculoService.actualizarImagen(id, imagenBase64)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener vehículo por matrícula
     * GET /api/vehiculos/matricula/{matricula}
     */
    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<VehiculoDTO> obtenerPorMatricula(@PathVariable String matricula) {
        return vehiculoService.obtenerPorMatricula(matricula)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtener vehículos por cliente
     * GET /api/vehiculos/cliente/{idCliente}
     */
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<VehiculoDTO>> obtenerPorCliente(@PathVariable Long idCliente) {
        List<VehiculoDTO> vehiculos = vehiculoService.obtenerPorCliente(idCliente);
        return ResponseEntity.ok(vehiculos);
    }

    /**
     * Obtener vehículos por mecánico
     * GET /api/vehiculos/mecanico/{idMecanico}
     */
    @GetMapping("/mecanico/{idMecanico}")
    public ResponseEntity<List<VehiculoDTO>> obtenerPorMecanico(@PathVariable Long idMecanico) {
        List<VehiculoDTO> vehiculos = vehiculoService.obtenerPorMecanico(idMecanico);
        return ResponseEntity.ok(vehiculos);
    }

    /**
     * Obtener vehículos por estado
     * GET /api/vehiculos/estado/{estado}
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<VehiculoDTO>> obtenerPorEstado(@PathVariable String estado) {
        List<VehiculoDTO> vehiculos = vehiculoService.obtenerPorEstado(estado);
        return ResponseEntity.ok(vehiculos);
    }

    /**
     * Obtener vehículos por cliente y estado
     * GET /api/vehiculos/cliente/{idCliente}/estado/{estado}
     */
    @GetMapping("/cliente/{idCliente}/estado/{estado}")
    public ResponseEntity<List<VehiculoDTO>> obtenerPorClienteYEstado(
            @PathVariable Long idCliente,
            @PathVariable String estado) {
        List<VehiculoDTO> vehiculos = vehiculoService.obtenerPorClienteYEstado(idCliente, estado);
        return ResponseEntity.ok(vehiculos);
    }

    /**
     * Obtener vehículos por mecánico y estado
     * GET /api/vehiculos/mecanico/{idMecanico}/estado/{estado}
     */
    @GetMapping("/mecanico/{idMecanico}/estado/{estado}")
    public ResponseEntity<List<VehiculoDTO>> obtenerPorMecanicoYEstado(
            @PathVariable Long idMecanico,
            @PathVariable String estado) {
        List<VehiculoDTO> vehiculos = vehiculoService.obtenerPorMecanicoYEstado(idMecanico, estado);
        return ResponseEntity.ok(vehiculos);
    }

    /**
     * Actualizar vehículo
     * PUT /api/vehiculos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehiculoDTO> actualizarVehiculo(
            @PathVariable Long id,
            @Valid @RequestBody VehiculoRequestDTO requestDTO) {
        try {
            return vehiculoService.actualizarVehiculo(id, requestDTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Asignar mecánico a vehículo
     * PATCH /api/vehiculos/{id}/mecanico
     */
    @PatchMapping("/{id}/mecanico")
    public ResponseEntity<VehiculoDTO> asignarMecanico(
            @PathVariable Long id,
            @RequestBody Map<String, Long> body) {
        try {
            Long idMecanico = body.get("idMecanico");
            if (idMecanico == null) {
                return ResponseEntity.badRequest().build();
            }
            return vehiculoService.asignarMecanico(id, idMecanico)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Actualizar estado de revisión
     * PATCH /api/vehiculos/{id}/estado
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<VehiculoDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estadoRevision");
        if (nuevoEstado == null || nuevoEstado.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return vehiculoService.actualizarEstado(id, nuevoEstado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Eliminar vehículo
     * DELETE /api/vehiculos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVehiculo(@PathVariable Long id) {
        vehiculoService.eliminarVehiculo(id);
        return ResponseEntity.noContent().build();
    }
}
