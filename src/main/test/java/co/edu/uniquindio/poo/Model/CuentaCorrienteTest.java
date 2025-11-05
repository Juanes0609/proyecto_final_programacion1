package co.edu.uniquindio.poo.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class CuentaCorrienteTest {

    private Cliente propietario;

    @BeforeEach
    void setUp() {
        propietario = new Cliente("Ana Gomez", "456", "Avenida Siempreviva 742", "3109876543", "ACC002", "5678");
    }

    @Test
    void constructorValido() {
        CuentaCorriente cuenta = new CuentaCorriente("CC001", 2000.0, LocalDateTime.now(), propietario, 500.0);
        assertEquals("CC001", cuenta.getNumeroCuenta());
        assertEquals(2000.0, cuenta.getSaldoCuenta());
        assertEquals(propietario, cuenta.getPropietario());
        assertEquals(500.0, cuenta.getLimiteSobreGiro());
        assertNotNull(cuenta.getFechaCreacion());
    }

    @Test
    void constructorLimiteSobregiroNegativo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CuentaCorriente("CC002", 2000.0, LocalDateTime.now(), propietario, -100.0);
        });
    }

    @Test
    void constructorSaldoInicialNegativo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CuentaCorriente("CC003", -100.0, LocalDateTime.now(), propietario, 500.0);
        });
    }

    @Test
    void constructorPropietarioNulo() {
        assertThrows(NullPointerException.class, () -> {
            new CuentaCorriente("CC004", 100.0, LocalDateTime.now(), null, 500.0);
        });
    }

    @Test
    void constructorNumeroCuentaNulo() {
        assertThrows(NullPointerException.class, () -> {
            new CuentaCorriente(null, 100.0, LocalDateTime.now(), propietario, 500.0);
        });
    }

    @Test
    void setLimiteSobreGiroValido() {
        CuentaCorriente cuenta = new CuentaCorriente("CC001", 2000.0, LocalDateTime.now(), propietario, 500.0);
        cuenta.setLimiteSobreGiro(600.0);
        assertEquals(600.0, cuenta.getLimiteSobreGiro());
    }

    @Test
    void setLimiteSobreGiroNegativo() {
        CuentaCorriente cuenta = new CuentaCorriente("CC001", 2000.0, LocalDateTime.now(), propietario, 500.0);
        assertThrows(IllegalArgumentException.class, () -> cuenta.setLimiteSobreGiro(-50.0));
    }

    @Test
    void depositarDineroValido() {
        CuentaCorriente cuenta = new CuentaCorriente("CC001", 2000.0, LocalDateTime.now(), propietario, 500.0);
        cuenta.depositarDinero(1000.0);
        assertEquals(3000.0, cuenta.getSaldoCuenta());
    }

    @Test
    void depositarDineroMontoCero() {
        CuentaCorriente cuenta = new CuentaCorriente("CC001", 1000.0, LocalDateTime.now(), propietario, 500.0);
        assertThrows(IllegalArgumentException.class, () -> cuenta.depositarDinero(0.0),
                "Se esperaba IllegalArgumentException para monto cero, verificar lógica en CuentaCorriente.depositarDinero");
    }

    @Test
    void depositarDineroMontoNegativo() {
        CuentaCorriente cuenta = new CuentaCorriente("CC001", 2000.0, LocalDateTime.now(), propietario, 500.0);
        assertThrows(IllegalArgumentException.class, () -> cuenta.depositarDinero(-200.0));
    }

    @Test
    void retirarDineroValidoSinSobregiro() throws SaldoInsuficienteException {
        CuentaCorriente cuenta = new CuentaCorriente("CC001", 2000.0, LocalDateTime.now(), propietario, 500.0);
        cuenta.retirarDinero(1000.0);
        assertEquals(1000.0, cuenta.getSaldoCuenta());
    }

    @Test
    void retirarDineroValidoUsandoParteDelSobregiro() throws SaldoInsuficienteException {
        CuentaCorriente cuenta = new CuentaCorriente("CC001", 300.0, LocalDateTime.now(), propietario, 500.0);
        cuenta.retirarDinero(500.0);
        assertEquals(-200.0, cuenta.getSaldoCuenta());
    }

    @Test
    void retirarDineroValidoUsandoTodoElSobregiro() throws SaldoInsuficienteException {
        CuentaCorriente cuenta = new CuentaCorriente("CC001", 300.0, LocalDateTime.now(), propietario, 500.0);
        cuenta.retirarDinero(800.0);
        assertEquals(-500.0, cuenta.getSaldoCuenta());
    }

    @Test
    void retirarDineroValidoConSaldoCeroUsandoSobregiro() throws SaldoInsuficienteException {
        CuentaCorriente cuenta = new CuentaCorriente("CC001", 0.0, LocalDateTime.now(), propietario, 500.0);
        cuenta.retirarDinero(300.0);
        assertEquals(-300.0, cuenta.getSaldoCuenta());
    }

    @Test
    void retirarDineroExcediendoSobregiro() {
        CuentaCorriente cuenta = new CuentaCorriente("CC001", 300.0, LocalDateTime.now(), propietario, 500.0);
        SaldoInsuficienteException exception = assertThrows(SaldoInsuficienteException.class, () -> cuenta.retirarDinero(900.0));
        assertTrue(exception.getMessage().contains("Límite de sobregiro excedido"));
        assertEquals(300.0, cuenta.getSaldoCuenta());
    }

    @Test
    void retirarDineroMontoCero() {
        CuentaCorriente cuenta = new CuentaCorriente("CC001", 2000.0, LocalDateTime.now(), propietario, 500.0);
        assertThrows(IllegalArgumentException.class, () -> cuenta.retirarDinero(0.0));
    }

    @Test
    void retirarDineroMontoNegativo() {
        CuentaCorriente cuenta = new CuentaCorriente("CC001", 2000.0, LocalDateTime.now(), propietario, 500.0);
        assertThrows(IllegalArgumentException.class, () -> cuenta.retirarDinero(-200.0));
    }
}