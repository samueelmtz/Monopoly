package excepciones;

// Nivel 3 jerarquia - El jugador ha sacado dados dobles tres veces seguidas
public class ExcepcionDadosDoblesTresVeces extends ExcepcionMovimiento {

    private final String jugador;

    public ExcepcionDadosDoblesTresVeces(String jugador) {
        super("El jugador '" + jugador + "' ha sacado dados dobles tres veces seguidas. " +
                "Debe ir directamente a la cárcel");
        this.jugador = jugador;
    }

    public String getJugador() {
        return jugador;
    }
}
