package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.AppUserRequestDTO;
import org.example.dto.AppUserResponseDTO;
import org.example.dto.LoginDTO;
import org.example.entities.AppUser;
import org.example.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Registrar nuevo usuario de seguridad.
     * POST /api/auth/registro
     * Este endpoint es PÚBLICO (configurado en SecurityConfig).
     *
     * Body: { "username": "admin@correo.com", "password": "mi1234", "role": "ROLE_ADMIN" }
     */
    @PostMapping("/registro")
    public ResponseEntity<AppUserResponseDTO> registrar(@Valid @RequestBody AppUserRequestDTO requestDTO) {

        // Comprobar si el username ya existe
        if (appUserRepository.existsByUsername(requestDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Asignar rol por defecto si no se indica
        String role = (requestDTO.getRole() != null && !requestDTO.getRole().isBlank())
                ? requestDTO.getRole()
                : "ROLE_USER";

        // Crear y guardar el usuario con la contraseña cifrada con BCrypt
        AppUser nuevoUsuario = new AppUser();
        nuevoUsuario.setUsername(requestDTO.getUsername());
        nuevoUsuario.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        nuevoUsuario.setRole(role);

        AppUser guardado = appUserRepository.save(nuevoUsuario);

        AppUserResponseDTO response = new AppUserResponseDTO(
                guardado.getId(),
                guardado.getUsername(),
                guardado.getRole()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Verificar credenciales (login).
     * POST /api/auth/verificar
     * Este endpoint es PRIVADO: el cliente ya debe autenticarse con Basic Auth.
     * Si las credenciales son correctas, Spring Security deja pasar la petición
     * y devolvemos 200 OK con los datos del usuario.
     *
     * NOTA: No usamos la URL /login porque está reservada por Spring Security
     * para el formulario web de login.
     *
     * Body: { "email": "usuario", "contrasena": "password" }
     */
    @PostMapping("/verificar")
    public ResponseEntity<AppUserResponseDTO> verificar(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            // Intentar autenticar con las credenciales recibidas
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getContrasena()
                    )
            );

            // Si no lanza excepción, las credenciales son válidas
            AppUser usuario = appUserRepository.findByUsername(loginDTO.getEmail())
                    .orElseThrow();

            return ResponseEntity.ok(new AppUserResponseDTO(
                    usuario.getId(),
                    usuario.getUsername(),
                    usuario.getRole()
            ));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
