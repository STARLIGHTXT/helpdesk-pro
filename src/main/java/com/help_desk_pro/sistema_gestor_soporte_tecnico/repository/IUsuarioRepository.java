package com.help_desk_pro.sistema_gestor_soporte_tecnico.repository;

import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Rol;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository {
    List<Usuario>findAll();
    Optional<Usuario>findById(int id);
    List<Usuario>findByRol(Rol rol);
    void save(Usuario u);
    void update(Usuario u);
    void deleteById(int id);
}
