package excepciones;

// Nivel 3 jerarquia - El jugador no existe en el sistema
public class ExcepcionJugadorNoExistente extends ExcepcionJugador {

    private final String nombreJugador;

    public ExcepcionJugadorNoExistente(String jugador) {
        super("El jugador '" + jugador + "' no existe");
        this.nombreJugador = jugador;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }
}
