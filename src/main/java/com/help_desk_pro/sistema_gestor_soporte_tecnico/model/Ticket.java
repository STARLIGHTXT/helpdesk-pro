package com.help_desk_pro.sistema_gestor_soporte_tecnico.model;

import java.time.LocalDateTime;

public abstract class Ticket {
    private int id;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private Estado estado;
    private TipoTicket tipoTicket;
    private Prioridad prioridad;
    private int idCliente;
    private Integer idTecnico;
    //constructor
    public Ticket(){


    }
    public Ticket(String descripcion, Estado estado,LocalDateTime fechaCreacion, int id, int idCliente, Integer idTecnico, Prioridad prioridad, String titulo, TipoTicket tipoTicket) {
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.id = id;
        this.idCliente = idCliente;
        this.idTecnico = idTecnico;
        this.prioridad = prioridad;
        this.titulo = titulo;
        this.tipoTicket = tipoTicket;
    }
    //getters y setters

    public TipoTicket getTipoTicket() {
        return tipoTicket;
    }

    public void setTipoTicket(TipoTicket tipoTicket) {
        this.tipoTicket = tipoTicket;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(Integer idTecnico) {
        this.idTecnico = idTecnico;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    //metodos
    public abstract double calcularTiempoEstimadoResolucion();
    public boolean estaAbierto(){
        return getEstado() == Estado.ABIERTO;
    }

}
