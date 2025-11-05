package co.edu.uniquindio.poo.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BancoTest {

    private Banco banco;

    // Datos quemados en el constructor de Banco que se usarán como referencia:
    // Cliente: "Carlos Pérez", id "1234", cuenta "ACC00000001", pin "1234"
    // Admin: "Ana Admin", id "admin1", pin "adminpass"
    // Cajero: "Luis Cajero", id "cajero1", pin "cajeropass"
    // CuentaAhorros: "ACC00000001", saldo 1_000_000.0

    @BeforeEach
    void setUp() {
        banco = Banco.getInstance();
        banco.setTransacciones(new ArrayList<>());

    }

    @AfterEach
    void tearDown() {
        banco.setTransacciones(new ArrayList<>());
    }

    @Test
    void getInstanceDebeRetornarSiempreMismaInstancia() {
        Banco instancia1 = Banco.getInstance();
        Banco instancia2 = Banco.getInstance();
        assertSame(instancia1, instancia2);
    }

    @Test
    void constructorDebeInicializarConDatosQuemados() {
        assertFalse(banco.getClientes().isEmpty());
        assertTrue(banco.getClientes().stream().anyMatch(c -> "1234".equals(c.getIdentificacion())));

        assertFalse(banco.getEmpleados().isEmpty());
        assertTrue(banco.getEmpleados().stream()
                .anyMatch(e -> "admin1".equals(e.getIdentificacion()) && e instanceof Administrador));
        assertTrue(banco.getEmpleados().stream()
                .anyMatch(e -> "cajero1".equals(e.getIdentificacion()) && e instanceof Cajero));

        assertFalse(banco.getCuentasBancarias().isEmpty());
        assertTrue(banco.getCuentasBancarias().stream().anyMatch(cb -> "ACC00000001".equals(cb.getNumeroCuenta())));

        assertTrue(banco.getTransacciones().isEmpty()); // Limpiado en setUp
    }

    @Test
    void registrarTransaccionValida() {
        Transaccion transaccionReal = new Transaccion(
                LocalDateTime.now(),
                "Deposito",
                100.0,
                "CODIGO_TRX_IGNORADO",
                EstadoTransaccion.PENDIENTE,
                null,
                "CTA_DESTINO_001");

        banco.registrarTransaccion(transaccionReal);
        assertEquals(1, banco.getTransacciones().size());
        assertTrue(banco.getTransacciones().contains(transaccionReal));
        assertNotNull(transaccionReal.getCodigoTransaccion());
        assertEquals(EstadoTransaccion.PENDIENTE, transaccionReal.getEstado());
    }

    @Test
    void registrarTransaccionNula() {
        assertThrows(NullPointerException.class, () -> banco.registrarTransaccion(null));
    }

    @Test
    void getTransaccionesDevuelveCopiaDefensiva() {
        Transaccion transaccionReal1 = new Transaccion(
                LocalDateTime.now(), "Retiro", 50.0, "IGNORED_CODE_1",
                EstadoTransaccion.PENDIENTE, "ORIGEN_001", null);
        Transaccion transaccionReal2 = new Transaccion(
                LocalDateTime.now(), "Transferencia", 75.0, "IGNORED_CODE_2",
                EstadoTransaccion.PENDIENTE, "ORIGEN_002", "DESTINO_002");
        banco.registrarTransaccion(transaccionReal1);
        List<Transaccion> obtenidas = banco.getTransacciones();
        obtenidas.add(transaccionReal2);
        assertEquals(1, banco.getTransacciones().size(), "La lista original no debe cambiar.");
        assertFalse(banco.getTransacciones().contains(transaccionReal2));
        assertTrue(banco.getTransacciones().contains(transaccionReal1));
    }

    @Test
    void gettersAndSettersSimples() {
        banco.setNombre("Banco Test");
        assertEquals("Banco Test", banco.getNombre());
        banco.setDireccion("Dir Test");
        assertEquals("Dir Test", banco.getDireccion());
        banco.setNit("NIT Test");
        assertEquals("NIT Test", banco.getNit());
    }

    @Test
    void depositarDineroExitoso() {
        String cuentaOrigenNum = "ACC00000001"; 
        String cuentaDestinoNum = banco.generarNumeroCuentaUnico();

        Cliente propietarioDestino = new Cliente("Dueño Destino", "destID", "dir", "tel", cuentaDestinoNum, "pin");
        CuentaAhorros cuentaDestinoObj = new CuentaAhorros(cuentaDestinoNum, 50000.0, LocalDateTime.now(), propietarioDestino, 0.01);
        banco.registrarCliente(propietarioDestino.getNombre(), propietarioDestino.getIdentificacion(), propietarioDestino.getDireccion(), propietarioDestino.getTelefono(), cuentaDestinoNum, propietarioDestino.getPin());
        banco.registrarCuenta(cuentaDestinoObj);

        CuentaBancaria cOrigen = banco.buscarCuentaPorNumero(cuentaOrigenNum);
        CuentaBancaria cDestino = banco.buscarCuentaPorNumero(cuentaDestinoNum);
        assertNotNull(cOrigen);
        assertNotNull(cDestino);

        double saldoInicialOrigen = cOrigen.getSaldoCuenta();
        double saldoInicialDestino = cDestino.getSaldoCuenta();
        double monto = 100000.0;

        banco.depositarDinero(cuentaDestinoNum, cuentaOrigenNum, monto);

        assertEquals(saldoInicialOrigen - monto, cOrigen.getSaldoCuenta());
        assertEquals(saldoInicialDestino + monto, cDestino.getSaldoCuenta());
    }

    @Test
    void depositarDineroSaldoInsuficienteEnOrigen() {
        String cuentaOrigenNum = "ACC00000001";
        String cuentaDestinoNum = banco.generarNumeroCuentaUnico();
        Cliente prop = new Cliente("Otro Dueño", "otroID", "dir", "tel", cuentaDestinoNum, "pin");
        CuentaAhorros cuentaDestinoObj = new CuentaAhorros(cuentaDestinoNum, 0.0, LocalDateTime.now(), prop, 0.01);
        banco.registrarCliente(prop.getNombre(), prop.getIdentificacion(), prop.getDireccion(), prop.getTelefono(),
                cuentaDestinoNum, prop.getPin());
        banco.registrarCuenta(cuentaDestinoObj);

        CuentaBancaria cOrigen = banco.buscarCuentaPorNumero(cuentaOrigenNum);
        CuentaBancaria cDestino = banco.buscarCuentaPorNumero(cuentaDestinoNum);
        double saldoInicialOrigen = cOrigen.getSaldoCuenta();
        double saldoInicialDestino = cDestino.getSaldoCuenta();
        double monto = saldoInicialOrigen + 1000.0;

        banco.depositarDinero(cuentaDestinoNum, cuentaOrigenNum, monto);

        assertEquals(saldoInicialOrigen, cOrigen.getSaldoCuenta(), "Saldo origen no debe cambiar.");
        assertEquals(saldoInicialDestino, cDestino.getSaldoCuenta(), "Saldo destino no debe cambiar.");
    }

    @Test
    void depositarDineroCuentaDestinoNoExiste() {
        String cuentaOrigenNum = "ACC00000001";
        String cuentaDestinoInexistente = "NOEXISTE001";

        CuentaBancaria cOrigen = banco.buscarCuentaPorNumero(cuentaOrigenNum);
        assertNotNull(cOrigen, "La cuenta origen quemada debe existir.");
        double saldoInicialOrigen = cOrigen.getSaldoCuenta();
        double monto = 1500000.0;

        banco.depositarDinero(cuentaDestinoInexistente, cuentaOrigenNum, monto);

        assertEquals(saldoInicialOrigen, cOrigen.getSaldoCuenta(),
                "Saldo origen NO debería cambiar si destino no existe.");
    }

    @Test
    void registrarClienteNuevo() {
        int numClientesAntes = banco.getClientes().size();
        banco.registrarCliente("Nuevo Cliente", "clienteIDUnico", "Dir Nueva", "tel", "NC001", "pin");
        assertEquals(numClientesAntes + 1, banco.getClientes().size());
        assertNotNull(banco.buscarCliente("clienteIDUnico"));
    }

    @Test
    void autenticarClienteExitoso() {
        Cliente cliente = banco.autenticarCliente("1234", "1234"); // Datos quemados
        assertNotNull(cliente);
        assertEquals("Carlos Pérez", cliente.getNombre());
    }

    @Test
    void autenticarClienteFallido() {
        assertNull(banco.autenticarCliente("1234", "pinIncorrecto"));
    }

    @Test
    void autenticarEmpleadoAdminExitoso() {
        Empleado empleado = banco.autenticarEmpleado("admin1", "adminpass"); // Datos quemados
        assertNotNull(empleado);
        assertTrue(empleado instanceof Administrador);
    }

    @Test
    void autenticarEmpleadoCajeroExitoso() {
        Empleado empleado = banco.autenticarEmpleado("cajero1", "cajeropass"); // Datos quemados
        assertNotNull(empleado);
        assertTrue(empleado instanceof Cajero);
    }

    @Test
    void autenticarEmpleadoFallido() {
        assertNull(banco.autenticarEmpleado("admin1", "pinIncorrecto"));
    }

    @Test
    void buscarClienteExistente() {
        assertNotNull(banco.buscarCliente("1234"));
    }

    @Test
    void buscarClienteNoExistente() {
        assertNull(banco.buscarCliente("idNoExiste"));
    }

    @Test
    void registrarCuentaNueva() {
        int numCuentasAntes = banco.getCuentasBancarias().size();
        Cliente propietario = banco.buscarCliente("1234"); // Cliente quemado
        assertNotNull(propietario);
        String nuevoNumCuenta = banco.generarNumeroCuentaUnico();
        CuentaAhorros nuevaCuenta = new CuentaAhorros(nuevoNumCuenta, 100.0, LocalDateTime.now(), propietario, 0.01);
        banco.registrarCuenta(nuevaCuenta);
        assertEquals(numCuentasAntes + 1, banco.getCuentasBancarias().size());
        assertNotNull(banco.buscarCuentaPorNumero(nuevoNumCuenta));
    }

    @Test
    void buscarCuentaPorNumeroExistente() {
        assertNotNull(banco.buscarCuentaPorNumero("ACC00000001"));
    }

    @Test
    void buscarCuentaPorNumeroNoExistente() {
        assertNull(banco.buscarCuentaPorNumero("cuentaNoExiste"));
    }

    @Test
    void generarNumeroCuentaUnico() {
        String num1 = banco.generarNumeroCuentaUnico();
        Cliente prop = new Cliente("Temp", "tempID", "d", "t", "c", "p");
        CuentaAhorros tempCuenta = new CuentaAhorros(num1, 0, LocalDateTime.now(), prop, 0);
        banco.registrarCuenta(tempCuenta);
        String num2 = banco.generarNumeroCuentaUnico();
        assertNotEquals(num1, num2);
        assertTrue(num2.matches("ACC\\d{8}"));
    }

    @Test
    void autenticarUsuarioComoCliente() {
        Object usuario = banco.autenticarUsuario("1234", "1234", "Cliente");
        assertNotNull(usuario);
        assertTrue(usuario instanceof Cliente);
    }

    @Test
    void autenticarUsuarioComoCajero() {
        Object usuario = banco.autenticarUsuario("cajero1", "cajeropass", "Cajero");
        assertNotNull(usuario);
        assertTrue(usuario instanceof Cajero);
    }

    @Test
    void autenticarUsuarioRolInvalido() {
        assertNull(banco.autenticarUsuario("1234", "1234", "RolQueNoExiste"));
    }
}