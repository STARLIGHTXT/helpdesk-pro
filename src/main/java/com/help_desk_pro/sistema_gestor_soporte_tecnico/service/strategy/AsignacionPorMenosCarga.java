package com.help_desk_pro.sistema_gestor_soporte_tecnico.service.strategy;

import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Estado;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Rol;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Ticket;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Component
public class AsignacionPorMenosCarga implements AsignadorTecnico{
    @Override
    public String getNombre() {
        return "Asignacion por menos carga";
    }

    @Override
    public Integer seleccionarTecnico(List<Usuario> tecnicos, List<Ticket> ticketsPendientes) {
       if (tecnicos == null  || tecnicos.isEmpty()){
           return  null;
       }
       //contar cuantos tickets abiertos tiene cada tecnico
      return tecnicos.stream().filter(Objects::nonNull)
              .filter(u->u.getRol()==Rol.TECNICO)
              .min(
                      Comparator.comparingLong(tecnico->ticketsPendientes.stream()
                              .filter(t->t.getIdTecnico() != null && t.getIdTecnico().equals(tecnico.getId()))
                              .filter(t->t.getEstado() != null && t.getEstado() != Estado.CERRADO).count())

              ).map(Usuario::getId).orElse(null);//va a saltar el valor minimo de algo
    }
}
