package hotel;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de Caja Negra — Sistema de Gestión de Hotel
 *
 * Técnicas aplicadas:
 *   PE  = Partición de Equivalencia
 *   AVL = Análisis de Valores Límite
 */
@DisplayName("Pruebas de Caja Negra — Sistema de Gestión de Hotel")
class HotelTest {

    private GestionHotel hotel;

    @BeforeEach
    void setUp() {
        hotel = new GestionHotel();
        Reserva.resetContador();
        Factura.resetContador();
    }

    // =========================================================================
    // MÓDULO 1 — CLIENTE
    // Técnica: PE (clases válidas e inválidas por campo)
    // =========================================================================

    @Nested
    @DisplayName("M1 — Registro de Clientes [PE]")
    class ClienteTest {

        @Test
        @DisplayName("PE-C01 (válida): Registrar cliente con datos completos y correctos")
        void registrarClienteValido() {
            Cliente c = hotel.registrarCliente("12345678", "Juan", "Pérez", "999888777", "juan@mail.com");
            assertNotNull(c);
            assertEquals("12345678", c.getDni());
            assertEquals("Juan", c.getNombre());
            assertEquals("Pérez", c.getApellido());
            assertEquals("juan@mail.com", c.getEmail());
        }

        @ParameterizedTest(name = "PE-C02 (inválida): DNI=''{0}'' debe rechazarse")
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        @DisplayName("PE-C02 (inválida): DNI nulo, vacío o solo espacios lanza excepción")
        void registrarClienteDniInvalido(String dni) {
            assertThrows(IllegalArgumentException.class,
                    () -> new Cliente(dni, "Juan", "Pérez", "999888777", "juan@mail.com"));
        }

        @ParameterizedTest(name = "PE-C03 (inválida): email=''{0}'' debe rechazarse")
        @ValueSource(strings = {"sinArroba", "correo.com", "", "   "})
        @DisplayName("PE-C03 (inválida): Email sin @ lanza excepción")
        void registrarClienteEmailInvalido(String email) {
            assertThrows(IllegalArgumentException.class,
                    () -> new Cliente("12345678", "Juan", "Pérez", "999888777", email));
        }

        @Test
        @DisplayName("PE-C04 (inválida): Registrar cliente con DNI duplicado lanza excepción")
        void registrarClienteDniDuplicado() {
            hotel.registrarCliente("12345678", "Juan", "Pérez", "999888777", "juan@mail.com");
            assertThrows(IllegalArgumentException.class,
                    () -> hotel.registrarCliente("12345678", "Pedro", "López", "111222333", "pedro@mail.com"));
        }

        @Test
        @DisplayName("PE-C05 (válida): Buscar cliente existente retorna el cliente correcto")
        void buscarClienteExistente() {
            hotel.registrarCliente("12345678", "Juan", "Pérez", "999888777", "juan@mail.com");
            assertTrue(hotel.buscarClientePorDni("12345678").isPresent());
        }

        @Test
        @DisplayName("PE-C06 (inválida): Buscar cliente inexistente retorna vacío")
        void buscarClienteInexistente() {
            assertFalse(hotel.buscarClientePorDni("99999999").isPresent());
        }

        @Test
        @DisplayName("PE-C07 (válida): getNombreCompleto retorna nombre y apellido concatenados")
        void nombreCompletoCliente() {
            Cliente c = new Cliente("12345678", "Juan", "Pérez", "999888777", "juan@mail.com");
            assertEquals("Juan Pérez", c.getNombreCompleto());
        }
    }

    // =========================================================================
    // MÓDULO 2 — HABITACIÓN
    // Técnica: PE + AVL (número y precio)
    // =========================================================================

    @Nested
    @DisplayName("M2 — Registro de Habitaciones [PE + AVL]")
    class HabitacionTest {

        @Test
        @DisplayName("PE-H01 (válida): Crear habitación con datos correctos")
        void crearHabitacionValida() {
            Habitacion h = new Habitacion(101, TipoHabitacion.SIMPLE, 150.0);
            assertEquals(101, h.getNumero());
            assertEquals(TipoHabitacion.SIMPLE, h.getTipo());
            assertEquals(150.0, h.getPrecioPorNoche(), 0.001);
            assertEquals(EstadoHabitacion.DISPONIBLE, h.getEstado());
            assertTrue(h.isDisponible());
        }

