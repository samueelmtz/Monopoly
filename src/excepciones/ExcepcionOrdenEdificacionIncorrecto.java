package excepciones;

//Nivel 3 de jerarquia - Cuando se intenta construir un edificio sin respetar el orden correcto (Casa-Hotel-Piscina_PistaDeporte)
public class ExcepcionOrdenEdificacionIncorrecto extends ExcepcionEdificacion {

    private final String tipoEdificioIntentado;
    private final String tipoEdificioRequerido;

    public ExcepcionOrdenEdificacionIncorrecto(String intentado, String requerido) {
        super("No se puede construir " + intentado +
                " sin tener primero " + requerido);
        this.tipoEdificioIntentado = intentado;
        this.tipoEdificioRequerido = requerido;
    }

    public String getTipoEdificioIntentado() {
        return tipoEdificioIntentado;
    }

    public String getTipoEdificioRequerido() {
        return tipoEdificioRequerido;
    }
}
