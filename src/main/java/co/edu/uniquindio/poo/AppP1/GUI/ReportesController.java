package co.edu.uniquindio.poo.AppP1.GUI;

import co.edu.uniquindio.poo.Model.Banco;
import co.edu.uniquindio.poo.Model.Transaccion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class ReportesController {

    @FXML
    private ListView<String> listaTransacciones;

    @FXML
    public void initialize() {
        cargarTransacciones();
    }

    private void cargarTransacciones() {
        List<Transaccion> todasLasTransacciones = Banco.getInstance().getTransacciones();

        // Ordenar las transacciones por fecha de forma descendente (las más recientes primero)
        todasLasTransacciones.sort(Comparator.comparing(Transaccion::getFechaHora, Comparator.reverseOrder()));

        ObservableList<String> items = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (todasLasTransacciones.isEmpty()) {
            items.add("No hay transacciones registradas.");
        } else {
            for (Transaccion t : todasLasTransacciones) {
                // Determinar el tipo de transacción basándonos en si tiene cuenta origen/destino
                String tipoTransaccion = "Desconocido";
                if (t.getNumeroCuentaOrigen() == null && t.getNumeroCuentaDestino() != null) {
                    tipoTransaccion = "Depósito";
                } else if (t.getNumeroCuentaOrigen() != null && t.getNumeroCuentaDestino() == null) {
                    tipoTransaccion = "Retiro";
                } else if (t.getNumeroCuentaOrigen() != null && t.getNumeroCuentaDestino() != null) {
                    tipoTransaccion = "Transferencia";
                }

                String descripcion = String.format("%s - %s - Monto: $%.2f (Origen: %s, Destino: %s) - Estado: %s",
                        t.getFechaHora().format(formatter),
                        tipoTransaccion, // Usamos el tipo determinado aquí
                        t.getMonto(),
                        t.getNumeroCuentaOrigen() != null ? t.getNumeroCuentaOrigen() : "N/A",
                        t.getNumeroCuentaDestino() != null ? t.getNumeroCuentaDestino() : "N/A",
                        t.getEstado());
                items.add(descripcion);
            }
        }
        listaTransacciones.setItems(items);
    }

    @FXML
    public void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) listaTransacciones.getScene().getWindow();
        stage.close();
    }
}