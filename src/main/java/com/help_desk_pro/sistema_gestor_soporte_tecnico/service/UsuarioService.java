package com.help_desk_pro.sistema_gestor_soporte_tecnico.service;

import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Rol;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.Usuario;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.repository.ITicketRepository;
import com.help_desk_pro.sistema_gestor_soporte_tecnico.repository.IUsuarioRepository;
import org.springframework.stereotype.Service;

import java.net.PortUnreachableException;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final IUsuarioRepository iUsuarioRepository;
    private final ITicketRepository ticketRepository;//no se está usando así que piedo eliminarlo
    public UsuarioService(IUsuarioRepository iUsuarioRepository, ITicketRepository ticketRepository) {
        this.iUsuarioRepository = iUsuarioRepository;
        this.ticketRepository = ticketRepository;
    }
    public List<Usuario>ListarTodos(){
        return iUsuarioRepository.findAll();
    }
    public Optional<Usuario>buscarPorId(int id){
        return iUsuarioRepository.findById(id);
    }
    public List<Usuario>encontrarPorRol(Rol rol){
        return iUsuarioRepository.findByRol(rol);
    }
    public void guardar(Usuario u){
        iUsuarioRepository.save(u);
    }
    public void actualizar(Usuario u){
        iUsuarioRepository.update(u);
    }
    public void eliminar(int id){
        iUsuarioRepository.deleteById(id);
    }
}
