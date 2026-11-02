package com.help_desk_pro.sistema_gestor_soporte_tecnico.controller;

import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Rol;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Usuario;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.service.TicketService;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.service.UsuarioService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    private final TicketService ticketService;
    private final UsuarioService usuarioService;

    public UsuarioController(TicketService ticketService, UsuarioService usuarioService) {
        this.ticketService = ticketService;
        this.usuarioService = usuarioService;
    }
    @GetMapping
    public String muestraUsuarios(Model moddel){
        moddel.addAttribute("usuarios",usuarioService.ListarTodos());
        return "usuarios/index";
    }
    @GetMapping("/nuevo")
    public String mostrarFormUsuario(Model model){
        model.addAttribute("usuario",new Usuario());
        model.addAttribute("roles", Rol.values());
        return "tickets/formulario-usuario";
    }
    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario){
        usuarioService.guardar(usuario);
        return "redirect:/usuarios";
    }

    // Editar un usuario existente
    @GetMapping("/editar/{id}")
    public String mostrarFormEditar(@PathVariable int id, Model model){
        Usuario usuario = usuarioService.buscarPorId(id).orElse(null);
        if (usuario == null) {
            return "redirect:/usuarios";
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", Rol.values());
        return "usuarios/formulario-editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable int id,
                                    @ModelAttribute Usuario usuario,
                                    RedirectAttributes redirectAttributes){
        usuario.setId(id);
        try {
            usuarioService.actualizar(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado correctamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "No se pudo actualizar: el correo ya está registrado por otro usuario.");
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable int id, RedirectAttributes redirectAttributes){
        try {
            usuarioService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado correctamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "No se puede eliminar: el usuario tiene tickets asociados. " +
                    "Reasigna o elimina esos tickets primero.");
        }
        return "redirect:/usuarios";
    }
}
