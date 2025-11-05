// Archivo: Banco.java (modificado con datos quemados)
package co.edu.uniquindio.poo.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Banco {
    private String nombre;
    private String direccion;
    private String nit;
    private List<CuentaBancaria> cuentasBancarias;
    private List<Empleado> empleados;
    private List<Cliente> clientes;
    private List<Transaccion> transacciones;

    private static Banco instancia = new Banco();

    public static Banco getInstance() {
        return instancia;
    }

    private Banco() {
        cuentasBancarias = new ArrayList<>();
        empleados = new ArrayList<>();
        clientes = new ArrayList<>();
        this.transacciones = new ArrayList<>();

        // Datos "quemados"
        Cliente cliente = new Cliente("Carlos Pérez", "1234", "Calle 1", "3001234567", "ACC00000001", "1234");
        Administrador admin = new Administrador("Ana Admin", "admin1", "Oficina 1", "3000000000", "EMP001", 5000000,
                "adminpass");
        Cajero cajero = new Cajero("Luis Cajero", "cajero1", "Sucursal 2", "3000001111", "EMP002", 2500000,
                "cajeropass");

        Administrador admin1 =  new Administrador("Jose e", "10909", "direccion", "310293021", "0239024", 25000000, "123456");

        CuentaBancaria cuenta = new CuentaAhorros("ACC00000001", 1000000.0, LocalDateTime.now(), cliente, 0.05);
        CuentaBancaria cuenta2 = new CuentaAhorros("ACC00043444", 500000.0, LocalDateTime.now(), cliente, 0.05);


        cuentasBancarias.add(cuenta);
        cliente.agregarCuenta(cuenta);
        cuentasBancarias.add(cuenta2);
        cliente.agregarCuenta(cuenta2);



        clientes.add(cliente);
        empleados.add(admin);
        empleados.add(cajero);
        empleados.add(admin);
    }

    public void registrarTransaccion(Transaccion transaccion) {
        Objects.requireNonNull(transaccion, "La transacción a registrar no puede ser nula.");
        this.transacciones.add(transaccion);
        System.out.println("Transacción registrada: " + transaccion.getCodigoTransaccion() + " - Monto: "
                + transaccion.getMonto());
    }

    public List<Transaccion> getTransacciones() {
        return new ArrayList<>(transacciones);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public List<CuentaBancaria> getCuentasBancarias() {
        return cuentasBancarias;
    }

    public void setCuentasBancarias(List<CuentaBancaria> cuentasBancarias) {
        this.cuentasBancarias = cuentasBancarias;
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public void setTransacciones(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }

    public static Banco getInstancia() {
        return instancia;
    }

    public static void setInstancia(Banco instancia) {
        Banco.instancia = instancia;
    }

    public void depositarDinero(String numeroCuentaDestino, String numeroCuentaOrigen, double monto) {
        for (CuentaBancaria cuentaOrigen : cuentasBancarias) {
            if (cuentaOrigen.getNumeroCuenta().equals(numeroCuentaOrigen)) {
                if (cuentaOrigen.getSaldoCuenta() >= monto) {
                    for (CuentaBancaria cuenta : cuentasBancarias) {
                        if (cuenta.getNumeroCuenta().equals(numeroCuentaDestino)) {
                            cuenta.setSaldoCuenta(cuenta.getSaldoCuenta() + monto);
                        }
                    }
                    cuentaOrigen.setSaldoCuenta(cuentaOrigen.getSaldoCuenta() - monto);
                    System.out.println("Se ha depositado " + monto + " a la cuenta " + numeroCuentaDestino);
                } else {
                    System.out.println("No hay suficiente saldo en la cuenta de origen");
                }
                break;
            }
        }
    }

    public void registrarCliente(String nombre, String identificacion, String direccion, String telefono,
            String numeroCuenta,
            String pin) {
        Cliente nuevoCliente = new Cliente(nombre, identificacion, direccion, telefono, numeroCuenta, pin);
        this.clientes.add(nuevoCliente);
        System.out.println("Cliente " + nombre + " registrado.");
    }

    public void registrarCajero(String nombre, String identificacion, String direccion, String telefono,
            String idEmpleado, double salario, String pin) {
        Cajero nuevoCajero = new Cajero(nombre, identificacion, direccion, telefono, idEmpleado, salario, pin);
        this.empleados.add(nuevoCajero);
        System.out.println("Cajero " + nombre + " registrado.");
    }

    public void registrarAdministrador(String nombre, String identificacion, String direccion, String telefono,
            String idEmpleado, double salario, String pin) {
        Administrador nuevoAdministrador = new Administrador(nombre, identificacion, direccion, telefono,
                idEmpleado, salario, pin);
        this.empleados.add(nuevoAdministrador);
        System.out.println("Administrador " + nombre + " registrado.");
    }

    public Cliente autenticarCliente(String identificacion, String pin) {
        for (Cliente cliente : clientes) {
            if (cliente.getIdentificacion().equals(identificacion) && cliente.getPin().equals(pin)) {
                return cliente;
            }
        }
        return null;
    }

    public Empleado autenticarEmpleado(String identificacion, String pin) {
        for (Empleado empleado : empleados) {
            if (empleado.getIdentificacion().equals(identificacion) && empleado.getPin().equals(pin)) {
                return empleado;
            }
        }
        return null;
    }

    public Cliente buscarCliente(String identificacion) {
        for (Cliente cliente : clientes) {
            if (cliente.getIdentificacion().equals(identificacion)) {
                return cliente;
            }
        }
        return null;
    }

    public Empleado buscarEmpleado(String identificacion) {
        for (Empleado empleado : empleados) {
            if (empleado.getIdentificacion().equals(identificacion)) {
                return empleado;
            }
        }
        return null;
    }

    public void registrarCuenta(CuentaBancaria cuenta) {
        this.cuentasBancarias.add(cuenta);
        System.out.println("Cuenta bancaria " + cuenta.getNumeroCuenta() + " registrada.");
    }

    public CuentaBancaria buscarCuentaPorNumero(String numeroCuenta) {
        for (CuentaBancaria cuenta : cuentasBancarias) {
            if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                return cuenta;
            }
        }
        return null;
    }

    public String generarNumeroCuentaUnico() {
        return "ACC" + String.format("%08d", cuentasBancarias.size() + 1);
    }

    public Object autenticarUsuario(String identificacion, String pin, String rol) {
        switch (rol) {
            case "Cliente":
                return autenticarCliente(identificacion, pin);
            case "Cajero":
            case "Administrador":
                return autenticarEmpleado(identificacion, pin);
            default:
                return null;
        }
    }
}
