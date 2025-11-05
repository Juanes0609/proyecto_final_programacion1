package co.edu.uniquindio.poo.AppP1.GUI;

import co.edu.uniquindio.poo.Model.Banco;
import co.edu.uniquindio.poo.Model.Cajero;
import co.edu.uniquindio.poo.Model.Cliente;
import co.edu.uniquindio.poo.Model.CuentaBancaria;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;

public class CajeroController {

    @FXML private Label nombreCajeroLabel;
    @FXML private TextField cuentaPrincipalField;
    @FXML private TextField pinUsuarioField;
    @FXML private TextField identificacionUsuarioField;

    private Cajero cajeroAutenticado; 
    public void setCajeroAutenticado(Cajero cajero) {
        this.cajeroAutenticado = cajero;
        if (cajero != null) {
            nombreCajeroLabel.setText(cajero.getNombre());
        }
    }

    @FXML
    public void consultarSaldo(ActionEvent event) {
        double saldo = 0;
        String cuentaUsuario = cuentaPrincipalField.getText();
        for(CuentaBancaria cuenta: Banco.getInstance().getCuentasBancarias()){
                if(cuenta.getNumeroCuenta().equals(cuentaUsuario)){
                    saldo = cuenta.getSaldoCuenta();
                }
            }if (saldo == 0) {
            new Alert(Alert.AlertType.WARNING, "El saldo de la cuenta es cero o no existe.").showAndWait();}

        if (cuentaUsuario == null) {
            new Alert(Alert.AlertType.WARNING, "No hay una cuenta asociada para consultar el saldo.").showAndWait();
            return;
        }
        // El saldo ya se actualiza en el label, solo mostramos una alerta para confirmar
        new Alert(Alert.AlertType.INFORMATION, "Su saldo actual es: $" + new DecimalFormat("#,##0.00").format(saldo)).showAndWait();
    }

    @FXML
    public void mostrarVentanaDepositar(ActionEvent event) {
        String cuentaUsuario = cuentaPrincipalField.getText();
        CuentaBancaria cuentaDelUser = Banco.getInstance().buscarCuentaPorNumero(cuentaUsuario);
        if (cuentaUsuario == null) {
            new Alert(Alert.AlertType.WARNING, "No hay una cuenta asociada para realizar depósitos.").showAndWait();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/poo/AppP1/GUI/deposito.fxml"));
            Parent root = loader.load();

            DepositarController controller = loader.getController();
            controller.setCuentaCliente(cuentaDelUser); // Pasamos la cuenta para que el controlador la use
            controller.setCajeroController(this); // Para poder llamar a actualizarSaldo() desde DepositarController

            Stage stage = new Stage();
            stage.setTitle("Depositar Dinero");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana principal hasta que se cierre
            stage.initStyle(StageStyle.UTILITY); // Estilo de ventana de utilidad
            stage.showAndWait(); // Espera a que la ventana se cierre

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al abrir la ventana de depósito: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void mostrarVentanaRetirar(ActionEvent event) {
        String cuentaUsuario = cuentaPrincipalField.getText();
        CuentaBancaria cuentaPrincipal = Banco.getInstance().buscarCuentaPorNumero(cuentaUsuario);
        if (cuentaUsuario == null) {
            new Alert(Alert.AlertType.WARNING, "No hay una cuenta asociada para realizar retiros.").showAndWait();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/poo/AppP1/GUI/retiro.fxml"));
            Parent root = loader.load();

            RetirarController controller = loader.getController();
            controller.setCuentaCliente(cuentaPrincipal); 
            controller.setCajeroController(this); 

            Stage stage = new Stage();
            stage.setTitle("Retirar Dinero");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al abrir la ventana de retiro: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void mostrarVentanaTransferir(ActionEvent event) {
        String cuentaUsuario = cuentaPrincipalField.getText();
        CuentaBancaria cuentaPrincipal = Banco.getInstance().buscarCuentaPorNumero(cuentaUsuario);
         // Verificamos si la cuenta existe
        if (cuentaUsuario == null) {
            new Alert(Alert.AlertType.WARNING, "No hay una cuenta asociada para realizar transferencias.").showAndWait();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/poo/AppP1/GUI/transferencia.fxml"));
            Parent root = loader.load();

            TransferirController controller = loader.getController();
            controller.setCuentaOrigen(cuentaPrincipal); 
            controller.setCajeroController(this); 

            Stage stage = new Stage();
            stage.setTitle("Transferir Dinero");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al abrir la ventana de transferencia: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void mostrarReportes(ActionEvent event) {
        try {
            // Cargar el FXML de reportes
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/poo/AppP1/GUI/reportes.fxml"));
            Parent root = loader.load();

            // Puedes obtener el controlador si necesitas pasarle datos, aunque en este caso
            // ReportesController ya carga los datos directamente del Banco.getInstance()
            // ReportesController controller = loader.getController();
            // Si tuvieras datos específicos que pasar, lo harías aquí:
            // controller.setDatos(miListaDeDatos);

            Stage stage = new Stage();
            stage.setTitle("Reportes de Transacciones");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); 
            stage.initStyle(StageStyle.UTILITY); 
            stage.showAndWait(); 

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al abrir la ventana de reportes: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void cambiarPIN(ActionEvent event) {
        String cuentaUsuario = cuentaPrincipalField.getText();
        String idUsuario = identificacionUsuarioField.getText();
        String pinUsuario = pinUsuarioField.getText();

        Cliente clienteAutenticado = Banco.getInstance().buscarCliente(pinUsuario);

        if (clienteAutenticado == null) {
            new Alert(Alert.AlertType.WARNING, "No hay un cliente autenticado para cambiar el PIN.").showAndWait();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/poo/AppP1/GUI/cambiar_pin.fxml"));
            Parent root = loader.load();

            CambiarPINController controller = loader.getController();
            controller.setClienteAutenticado(clienteAutenticado); // Pasamos el cliente actual

            Stage stage = new Stage();
            stage.setTitle("Cambiar PIN");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.showAndWait();

            // No necesitas actualizar saldo aquí, pero si el PIN fuera visible en la pantalla principal
            // y se actualizara, lo harías. En este caso, solo se actualiza en el objeto Cliente.

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al abrir la ventana de cambio de PIN: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void cerrarSesion(ActionEvent event) {
        try {
            // Cargar la vista de login
            URL fxmlUrl = getClass().getResource("/co/edu/uniquindio/poo/AppP1/GUI/login.fxml");
            if (fxmlUrl == null) {
                throw new RuntimeException("No se encontró login.fxml en recursos");
            }
            Parent root = FXMLLoader.load(fxmlUrl);
            Scene scene = new Scene(root);

            // Obtener el Stage actual y cambiar la escena
            Stage stage = (Stage) nombreCajeroLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Banco UQ - Login");
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al cerrar sesión: " + e.getMessage()).showAndWait();
        }
    }
}
