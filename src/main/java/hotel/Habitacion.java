package hotel;

public class Habitacion {
    private final int numero;
    private final TipoHabitacion tipo;
    private final double precioPorNoche;
    private EstadoHabitacion estado;

    public Habitacion(int numero, TipoHabitacion tipo, double precioPorNoche) {
        if (numero <= 0)
            throw new IllegalArgumentException("El número de habitación debe ser positivo");
        if (tipo == null)
            throw new IllegalArgumentException("El tipo de habitación no puede ser nulo");
        if (precioPorNoche <= 0)
            throw new IllegalArgumentException("El precio por noche debe ser positivo");

        this.numero         = numero;
        this.tipo           = tipo;
        this.precioPorNoche = precioPorNoche;
        this.estado         = EstadoHabitacion.DISPONIBLE;
    }

    public int getNumero()               { return numero; }
    public TipoHabitacion getTipo()      { return tipo; }
    public double getPrecioPorNoche()    { return precioPorNoche; }
    public EstadoHabitacion getEstado()  { return estado; }

    public boolean isDisponible() {
        return estado == EstadoHabitacion.DISPONIBLE;
    }

    public void setEstado(EstadoHabitacion estado) {
        if (estado == null)
            throw new IllegalArgumentException("El estado no puede ser nulo");
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Habitacion[" + numero + " - " + tipo + " - S/." + precioPorNoche + " - " + estado + "]";
    }
}
