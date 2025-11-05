package co.edu.uniquindio.poo.AppP1.GUI;

import co.edu.uniquindio.poo.Model.Cliente;
import co.edu.uniquindio.poo.Model.CuentaBancaria;
import co.edu.uniquindio.poo.Model.Banco;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.text.DecimalFormat;

public class ClienteController {

    @FXML private Label nombreClienteLabel;
    @FXML private Label saldoActualLabel;
    @FXML private ComboBox<String> cuentasComboBox;

    private Cliente clienteAutenticado;
    private CuentaBancaria cuentaSeleccionada;


    public void setClienteAutenticado(Cliente cliente) {
        this.clienteAutenticado = cliente;
        if (cliente != null) {
            nombreClienteLabel.setText(cliente.getNombre());

            if (!cliente.getListaCuentas().isEmpty()) {
                for (CuentaBancaria cuenta : cliente.getListaCuentas()) {
                    cuentasComboBox.getItems().add(cuenta.getNumeroCuenta());
                }
                cuentasComboBox.getSelectionModel().selectFirst();
                actualizarCuentaSeleccionada();
            } else {
                saldoActualLabel.setText("$ 0.00 (Sin cuenta)");
            }
        }
    }


    @FXML
    public void actualizarCuentaSeleccionada() {
        String numeroSeleccionado = cuentasComboBox.getValue();
        cuentaSeleccionada = Banco.getInstance().buscarCuentaPorNumero(numeroSeleccionado);
        if (cuentaSeleccionada != null) {
            DecimalFormat df = new DecimalFormat("#,##0.00");
            saldoActualLabel.setText("$ " + df.format(cuentaSeleccionada.getSaldoCuenta()));
        }
    }

    @FXML
    public void consultarSaldo() {
        if (cuentaSeleccionada == null) {
            mostrarMensaje("Selecciona una cuenta para consultar el saldo.");
            return;
        }
        DecimalFormat df = new DecimalFormat("#,##0.00");
        mostrarMensaje("Saldo actual: $" + df.format(cuentaSeleccionada.getSaldoCuenta()));
    }

    @FXML
    public void realizarTransferencia() {
        if (cuentaSeleccionada == null) {
            mostrarMensaje("Selecciona una cuenta para transferir.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/poo/AppP1/GUI/transferencia.fxml"));
            Parent root = loader.load();

            TransferirController controller = loader.getController();
            controller.setCuentaOrigen(cuentaSeleccionada);
            controller.setClienteController(this);

            Stage stage = new Stage();
            stage.setTitle("Transferir Dinero");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.showAndWait();

            actualizarCuentaSeleccionada();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje("Error al abrir la ventana de transferencia: " + e.getMessage());
        }
    }

    private void mostrarMensaje(String mensaje) {
        new Alert(Alert.AlertType.INFORMATION, mensaje).showAndWait();
    }
    @FXML
    public void cerrarSesion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) nombreClienteLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inicio de Sesión");
            stage.show();
        } catch (IOException e) {
            mostrarMensaje("No se pudo cerrar sesión: " + e.getMessage());
        }
    }

 
}
