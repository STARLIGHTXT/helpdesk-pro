package com.help_desk_pro.sistema_gestor_soporte_tecnico.repository;

import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Estado;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Ticket;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
public class TicketRepository implements ITicketRepository{
    @Autowired
    private  JdbcTemplate jdbcTemplate;
    @Autowired
    private RowMapper<Ticket> ticketRowMapper;

    @Override
    public List<Ticket> findAll() {
        String query="SELECT * FROM ticket";
        return jdbcTemplate.query(query,ticketRowMapper);
    }

    @Override
    public Optional<Ticket> findById(int id) {
        String query="SELECT * FROM ticket WHERE id =?";
        List<Ticket> listaTicket=jdbcTemplate.query(query,ticketRowMapper,id);
        return listaTicket.stream().findFirst();
    }
    //juntar el save y update  implicaría modificar la interfaz

    @Override
    public void save(Ticket t) {
        String query="INSERT INTO ticket (titulo,descripcion,fecha_creacion,estado,prioridad,tipo,id_cliente,id_tecnico) VALUES (?,?,?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection->{
            PreparedStatement ps= connection.prepareStatement(query, new String[]{"id"});
            ps.setString(1,t.getTitulo());
            ps.setString(2,t.getDescripcion());
            // La columna fecha_creacion es TIMESTAMP: se envía fecha y hora
            ps.setTimestamp(3,java.sql.Timestamp.valueOf(t.getFechaCreacion()));
            ps.setString(4,t.getEstado().name());
            ps.setString(5,t.getPrioridad().name());
            ps.setString(6,t.getTipoTicket().name());
            ps.setInt(7,t.getIdCliente());
            // Un ticket nuevo no tiene técnico: setObject admite NULL sin lanzar NullPointerException
            ps.setObject(8,t.getIdTecnico(),Types.INTEGER);
            return ps;
        },keyHolder);
        if (keyHolder.getKey() != null){
            t.setId(keyHolder.getKey().intValue());
        }
    }

    @Override
    public void update(Ticket t) {
        String query="UPDATE  ticket SET titulo=?,descripcion=?,fecha_creacion=?,estado=?,prioridad=?,tipo=?,id_cliente=?,id_tecnico=? WHERE id=?";
        // Se envían los enums como texto (los VARCHAR de la BD) y el id_tecnico puede ser NULL
        jdbcTemplate.update(query,
                new Object[]{
                        t.getTitulo(),
                        t.getDescripcion(),
                        java.sql.Timestamp.valueOf(t.getFechaCreacion()),
                        t.getEstado().name(),
                        t.getPrioridad().name(),
                        t.getTipoTicket().name(),
                        t.getIdCliente(),
                        t.getIdTecnico(),
                        t.getId()
                },
                new int[]{
                        Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER
                });
    }

    @Override
    public List<Ticket> findByEstado(Estado estado) {
        String query="SELECT * FROM ticket WHERE estado=?";
        return jdbcTemplate.query(query,ticketRowMapper,estado.name());
    }

    @Override
    public List<Ticket> findSinTecnicoAsignado() {
        String query="SELECT * FROM ticket WHERE id_tecnico IS NULL";
        return jdbcTemplate.query(query,ticketRowMapper);
    }

    @Override
    public List<Ticket> findByTecnico(int idTecnico) {
        String query="SELECT * FROM ticket WHERE id_tecnico=?";
        return jdbcTemplate.query(query,ticketRowMapper,idTecnico);
    }

    @Override
    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM ticket WHERE id = ?", id);
    }
}
