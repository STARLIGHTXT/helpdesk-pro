package com.help_desk_pro.sistema_gestor_soporte_tecnico.service.strategy;

import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Ticket;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Usuario;

import java.util.List;

public interface AsignadorTecnico {
    String getNombre();
    Integer seleccionarTecnico(List<Usuario>tecnicos, List<Ticket> ticketsPendientes);
}
