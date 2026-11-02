package com.help_desk_pro.sistema_gestor_soporte_tecnico.repository;

import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Estado;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Ticket;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface ITicketRepository {
    List<Ticket>findAll();
    Optional<Ticket> findById(int id);
    void save(Ticket t);
    void update(Ticket t);
    List<Ticket>findByEstado(Estado estado);
    List<Ticket>findSinTecnicoAsignado();
    List<Ticket>findByTecnico(int idTecnico);
    void deleteById(int id);
}
