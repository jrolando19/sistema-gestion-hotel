package hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GestionHotel {
    private final List<Cliente>    clientes    = new ArrayList<>();
    private final List<Habitacion> habitaciones = new ArrayList<>();
    private final List<Reserva>    reservas    = new ArrayList<>();
    private final List<Factura>    facturas    = new ArrayList<>();

    // ── Clientes ─────────────────────────────────────────────────────────────

    public Cliente registrarCliente(String dni, String nombre, String apellido,
                                    String telefono, String email) {
        if (buscarClientePorDni(dni).isPresent())
            throw new IllegalArgumentException("Ya existe un cliente con el DNI: " + dni);
        Cliente c = new Cliente(dni, nombre, apellido, telefono, email);
        clientes.add(c);
        return c;
    }

    public Optional<Cliente> buscarClientePorDni(String dni) {
        return clientes.stream().filter(c -> c.getDni().equals(dni)).findFirst();
    }

    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes);
    }

    // ── Habitaciones ─────────────────────────────────────────────────────────

    public Habitacion registrarHabitacion(int numero, TipoHabitacion tipo, double precio) {
        if (buscarHabitacionPorNumero(numero).isPresent())
            throw new IllegalArgumentException("Ya existe una habitación con el número: " + numero);
        Habitacion h = new Habitacion(numero, tipo, precio);
        habitaciones.add(h);
        return h;
    }

    public Optional<Habitacion> buscarHabitacionPorNumero(int numero) {
        return habitaciones.stream().filter(h -> h.getNumero() == numero).findFirst();
    }

    public List<Habitacion> listarHabitacionesDisponibles() {
        List<Habitacion> disponibles = new ArrayList<>();
        for (Habitacion h : habitaciones) {
            if (h.isDisponible()) disponibles.add(h);
        }
        return disponibles;
    }

    public List<Habitacion> listarHabitaciones() {
        return new ArrayList<>(habitaciones);
    }

    // ── Reservas ─────────────────────────────────────────────────────────────

    public Reserva crearReserva(String dniCliente, int numeroHabitacion,
                                LocalDate fechaEntrada, LocalDate fechaSalida) {
        Cliente c = buscarClientePorDni(dniCliente)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + dniCliente));
        Habitacion h = buscarHabitacionPorNumero(numeroHabitacion)
                .orElseThrow(() -> new IllegalArgumentException("Habitación no encontrada: " + numeroHabitacion));

        Reserva r = new Reserva(c, h, fechaEntrada, fechaSalida);
        h.setEstado(EstadoHabitacion.OCUPADA);
        reservas.add(r);
        return r;
    }

    public void cancelarReserva(String idReserva) {
        Reserva r = buscarReservaPorId(idReserva)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada: " + idReserva));
        r.cancelar();
        r.getHabitacion().setEstado(EstadoHabitacion.DISPONIBLE);
    }

    public void realizarCheckIn(String idReserva) {
        Reserva r = buscarReservaPorId(idReserva)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada: " + idReserva));
        r.activar();
    }

    public Factura realizarCheckOut(String idReserva) {
        Reserva r = buscarReservaPorId(idReserva)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada: " + idReserva));
        r.finalizar();
        r.getHabitacion().setEstado(EstadoHabitacion.DISPONIBLE);
        Factura f = new Factura(r);
        facturas.add(f);
        return f;
    }

    public Optional<Reserva> buscarReservaPorId(String id) {
        return reservas.stream().filter(r -> r.getId().equals(id)).findFirst();
    }

    public List<Reserva> listarReservas() {
        return new ArrayList<>(reservas);
    }

    // ── Facturas ──────────────────────────────────────────────────────────────

    public List<Factura> listarFacturas() {
        return new ArrayList<>(facturas);
    }
}
