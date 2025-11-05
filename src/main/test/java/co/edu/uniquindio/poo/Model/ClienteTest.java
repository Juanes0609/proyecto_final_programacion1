package co.edu.uniquindio.poo.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    private Cliente cliente;
    private CuentaAhorros cuentaAhorros;
    private CuentaCorriente cuentaCorriente;
    private Cliente propietarioParaCuentas; // Propietario para las cuentas

    @BeforeEach
    void setUp() {
        cliente = new Cliente("Carlos Pérez", "1234", "Calle 1", "3001234567", "CLI001", "1234");

        // Propietario para las cuentas (puede ser el mismo cliente o uno diferente)
        propietarioParaCuentas = cliente;
        cuentaAhorros = new CuentaAhorros("AH001", 1000.0, LocalDateTime.now(), propietarioParaCuentas, 0.02);
        cuentaCorriente = new CuentaCorriente("CC001", 2000.0, LocalDateTime.now(), propietarioParaCuentas, 500.0);
    }

    @Test
    void constructorValidoYGetters() {
        assertEquals("Carlos Pérez", cliente.getNombre());
        assertEquals("1234", cliente.getIdentificacion());
        assertEquals("Calle 1", cliente.getDireccion());
        assertEquals("3001234567", cliente.getTelefono());
        assertEquals("CLI001", cliente.getNumeroCuenta()); 
        assertEquals("1234", cliente.getPin());
        assertNotNull(cliente.getListaCuentas());
        assertTrue(cliente.getListaCuentas().isEmpty());
    }

    @Test
    void setNumeroCuentaValido() {
        cliente.setNumeroCuenta("NEWACC001");
        assertEquals("NEWACC001", cliente.getNumeroCuenta());
    }

    @Test
    void setNumeroCuentaNulo() {
        assertThrows(NullPointerException.class, () -> cliente.setNumeroCuenta(null));
    }

    @Test
    void setPinValido() {
        cliente.setPin("5678");
        assertEquals("5678", cliente.getPin());
    }

    @Test
    void agregarCuentaValida() {
        cliente.agregarCuenta(cuentaAhorros);
        assertEquals(1, cliente.getListaCuentas().size());
        assertTrue(cliente.getListaCuentas().contains(cuentaAhorros));
    }

    @Test
    void agregarCuentaNula() {
        assertThrows(NullPointerException.class, () -> cliente.agregarCuenta(null));
    }

    @Test
    void agregarCuentaDuplicada() {
        cliente.agregarCuenta(cuentaAhorros); 
        CuentaAhorros cuentaDuplicada = new CuentaAhorros("AH001", 500.0, LocalDateTime.now(), propietarioParaCuentas, 0.01);

        assertThrows(IllegalArgumentException.class, () -> cliente.agregarCuenta(cuentaDuplicada));
        assertEquals(1, cliente.getListaCuentas().size());
    }

    @Test
    void buscarCuentaExistente() {
        Cliente clienteConCuentaAsociada = new Cliente("Test", "t1", "dir", "tel", "AH001", "pin");
        CuentaAhorros cuentaParaBuscar = new CuentaAhorros("AH001", 100.0, LocalDateTime.now(), clienteConCuentaAsociada, 0.01);
        CuentaCorriente otraCuenta = new CuentaCorriente("CC002", 200.0, LocalDateTime.now(), clienteConCuentaAsociada, 100.0);

        clienteConCuentaAsociada.agregarCuenta(cuentaParaBuscar);
        clienteConCuentaAsociada.agregarCuenta(otraCuenta);

        assertEquals(cuentaParaBuscar, clienteConCuentaAsociada.buscarCuenta());
    }

    @Test
    void buscarCuentaNoExistente() {
        cliente.agregarCuenta(cuentaAhorros); 
        assertNull(cliente.buscarCuenta());
    }

    @Test
    void buscarCuentaEnListaVacia() {
        
        assertNull(cliente.buscarCuenta());
    }

    @Test
    void eliminarCuentaExistente() {
        cliente.agregarCuenta(cuentaAhorros);    // AH001
        cliente.agregarCuenta(cuentaCorriente); // CC001
        assertEquals(2, cliente.getListaCuentas().size());

        cliente.eliminarCuenta("AH001");
        assertEquals(1, cliente.getListaCuentas().size());
        assertFalse(cliente.getListaCuentas().contains(cuentaAhorros));
        assertTrue(cliente.getListaCuentas().contains(cuentaCorriente));
    }

    @Test
    void eliminarCuentaNoExistente() {
        cliente.agregarCuenta(cuentaAhorros);
        assertThrows(IllegalArgumentException.class, () -> cliente.eliminarCuenta("NONEXISTENT001"));
        assertEquals(1, cliente.getListaCuentas().size());
    }

    @Test
    void eliminarCuentaNumeroNulo() {
        assertThrows(NullPointerException.class, () -> cliente.eliminarCuenta(null));
    }

    @Test
    void obtenerDetallesSinCuentas() {
        String detallesEsperados = "Cliente: Carlos Pérez, Identificación: 1234, Cuentas: 0";
        assertEquals(detallesEsperados, cliente.obtenerDetalles());
    }

    @Test
    void obtenerDetallesConCuentas() {
        cliente.agregarCuenta(cuentaAhorros);
        cliente.agregarCuenta(cuentaCorriente);
        String detallesEsperados = "Cliente: Carlos Pérez, Identificación: 1234, Cuentas: 2";
        assertEquals(detallesEsperados, cliente.obtenerDetalles());
    }
}