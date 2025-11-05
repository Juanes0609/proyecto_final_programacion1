package co.edu.uniquindio.poo.AppP1.GUI;

import co.edu.uniquindio.poo.Model.Banco;
import co.edu.uniquindio.poo.Model.CuentaBancaria;
import co.edu.uniquindio.poo.Model.EstadoTransaccion; // Importar EstadoTransaccion
import co.edu.uniquindio.poo.Model.Transaccion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class DepositarController {

    @FXML
    private TextField numeroCuentaDestinoField;
    @FXML
    private Label numeroCuentaOrigenLabel;
    @FXML
    private TextField montoField;

    private CuentaBancaria cuentaCliente;
    private CuentaBancaria cuentaDestino; // Esta variable se puede usar si necesitas una referencia a la cuenta destino
    private CajeroController cajeroController;

    public void setCuentaCliente(CuentaBancaria cuenta) {
        
        if (cuenta == null) {
            new Alert(Alert.AlertType.ERROR, "Ingrese el número de cuenta de destino.").showAndWait();
            return;
        }
        this.cuentaCliente = cuenta;
        if (cuenta != null) {
            numeroCuentaOrigenLabel.setText(cuenta.getNumeroCuenta());
        }
    }

    public void setCuentaDestino(CuentaBancaria cuenta) {

        this.cuentaDestino = cuenta;
        if (cuenta != null) {
            numeroCuentaDestinoField.setText(cuenta.getNumeroCuenta());
        }
    }

    public void setCajeroController(CajeroController controller) {
        this.cajeroController = controller;
    }

    @FXML
    public void confirmarDeposito(ActionEvent event) {
        String numeroCuentaDestino = numeroCuentaDestinoField.getText();
        CuentaBancaria cuentaDestino = Banco.getInstance().buscarCuentaPorNumero(numeroCuentaDestino);
        if (numeroCuentaDestino == null || numeroCuentaDestino.trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Ingrese el número de cuenta de destino.").showAndWait();
            return;
        }
        try {
            double monto = Double.parseDouble(montoField.getText());

            if (monto <= 0) {
                new Alert(Alert.AlertType.ERROR, "El monto a depositar debe ser positivo.").showAndWait();
                return;
            }

            if (cuentaDestino == null) {
                new Alert(Alert.AlertType.ERROR, "No se ha especificado la cuenta para el depósito.").showAndWait();
                return;
            }

            if(cuentaCliente.getSaldoCuenta() >= monto){
                cuentaDestino.depositarDinero(monto); // Llama al método de depósito de la cuenta
                cuentaCliente.setSaldoCuenta(cuentaCliente.getSaldoCuenta() - monto); // Actualiza el saldo de la cuenta cliente
            }else{
                new Alert(Alert.AlertType.ERROR, "No alcanza el saldo").showAndWait();
                return;
            }
            

            // Crear y registrar la transacción con TU CONSTRUCTOR
            Banco.getInstance().registrarTransaccion(
                    new Transaccion(
                            LocalDateTime.now(), // fechaHora
                            "Deposito", // tipoTransaccion
                            monto, // monto
                            null, // codigoTransaccion (se genera internamente en tu Transaccion)
                            EstadoTransaccion.COMPLETADA, // estado (suponemos exitoso)
                            null, // cuentaOrigen (null para depósito)
                            cuentaDestino.getNumeroCuenta() // cuentaDestino
                    ));

            new Alert(Alert.AlertType.INFORMATION, "Depósito de $" + monto + " realizado con éxito.").showAndWait();
            cerrarVentana();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Monto inválido. Ingrese solo números.").showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al procesar el depósito: " + e.getMessage()).showAndWait();
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