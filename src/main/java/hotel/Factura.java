package hotel;

import java.time.LocalDateTime;

public class Factura {
    private static int contadorFactura = 1;

    private final String numeroFactura;
    private final Reserva reserva;
    private final double subtotal;
    private final double igv;
    private final double total;
    private final LocalDateTime fechaEmision;

    private static final double TASA_IGV = 0.18;

    public Factura(Reserva reserva) {
        if (reserva == null)
            throw new IllegalArgumentException("La reserva no puede ser nula para generar factura");
        if (reserva.getEstado() != EstadoReserva.FINALIZADA)
            throw new IllegalStateException("Solo se puede facturar una reserva finalizada");

        this.numeroFactura = "FAC-" + String.format("%04d", contadorFactura++);
        this.reserva       = reserva;
        this.subtotal      = reserva.calcularCostoTotal();
        this.igv           = Math.round(subtotal * TASA_IGV * 100.0) / 100.0;
        this.total         = Math.round((subtotal + igv) * 100.0) / 100.0;
        this.fechaEmision  = LocalDateTime.now();
    }

    public String getNumeroFactura()    { return numeroFactura; }
    public Reserva getReserva()         { return reserva; }
    public double getSubtotal()         { return subtotal; }
    public double getIgv()              { return igv; }
    public double getTotal()            { return total; }
    public LocalDateTime getFechaEmision() { return fechaEmision; }

    public static void resetContador() {
        contadorFactura = 1;
    }

    @Override
    public String toString() {
        return "Factura[" + numeroFactura
                + " - " + reserva.getCliente().getNombreCompleto()
                + " - Total: S/." + total
                + " - " + fechaEmision + "]";
    }
}
