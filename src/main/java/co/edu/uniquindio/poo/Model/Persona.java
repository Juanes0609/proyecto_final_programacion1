package co.edu.uniquindio.poo.Model;

import java.util.Objects;

public abstract class Persona {
    private String nombre;
    private String identificacion;
    private String direccion;
    private String telefono;

    public Persona(String nombre, String identificacion, String direccion, String telefono) {
        // Validaciones en el constructor
        this.nombre = Objects.requireNonNull(nombre, "El nombre de la persona no puede ser nulo.");
        this.identificacion = Objects.requireNonNull(identificacion,
                "La identificación de la persona no puede ser nula.");
        this.direccion = Objects.requireNonNull(direccion, "La dirección de la persona no puede ser nula.");
        this.telefono = Objects.requireNonNull(telefono, "El teléfono de la persona no puede ser nulo.");
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setNombre(String nombre) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo.");
        this.nombre = nombre;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = Objects.requireNonNull(identificacion, "La identificación no puede ser nula.");
        this.identificacion = identificacion;
    }

    public void setDireccion(String direccion) {
        this.direccion = Objects.requireNonNull(direccion, "La dirección no puede ser nula.");
        this.direccion = direccion;
    }

    public void setTelefono(String telefono) {
        this.telefono = Objects.requireNonNull(telefono, "El teléfono no puede ser nulo.");
        this.telefono = telefono;
    }

    public abstract String obtenerDetalles();

}