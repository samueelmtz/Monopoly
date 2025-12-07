package excepciones;

// Nivel 3 de jerarquía - Se intenta cobrar un bote/parking que está vacío
public class ExcepcionBoteVacio extends ExcepcionAccion {
    private final String jugador;

    public ExcepcionBoteVacio(String jugador) {
        super("El jugador '" + jugador + "' no puede cobrar el bote porque está vacío (0€).");
        this.jugador = jugador;
    }

    // Getters
    public String getJugador() {
        return jugador;
    }
}