package com.help_desk_pro.sistema_gestor_soporte_tecnico.model;

import java.time.LocalDateTime;

public class TicketSolicitud extends Ticket{
    public TicketSolicitud(){
        this.setTipoTicket(TipoTicket.SOLICITUD);
    }
    public TicketSolicitud(String descripcion, Estado estado, LocalDateTime fechaCreacion, int id, int idCliente, Integer idTecnico, Prioridad prioridad, String titulo){
        super(descripcion, estado, fechaCreacion, id, idCliente, idTecnico, prioridad, titulo,TipoTicket.SOLICITUD);
    }

    @Override
    public double calcularTiempoEstimadoResolucion() {
        return getPrioridad()==Prioridad.ALTA ? 12.0:48.0;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
