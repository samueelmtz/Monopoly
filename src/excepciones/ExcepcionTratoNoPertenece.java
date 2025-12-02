package excepciones;

// Nivel 3 jerarquia - El trato no pertenece al jugador que intenta modificarlo o eliminarlo
public class ExcepcionTratoNoPertenece extends ExcepcionTrato {

    private final String idTrato;
    private final String nombreJugador;
    private final String nombrePropietarioTrato;

    public ExcepcionTratoNoPertenece(String idTrato, String jugador, String propietario) {
        super("El jugador '" + jugador + "' no puede modificar el trato '" + idTrato +
                "' porque pertenece a '" + propietario + "'");
        this.idTrato = idTrato;
        this.nombreJugador = jugador;
        this.nombrePropietarioTrato = propietario;
    }

    public String getIdTrato() {
        return idTrato;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public String getNombrePropietarioTrato() {
        return nombrePropietarioTrato;
    }
}
