package excepciones;

// Nivel 3 jerarquia - Una propiedad no está disponible para el trato
public class ExcepcionPropiedadTratoNoDisponible extends ExcepcionTrato {

    private final String nombrePropiedad;
    private final String nombreJugador;
    private final String motivo;

    public ExcepcionPropiedadTratoNoDisponible(String propiedad, String jugador, String motivo) {
        super("La propiedad '" + propiedad + "' no está disponible para el trato con " +
                jugador + ": " + motivo);
        this.nombrePropiedad = propiedad;
        this.nombreJugador = jugador;
        this.motivo = motivo;
    }

    public String getNombrePropiedad() {
        return nombrePropiedad;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public String getMotivo() {
        return motivo;
    }
}
