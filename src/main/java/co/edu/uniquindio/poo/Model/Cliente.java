package co.edu.uniquindio.poo.Model;

import java.util.ArrayList;
import java.util.Objects;

public class Cliente extends Persona {
    private String numeroCuenta;
    private String pin;
    private ArrayList<CuentaBancaria> listaCuentas = new ArrayList<>();

    public Cliente(String nombre, String identificacion, String direccion, String telefono, String numeroCuenta, String pin) {
        super(nombre, identificacion, direccion, telefono);
        this.numeroCuenta = numeroCuenta;
        this.pin = pin;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = Objects.requireNonNull(numeroCuenta, "El numero de cuenta no puede ser nula");
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public ArrayList<CuentaBancaria> getListaCuentas() {
        return listaCuentas;
    }

    public void setListaCuentas(ArrayList<CuentaBancaria> listaCuentas) {
        this.listaCuentas = listaCuentas;
    }

    @Override
    public String obtenerDetalles() {
        return "Cliente: " + getNombre() + ", Identificación: " + getIdentificacion() + ", Cuentas: "
                + listaCuentas.size();
    }

    public void agregarCuenta(CuentaBancaria cuenta) {
        Objects.requireNonNull(cuenta, "La cuenta a agregar no puede ser nula.");

        for (CuentaBancaria cuentaBancaria : listaCuentas) {
            if (cuentaBancaria.getNumeroCuenta().equals(cuenta.getNumeroCuenta())) {
                throw new IllegalArgumentException(
                        "La cuenta " + cuenta.getNumeroCuenta() + " ya está asociada a este cliente.");
            }
        }
        listaCuentas.add(cuenta);
    }
    
    public CuentaBancaria buscarCuenta() {
        for (CuentaBancaria cuenta : listaCuentas) {
            if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                return cuenta;
            }
        }
        return null;
    }

    public void eliminarCuenta(String numeroCuenta) {
        Objects.requireNonNull(numeroCuenta, "El número de cuenta no puede ser nulo para la eliminación.");
        CuentaBancaria cuentaAEliminar = null;
        for (CuentaBancaria cuenta : listaCuentas) {
            if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                cuentaAEliminar = cuenta;
                break;
            }
        }

        if (cuentaAEliminar != null) {
            listaCuentas.remove(cuentaAEliminar);
        } else {
            throw new IllegalArgumentException("La cuenta con número " + numeroCuenta
                    + " no se encontró en la lista de cuentas del cliente " + this.getNombre() + ".");
        }
    }
}
