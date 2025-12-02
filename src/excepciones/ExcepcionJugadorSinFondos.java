package excepciones;

// Nivel 3 jerarquia - El jugador no tiene fondos suficientes para realizar un pago
public class ExcepcionJugadorSinFondos extends ExcepcionJugador {

    private final String nombreJugador;
    private final float cantidadRequerida;
    private final float fondosActuales;
    private final String motivo;

    public ExcepcionJugadorSinFondos(String jugador, float requerida, float actuales, String motivo) {
        super("El jugador '" + jugador + "' no tiene fondos para " + motivo +
                ". Necesita " + requerida + "€ pero tiene " + actuales + "€");
        this.nombreJugador = jugador;
        this.cantidadRequerida = requerida;
        this.fondosActuales = actuales;
        this.motivo = motivo;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public float getCantidadRequerida() {
        return cantidadRequerida;
    }

    public float getFondosActuales() {
        return fondosActuales;
    }

    public String getMotivo() {
        return motivo;
    }
}