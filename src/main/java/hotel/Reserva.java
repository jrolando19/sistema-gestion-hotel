package hotel;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reserva {
    private static int contadorId = 1;

    private final String id;
    private final Cliente cliente;
    private final Habitacion habitacion;
    private final LocalDate fechaEntrada;
    private final LocalDate fechaSalida;
    private EstadoReserva estado;

    public Reserva(Cliente cliente, Habitacion habitacion,
                   LocalDate fechaEntrada, LocalDate fechaSalida) {
        if (cliente == null)
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        if (habitacion == null)
            throw new IllegalArgumentException("La habitación no puede ser nula");
        if (fechaEntrada == null)
            throw new IllegalArgumentException("La fecha de entrada no puede ser nula");
        if (fechaSalida == null)
            throw new IllegalArgumentException("La fecha de salida no puede ser nula");
        if (!fechaSalida.isAfter(fechaEntrada))
            throw new IllegalArgumentException("La fecha de salida debe ser posterior a la fecha de entrada");
        if (!habitacion.isDisponible())
            throw new IllegalStateException("La habitación no está disponible para reservar");

        this.id           = "RES-" + (contadorId++);
        this.cliente      = cliente;
        this.habitacion   = habitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida  = fechaSalida;
        this.estado       = EstadoReserva.CONFIRMADA;
    }

    public String getId()                { return id; }
    public Cliente getCliente()          { return cliente; }
    public Habitacion getHabitacion()    { return habitacion; }
    public LocalDate getFechaEntrada()   { return fechaEntrada; }
    public LocalDate getFechaSalida()    { return fechaSalida; }
    public EstadoReserva getEstado()     { return estado; }

    public long getNochesReservadas() {
        return ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
    }

    public double calcularCostoTotal() {
        return habitacion.getPrecioPorNoche() * getNochesReservadas();
    }

    public void confirmar() {
        if (estado != EstadoReserva.CONFIRMADA)
            throw new IllegalStateException("Solo se puede confirmar una reserva en estado CONFIRMADA");
        this.estado = EstadoReserva.CONFIRMADA;
    }

    public void cancelar() {
        if (estado == EstadoReserva.FINALIZADA || estado == EstadoReserva.CANCELADA)
            throw new IllegalStateException("No se puede cancelar una reserva ya finalizada o cancelada");
        this.estado = EstadoReserva.CANCELADA;
    }

    public void activar() {
        if (estado != EstadoReserva.CONFIRMADA)
            throw new IllegalStateException("Solo se puede activar una reserva confirmada");
        this.estado = EstadoReserva.ACTIVA;
    }

    public void finalizar() {
        if (estado != EstadoReserva.ACTIVA)
            throw new IllegalStateException("Solo se puede finalizar una reserva activa");
        this.estado = EstadoReserva.FINALIZADA;
    }

    // Reset del contador para pruebas
    public static void resetContador() {
        contadorId = 1;
    }

    @Override
    public String toString() {
        return "Reserva[" + id + " - " + cliente.getNombreCompleto()
                + " - Hab." + habitacion.getNumero()
                + " - " + fechaEntrada + " a " + fechaSalida
                + " - " + estado + "]";
    }
}
