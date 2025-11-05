package co.edu.uniquindio.poo.Model;

public class Cajero extends Empleado {
    public Cajero(String nombre, String identificacion, String direccion, String telefono, String idEmpleado, double salario, String pin) {
        super(nombre, identificacion, direccion, telefono, idEmpleado, salario, pin);
    }

    @Override
    public String obtenerDetalles() {
        return "Cajero: " + getNombre() + ", ID Empleado: " + getIdEmpleado() + ", Identificación: " + getIdentificacion();
    }
}