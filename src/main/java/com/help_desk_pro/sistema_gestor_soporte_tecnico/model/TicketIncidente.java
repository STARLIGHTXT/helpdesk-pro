package com.help_desk_pro.sistema_gestor_soporte_tecnico.model;

import java.time.LocalDateTime;

public class TicketIncidente extends Ticket{

    //atributos
    //constructor
    public TicketIncidente(){
        this.setTipoTicket(TipoTicket.INCIDENTE);
    }
    public TicketIncidente(String descripcion, Estado estado, LocalDateTime fechaCreacion, int  id, int idCliente, Integer idTecnico, Prioridad prioridad, String titulo){
        super(descripcion, estado, fechaCreacion, id, idCliente, idTecnico, prioridad, titulo,TipoTicket.INCIDENTE);
    }
    //getters y setters

    //metodos e implementaciones
    @Override
    public double calcularTiempoEstimadoResolucion() {
        return getPrioridad()==Prioridad.ALTA ? 4.0:24.0;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
