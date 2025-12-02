package excepciones;

// Nivel 3 jerarquia - Fondos insuficientes para aceptar un trato
public class ExcepcionFondosInsuficientesTrato extends ExcepcionTrato {

    private final String nombreJugador;
    private final float fondosNecesarios;
    private final float fondosActuales;
    private final String idTrato;

    public ExcepcionFondosInsuficientesTrato(String jugador, float necesarios, float actuales, String idTrato) {
        super("El jugador '" + jugador + "' no tiene fondos suficientes para aceptar el trato '" +
                idTrato + "'. Necesita " + necesarios + "€ pero tiene " + actuales + "€");
        this.nombreJugador = jugador;
        this.fondosNecesarios = necesarios;
        this.fondosActuales = actuales;
        this.idTrato = idTrato;
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

    public String getIdTrato() {
        return idTrato;
    }
}
