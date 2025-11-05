package co.edu.uniquindio.poo.AppP1.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        
        try {
            URL fxmlUrl = getClass().getResource("/co/edu/uniquindio/poo/AppP1/GUI/login.fxml");
            if (fxmlUrl == null) {
                throw new RuntimeException("No se encontró login.fxml en recursos");
            }
            Parent root = FXMLLoader.load(fxmlUrl);
            Scene scene = new Scene(root);
            primaryStage.setTitle("Banco UQ - Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
