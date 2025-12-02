package excepciones;

// Nivel 3 jerarquia - Se ha alcanzado el número máximo de jugadores permitidos (4)
public class ExcepcionMaxJugadores extends ExcepcionJugador {

    private final int maxJugadores;
    private final int jugadoresActuales;

    public ExcepcionMaxJugadores(int max, int actuales) {
        super("No se pueden añadir más jugadores. Máximo permitido: " + max +
                " (actuales: " + actuales + ")");
        this.maxJugadores = max;
        this.jugadoresActuales = actuales;
    }

    public int getMaxJugadores() {
        return maxJugadores;
    }

    public int getJugadoresActuales() {
        return jugadoresActuales;
    }
}