        // AVL — número de habitación
        @Test
        @DisplayName("AVL-H02 (límite inferior): Número de habitación = 0 lanza excepción")
        void numeroHabitacionCero() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Habitacion(0, TipoHabitacion.DOBLE, 200.0));
        }

        @Test
        @DisplayName("AVL-H03 (límite inferior válido): Número de habitación = 1 es aceptado")
        void numeroHabitacionUno() {
            assertDoesNotThrow(() -> new Habitacion(1, TipoHabitacion.DOBLE, 200.0));
        }

        @Test
        @DisplayName("AVL-H04 (límite inválido): Número de habitación negativo lanza excepción")
        void numeroHabitacionNegativo() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Habitacion(-1, TipoHabitacion.SUITE, 500.0));
        }

        // AVL — precio por noche
        @ParameterizedTest(name = "AVL-H05 (inválida): precio={0} debe rechazarse")
        @ValueSource(doubles = {0.0, -1.0, -100.0})
        @DisplayName("AVL-H05 (inválida): Precio cero o negativo lanza excepción")
        void precioHabitacionInvalido(double precio) {
            assertThrows(IllegalArgumentException.class,
                    () -> new Habitacion(101, TipoHabitacion.SIMPLE, precio));
        }

        @Test
        @DisplayName("AVL-H06 (límite válido): Precio = 0.01 es aceptado")
        void precioMinimoCentavo() {
            assertDoesNotThrow(() -> new Habitacion(101, TipoHabitacion.SIMPLE, 0.01));
        }

        @Test
        @DisplayName("PE-H07 (inválida): Tipo de habitación nulo lanza excepción")
        void tipoHabitacionNulo() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Habitacion(101, null, 150.0));
        }

        @Test
        @DisplayName("PE-H08 (válida): Registrar habitación duplicada lanza excepción")
        void habitacionDuplicada() {
            hotel.registrarHabitacion(101, TipoHabitacion.SIMPLE, 150.0);
            assertThrows(IllegalArgumentException.class,
                    () -> hotel.registrarHabitacion(101, TipoHabitacion.DOBLE, 200.0));
        }

        @Test
        @DisplayName("PE-H09 (válida): Listar habitaciones disponibles retorna solo las disponibles")
        void listarHabitacionesDisponibles() {
            hotel.registrarHabitacion(101, TipoHabitacion.SIMPLE, 150.0);
            hotel.registrarHabitacion(102, TipoHabitacion.DOBLE, 200.0);
            List<Habitacion> disponibles = hotel.listarHabitacionesDisponibles();
            assertEquals(2, disponibles.size());
        }

        @Test
        @DisplayName("PE-H10 (válida): Cambiar estado de habitación actualiza correctamente")
        void cambiarEstadoHabitacion() {
            Habitacion h = new Habitacion(101, TipoHabitacion.SIMPLE, 150.0);
            h.setEstado(EstadoHabitacion.MANTENIMIENTO);
            assertEquals(EstadoHabitacion.MANTENIMIENTO, h.getEstado());
            assertFalse(h.isDisponible());
        }

        @Test
        @DisplayName("PE-H11 (inválida): Cambiar estado a nulo lanza excepción")
        void cambiarEstadoNulo() {
            Habitacion h = new Habitacion(101, TipoHabitacion.SIMPLE, 150.0);
            assertThrows(IllegalArgumentException.class, () -> h.setEstado(null));
        }
    }

    // =========================================================================
    // MÓDULO 3 — RESERVA
    // Técnica: PE + AVL (fechas)
    // =========================================================================

    @Nested
    @DisplayName("M3 — Gestión de Reservas [PE + AVL]")
    class ReservaTest {

        private Cliente cliente;
        private Habitacion habitacion;
        private final LocalDate HOY       = LocalDate.now();
        private final LocalDate MANANA    = HOY.plusDays(1);
        private final LocalDate PASADO    = HOY.minusDays(1);
        private final LocalDate EN3DIAS   = HOY.plusDays(3);
        private final LocalDate EN5DIAS   = HOY.plusDays(5);

        @BeforeEach
        void setUpReserva() {
            cliente    = new Cliente("12345678", "Juan", "Pérez", "999888777", "juan@mail.com");
            habitacion = new Habitacion(101, TipoHabitacion.DOBLE, 200.0);
        }

        @Test
        @DisplayName("PE-R01 (válida): Crear reserva con datos correctos")
        void crearReservaValida() {
            Reserva r = new Reserva(cliente, habitacion, HOY, EN3DIAS);
            assertNotNull(r);
            assertNotNull(r.getId());
            assertEquals(EstadoReserva.CONFIRMADA, r.getEstado());
            assertEquals(3, r.getNochesReservadas());
        }

        // AVL — fechas
        @Test
        @DisplayName("AVL-R02 (límite): Fecha salida = fecha entrada lanza excepción")
        void fechaSalidaIgualEntrada() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Reserva(cliente, habitacion, HOY, HOY));
        }

        @Test
        @DisplayName("AVL-R03 (límite): Fecha salida anterior a entrada lanza excepción")
        void fechaSalidaAnteriorEntrada() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Reserva(cliente, habitacion, EN3DIAS, HOY));
        }

        @Test
        @DisplayName("AVL-R04 (límite válido): Reserva de exactamente 1 noche es aceptada")
        void reservaUnaNoches() {
            Reserva r = new Reserva(cliente, habitacion, HOY, MANANA);
            assertEquals(1, r.getNochesReservadas());
        }

        @Test
        @DisplayName("PE-R05 (inválida): Reservar habitación no disponible lanza excepción")
        void reservarHabitacionOcupada() {
            habitacion.setEstado(EstadoHabitacion.OCUPADA);
            assertThrows(IllegalStateException.class,
                    () -> new Reserva(cliente, habitacion, HOY, EN3DIAS));
        }

        @Test
        @DisplayName("PE-R06 (inválida): Cliente nulo lanza excepción")
        void reservaClienteNulo() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Reserva(null, habitacion, HOY, EN3DIAS));
        }

        @Test
        @DisplayName("PE-R07 (inválida): Habitación nula lanza excepción")
        void reservaHabitacionNula() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Reserva(cliente, null, HOY, EN3DIAS));
        }

        @Test
        @DisplayName("PE-R08 (inválida): Fechas nulas lanzan excepción")
        void reservaFechasNulas() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Reserva(cliente, habitacion, null, EN3DIAS));
            habitacion = new Habitacion(102, TipoHabitacion.SIMPLE, 100.0);
            assertThrows(IllegalArgumentException.class,
                    () -> new Reserva(cliente, habitacion, HOY, null));
        }

        @Test
        @DisplayName("PE-R09 (válida): Cancelar reserva confirmada cambia estado y libera habitación")
        void cancelarReservaConfirmada() {
            hotel.registrarCliente("12345678", "Juan", "Pérez", "999888777", "juan@mail.com");
            hotel.registrarHabitacion(101, TipoHabitacion.DOBLE, 200.0);
            Reserva r = hotel.crearReserva("12345678", 101, HOY, EN3DIAS);
            hotel.cancelarReserva(r.getId());
            assertEquals(EstadoReserva.CANCELADA, r.getEstado());
            assertTrue(hotel.buscarHabitacionPorNumero(101).get().isDisponible());
        }

        @Test
        @DisplayName("PE-R10 (inválida): Cancelar reserva ya cancelada lanza excepción")
        void cancelarReservaCancelada() {
            Reserva r = new Reserva(cliente, habitacion, HOY, EN3DIAS);
            r.cancelar();
            assertThrows(IllegalStateException.class, r::cancelar);
        }

        @Test
        @DisplayName("PE-R11 (inválida): Activar reserva no confirmada lanza excepción")
        void activarReservaNoConfirmada() {
            Reserva r = new Reserva(cliente, habitacion, HOY, EN3DIAS);
            r.cancelar();
            assertThrows(IllegalStateException.class, r::activar);
        }

        @Test
        @DisplayName("PE-R12 (inválida): Finalizar reserva no activa lanza excepción")
        void finalizarReservaNoActiva() {
            Reserva r = new Reserva(cliente, habitacion, HOY, EN3DIAS);
            assertThrows(IllegalStateException.class, r::finalizar);
        }
    }

    // =========================================================================
    // MÓDULO 4 — CHECK-IN / CHECK-OUT
    // Técnica: PE (flujo de estados)
    // =========================================================================

    @Nested
    @DisplayName("M4 — Check-in y Check-out [PE]")
    class CheckInOutTest {

        private final LocalDate HOY     = LocalDate.now();
        private final LocalDate EN3DIAS = HOY.plusDays(3);

        @BeforeEach
        void setUpHotel() {
            hotel.registrarCliente("12345678", "Ana", "García", "987654321", "ana@mail.com");
            hotel.registrarHabitacion(201, TipoHabitacion.SUITE, 400.0);
        }

        @Test
        @DisplayName("PE-CI01 (válida): Check-in activa la reserva correctamente")
        void checkInValido() {
            Reserva r = hotel.crearReserva("12345678", 201, HOY, EN3DIAS);
            hotel.realizarCheckIn(r.getId());
            assertEquals(EstadoReserva.ACTIVA, r.getEstado());
        }

        @Test
        @DisplayName("PE-CI02 (inválida): Check-in con ID inexistente lanza excepción")
        void checkInIdInexistente() {
            assertThrows(IllegalArgumentException.class,
                    () -> hotel.realizarCheckIn("RES-999"));
        }

        @Test
        @DisplayName("PE-CO01 (válida): Check-out finaliza reserva y genera factura")
        void checkOutValido() {
            Reserva r = hotel.crearReserva("12345678", 201, HOY, EN3DIAS);
            hotel.realizarCheckIn(r.getId());
            Factura f = hotel.realizarCheckOut(r.getId());
            assertNotNull(f);
            assertEquals(EstadoReserva.FINALIZADA, r.getEstado());
            assertTrue(hotel.buscarHabitacionPorNumero(201).get().isDisponible());
        }

        @Test
        @DisplayName("PE-CO02 (inválida): Check-out sin check-in previo lanza excepción")
        void checkOutSinCheckIn() {
            Reserva r = hotel.crearReserva("12345678", 201, HOY, EN3DIAS);
            assertThrows(IllegalStateException.class,
                    () -> hotel.realizarCheckOut(r.getId()));
        }

        @Test
        @DisplayName("PE-CO03 (inválida): Check-out con ID inexistente lanza excepción")
        void checkOutIdInexistente() {
            assertThrows(IllegalArgumentException.class,
                    () -> hotel.realizarCheckOut("RES-999"));
        }
    }

    // =========================================================================
    // MÓDULO 5 — FACTURACIÓN
    // Técnica: PE + AVL (cálculo de costos y límites numéricos)
    // =========================================================================

    @Nested
    @DisplayName("M5 — Facturación [PE + AVL]")
    class FacturaTest {

        private final LocalDate HOY     = LocalDate.now();
        private final LocalDate EN5DIAS = HOY.plusDays(5);
        private final LocalDate EN1DIA  = HOY.plusDays(1);

        @BeforeEach
        void setUpFactura() {
            hotel.registrarCliente("12345678", "Luis", "Torres", "912345678", "luis@mail.com");
            hotel.registrarHabitacion(301, TipoHabitacion.DOBLE, 100.0);
        }

        private Factura generarFactura(int dias) {
            // Instancia aislada para evitar conflictos entre iteraciones parametrizadas
            GestionHotel h = new GestionHotel();
            h.registrarCliente("12345678", "Luis", "Torres", "912345678", "luis@mail.com");
            h.registrarHabitacion(100, TipoHabitacion.DOBLE, 100.0);
            Reserva r = h.crearReserva("12345678", 100, HOY, HOY.plusDays(dias));
            h.realizarCheckIn(r.getId());
            return h.realizarCheckOut(r.getId());
        }

        @Test
        @DisplayName("PE-F01 (válida): Factura se genera con número, subtotal, IGV y total correctos")
        void generarFacturaValida() {
            Reserva r = hotel.crearReserva("12345678", 301, HOY, EN5DIAS);
            hotel.realizarCheckIn(r.getId());
            Factura f = hotel.realizarCheckOut(r.getId());

            assertNotNull(f.getNumeroFactura());
            assertEquals(500.0, f.getSubtotal(), 0.001);        // 100 * 5 noches
            assertEquals(90.0,  f.getIgv(),      0.001);        // 18% de 500
            assertEquals(590.0, f.getTotal(),    0.001);        // 500 + 90
            assertNotNull(f.getFechaEmision());
        }

        @Test
        @DisplayName("PE-F02 (inválida): Factura de reserva no finalizada lanza excepción")
        void facturaReservaNoFinalizada() {
            Reserva r = hotel.crearReserva("12345678", 301, HOY, EN5DIAS);
            assertThrows(IllegalStateException.class, () -> new Factura(r));
        }

        @Test
        @DisplayName("PE-F03 (inválida): Factura con reserva nula lanza excepción")
        void facturaReservaNula() {
            assertThrows(IllegalArgumentException.class, () -> new Factura(null));
        }

        // AVL — 1 noche (mínimo válido)
        @Test
        @DisplayName("AVL-F04 (límite mínimo): Factura de 1 noche calcula correctamente")
        void facturaUnaNoches() {
            Reserva r = hotel.crearReserva("12345678", 301, HOY, EN1DIA);
            hotel.realizarCheckIn(r.getId());
            Factura f = hotel.realizarCheckOut(r.getId());
            assertEquals(100.0, f.getSubtotal(), 0.001);
            assertEquals(18.0,  f.getIgv(),      0.001);
            assertEquals(118.0, f.getTotal(),    0.001);
        }

        // AVL — cálculo parametrizado por número de noches
        @ParameterizedTest(name = "AVL-F05: {0} noches × S/.100 = subtotal S/.{1}, total S/.{2}")
        @CsvSource({
                "1,  100.0, 118.0",
                "3,  300.0, 354.0",
                "5,  500.0, 590.0",
                "10, 1000.0, 1180.0"
        })
        @DisplayName("AVL-F05 (parametrizada): Cálculo de factura para distintas noches")
        void facturaParametrizada(int noches, double subtotalEsperado, double totalEsperado) {
            Factura f = generarFactura(noches);
            assertEquals(subtotalEsperado, f.getSubtotal(), 0.01);
            assertEquals(totalEsperado,   f.getTotal(),    0.01);
        }

        @Test
        @DisplayName("PE-F06 (válida): Listar facturas retorna todas las generadas")
        void listarFacturas() {
            Reserva r = hotel.crearReserva("12345678", 301, HOY, EN5DIAS);
            hotel.realizarCheckIn(r.getId());
            hotel.realizarCheckOut(r.getId());
            assertEquals(1, hotel.listarFacturas().size());
        }
    }

    // =========================================================================
    // MÓDULO 6 — FLUJO COMPLETO (integración de módulos)
    // Técnica: PE (escenario de punta a punta)
    // =========================================================================

    @Nested
    @DisplayName("M6 — Flujo Completo del Sistema [PE integración]")
    class FlujoCompletoTest {

        @Test
        @DisplayName("PE-INT01: Flujo completo — registro, reserva, check-in, check-out, factura")
        void flujoCompletoHotel() {
            // Registro
            hotel.registrarCliente("87654321", "María", "López", "955555555", "maria@mail.com");
            hotel.registrarHabitacion(501, TipoHabitacion.SUITE, 300.0);

            // Reserva
            LocalDate entrada = LocalDate.now();
            LocalDate salida  = entrada.plusDays(4);
            Reserva r = hotel.crearReserva("87654321", 501, entrada, salida);
            assertEquals(EstadoReserva.CONFIRMADA, r.getEstado());
            assertFalse(hotel.listarHabitacionesDisponibles().stream()
                    .anyMatch(h -> h.getNumero() == 501));

            // Check-in
            hotel.realizarCheckIn(r.getId());
            assertEquals(EstadoReserva.ACTIVA, r.getEstado());

            // Check-out
            Factura f = hotel.realizarCheckOut(r.getId());
            assertEquals(EstadoReserva.FINALIZADA, r.getEstado());
            assertEquals(1200.0, f.getSubtotal(), 0.001);  // 300 × 4
            assertEquals(216.0,  f.getIgv(),      0.001);  // 18% de 1200
            assertEquals(1416.0, f.getTotal(),    0.001);

            // Habitación vuelve a estar disponible
            assertTrue(hotel.buscarHabitacionPorNumero(501).get().isDisponible());

            // Historial
            assertEquals(1, hotel.listarFacturas().size());
            assertEquals(1, hotel.listarReservas().size());
        }

        @Test
        @DisplayName("PE-INT02: Habitación liberada tras cancelación puede volver a reservarse")
        void habitacionReutilizableTrasCancel() {
            hotel.registrarCliente("11111111", "Carlos", "Ruiz", "900000001", "c@mail.com");
            hotel.registrarCliente("22222222", "Sofía",  "Vega", "900000002", "s@mail.com");
            hotel.registrarHabitacion(601, TipoHabitacion.SIMPLE, 80.0);

            LocalDate entrada = LocalDate.now();
            LocalDate salida  = entrada.plusDays(2);

            Reserva r1 = hotel.crearReserva("11111111", 601, entrada, salida);
            hotel.cancelarReserva(r1.getId());
            assertEquals(EstadoReserva.CANCELADA, r1.getEstado());

            // Segunda reserva de la misma habitación
            Reserva r2 = hotel.crearReserva("22222222", 601, entrada, salida);
            assertNotNull(r2);
            assertEquals(EstadoReserva.CONFIRMADA, r2.getEstado());
        }
    }
}
