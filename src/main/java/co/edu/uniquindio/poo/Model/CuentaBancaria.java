package co.edu.uniquindio.poo.Model;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class CuentaBancaria {
    private String numeroCuenta;
    private double saldoCuenta;
    private LocalDateTime fechaCreacion;
    private Cliente propietario;

    public CuentaBancaria(String numeroCuenta, double saldoInicial, LocalDateTime fechaCreacion, Cliente propietario) {
        this.numeroCuenta = Objects.requireNonNull(numeroCuenta, "El número de cuenta no puede ser nulo.");
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("El saldo inicial no puede ser negativo.");
        }
        this.saldoCuenta = saldoInicial;
        this.fechaCreacion = LocalDateTime.now();
        this.propietario = Objects.requireNonNull(propietario, "El propietario de la cuenta no puede ser nulo.");
    
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public double getSaldoCuenta() {
        return saldoCuenta;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Cliente getPropietario() {
        return propietario;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = Objects.requireNonNull(numeroCuenta, "El número de cuenta no puede ser nulo.");
        this.numeroCuenta = numeroCuenta;
    }

    public void setSaldoCuenta(double saldoCuenta) {
        this.saldoCuenta = saldoCuenta;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = LocalDateTime.now();
    }

    public void setPropietario(Cliente propietario) {
        this.propietario = Objects.requireNonNull(propietario, "El propietario no puede ser nulo.");
        this.propietario = propietario;
    }

    public abstract void depositarDinero(double monto);

    public abstract void retirarDinero(double monto) throws SaldoInsuficienteException;
}
