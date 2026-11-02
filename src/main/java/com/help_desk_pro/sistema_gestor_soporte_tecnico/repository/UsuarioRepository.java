package com.help_desk_pro.sistema_gestor_soporte_tecnico.repository;

import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Rol;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Usuario;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository

public class UsuarioRepository implements IUsuarioRepository{

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Usuario>usuarioRowMapper;

    public UsuarioRepository(JdbcTemplate jdbcTemplate, RowMapper<Usuario> usuarioRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.usuarioRowMapper = usuarioRowMapper;
    }

    @Override
    public List<Usuario> findAll() {
        String query="SELECT * FROM usuario";
        return jdbcTemplate.query(query,usuarioRowMapper);
    }

    //SELECT * : Indica que se seleccionan todas las columnas de la tabla
    //FROM usuario :Especifica la tabla de la base de datos sobre la cual se realizará la búsqueda
    //WHERE id=? Filtra los registros para que la base de datos devuelva la fila cuyo campo id
    //coincida con el valor que buscamos
    @Override
    public Optional<Usuario> findById(int id) {
        String query="SELECT * FROM usuario WHERE id=?";
        List<Usuario>listaUsuario=jdbcTemplate.query(query,usuarioRowMapper,id);
        return listaUsuario.stream().filter(Objects::nonNull)
                .findFirst();
    }

    //recordatorio de que jdbcTemplate siempre devuelve una lista
    @Override
    public List<Usuario> findByRol(Rol rol) {
        String query="SELECT * FROM usuario WHERE rol=?";
        List<Usuario>listaUsuarioRol=jdbcTemplate.query(query,usuarioRowMapper,rol.name());
        return listaUsuarioRol.stream().filter(Objects::nonNull)
                .filter(u->u.getRol().equals(rol)).toList();
    }

    @Override
    public void save(Usuario u) {
        String query="INSERT INTO usuario(nombre,email,rol) VALUES (?,?,?)";
        KeyHolder keyHolder=new GeneratedKeyHolder();
        jdbcTemplate.update(connection->{
            PreparedStatement ps=connection.prepareStatement(query, new String[]{"id"});
            ps.setString(1,u.getNombre());
            ps.setString(2,u.getEmail());
            ps.setString(3,u.getRol().name());
            return ps;
        },keyHolder);
        if (keyHolder.getKey() != null){
            u.setId(keyHolder.getKey().intValue());
        }
    }

    @Override
    public void update(Usuario u) {
        jdbcTemplate.update("UPDATE usuario SET nombre = ?, email = ?, rol = ? WHERE id = ?",
                u.getNombre(), u.getEmail(), u.getRol().name(), u.getId());
    }

    @Override
    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM usuario WHERE id = ?", id);
    }
}
