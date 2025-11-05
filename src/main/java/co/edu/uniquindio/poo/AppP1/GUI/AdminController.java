package co.edu.uniquindio.poo.AppP1.GUI;

import co.edu.uniquindio.poo.Model.Banco;
import co.edu.uniquindio.poo.Model.Empleado;
import co.edu.uniquindio.poo.Model.Transaccion;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class AdminController {

    @FXML private TextArea areaReportes;
    @FXML private ComboBox<String> comboEmpleados;
    @FXML private TextField nombreField;
    @FXML private TextField idField;
    @FXML private TextField direccionField;
    @FXML private TextField telefonoField;
    @FXML private TextField salarioField;
    @FXML private PasswordField pinField;

    private final Banco banco = Banco.getInstance();

    @FXML
    public void initialize() {
        cargarEmpleados();
    }

    private void cargarEmpleados() {
        comboEmpleados.getItems().clear();
        for (Empleado emp : banco.getEmpleados()) {
            comboEmpleados.getItems().add(emp.getNombre() + " - " + emp.getIdentificacion());
        }
    }

    @FXML
    public void registrarEmpleado() {
        try {
            String nombre = nombreField.getText();
            String id = idField.getText();
            String direccion = direccionField.getText();
            String telefono = telefonoField.getText();
            double salario = Double.parseDouble(salarioField.getText());
            String pin = pinField.getText();

            banco.registrarCajero(nombre, id, direccion, telefono, "EMP" + (banco.getEmpleados().size() + 1), salario, pin);
            mostrarAlerta("Éxito", "Empleado registrado correctamente.");
            limpiarCampos();
            cargarEmpleados();
        } catch (Exception e) {
            mostrarAlerta("Error", "Verifica los datos ingresados: " + e.getMessage());
        }
    }

    @FXML
    public void eliminarEmpleado() {
        String seleccionado = comboEmpleados.getValue();
        if (seleccionado == null) {
            mostrarAlerta("Atención", "Selecciona un empleado para eliminar.");
            return;
        }

        String id = seleccionado.split("-")[1].trim();
        Empleado emp = banco.buscarEmpleado(id);
        if (emp != null) {
            banco.getEmpleados().remove(emp);
            mostrarAlerta("Éxito", "Empleado eliminado.");
            cargarEmpleados();
        } else {
            mostrarAlerta("Error", "Empleado no encontrado.");
        }
    }

    @FXML
    public void mostrarTransacciones() {
        List<Transaccion> transacciones = banco.getTransacciones();
        StringBuilder sb = new StringBuilder("REPORTE DE TRANSACCIONES:\n\n");
        for (Transaccion t : transacciones) {
            sb.append("Fecha: ").append(t.getFechaHora())
              .append(" | Monto: $").append(t.getMonto())
              .append(" | Estado: ").append(t.getEstado())
              .append(" | Cuenta Destino: ").append(t.getNumeroCuentaDestino())
              .append("\n");
        }

        areaReportes.setText(sb.toString());
    }

    @FXML
private void cerrarSesion() {
    try {
        // Cargar la ventana de inicio de sesión (Login.fxml)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();

        // Obtener la escena actual y reemplazarla
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Inicio de Sesión");
        stage.show();
    } catch (Exception e) {
        mostrarAlerta("Error", "No se pudo cerrar sesión: " + e.getMessage());
    }
}

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarCampos() {
        nombreField.clear();
        idField.clear();
        direccionField.clear();
        telefonoField.clear();
        salarioField.clear();
        pinField.clear();
    }
}
