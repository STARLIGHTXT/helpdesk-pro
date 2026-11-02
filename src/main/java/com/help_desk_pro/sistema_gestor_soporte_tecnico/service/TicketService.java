package com.help_desk_pro.sistema_gestor_soporte_tecnico.service;

import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.*;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.repository.ITicketRepository;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.repository.IUsuarioRepository;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.service.strategy.AsignadorTecnico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TicketService {
    @Autowired
    private IUsuarioRepository iUsuarioRepo;
    @Autowired
    private ITicketRepository iTicketRepo;
    @Autowired
    private List<AsignadorTecnico> asignadorTecTodasLasImplementaciones; //aun no se esta usando

    public List<Ticket> obtenerTodos(){
        return iTicketRepo.findAll();
    }
    public List<Ticket>obtenerPorEstado(Estado estado){
        return iTicketRepo.findByEstado(estado);
    }
    public Optional<Ticket> buscarPorId(int id){
        return iTicketRepo.findById(id);
    }
    public void crearTicket(Ticket t){
        // Valores por defecto: un ticket nuevo siempre nace ABIERTO y con la fecha actual
        if (t.getFechaCreacion() == null){
            t.setFechaCreacion(java.time.LocalDateTime.now());
        }
        if (t.getEstado() == null){
            t.setEstado(Estado.ABIERTO);
        }
        if (t.getPrioridad() == null){
            t.setPrioridad(Prioridad.MEDIA);
        }
        iTicketRepo.save(t);
    }
    public void asignarTecnico(int idTicket,String nombreEstrategia){
        // 1. Buscamos el ticket objetivo
        Ticket ticket = iTicketRepo.findById(idTicket)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + idTicket));

        // 2. Buscamos la estrategia en la lista inyectada por Spring usando el método getNombre()
        AsignadorTecnico estrategiaSeleccionada = asignadorTecTodasLasImplementaciones.stream()
                .filter(e -> e.getNombre().equalsIgnoreCase(nombreEstrategia))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Estrategia de asignación no encontrada: " + nombreEstrategia));

        // 3. Obtenemos los datos que la estrategia necesita.
        // Para "menos carga" hacen falta TODOS los tickets: si solo le pasamos los
        // que no tienen técnico, todos los técnicos tendrían carga 0 y siempre
        // se elegiría al primero de la lista.
        List<Usuario> tecnicosDisponibles = iUsuarioRepo.findByRol(Rol.TECNICO);
        List<Ticket> ticketsPendientes = iTicketRepo.findAll();

        // 4. Ejecutamos la estrategia para que nos devuelva el ID del técnico seleccionado
        Integer idTecnicoAsignado = estrategiaSeleccionada.seleccionarTecnico(tecnicosDisponibles, ticketsPendientes);
        if (idTecnicoAsignado == null) {
            throw new IllegalStateException(
                    "No hay técnicos disponibles para asignar el ticket " + idTicket);
        }

        // 5. Actualizamos el ticket con el ID del técnico que eligió la estrategia
        ticket.setIdTecnico(idTecnicoAsignado);

        // Opcional: Si también quieres cambiarle el estado a "ASIGNADO" o "EN_PROCESO", lo harías aquí

        // 6. Guardamos los cambios en la base de datos usando tu repositorio
        iTicketRepo.update(ticket);
    }
    // Actualiza los datos editables de un ticket (el tipo no cambia)
    public void actualizarTicket(int id, String titulo, String descripcion,
                                 Estado estado, Prioridad prioridad, int idCliente) {
        Ticket ticket = iTicketRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + id));

        ticket.setTitulo(titulo);
        ticket.setDescripcion(descripcion);
        ticket.setEstado(estado);
        ticket.setPrioridad(prioridad);
        ticket.setIdCliente(idCliente);
        if (ticket.getFechaCreacion() == null) {
            ticket.setFechaCreacion(java.time.LocalDateTime.now());
        }
        iTicketRepo.update(ticket);
    }

    public void eliminarTicket(int id) {
        iTicketRepo.deleteById(id);
    }

    public Map<Prioridad,Long> contarTicketsPorPrioridad(){
        List<Ticket> listaTickets=iTicketRepo.findAll();
        return listaTickets.stream().filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Ticket::getPrioridad,Collectors.counting()));
    }
    public Optional<Ticket> ticketMasAntiguoAbierto(){
        List<Ticket> listaTickets=iTicketRepo.findAll();
        return listaTickets.stream().filter(Objects::nonNull)
                .filter(ticket->ticket.getEstado() != null &&ticket.getEstado()== Estado.ABIERTO)
                .min(Comparator.comparing(Ticket::getFechaCreacion));
    }
    public double promedioTiempoResolucionGlobal(){
        List<Ticket> listaTickets = iTicketRepo.findAll();
        return listaTickets.stream().filter(Objects::nonNull)
                .collect(Collectors.averagingDouble(Ticket::calcularTiempoEstimadoResolucion));
    }
    public Map<String,List<Ticket>>agruparTicketsPorTecnico(){
        List<Ticket>listaTickets=iTicketRepo.findAll();
        return listaTickets.stream().filter(Objects::nonNull)
                .collect(Collectors.groupingBy(t->{
                    return iUsuarioRepo.findById(t.getIdTecnico()).map(Usuario::getNombre).orElse("Sin asignar");
                }));
    }
}