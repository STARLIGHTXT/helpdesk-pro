package com.help_desk_pro.sistema_gestor_soporte_tecnico.model;

public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private Rol rol;
    //constructor
    public Usuario(){

    }
    public Usuario(int id,String nombre,String email,Rol rol){
        setId(id);
        setNombre(nombre);
        setEmail(email);
        setRol(rol);
    }
    //getters y setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
    //metodos

    @Override
    public String toString() {
        return "Usuario{" +
                "email='" + email + '\'' +
                ", id=" + id +
                ", nombre='" + nombre + '\'' +
                ", rol=" + rol +
                '}';
    }
}
