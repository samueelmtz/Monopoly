package excepciones;

// Nivel 3 jerarquia - El jugador está en la cárcel y no puede lanzar dados
public class ExcepcionJugadorEnCarcel extends ExcepcionJugador {

    private final String nombreJugador;
    private final int turnosRestantes;

    public ExcepcionJugadorEnCarcel(String jugador, int turnosRestantes) {
        super("El jugador '" + jugador + "' está en la cárcel. Turnos restantes: " + turnosRestantes);
        this.nombreJugador = jugador;
        this.turnosRestantes = turnosRestantes;
    }


    public String getNombreJugador() {
        return nombreJugador;
    }

    public int getTurnosRestantes() {
        return turnosRestantes;
    }
}
