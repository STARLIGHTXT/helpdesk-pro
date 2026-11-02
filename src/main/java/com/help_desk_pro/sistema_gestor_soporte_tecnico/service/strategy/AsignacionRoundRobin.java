package com.help_desk_pro.sistema_gestor_soporte_tecnico.service.strategy;

import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Ticket;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AsignacionRoundRobin implements AsignadorTecnico {

    // Contador compartido: cada asignación avanza al siguiente técnico de la lista
    private final AtomicInteger turno = new AtomicInteger(0);

    @Override
    public String getNombre() {
        return "Round Robin";
    }

    @Override
    public Integer seleccionarTecnico(List<Usuario> tecnicos, List<Ticket> ticketsPendientes) {
        if (tecnicos == null || tecnicos.isEmpty()) {
            return null;
        }
        // floorMod evita índices negativos si el contador llegara a desbordarse
        int posicion = Math.floorMod(turno.getAndIncrement(), tecnicos.size());
        return tecnicos.get(posicion).getId();
    }
}
