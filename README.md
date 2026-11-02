# 🛠️ Sistema Gestor de Soporte Técnico (HelpDesk Pro)

Aplicación web desarrollada en **Spring Boot** siguiendo los principios de la arquitectura MVC 
y patrones de diseño orientados a objetos. Diseñada para la gestión integral de incidencias y solicitudes de 
soporte técnico, con persistencia basada en **PostgreSQL** y **JdbcTemplate** nativo 
(sin frameworks ORM pesados como JPA/Hibernate).

---

## Características Principales
- **Arquitectura en Capas Tradicional**: Separación estricta entre Model, Repository, Service y Controller.
- **Persistencia SQL Nativa**: Uso de `JdbcTemplate`, `RowMapper` personalizados y `KeyHolder` para la gestión segura de llaves auto-generadas.
- **Polimorfismo y Herencia**: Jerarquía de clases para diferentes tipos de tickets (*Incidentes* y *Solicitudes*).
- **Patrón Estrategia (Strategy Pattern)**: Inyección dinámica de algoritmos de asignación automática de técnicos (`List<AsignadorTecnico>`).
- **Consultas Funcionales**: Procesamiento de datos mediante Java Streams (`Collectors.groupingBy`, promedios, filtros y ordenamientos).
- **Interfaz Web Dinámica**: Renderizado de vistas del lado del servidor utilizando **Thymeleaf**.

---

##  Tecnologías Utilizadas
- **Java 17 / 21**
- **Spring Boot** (MVC)
- **Spring JDBC / JdbcTemplate**
- **PostgreSQL**
- **Thymeleaf**
- **Maven** (Gestión de dependencias)

---

## ⚙  Configuración y Ejecución Local

1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/STARLIGHTXT/helpdesk-pro.git](https://github.com/STARLIGHTXT/helpdesk-pro.git)