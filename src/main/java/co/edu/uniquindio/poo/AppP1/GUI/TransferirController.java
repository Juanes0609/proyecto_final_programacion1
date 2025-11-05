package co.edu.uniquindio.poo.AppP1.GUI;

import co.edu.uniquindio.poo.Model.Banco;
import co.edu.uniquindio.poo.Model.CuentaBancaria;
import co.edu.uniquindio.poo.Model.EstadoTransaccion; // Importar EstadoTransaccion
import co.edu.uniquindio.poo.Model.SaldoInsuficienteException;
import co.edu.uniquindio.poo.Model.Transaccion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class TransferirController {

    @FXML private Label numeroCuentaOrigenLabel;
    @FXML private TextField cuentaDestinoField;
    @FXML private TextField montoField;

    private CuentaBancaria cuentaOrigen;
    private CajeroController cajeroController;
    private ClienteController clienteController;

    public void setCuentaOrigen(CuentaBancaria cuenta) {
        this.cuentaOrigen = cuenta;
        if (cuenta != null) {
            numeroCuentaOrigenLabel.setText(cuenta.getNumeroCuenta());
        }
    }

    public void setCajeroController(CajeroController controller) {
        this.cajeroController = controller;
    }

    public void setClienteController(ClienteController controller) {
    this.clienteController = controller;
}


    @FXML
    public void confirmarTransferencia(ActionEvent event) {
        Transaccion transaccion = null; // Declarar aquí para que sea accesible en el catch
        try {
            String numeroCuentaDestino = cuentaDestinoField.getText();
            double monto = Double.parseDouble(montoField.getText());

            if (monto <= 0) {
                new Alert(Alert.AlertType.ERROR, "El monto a transferir debe ser positivo.").showAndWait();
                return;
            }
            if (numeroCuentaDestino == null || numeroCuentaDestino.trim().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Ingrese el número de cuenta de destino.").showAndWait();
                return;
            }
            if (cuentaOrigen == null) {
                new Alert(Alert.AlertType.ERROR, "No se ha especificado la cuenta de origen para la transferencia.").showAndWait();
                return;
            }
            if (cuentaOrigen.getNumeroCuenta().equals(numeroCuentaDestino)) {
                new Alert(Alert.AlertType.ERROR, "No puede transferir dinero a la misma cuenta de origen.").showAndWait();
                return;
            }

            Banco banco = Banco.getInstance();
            CuentaBancaria cuentaDestino = banco.buscarCuentaPorNumero(numeroCuentaDestino);

            if (cuentaDestino == null) {
                new Alert(Alert.AlertType.ERROR, "La cuenta de destino " + numeroCuentaDestino + " no existe.").showAndWait();
                return;
            }

            // Crear la transacción antes de intentar la operación para tener un ID inicial
            transaccion = new Transaccion(
                LocalDateTime.now(),
                "Transferencia",
                monto,
                null, // codigoTransaccion (se genera internamente)
                EstadoTransaccion.PENDIENTE, // Inicia como pendiente
                cuentaOrigen.getNumeroCuenta(),
                cuentaDestino.getNumeroCuenta()
            );
            Banco.getInstance().registrarTransaccion(transaccion); // Registrar la transacción inicialmente

            // Realizar la transferencia (retira de origen, deposita en destino)
            cuentaOrigen.retirarDinero(monto);
            cuentaDestino.depositarDinero(monto);

            // Si todo fue bien, cambiar el estado a COMPLETADA
            transaccion.setEstado(EstadoTransaccion.COMPLETADA);

            new Alert(Alert.AlertType.INFORMATION, "Transferencia de $" + monto + " a la cuenta " + numeroCuentaDestino + " realizada con éxito.").showAndWait();
            cerrarVentana();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Monto inválido. Ingrese solo números.").showAndWait();
            if (transaccion != null) {
                transaccion.setEstado(EstadoTransaccion.FALLIDA);
            }
        } catch (SaldoInsuficienteException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            if (transaccion != null) {
                transaccion.setEstado(EstadoTransaccion.FALLIDA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al procesar la transferencia: " + e.getMessage()).showAndWait();
            if (transaccion != null) {
                transaccion.setEstado(EstadoTransaccion.FALLIDA);
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