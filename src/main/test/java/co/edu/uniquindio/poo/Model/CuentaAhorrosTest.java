package co.edu.uniquindio.poo.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class CuentaAhorrosTest {

    private Cliente propietario;

    @BeforeEach
    void setUp() {
        propietario = new Cliente("Juan Perez", "123", "Calle Falsa 123", "3001234567", "ACC001", "1234");
    }

    @Test
    void constructorValido() {
        CuentaAhorros cuenta = new CuentaAhorros("AH001", 1000.0, LocalDateTime.now(), propietario, 0.05);
        assertEquals("AH001", cuenta.getNumeroCuenta());
        assertEquals(1000.0, cuenta.getSaldoCuenta());
        assertEquals(propietario, cuenta.getPropietario());
        assertEquals(0.05, cuenta.getTasaInteres());
        assertNotNull(cuenta.getFechaCreacion());
    }

    @Test
    void constructorTasaInteresNegativa() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CuentaAhorros("AH002", 1000.0, LocalDateTime.now(), propietario, -0.01);
        });
    }

    @Test
    void constructorSaldoInicialNegativo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CuentaAhorros("AH003", -100.0, LocalDateTime.now(), propietario, 0.05);
        });
    }

    @Test
    void constructorPropietarioNulo() {
        assertThrows(NullPointerException.class, () -> {
            new CuentaAhorros("AH004", 100.0, LocalDateTime.now(), null, 0.05);
        });
    }

    @Test
    void constructorNumeroCuentaNulo() {
        assertThrows(NullPointerException.class, () -> {
            new CuentaAhorros(null, 100.0, LocalDateTime.now(), propietario, 0.05);
        });
    }

    @Test
    void setTasaInteresValida() {
        CuentaAhorros cuenta = new CuentaAhorros("AH001", 1000.0, LocalDateTime.now(), propietario, 0.05);
        cuenta.setTasaInteres(0.06);
        assertEquals(0.06, cuenta.getTasaInteres());
    }

    @Test
    void setTasaInteresNegativa() {
        CuentaAhorros cuenta = new CuentaAhorros("AH001", 1000.0, LocalDateTime.now(), propietario, 0.05);
        assertThrows(IllegalArgumentException.class, () -> cuenta.setTasaInteres(-0.01));
    }

    @Test
    void depositarDineroValido() {
        CuentaAhorros cuenta = new CuentaAhorros("AH001", 1000.0, LocalDateTime.now(), propietario, 0.05);
        cuenta.depositarDinero(500.0);
        assertEquals(1500.0, cuenta.getSaldoCuenta());
    }

    @Test
    void depositarDineroMontoCero() {
        CuentaAhorros cuenta = new CuentaAhorros("AH001", 1000.0, LocalDateTime.now(), propietario, 0.05);
        assertThrows(IllegalArgumentException.class, () -> cuenta.depositarDinero(0.0));
    }

    @Test
    void depositarDineroMontoNegativo() {
        CuentaAhorros cuenta = new CuentaAhorros("AH001", 1000.0, LocalDateTime.now(), propietario, 0.05);
        assertThrows(IllegalArgumentException.class, () -> cuenta.depositarDinero(-100.0));
    }

    @Test
    void retirarDineroValido() throws SaldoInsuficienteException {
        CuentaAhorros cuenta = new CuentaAhorros("AH001", 1000.0, LocalDateTime.now(), propietario, 0.05);
        cuenta.retirarDinero(500.0);
        assertEquals(500.0, cuenta.getSaldoCuenta());
    }

    @Test
    void retirarDineroExacto() throws SaldoInsuficienteException {
        CuentaAhorros cuenta = new CuentaAhorros("AH001", 1000.0, LocalDateTime.now(), propietario, 0.05);
        cuenta.retirarDinero(1000.0);
        assertEquals(0.0, cuenta.getSaldoCuenta());
    }

    @Test
    void retirarDineroSaldoInsuficiente() {
        CuentaAhorros cuenta = new CuentaAhorros("AH001", 100.0, LocalDateTime.now(), propietario, 0.05);
        SaldoInsuficienteException exception = assertThrows(SaldoInsuficienteException.class, () -> cuenta.retirarDinero(200.0));
        assertTrue(exception.getMessage().contains("Saldo insuficiente"));
        assertEquals(100.0, cuenta.getSaldoCuenta()); // Saldo no debe cambiar
    }

    @Test
    void retirarDineroMontoCero() {
        CuentaAhorros cuenta = new CuentaAhorros("AH001", 1000.0, LocalDateTime.now(), propietario, 0.05);
        assertThrows(IllegalArgumentException.class, () -> cuenta.retirarDinero(0.0));
    }

    @Test
    void retirarDineroMontoNegativo() {
        CuentaAhorros cuenta = new CuentaAhorros("AH001", 1000.0, LocalDateTime.now(), propietario, 0.05);
        assertThrows(IllegalArgumentException.class, () -> cuenta.retirarDinero(-100.0));
    }
}