package excepciones;

//Nivel 3 de jerarquia - Cuando un jugador no tiene fondos suficientes para realizar una operacion
public class ExcepcionFondosInsuficientes extends ExcepcionEdificacion {

    private final String nombreJugador;
    private final float fondosNecesarios;
    private final float fondosActuales;
    private final String tipoOperacion;

    public ExcepcionFondosInsuficientes(String jugador, float necesarios, float actuales, String operacion) {
        super("El jugador '" + jugador + "' no tiene fondos suficientes para " + operacion +
                ". Necesita " + necesarios + "€ pero tiene " + actuales + "€");
        this.nombreJugador = jugador;
        this.fondosNecesarios = necesarios;
        this.fondosActuales = actuales;
        this.tipoOperacion = operacion;
    }

    public ExcepcionFondosInsuficientes(String jugador, float necesarios, float actuales) {
        this(jugador, necesarios, actuales, "edificar");
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public float getFondosNecesarios() {
        return fondosNecesarios;
    }

    public float getFondosActuales() {
        return fondosActuales;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }
}