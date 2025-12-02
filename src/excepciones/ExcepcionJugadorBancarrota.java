package excepciones;

// Nivel 3 jerarquia - El jugador está en bancarrota y no puede realizar acciones
public class ExcepcionJugadorBancarrota extends ExcepcionJugador {

    private final String nombreJugador;

    public ExcepcionJugadorBancarrota(String jugador) {
        super("El jugador '" + jugador + "' está en bancarrota y no puede realizar acciones");
        this.nombreJugador = jugador;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }
}