package co.edu.uniquindio.poo.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CajeroTest {

    @Test
    void constructorValidoYGetters() {
        Cajero cajero = new Cajero("Luis Cajero", "cajero1", "Sucursal 2", "3000001111", "EMP002", 2500000.0, "cajeropass");

        assertEquals("Luis Cajero", cajero.getNombre());
        assertEquals("cajero1", cajero.getIdentificacion());
        assertEquals("Sucursal 2", cajero.getDireccion());
        assertEquals("3000001111", cajero.getTelefono());
        assertEquals("EMP002", cajero.getIdEmpleado());
        assertEquals(2500000.0, cajero.getSalario());
        assertEquals("cajeropass", cajero.getPin());
    }

    @Test
    void obtenerDetalles() {
        Cajero cajero = new Cajero("Luis Cajero", "cajero1", "Sucursal 2", "3000001111", "EMP002", 2500000.0, "cajeropass");
        String detallesEsperados = "Cajero: Luis Cajero, ID Empleado: EMP002, Identificación: cajero1";
        assertEquals(detallesEsperados, cajero.obtenerDetalles());
    }

    @Test
    void constructorConNombreNulo() {
        assertThrows(NullPointerException.class, () -> new Cajero(null, "cajero1", "Sucursal 2", "3000001111", "EMP002", 2500000.0, "cajeropass"));
    }
}