package co.edu.uniquindio.poo.Model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Transaccion {

    private LocalDateTime fechaHora;
    private double monto;
    private String codigoTransaccion;
    private EstadoTransaccion estado;
    private String numeroCuentaOrigen;
    private String numeroCuentaDestino;

    public Transaccion(LocalDateTime fechaHora, String tipoTransaccion, double monto, String codigoTransaccion,
            EstadoTransaccion estado,
            String cuentaOrigen, String cuentaDestino) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto de la transacción debe ser positivo.");
        }
        Objects.requireNonNull(estado, "El tipo de transacción no puede ser nulo.");

        this.fechaHora = LocalDateTime.now();
        this.monto = monto;
        this.codigoTransaccion = generarCodigoTransaccion();
        this.estado = EstadoTransaccion.PENDIENTE;

        if (tipoTransaccion.equalsIgnoreCase("Deposito")) {
            if (cuentaOrigen != null) {
                throw new IllegalArgumentException("Un depósito no debe especificar una cuenta de origen.");
            }

            this.numeroCuentaDestino = Objects.requireNonNull(cuentaDestino,
                    "La cuenta de destino no puede ser nula para un depósito.");
            this.numeroCuentaOrigen = null;

        } else if (tipoTransaccion.equalsIgnoreCase("Retiro")) {
            if (cuentaDestino != null) {
                throw new IllegalArgumentException("Un retiro no debe especificar una cuenta de destino.");
            }
            this.numeroCuentaOrigen = Objects.requireNonNull(cuentaOrigen,
                    "La cuenta de origen no puede ser nula para un retiro.");
            this.numeroCuentaDestino = null;

        } else if (tipoTransaccion.equalsIgnoreCase("Transferencia")) {
            this.numeroCuentaOrigen = Objects.requireNonNull(cuentaOrigen,
                    "La cuenta de origen no puede ser nula para una transferencia.");
            this.numeroCuentaDestino = Objects.requireNonNull(cuentaDestino,
                    "La cuenta de destino no puede ser nula para una transferencia.");
            if (this.numeroCuentaOrigen.equals(this.numeroCuentaDestino)) {
                throw new IllegalArgumentException(
                        "La cuenta de origen y destino no pueden ser la misma para una transferencia.");
            }
            
        } else {
            throw new IllegalArgumentException("Tipo de transacción no válido: " + tipoTransaccion
                    + ". Tipos válidos son: Deposito, Retiro, Transferencia.");
        }
    }

    private String generarCodigoTransaccion() {
        return "TRX-" + System.currentTimeMillis();
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public double getMonto() {
        return monto;
    }

    public String getCodigoTransaccion() {
        return codigoTransaccion;
    }

    public EstadoTransaccion getEstado() {
        return estado;
    }

    public String getNumeroCuentaOrigen() {
        return numeroCuentaOrigen;
    }

    public String getNumeroCuentaDestino() {
        return numeroCuentaDestino;
    }

    public void setEstado(EstadoTransaccion estado) {
        this.estado = Objects.requireNonNull(estado, "El estado de la transacción no puede ser nulo.");
    }
}
