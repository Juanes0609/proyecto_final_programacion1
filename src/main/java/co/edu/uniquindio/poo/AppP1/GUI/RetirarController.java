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

public class RetirarController {

    @FXML
    private Label numeroCuentaLabel;
    @FXML
    private TextField montoField;

    private CuentaBancaria cuentaCliente;
    private CajeroController cajeroController;

    public void setCuentaCliente(CuentaBancaria cuenta) {
        this.cuentaCliente = cuenta;
        if (cuenta != null) {
            numeroCuentaLabel.setText(cuenta.getNumeroCuenta());
        }
    }

    public void setCajeroController(CajeroController controller) {
        this.cajeroController = controller;
    }

    @FXML
    public void confirmarRetiro(ActionEvent event) {
        double monto = 0.0;
        try {
            monto = Double.parseDouble(montoField.getText()); // Asignar valor dentro del try

            if (monto <= 0) {
                new Alert(Alert.AlertType.ERROR, "El monto a retirar debe ser positivo.").showAndWait();
                return;
            }

            if (cuentaCliente == null) {
                new Alert(Alert.AlertType.ERROR, "No se ha especificado la cuenta para el retiro.").showAndWait();
                return;
            }

            cuentaCliente.retirarDinero(monto); // Llama al método de retiro de la cuenta

            // Crear y registrar la transacción con TU CONSTRUCTOR
            Banco.getInstance().registrarTransaccion(new Transaccion(LocalDateTime.now(), "Retiro", monto, null,
                    EstadoTransaccion.COMPLETADA, cuentaCliente.getNumeroCuenta(), null));

            new Alert(Alert.AlertType.INFORMATION, "Retiro de $" + monto + " realizado con éxito.").showAndWait();
            cerrarVentana();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Monto inválido. Ingrese solo números.").showAndWait();
            // Aquí 'monto' ya es accesible porque fue declarado fuera del try
            // Podrías registrar una transacción fallida si quieres, pero si el monto es
            // inválido,
            // quizás no tiene sentido registrar una transacción de 0 o un valor parcial
            // Banco.getInstance().registrarTransaccion(
            // new Transaccion(
            // LocalDateTime.now(),
            // "Retiro", monto, // Usando 'monto' que puede ser 0 o el valor parcial
            // inválido
            // null,
            // EstadoTransaccion.FALLIDA,
            // cuentaCliente != null ? cuentaCliente.getNumeroCuenta() : null,
            // null));
        } catch (SaldoInsuficienteException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            // Si hay saldo insuficiente, la transacción podría registrarse como FALLIDA
            Banco.getInstance().registrarTransaccion(new Transaccion(LocalDateTime.now(), "Retiro", monto, null,
                    EstadoTransaccion.FALLIDA, cuentaCliente.getNumeroCuenta(), null));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al procesar el retiro: " + e.getMessage()).showAndWait();
            // Registrar como fallida en caso de otros errores
            Banco.getInstance().registrarTransaccion(new Transaccion(LocalDateTime.now(), "Retiro", monto, null,
                    EstadoTransaccion.FALLIDA, cuentaCliente.getNumeroCuenta(), null));
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