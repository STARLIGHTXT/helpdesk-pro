package com.help_desk_pro.sistema_gestor_soporte_tecnico.controller;

import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.*;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.service.TicketService;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.service.UsuarioService;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.service.strategy.AsignadorTecnico;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/helpdesk")
public class TicketController {

    private final TicketService ticketService;
    private final UsuarioService usuarioService;
    private final List<AsignadorTecnico> asignadorTecnicos;

    // Inyección por constructor (estándar profesional)
    public TicketController(TicketService ticketService, UsuarioService usuarioService, List<AsignadorTecnico> asignadorTecnicos) {
        this.ticketService = ticketService;
        this.usuarioService = usuarioService;
        this.asignadorTecnicos = asignadorTecnicos;
    }

    // 1. Listar todos los tickets con filtro opcional por Estado
    @GetMapping
    public String mostrarTodosLosTickets(@RequestParam(required = false) Estado estado, Model model) {
        List<Ticket> listaTickets;
        if (estado != null) {
            listaTickets = ticketService.obtenerPorEstado(estado);
        } else {
            listaTickets = ticketService.obtenerTodos();
        }
        model.addAttribute("tickets", listaTickets);
        model.addAttribute("estadoActual", estado);
        // Mapa id -> nombre para mostrar el técnico/cliente por su nombre
        // en lugar de únicamente el "#id".
        Map<Integer, String> nombresUsuarios = usuarioService.ListarTodos().stream()
                .collect(Collectors.toMap(Usuario::getId, Usuario::getNombre, (a, b) -> a));
        model.addAttribute("nombresUsuarios", nombresUsuarios);
        return "tickets/index";
    }

    // 2. Mostrar formulario para Ticket Incidente
    @GetMapping("/nuevo/incidente")
    public String muestraFormularioIncidente(Model model) {
        model.addAttribute("ticketIncidente", new TicketIncidente());
        model.addAttribute("usuarios", usuarioService.encontrarPorRol(Rol.CLIENTE));
        return "tickets/formulario-incidente";
    }

    // 3. Mostrar formulario para Ticket Solicitud
    @GetMapping("/nuevo/solicitud")
    public String muestraFormularioSolicitud(Model model) {
        model.addAttribute("ticketSolicitud", new TicketSolicitud());
        model.addAttribute("usuarios", usuarioService.encontrarPorRol(Rol.CLIENTE));
        return "tickets/formulario-solicitud";
    }

    // 4. Mostrar el detalle de un ticket específico (con opción de asignar técnico y estrategias)
    @GetMapping("/ver/{id}")
    public String mostrarDetalleTicket(@PathVariable int id, Model model) {
        Ticket ticket = ticketService.buscarPorId(id).orElse(null);
        model.addAttribute("ticket", ticket);
        model.addAttribute("tecnicos", usuarioService.encontrarPorRol(Rol.TECNICO));
        model.addAttribute("estrategias", asignadorTecnicos);

        // Nombres de cliente y técnico para mostrarlos en el detalle
        if (ticket != null) {
            model.addAttribute("cliente",
                    usuarioService.buscarPorId(ticket.getIdCliente()).orElse(null));
            if (ticket.getIdTecnico() != null) {
                model.addAttribute("tecnicoAsignado",
                        usuarioService.buscarPorId(ticket.getIdTecnico()).orElse(null));
            }
        }
        return "tickets/mostrar-detalle-tickets";
    }

    // 5. Mostrar la sección de estadísticas globales del sistema
    @GetMapping("/estadisticas")
    public String muestraEstadisticas(Model model) {
        model.addAttribute("conteoPrioridades", ticketService.contarTicketsPorPrioridad());
        model.addAttribute("ticketMasAntiguo", ticketService.ticketMasAntiguoAbierto().orElse(null));
        model.addAttribute("promedioResolucion", ticketService.promedioTiempoResolucionGlobal());
        return "tickets/estadisticas";
    }

    // 6. Guardar un Ticket Incidente
    @PostMapping("/guardar/incidente")
    public String guardarIncidente(@ModelAttribute TicketIncidente ticketIncidente) {
        ticketService.crearTicket(ticketIncidente);
        return "redirect:/helpdesk";
    }

    // 7. Guardar un Ticket Solicitud
    @PostMapping("/guardar/solicitud")
    public String guardarSolicitud(@ModelAttribute TicketSolicitud ticketSolicitud) {
        ticketService.crearTicket(ticketSolicitud);
        return "redirect:/helpdesk";
    }

    // 8. Aplicar la estrategia seleccionada para asignar técnico a un ticket
    @PostMapping("/asignar/{id}")
    public String estrategiaPorId(@PathVariable int id,
                                  @RequestParam String estrategia,
                                  RedirectAttributes redirectAttributes) {
        ticketService.asignarTecnico(id, estrategia);

        // Mensaje de confirmación con el nombre del técnico asignado.
        ticketService.buscarPorId(id).ifPresent(t -> {
            String nombreTecnico = t.getIdTecnico() == null
                    ? "sin técnico"
                    : usuarioService.buscarPorId(t.getIdTecnico())
                            .map(Usuario::getNombre)
                            .orElse("técnico #" + t.getIdTecnico());
            redirectAttributes.addFlashAttribute("mensaje",
                    "Ticket #" + id + " asignado a " + nombreTecnico + " (" + estrategia + ").");
        });

        return "redirect:/helpdesk/ver/" + id;
    }

    // 9. Mostrar formulario para editar un ticket
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable int id, Model model) {
        Ticket ticket = ticketService.buscarPorId(id).orElse(null);
        if (ticket == null) {
            return "redirect:/helpdesk";
        }
        model.addAttribute("ticket", ticket);
        model.addAttribute("clientes", usuarioService.encontrarPorRol(Rol.CLIENTE));
        model.addAttribute("estados", Estado.values());
        model.addAttribute("prioridades", Prioridad.values());
        return "tickets/formulario-editar";
    }

    // 10. Guardar los cambios de un ticket
    @PostMapping("/actualizar/{id}")
    public String actualizarTicket(@PathVariable int id,
                                   @RequestParam String titulo,
                                   @RequestParam(required = false) String descripcion,
                                   @RequestParam Estado estado,
                                   @RequestParam Prioridad prioridad,
                                   @RequestParam int idCliente,
                                   RedirectAttributes redirectAttributes) {
        ticketService.actualizarTicket(id, titulo, descripcion, estado, prioridad, idCliente);
        redirectAttributes.addFlashAttribute("mensaje", "Ticket #" + id + " actualizado correctamente.");
        return "redirect:/helpdesk/ver/" + id;
    }

    // 11. Eliminar un ticket
    @PostMapping("/eliminar/{id}")
    public String eliminarTicket(@PathVariable int id, RedirectAttributes redirectAttributes) {
        ticketService.eliminarTicket(id);
        redirectAttributes.addFlashAttribute("mensaje", "Ticket #" + id + " eliminado correctamente.");
        return "redirect:/helpdesk";
    }
}