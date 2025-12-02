package excepciones;

// Nivel 3 jerarquia - Ya existe un jugador con el mismo nombre
public class ExcepcionJugadorNombreDuplicado extends ExcepcionJugador {

    private final String nombreJugador;

    public ExcepcionJugadorNombreDuplicado(String jugador) {
        super("Ya existe un jugador con el nombre '" + jugador + "'");
        this.nombreJugador = jugador;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }
}