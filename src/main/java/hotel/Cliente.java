package hotel;

public class Cliente {
    private final String dni;
    private final String nombre;
    private final String apellido;
    private final String telefono;
    private final String email;

    public Cliente(String dni, String nombre, String apellido, String telefono, String email) {
        if (dni == null || dni.trim().isEmpty())
            throw new IllegalArgumentException("El DNI del cliente no puede ser vacío");
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre del cliente no puede ser vacío");
        if (apellido == null || apellido.trim().isEmpty())
            throw new IllegalArgumentException("El apellido del cliente no puede ser vacío");
        if (telefono == null || telefono.trim().isEmpty())
            throw new IllegalArgumentException("El teléfono del cliente no puede ser vacío");
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("El email del cliente no es válido");

        this.dni      = dni.trim();
        this.nombre   = nombre.trim();
        this.apellido = apellido.trim();
        this.telefono = telefono.trim();
        this.email    = email.trim();
    }

    public String getDni()      { return dni; }
    public String getNombre()   { return nombre; }
    public String getApellido() { return apellido; }
    public String getTelefono() { return telefono; }
    public String getEmail()    { return email; }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public String toString() {
        return "Cliente[" + dni + " - " + getNombreCompleto() + "]";
    }
}
