package org.example.repository;

import org.example.entities.Coche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CocheRepository extends JpaRepository<Coche, Long> {

    List<Coche> findByUidUsuario(String uidUsuario);

    boolean existsByIdAndUidUsuario(Long id, String uidUsuario);

    long countByUidUsuario(String uidUsuario);
}