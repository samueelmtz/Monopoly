package excepciones;

// Nivel 3 de jerarquía - Se intenta hipotecar una propiedad que tiene edificaciones
public class ExcepcionExistenEdificios extends ExcepcionPropiedad {

    private final String nombrePropiedad;
    private final String nombreJugador;

    public ExcepcionExistenEdificios(String propiedad, String jugador) {
        super("No se puede hipotecar '" + propiedad + "' porque tiene edificios construidos. Debes venderlos primero.");
        this.nombrePropiedad = propiedad;
        this.nombreJugador = jugador;
    }

    // Getters
    public String getNombrePropiedad() {
        return nombrePropiedad;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }
}