package co.edu.uniquindio.poo.AppP1.GUI;

import co.edu.uniquindio.poo.Model.Banco;
import co.edu.uniquindio.poo.Model.CuentaBancaria;
import co.edu.uniquindio.poo.Model.EstadoTransaccion; // Importar EstadoTransaccion
import co.edu.uniquindio.poo.Model.SaldoInsuficienteException;
import co.edu.uniquindio.poo.Model.Transaccion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.function.Consumer;

public class TransferirController {

    private boolean pagoExitoso = false;

    @FXML
    private Label numeroCuentaOrigenLabel;
    @FXML
    private TextField cuentaDestinoField;
    @FXML
    private TextField montoField;

    private CuentaBancaria cuentaOrigen;
    private CajeroController cajeroController;
    private ClienteController clienteController;

    private CuentaBancaria cuentaDestinoEmpresa;
    private Consumer<Boolean> onTransferComplete;

    public void setCuentaOrigen(CuentaBancaria cuenta) {
        this.cuentaOrigen = cuenta;
        if (cuenta != null) {
            numeroCuentaOrigenLabel.setText(cuenta.getNumeroCuenta());
        }
    }

    public void setCuentaDestino(CuentaBancaria cuenta) {
        this.cuentaDestinoEmpresa = cuenta;
        if (cuentaDestinoField != null) {
            cuentaDestinoField.setText(cuenta.getNumeroCuenta());
            cuentaDestinoField.setEditable(false);
        }
    }

    public void setOnTransferComplete(Consumer<Boolean> action) {
        this.onTransferComplete = action;
    }

    public void setCajeroController(CajeroController controller) {
        this.cajeroController = controller;
    }

    public void setClienteController(ClienteController controller) {
        this.clienteController = controller;
    }

    public boolean isPagoExitoso() {
        return pagoExitoso;
    }

    @FXML
    public void confirmarTransferencia(ActionEvent event) {
        Transaccion transaccion = null;

        try {
            double monto = Double.parseDouble(this.montoField.getText());
            if (monto <= 0.0) {
                new Alert(AlertType.ERROR, "El monto debe ser positivo.").showAndWait();
                return;
            }

            if (this.cuentaDestinoEmpresa == null) {
                new Alert(AlertType.ERROR, "La cuenta de destino de la empresa no está configurada.").showAndWait();
                return;
            }

            CuentaBancaria cuentaDestino = this.cuentaDestinoEmpresa;
            String numeroCuentaDestino = cuentaDestino.getNumeroCuenta();

            if (numeroCuentaDestino == null || numeroCuentaDestino.trim().isEmpty()) {
                new Alert(AlertType.ERROR, "Ingrese el número de cuenta de destino.").showAndWait();
                return;
            }

            if (this.cuentaOrigen == null) {
                new Alert(AlertType.ERROR, "Cuenta de origen no definida.").showAndWait();
                return;
            }

            if (this.cuentaOrigen.getNumeroCuenta().equals(numeroCuentaDestino)) {
                new Alert(AlertType.ERROR, "No puede transferir a la misma cuenta.").showAndWait();
                return;
            }

            transaccion = new Transaccion(LocalDateTime.now(), "Transferencia", monto, null,
                    EstadoTransaccion.PENDIENTE,
                    this.cuentaOrigen.getNumeroCuenta(),
                    cuentaDestino.getNumeroCuenta());

            Banco.getInstance().registrarTransaccion(transaccion);

            this.cuentaOrigen.retirarDinero(monto);
            cuentaDestino.depositarDinero(monto);
            transaccion.setEstado(EstadoTransaccion.COMPLETADA);

            if (this.onTransferComplete != null) {
                this.onTransferComplete.accept(true);
            }

            new Alert(AlertType.INFORMATION,
                    "Transferencia realizada con éxito.").showAndWait();

            this.cerrarVentana();

        } catch (NumberFormatException e) {
            new Alert(AlertType.ERROR, "Monto inválido.").showAndWait();
            if (transaccion != null)
                transaccion.setEstado(EstadoTransaccion.FALLIDA);

            if (this.onTransferComplete != null) {
                this.onTransferComplete.accept(false);
            }

        } catch (SaldoInsuficienteException e) {
            new Alert(AlertType.ERROR, e.getMessage()).showAndWait();
            if (transaccion != null)
                transaccion.setEstado(EstadoTransaccion.FALLIDA);

            if (this.onTransferComplete != null) {
                this.onTransferComplete.accept(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(AlertType.ERROR, "Error inesperado: " + e.getMessage()).showAndWait();
            if (transaccion != null)
                transaccion.setEstado(EstadoTransaccion.FALLIDA);
                
            if (this.onTransferComplete != null) {
                this.onTransferComplete.accept(false);
            }
        }
    }

    @FXML
    public void cancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) montoField.getScene().getWindow();
        stage.close();
    }
}