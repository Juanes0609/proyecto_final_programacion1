package co.edu.uniquindio.poo.Model;

import java.time.LocalDateTime;

public class CuentaCorriente extends CuentaBancaria {
    private double limiteSobregiro;

    public CuentaCorriente(String numeroCuenta, double saldoInicial, LocalDateTime fechaCreacion, double limiteSobregiro,
            Cliente propietario, double limiteSobreGiro) {
        super(numeroCuenta, saldoInicial, fechaCreacion, propietario);
        if (limiteSobreGiro < 0) {
            throw new IllegalArgumentException("El limite de sobregiro no puede ser negativo.");
        }
        this.limiteSobregiro = limiteSobregiro;
    }

    public double getLimiteSobreGiro() {
        return limiteSobregiro;
    }

    public void setLimiteSobreGiro(double limiteSobregiro) {
        if (limiteSobregiro < 0) {
            throw new IllegalArgumentException("El limite de sobregiro no puede ser negativo.");
        }
        this.limiteSobregiro = limiteSobregiro;
    }

    @Override
    public void depositarDinero(double monto) {
        if (monto < 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        setSaldoCuenta(getSaldoCuenta() + monto);
    }

    @Override
    public void retirarDinero(double monto) throws SaldoInsuficienteException {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser positivo.");
        }
        // Permite retirar si el saldo actual + el límite de sobregiro es suficiente
        if (getSaldoCuenta() + limiteSobregiro < monto) {
            throw new SaldoInsuficienteException(
                    "Límite de sobregiro excedido en la cuenta corriente " + getNumeroCuenta() + ". Saldo actual: $"
                            + getSaldoCuenta() + ", Límite permitido: $" + limiteSobregiro);
        }
        setSaldoCuenta(getSaldoCuenta() - monto);
    }

}