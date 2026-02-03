package org.example.repository;

import org.example.entities.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    
    // Buscar por cliente
    List<Vehiculo> findByClienteIdCliente(Long idCliente);
    
    // Buscar por mecánico
    List<Vehiculo> findByMecanicoIdMecanico(Long idMecanico);
    
    // Buscar por matrícula
    Optional<Vehiculo> findByMatricula(String matricula);
    
    // Buscar por estado
    List<Vehiculo> findByEstadoRevision(String estadoRevision);
    
    // Buscar por cliente y estado
    List<Vehiculo> findByClienteIdClienteAndEstadoRevision(Long idCliente, String estadoRevision);
    
    // Buscar por mecánico y estado
    List<Vehiculo> findByMecanicoIdMecanicoAndEstadoRevision(Long idMecanico, String estadoRevision);
    
    // Contar vehículos por cliente
    long countByClienteIdCliente(Long idCliente);
    
    // Contar vehículos por mecánico
    long countByMecanicoIdMecanico(Long idMecanico);
}
