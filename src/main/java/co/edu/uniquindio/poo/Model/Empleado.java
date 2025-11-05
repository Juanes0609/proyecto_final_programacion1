package co.edu.uniquindio.poo.Model;

public abstract class Empleado extends Persona {
    private String idEmpleado;
    private double salario;
    private String pin;

    public Empleado(String nombre, String identificacion, String direccion, String telefono, String idEmpleado,
            double salario, String pin) {
        super(nombre, identificacion, direccion, telefono);
        this.idEmpleado = idEmpleado;
        this.salario = salario;
        this.pin = pin;

    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String obtenerDetalles() {
        return "Empleado: " + getNombre() + ", ID: " + getIdEmpleado() + ", Identificación: " + getIdentificacion();
    }
}
