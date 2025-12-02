package excepciones;

// Nivel 3 jerarquia - El jugador no tiene fondos suficientes para pagar la fianza y salir de la cárcel
public class ExcepcionSalirCarcelSinFondos extends ExcepcionJugador {

    private final String nombreJugador;
    private final float fianza;
    private final float fondosActuales;

    public ExcepcionSalirCarcelSinFondos(String jugador, float fianza, float fondos) {
        super("El jugador '" + jugador + "' no puede salir de la cárcel. " +
                "Fianza: " + fianza + "€, fondos disponibles: " + fondos + "€");
        this.nombreJugador = jugador;
        this.fianza = fianza;
        this.fondosActuales = fondos;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public float getFianza() {
        return fianza;
    }

    public float getFondosActuales() {
        return fondosActuales;
    }
}