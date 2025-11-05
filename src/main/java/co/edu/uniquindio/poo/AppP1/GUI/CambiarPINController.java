package co.edu.uniquindio.poo.AppP1.GUI;

import co.edu.uniquindio.poo.Model.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class CambiarPINController {

    @FXML private PasswordField pinActualField;
    @FXML private PasswordField nuevoPinField;
    @FXML private PasswordField confirmarNuevoPinField;

    private Cliente clienteAutenticado;

    public void setClienteAutenticado(Cliente cliente) {
        this.clienteAutenticado = cliente;
    }

    @FXML
    public void confirmarCambioPIN(ActionEvent event) {
        if (clienteAutenticado == null) {
            new Alert(Alert.AlertType.ERROR, "No hay un cliente autenticado para cambiar el PIN.").showAndWait();
            return;
        }

        String pinActual = pinActualField.getText();
        String nuevoPin = nuevoPinField.getText();
        String confirmarNuevoPin = confirmarNuevoPinField.getText();

        // 1. Validar que el PIN actual sea correcto
        if (!clienteAutenticado.getPin().equals(pinActual)) {
            new Alert(Alert.AlertType.ERROR, "El PIN actual ingresado es incorrecto.").showAndWait();
            return;
        }

        // 2. Validar que el nuevo PIN no esté vacío
        if (nuevoPin.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "El nuevo PIN no puede estar vacío.").showAndWait();
            return;
        }

        // 3. Validar que el nuevo PIN y la confirmación coincidan
        if (!nuevoPin.equals(confirmarNuevoPin)) {
            new Alert(Alert.AlertType.ERROR, "El nuevo PIN y su confirmación no coinciden.").showAndWait();
            return;
        }

        // 4. Validar que el nuevo PIN no sea igual al actual (opcional pero recomendado)
        if (nuevoPin.equals(pinActual)) {
            new Alert(Alert.AlertType.WARNING, "El nuevo PIN no puede ser igual al PIN actual.").showAndWait();
            return;
        }

        // 5. Validar formato del PIN (ej: 4 dígitos)
        if (nuevoPin.length() != 4 || !nuevoPin.matches("\\d+")) {
            new Alert(Alert.AlertType.ERROR, "El PIN debe ser numérico y tener 4 dígitos.").showAndWait();
            return;
        }


        // Si todas las validaciones pasan, cambiar el PIN
        clienteAutenticado.setPin(nuevoPin); // Asumiendo que Cliente tiene un setPin(String)

        new Alert(Alert.AlertType.INFORMATION, "El PIN ha sido cambiado exitosamente.").showAndWait();
        cerrarVentana();
    }

    @FXML
    public void cancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) pinActualField.getScene().getWindow();
        stage.close();
    }
}