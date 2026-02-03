package org.example.service;

import org.example.dto.MecanicoDTO;
import org.example.dto.MecanicoRequestDTO;
import org.example.dto.LoginDTO;
import org.example.entities.Mecanico;
import org.example.repository.MecanicoRepository;
import org.example.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MecanicoService {

    @Autowired
    private MecanicoRepository mecanicoRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    /**
     * Encriptar contraseña con MD5
     */
    public String encriptarContrasena(String contrasena) throws NoSuchAlgorithmException {
        byte[] bytes = contrasena.getBytes(StandardCharsets.UTF_8);
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(bytes);
        return Base64.getEncoder().encodeToString(digest);
    }

    /**
     * Registrar nuevo mecanico
     */
    @Transactional
    public MecanicoDTO registrarMecanico(MecanicoRequestDTO requestDTO) throws NoSuchAlgorithmException {
        if (mecanicoRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        Mecanico mecanico = new Mecanico();
        mecanico.setNombre(requestDTO.getNombre());
        mecanico.setEmail(requestDTO.getEmail());
        mecanico.setContrasena(encriptarContrasena(requestDTO.getContrasena()));

        Mecanico guardado = mecanicoRepository.save(mecanico);
        return convertirADTO(guardado);
    }

    /**
     * Login de mecanico
     */
    public Optional<MecanicoDTO> login(LoginDTO loginDTO) throws NoSuchAlgorithmException {
        String contrasenaEncriptada = encriptarContrasena(loginDTO.getContrasena());
        return mecanicoRepository.findByEmail(loginDTO.getEmail())
                .filter(mecanico -> mecanico.getContrasena().equals(contrasenaEncriptada))
                .map(this::convertirADTO);
    }

    /**
     * Obtener mecanico por ID
     */
    public Optional<MecanicoDTO> obtenerPorId(Long id) {
        return mecanicoRepository.findById(id).map(this::convertirADTO);
    }

    /**
     * Obtener mecanico por email
     */
    public Optional<MecanicoDTO> obtenerPorEmail(String email) {
        return mecanicoRepository.findByEmail(email).map(this::convertirADTO);
    }

    /**
     * Obtener todos los mecanicos
     */
    public List<MecanicoDTO> obtenerTodos() {
        return mecanicoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualizar mecanico
     */
    @Transactional
    public Optional<MecanicoDTO> actualizarMecanico(Long id, MecanicoRequestDTO requestDTO) throws NoSuchAlgorithmException {
        return mecanicoRepository.findById(id).map(mecanico -> {
            mecanico.setNombre(requestDTO.getNombre());
            
            // Si cambia el email, verificar que no esté en uso
            if (!mecanico.getEmail().equals(requestDTO.getEmail())) {
                if (mecanicoRepository.existsByEmail(requestDTO.getEmail())) {
                    throw new IllegalArgumentException("El email ya está en uso");
                }
                mecanico.setEmail(requestDTO.getEmail());
            }
            
            try {
                mecanico.setContrasena(encriptarContrasena(requestDTO.getContrasena()));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Error al encriptar contraseña", e);
            }
            
            Mecanico actualizado = mecanicoRepository.save(mecanico);
            return convertirADTO(actualizado);
        });
    }

    /**
     * Eliminar mecanico
     */
    @Transactional
    public void eliminarMecanico(Long id) {
        mecanicoRepository.deleteById(id);
    }

    /**
     * Convertir entidad a DTO
     */
    private MecanicoDTO convertirADTO(Mecanico mecanico) {
        int cantidadVehiculos = (int) vehiculoRepository.countByMecanicoIdMecanico(mecanico.getIdMecanico());
        
        return new MecanicoDTO(
                mecanico.getIdMecanico(),
                mecanico.getNombre(),
                mecanico.getEmail(),
                cantidadVehiculos
        );
    }
}
