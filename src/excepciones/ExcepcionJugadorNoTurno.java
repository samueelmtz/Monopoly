package excepciones;

// Nivel 3 jerarquia - El jugador intenta realizar una acción fuera de su turno
public class ExcepcionJugadorNoTurno extends ExcepcionJugador {

    private final String nombreJugador;
    private final String jugadorTurnoActual;

    public ExcepcionJugadorNoTurno(String jugador, String turnoActual) {
        super("El jugador '" + jugador + "' no tiene el turno. Turno actual: " + turnoActual);
        this.nombreJugador = jugador;
        this.jugadorTurnoActual = turnoActual;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public String getJugadorTurnoActual() {
        return jugadorTurnoActual;
    }
}