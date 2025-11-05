package co.edu.uniquindio.poo.Model;

import java.time.LocalDateTime;

public class CuentaAhorros extends CuentaBancaria {
    private double tasaInteres;

    public CuentaAhorros(String numeroCuenta, double saldoInicial, LocalDateTime fechaCreacion, Cliente propietario, double tasaInteres) {
        super(numeroCuenta, saldoInicial, fechaCreacion, propietario);
        if (tasaInteres < 0) {
            throw new IllegalArgumentException("La tasa de interés no puede ser negativa.");
        }
        this.tasaInteres = tasaInteres;
    }

    public double getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(double tasaInteres) {
        if (tasaInteres < 0) {
            throw new IllegalArgumentException("La tasa de interés no puede ser negativa.");
        }
        this.tasaInteres = tasaInteres;
    }

    @Override
    public void depositarDinero(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto a depositar debe ser positivo.");
        }
        setSaldoCuenta(getSaldoCuenta() + monto);
    }

    @Override
    public void retirarDinero(double monto) throws SaldoInsuficienteException {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser positivo.");
        }
        if (getSaldoCuenta() < monto) {
            throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta de ahorros " + getNumeroCuenta() + ". Saldo actual: $" + getSaldoCuenta());
        }
        setSaldoCuenta(getSaldoCuenta() - monto);
    }
}
