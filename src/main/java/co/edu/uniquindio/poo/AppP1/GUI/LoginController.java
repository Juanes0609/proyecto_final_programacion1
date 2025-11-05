package co.edu.uniquindio.poo.AppP1.GUI;

import co.edu.uniquindio.poo.Model.Banco;
import co.edu.uniquindio.poo.Model.Cajero;
import co.edu.uniquindio.poo.Model.Cliente; // Importar la clase Cliente
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException; // Importar IOException
import java.net.URL;

public class LoginController {

    @FXML
    private TextField usuarioField;
    @FXML
    private PasswordField contrasenaField;
    @FXML
    private ComboBox<String> rolComboBox;

    @FXML
    public void initialize() {
        rolComboBox.getItems().addAll("Cliente", "Cajero", "Administrador");
    }

    @FXML
    public void iniciarSesion(ActionEvent event) {
        String usuario = usuarioField.getText();
        String contrasena = contrasenaField.getText();
        String rol = rolComboBox.getValue();

        System.out.println("--- Inicio de Sesión ---");
        System.out.println("Usuario: " + usuario);
        System.out.println("Contraseña: " + contrasena);
        System.out.println("Rol seleccionado: " + rol);

        if (usuario == null || contrasena == null || rol == null || usuario.isEmpty() || contrasena.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Completa todos los campos").showAndWait();
            System.out.println("Campos incompletos.");
            return;
        }

        Banco banco = Banco.getInstance();
        Object usuarioAutenticado = banco.autenticarUsuario(usuario, contrasena, rol);

        if (usuarioAutenticado != null) {
            System.out.println("Usuario autenticado exitosamente. Rol: " + rol);
            try {
                Stage stage = (Stage) usuarioField.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader();
                URL fxmlLocation = null;
                String title = "Banco UQ"; // Título por defecto

                switch (rol) {
                    case "Cliente":
                        fxmlLocation = getClass().getResource("/co/edu/uniquindio/poo/AppP1/GUI/cliente.fxml");
                        title = "Banco UQ - Cliente";
                        break;
                    case "Cajero":
                        fxmlLocation = getClass().getResource("/co/edu/uniquindio/poo/AppP1/GUI/cajero.fxml");
                        title = "Banco UQ - Cajero";
                        break;
                    case "Administrador":
                        fxmlLocation = getClass().getResource("/co/edu/uniquindio/poo/AppP1/GUI/admin.fxml");
                        title = "Banco UQ - Administrador";
                        break;
                    default:
                        System.err.println("Error: Rol de usuario no reconocido en el switch: " + rol);
                        new Alert(Alert.AlertType.ERROR, "Rol de usuario no reconocido.").showAndWait();
                        return;
                }

                System.out.println("Intentando cargar FXML para rol '" + rol + "'. URL obtenida: " + fxmlLocation);

                if (fxmlLocation == null) {
                    System.err.println("¡CRÍTICO! La URL del FXML es NULL para el rol: " + rol
                            + ". Asegúrate de que la ruta sea correcta: " + "/co/edu/uniquindio/poo/AppP1/GUI/"
                            + rol.toLowerCase() + ".fxml");
                    throw new RuntimeException(
                            "No se encontró el archivo FXML para el rol: " + rol + ". Verifica la ruta.");
                }

                // Set the location and load the FXML once after determining the path
                loader.setLocation(fxmlLocation);
                Parent root = loader.load(); // AHORA ESTA ES LA ÚNICA LLAMADA A load()

                // Post-load processing based on role (getting controller and passing data)
                switch (rol) {
                    case "Cliente":
                        ClienteController clienteController = loader.getController();
                        clienteController.setClienteAutenticado((Cliente) usuarioAutenticado);
                        break;
                    // Aquí puedes añadir más casos si necesitas configurar los controladores de
                    // Cajero o Admin
                        case "Cajero":
                        CajeroController cajeroController = loader.getController();
                        cajeroController.setCajeroAutenticado((Cajero) usuarioAutenticado);
                        break;
                        //case "Administrador":
                        //AdministradorController adminController = loader.getController();
                        //adminController.setAdministradorAutenticado((Administrador)
                        //usuarioAutenticado);
                       // break;
                }

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle(title); // Establece el título una vez al final
                System.out.println("Vista cargada exitosamente para el rol: " + rol);

            } catch (IOException e) { // Cambiado a IOException
                System.err.println("Excepción durante la carga del FXML o cambio de escena:");
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error al cargar la vista: " + e.getMessage()).showAndWait();
            } catch (ClassCastException e) {
                System.err.println("Error de casting al pasar el objeto autenticado al controlador: " + e.getMessage());
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error interno: Problema con el rol del usuario.").showAndWait();
            } catch (Exception e) { // Catch-all para cualquier otra excepción inesperada
                System.err.println("Excepción inesperada: ");
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Ocurrió un error inesperado: " + e.getMessage()).showAndWait();
            }
        } else {
            System.out.println("Autenticación fallida. Credenciales incorrectas.");
            new Alert(Alert.AlertType.ERROR, "Credenciales incorrectas").showAndWait();
        }
    }
}