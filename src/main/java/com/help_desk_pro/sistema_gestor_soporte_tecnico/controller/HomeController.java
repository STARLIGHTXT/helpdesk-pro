package com.help_desk_pro.sistema_gestor_soporte_tecnico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // La raíz redirige al listado principal del help desk
    @GetMapping("/")
    public String inicio() {
        return "redirect:/helpdesk";
    }
}
