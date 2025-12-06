package excepciones;

public class ExcepcionAccionNoPermitida extends ExcepcionAccion {

    private final String accion;
    private final String contexto;
    private final String razon;

    public ExcepcionAccionNoPermitida(String accion, String contexto, String razon) {
        super("Acción '" + accion + "' no permitida en " + contexto + ": " + razon);
        this.accion = accion;
        this.contexto = contexto;
        this.razon = razon;
    }

    public ExcepcionAccionNoPermitida(String accion, String razon) {
        this(accion, "el contexto actual", razon);
    }

    public String getAccion() {
        return accion;
    }

    public String getContexto() {
        return contexto;
    }

    public String getRazon() {
        return razon;
    }
}