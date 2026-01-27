package org.example.dao;

import org.example.entities.Coche;
import org.example.repository.CocheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
@Component
public class CocheDAO {

    @Autowired
    private CocheRepository cocheRepository;

    public Coche guardar(Coche coche) {
        return cocheRepository.save(coche);
    }

    public List<Coche> buscarPorUsuario(String uidUsuario) {
        return cocheRepository.findByUidUsuario(uidUsuario);
    }

    public List<Coche> buscarTodos() {
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