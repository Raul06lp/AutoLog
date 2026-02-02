package org.example.repository;

import org.example.entities.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehiculo, Long> {

    List<Vehiculo> findByUidUsuario(String uidUsuario);

    boolean existsByIdAndUidUsuario(Long id, String uidUsuario);

    long countByUidUsuario(String uidUsuario);
}