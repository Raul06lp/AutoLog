package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.MecanicoDTO;
import org.example.dto.MecanicoRequestDTO;
import org.example.dto.LoginDTO;
import org.example.service.MecanicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mecanicos")
@CrossOrigin(origins = "*")
public class MecanicoController {

    @Autowired
    private MecanicoService mecanicoService;

    /**
     * Registrar nuevo mecánico
     * POST /api/mecanicos/registro
     */
    @PostMapping("/registro")
    public ResponseEntity<MecanicoDTO> registrarMecanico(@Valid @RequestBody MecanicoRequestDTO requestDTO) {
        try {
            MecanicoDTO mecanico = mecanicoService.registrarMecanico(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(mecanico);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Login de mecánico
     * POST /api/mecanicos/login
     */
    @PostMapping("/login")
    public ResponseEntity<MecanicoDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            Optional<MecanicoDTO> mecanico = mecanicoService.login(loginDTO);
            return mecanico.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener todos los mecánicos
     * GET /api/mecanicos
     */
    @GetMapping
    public ResponseEntity<List<MecanicoDTO>> obtenerTodos() {
        List<MecanicoDTO> mecanicos = mecanicoService.obtenerTodos();
        return ResponseEntity.ok(mecanicos);
    }

    /**
     * Obtener mecánico por ID
     * GET /api/mecanicos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<MecanicoDTO> obtenerPorId(@PathVariable Long id) {
        return mecanicoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtener mecánico por email
     * GET /api/mecanicos/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<MecanicoDTO> obtenerPorEmail(@PathVariable String email) {
        return mecanicoService.obtenerPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualizar mecánico
     * PUT /api/mecanicos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<MecanicoDTO> actualizarMecanico(
            @PathVariable Long id,
            @Valid @RequestBody MecanicoRequestDTO requestDTO) {
        try {
            return mecanicoService.actualizarMecanico(id, requestDTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Eliminar mecánico
     * DELETE /api/mecanicos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMecanico(@PathVariable Long id) {
        mecanicoService.eliminarMecanico(id);
        return ResponseEntity.noContent().build();
    }
}
