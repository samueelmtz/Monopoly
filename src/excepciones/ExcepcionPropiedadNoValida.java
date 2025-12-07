package excepciones;

// Nivel 3 de jerarquía - Se intenta realizar una operación en una propiedad cuando no es válido
public class ExcepcionPropiedadNoValida extends ExcepcionPropiedad {

    private final String nombrePropiedad;
    private final String nombreJugador;
    private final String operacionIntentada;

    public ExcepcionPropiedadNoValida(String propiedad, String jugador, String operacion) {
        super("El jugador '" + jugador + "' no puede " + operacion + " la propiedad '" + propiedad +
                "' porque no está en esa casilla.");
        this.nombrePropiedad = propiedad;
        this.nombreJugador = jugador;
        this.operacionIntentada = operacion;
    }

    public ExcepcionPropiedadNoValida(String propiedad, String jugador) {
        this(propiedad, jugador, "comprar");
    }

    public String getNombrePropiedad() {
        return nombrePropiedad;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public String getOperacionIntentada() {
        return operacionIntentada;
    }
}
