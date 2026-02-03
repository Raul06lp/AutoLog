package org.example.service;

import org.example.dto.ClienteDTO;
import org.example.dto.ClienteRequestDTO;
import org.example.dto.LoginDTO;
import org.example.repository.ClienteRepository;
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
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

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
     * Registrar nuevo cliente
     */
    @Transactional
    public ClienteDTO registrarCliente(ClienteRequestDTO requestDTO) throws NoSuchAlgorithmException {
        if (clienteRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(requestDTO.getNombre());
        cliente.setEmail(requestDTO.getEmail());
        cliente.setContrasena(encriptarContrasena(requestDTO.getContrasena()));

        Cliente guardado = clienteRepository.save(cliente);
        return convertirADTO(guardado);
    }

    /**
     * Login de cliente
     */
    public Optional<ClienteDTO> login(LoginDTO loginDTO) throws NoSuchAlgorithmException {
        String contrasenaEncriptada = encriptarContrasena(loginDTO.getContrasena());
        return clienteRepository.findByEmail(loginDTO.getEmail())
                .filter(cliente -> cliente.getContrasena().equals(contrasenaEncriptada))
                .map(this::convertirADTO);
    }

    /**
     * Obtener cliente por ID
     */
    public Optional<ClienteDTO> obtenerPorId(Long id) {
        return clienteRepository.findById(id).map(this::convertirADTO);
    }

    /**
     * Obtener cliente por email
     */
    public Optional<ClienteDTO> obtenerPorEmail(String email) {
        return clienteRepository.findByEmail(email).map(this::convertirADTO);
    }

    /**
     * Obtener todos los clientes
     */
    public List<ClienteDTO> obtenerTodos() {
        return clienteRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualizar cliente
     */
    @Transactional
    public Optional<ClienteDTO> actualizarCliente(Long id, ClienteRequestDTO requestDTO) throws NoSuchAlgorithmException {
        return clienteRepository.findById(id).map(cliente -> {
            cliente.setNombre(requestDTO.getNombre());
            
            // Si cambia el email, verificar que no esté en uso
            if (!cliente.getEmail().equals(requestDTO.getEmail())) {
                if (clienteRepository.existsByEmail(requestDTO.getEmail())) {
                    throw new IllegalArgumentException("El email ya está en uso");
                }
                cliente.setEmail(requestDTO.getEmail());
            }
            
            try {
                cliente.setContrasena(encriptarContrasena(requestDTO.getContrasena()));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Error al encriptar contraseña", e);
            }
            
            Cliente actualizado = clienteRepository.save(cliente);
            return convertirADTO(actualizado);
        });
    }

    /**
     * Eliminar cliente
     */
    @Transactional
    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    /**
     * Convertir entidad a DTO
     */
    private ClienteDTO convertirADTO(Cliente cliente) {
        int cantidadVehiculos = (int) vehiculoRepository.countByClienteIdCliente(cliente.getIdCliente());
        
        return new ClienteDTO(
                cliente.getIdCliente(),
                cliente.getNombre(),
                cliente.getEmail(),
                cantidadVehiculos
        );
    }
}
