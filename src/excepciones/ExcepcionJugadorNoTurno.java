package excepciones;

// Nivel 3 jerarquia - El jugador intenta realizar una acción fuera de su turno
public class ExcepcionJugadorNoTurno extends ExcepcionMonopoly {
    public ExcepcionJugadorNoTurno(String nombreJugador) {
        super("El jugador " + nombreJugador + " no puede lanzar dados. " +
                "Ya ha lanzado en este turno. Usa 'acabar turno'.");
    }
}