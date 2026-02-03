package org.example.service;

import org.example.dto.VehiculoDTO;
import org.example.dto.VehiculoRequestDTO;
import org.example.entities.Cliente;
import org.example.entities.Mecanico;
import org.example.entities.Vehiculo;
import org.example.repository.ClienteRepository;
import org.example.repository.MecanicoRepository;
import org.example.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MecanicoRepository mecanicoRepository;

    /**
     * Crear nuevo vehículo con imagen
     */
    @Transactional
    public VehiculoDTO crearVehiculo(VehiculoRequestDTO requestDTO) {
        // Verificar que el cliente existe
        Cliente cliente = clienteRepository.findById(requestDTO.getIdCliente())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        // Verificar si la matrícula ya existe
        if (vehiculoRepository.findByMatricula(requestDTO.getMatricula()).isPresent()) {
            throw new IllegalArgumentException("La matrícula ya está registrada");
        }

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setMatricula(requestDTO.getMatricula());
        vehiculo.setMarca(requestDTO.getMarca());
        vehiculo.setModelo(requestDTO.getModelo());
        vehiculo.setAño(requestDTO.getAño());
        vehiculo.setColor(requestDTO.getColor());
        vehiculo.setKilometraje(requestDTO.getKilometraje());
        vehiculo.setObservaciones(requestDTO.getObservaciones());
        vehiculo.setMedidasTomadas(requestDTO.getMedidasTomadas());
        vehiculo.setEstadoRevision(requestDTO.getEstadoRevision());
        
        // Convertir imagen Base64 a bytes
        if (requestDTO.getImagenBase64() != null && !requestDTO.getImagenBase64().isEmpty()) {
            try {
                byte[] imagenBytes = Base64.getDecoder().decode(requestDTO.getImagenBase64());
                vehiculo.setImagen(imagenBytes);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Imagen Base64 inválida");
            }
        }
        
        vehiculo.setCliente(cliente);

        // Asignar mecánico si se especifica
        if (requestDTO.getIdMecanico() != null) {
            Mecanico mecanico = mecanicoRepository.findById(requestDTO.getIdMecanico())
                    .orElseThrow(() -> new IllegalArgumentException("Mecánico no encontrado"));
            vehiculo.setMecanico(mecanico);
        }

        Vehiculo guardado = vehiculoRepository.save(vehiculo);
        return convertirADTO(guardado);
    }

    /**
     * Obtener vehículo por ID
     */
    public Optional<VehiculoDTO> obtenerPorId(Long id) {
        return vehiculoRepository.findById(id).map(this::convertirADTO);
    }

    /**
     * Obtener vehículo por matrícula
     */
    public Optional<VehiculoDTO> obtenerPorMatricula(String matricula) {
        return vehiculoRepository.findByMatricula(matricula).map(this::convertirADTO);
    }

    /**
     * Obtener todos los vehículos
     */
    public List<VehiculoDTO> obtenerTodos() {
        return vehiculoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener vehículos por cliente
     */
    public List<VehiculoDTO> obtenerPorCliente(Long idCliente) {
        return vehiculoRepository.findByClienteIdCliente(idCliente).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener vehículos por mecánico
     */
    public List<VehiculoDTO> obtenerPorMecanico(Long idMecanico) {
        return vehiculoRepository.findByMecanicoIdMecanico(idMecanico).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener vehículos por estado
     */
    public List<VehiculoDTO> obtenerPorEstado(String estado) {
        return vehiculoRepository.findByEstadoRevision(estado).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener vehículos por cliente y estado
     */
    public List<VehiculoDTO> obtenerPorClienteYEstado(Long idCliente, String estado) {
        return vehiculoRepository.findByClienteIdClienteAndEstadoRevision(idCliente, estado).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener vehículos por mecánico y estado
     */
    public List<VehiculoDTO> obtenerPorMecanicoYEstado(Long idMecanico, String estado) {
        return vehiculoRepository.findByMecanicoIdMecanicoAndEstadoRevision(idMecanico, estado).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualizar vehículo
     */
    @Transactional
    public Optional<VehiculoDTO> actualizarVehiculo(Long id, VehiculoRequestDTO requestDTO) {
        return vehiculoRepository.findById(id).map(vehiculo -> {
            // Verificar si la matrícula cambió y si ya existe
            if (!vehiculo.getMatricula().equals(requestDTO.getMatricula())) {
                if (vehiculoRepository.findByMatricula(requestDTO.getMatricula()).isPresent()) {
                    throw new IllegalArgumentException("La matrícula ya está registrada");
                }
                vehiculo.setMatricula(requestDTO.getMatricula());
            }

            vehiculo.setMarca(requestDTO.getMarca());
            vehiculo.setModelo(requestDTO.getModelo());
            vehiculo.setAño(requestDTO.getAño());
            vehiculo.setColor(requestDTO.getColor());
            vehiculo.setKilometraje(requestDTO.getKilometraje());
            vehiculo.setObservaciones(requestDTO.getObservaciones());
            vehiculo.setMedidasTomadas(requestDTO.getMedidasTomadas());
            vehiculo.setEstadoRevision(requestDTO.getEstadoRevision());

            // Actualizar imagen si se proporciona
            if (requestDTO.getImagenBase64() != null && !requestDTO.getImagenBase64().isEmpty()) {
                try {
                    byte[] imagenBytes = Base64.getDecoder().decode(requestDTO.getImagenBase64());
                    vehiculo.setImagen(imagenBytes);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Imagen Base64 inválida");
                }
            }

            // Actualizar cliente si cambió
            if (!vehiculo.getCliente().getIdCliente().equals(requestDTO.getIdCliente())) {
                Cliente cliente = clienteRepository.findById(requestDTO.getIdCliente())
                        .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
                vehiculo.setCliente(cliente);
            }

            // Actualizar mecánico si cambió
            if (requestDTO.getIdMecanico() != null) {
                if (vehiculo.getMecanico() == null || 
                    !vehiculo.getMecanico().getIdMecanico().equals(requestDTO.getIdMecanico())) {
                    Mecanico mecanico = mecanicoRepository.findById(requestDTO.getIdMecanico())
                            .orElseThrow(() -> new IllegalArgumentException("Mecánico no encontrado"));
                    vehiculo.setMecanico(mecanico);
                }
            } else {
                vehiculo.setMecanico(null);
            }

            Vehiculo actualizado = vehiculoRepository.save(vehiculo);
            return convertirADTO(actualizado);
        });
    }

    /**
     * Actualizar solo la imagen
     */
    @Transactional
    public Optional<VehiculoDTO> actualizarImagen(Long id, String imagenBase64) {
        return vehiculoRepository.findById(id).map(vehiculo -> {
            try {
                byte[] imagenBytes = Base64.getDecoder().decode(imagenBase64);
                vehiculo.setImagen(imagenBytes);
                Vehiculo actualizado = vehiculoRepository.save(vehiculo);
                return convertirADTO(actualizado);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Imagen Base64 inválida");
            }
        });
    }

    /**
     * Obtener solo la imagen de un vehículo
     */
    public Optional<byte[]> obtenerImagen(Long id) {
        return vehiculoRepository.findById(id)
                .map(Vehiculo::getImagen);
    }

    /**
     * Asignar mecánico a vehículo
     */
    @Transactional
    public Optional<VehiculoDTO> asignarMecanico(Long idVehiculo, Long idMecanico) {
        return vehiculoRepository.findById(idVehiculo).map(vehiculo -> {
            Mecanico mecanico = mecanicoRepository.findById(idMecanico)
                    .orElseThrow(() -> new IllegalArgumentException("Mecánico no encontrado"));
            
            vehiculo.setMecanico(mecanico);
            Vehiculo actualizado = vehiculoRepository.save(vehiculo);
            return convertirADTO(actualizado);
        });
    }

    /**
     * Actualizar estado de revisión
     */
    @Transactional
    public Optional<VehiculoDTO> actualizarEstado(Long id, String nuevoEstado) {
        return vehiculoRepository.findById(id).map(vehiculo -> {
            vehiculo.setEstadoRevision(nuevoEstado);
            Vehiculo actualizado = vehiculoRepository.save(vehiculo);
            return convertirADTO(actualizado);
        });
    }

    /**
     * Eliminar vehículo
     */
    @Transactional
    public void eliminarVehiculo(Long id) {
        vehiculoRepository.deleteById(id);
    }

    /**
     * Convertir entidad a DTO
     */
    private VehiculoDTO convertirADTO(Vehiculo vehiculo) {
        // Convertir imagen bytes a Base64
        String imagenBase64 = null;
        if (vehiculo.getImagen() != null) {
            imagenBase64 = Base64.getEncoder().encodeToString(vehiculo.getImagen());
        }

        return VehiculoDTO.builder()
                .idVehiculo(vehiculo.getIdVehiculo())
                .matricula(vehiculo.getMatricula())
                .marca(vehiculo.getMarca())
                .modelo(vehiculo.getModelo())
                .año(vehiculo.getAño())
                .color(vehiculo.getColor())
                .kilometraje(vehiculo.getKilometraje())
                .observaciones(vehiculo.getObservaciones())
                .medidasTomadas(vehiculo.getMedidasTomadas())
                .estadoRevision(vehiculo.getEstadoRevision())
                .imagenBase64(imagenBase64)
                .idCliente(vehiculo.getCliente().getIdCliente())
                .nombreCliente(vehiculo.getCliente().getNombre())
                .emailCliente(vehiculo.getCliente().getEmail())
                .idMecanico(vehiculo.getMecanico() != null ? vehiculo.getMecanico().getIdMecanico() : null)
                .nombreMecanico(vehiculo.getMecanico() != null ? vehiculo.getMecanico().getNombre() : null)
                .emailMecanico(vehiculo.getMecanico() != null ? vehiculo.getMecanico().getEmail() : null)
                .fechaIngreso(vehiculo.getFechaIngreso())
                .fechaActualizacion(vehiculo.getFechaActualizacion())
                .build();
    }
}
