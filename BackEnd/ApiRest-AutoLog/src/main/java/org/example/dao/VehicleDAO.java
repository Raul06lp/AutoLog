package org.example.dao;

import org.example.entities.Vehiculo;
import org.example.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
@Component
public class VehicleDAO {

    @Autowired
    private VehicleRepository cocheRepository;

    public Vehiculo guardar(Vehiculo vehiculo) {
        return cocheRepository.save(vehiculo);
    }

    public List<Vehiculo> buscarPorUsuario(String uidUsuario) {
        return cocheRepository.findByUidUsuario(uidUsuario);
    }

    public List<Vehiculo> buscarTodos() {
        return cocheRepository.findAll();
    }

    public void eliminar(Long id) {
        cocheRepository.deleteById(id);
    }

    public boolean existePorIdYUsuario(Long id, String uidUsuario) {
        return cocheRepository.existsByIdAndUidUsuario(id, uidUsuario);
    }

    public long contarPorUsuario(String uidUsuario) {
        return cocheRepository.countByUidUsuario(uidUsuario);
    }
}