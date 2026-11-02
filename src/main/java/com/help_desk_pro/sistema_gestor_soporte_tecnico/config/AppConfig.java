package com.help_desk_pro.sistema_gestor_soporte_tecnico.config;

import com.help_desk_pro.sistema_gestor_soporte_tecnico.model.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import java.time.LocalDateTime;

@Configuration
public class AppConfig {
    //rs o ResultSet acua como un traductor de SQL a java , es decir , rs busca en este caso
    //el nombre de la taba ingresado  directamente de la base de datos y lo ingresa directamente
    //en el campo que se desee por iteracion , por ejemplo: se tienen 3 usuarios en una base de datos
    //el usuario  tiene los atributos id,nombre,email, por lo que el row mapper tendrá qeu mapear esos
    //atributos de la tabla SQL a un objeto en java , lo que se hace es generar un objeto nuevo
    // e ir bindando atributo por atributo y auqnue en este caso en especifico , este metodo no interactua directamente con la
    //base de datos , lo que se hace es llamar al metodo en otra capa donde esté instanciado jdbcTemplate
    //ya que este puede interactuar directamente con la base de datos , es decir , debe de haber un metodo
    //que ocupe jdbcTemplate.query() en donde como primer parametro se introduce la consulta y luego se introduce
    //la traduccion (usuarioRowMapper) , la traduccion se encarga de tomar los datos de las columns de la tabla de la base de datos
    //y bindearlo a un objeto java  por iteracion (rowNum)
    @Bean
    public RowMapper<Usuario>usuarioRowMapper(){
        return ((rs, rowNum) -> {//regresar un objeto Usuario producto de la lambda pero como producto que entrega el bean
            Usuario usuario=new Usuario();//declaracion del objeto para el binding
            usuario.setId(rs.getInt("id"));
            usuario.setNombre(rs.getString("nombre"));
            usuario.setEmail(rs.getString("email"));
            usuario.setRol(Rol.valueOf(rs.getString("rol")));
            return usuario;//regresa el resultado de la funcion lambda al metodo
        });
    }

    //hay que tener en cuenta que hay 2 tipos de tickets diferentes
    //conviene hacer el binding por separado , los dos tipos de tickets cargan la misma informacion
    //la estrategia es  asociar atributos de ticket a sus respectivas traducciones , luego
    //extraer el tipo de ticket y a partir de ahí crear el objeto correspondiente
    //por ultimo hacer el binding con el objeto creado
    @Bean
    public RowMapper<Ticket>ticketRowMapper(){
        return ((rs, rowNum) -> {
            int id=rs.getInt("id");
            String titulo=rs.getString("titulo");
            String descripcion=rs.getString("descripcion");

            // La columna es TIMESTAMP: se conserva la hora del ticket
            LocalDateTime fecha_creacion=rs.getTimestamp("fecha_creacion").toLocalDateTime();
            Estado estado= Estado.valueOf(rs.getString("estado"));
            Prioridad prioridad=Prioridad.valueOf(rs.getString("prioridad"));
            int id_cliente=rs.getInt("id_cliente");
            // getObject devuelve null cuando la columna es NULL (getInt devolvería 0)
            Integer id_tecnico=rs.getObject("id_tecnico", Integer.class);

            TipoTicket tipo=TipoTicket.valueOf(rs.getString("tipo"));

            Ticket ticket = null;
            if (tipo == TipoTicket.INCIDENTE){
                 ticket=new TicketIncidente(descripcion,estado,fecha_creacion,id,id_cliente,id_tecnico,prioridad,titulo);
            } else if (tipo == TipoTicket.SOLICITUD) {
                 ticket=new TicketSolicitud(descripcion,estado,fecha_creacion,id,id_cliente,id_tecnico,prioridad,titulo);
            }
            return ticket;
        });
    }
}
