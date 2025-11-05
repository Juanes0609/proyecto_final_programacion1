package co.edu.uniquindio.poo.Model;

import java.time.LocalDateTime;
import java.util.Objects;

public class CuentaEmpresarial extends CuentaBancaria {
    private String nombreEmpresa;
    private String nit;
    private int transaccionesLibres;

    public CuentaEmpresarial(String numeroCuenta, double saldoInicial, LocalDateTime fechaCreacion, Cliente propietario,
            String nombreEmpresa, String nit, int transaccionesLibres) {
        super(numeroCuenta, saldoInicial, fechaCreacion, propietario);

        if (transaccionesLibres < 0) {
            throw new IllegalArgumentException("El número de transacciones libres no puede ser negativo.");
        }

        this.nombreEmpresa = nombreEmpresa;
        this.nit = Objects.requireNonNull(nit, "El NIT no puede ser nulo");
        this.transaccionesLibres = transaccionesLibres;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = Objects.requireNonNull(nombreEmpresa, "El nombre de la empresa no puede ser nulo.");
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = Objects.requireNonNull(nit, "El NIT no puede ser nulo");
    }

    public int getTransaccionesLibres() {
        return transaccionesLibres;
    }

    public void setTransaccionesLibres(int transaccionesLibres) {
        if (transaccionesLibres < 0) {
            throw new IllegalArgumentException("El número de transacciones libres no puede ser negativo.");
        }
        this.transaccionesLibres = transaccionesLibres;
    }

    public void depositarDinero(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto a depositar debe ser positivo.");
        }

        setSaldoCuenta(getSaldoCuenta() + monto);
    }

    public void retirarDinero(double monto) throws SaldoInsuficienteException {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser positivo.");
        }

        if (getSaldoCuenta() < monto) {
            throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta empresarial " + getNumeroCuenta()
                    + ". Saldo actual: $" + getSaldoCuenta());
        }
        setSaldoCuenta(getSaldoCuenta() - monto);
    }
}