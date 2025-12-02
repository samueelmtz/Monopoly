package excepciones;

// Nivel 3 jerarquia -
public class ExcepcionTratoInvalido extends ExcepcionTrato {

    private final String idTrato;
    private final String razon;

    public ExcepcionTratoInvalido(String idTrato, String razon) {
        super("El trato '" + idTrato + "' no puede llevarse a cabo: " + razon);
        this.idTrato = idTrato;
        this.razon = razon;
    }

    public ExcepcionTratoInvalido(String razon) {
        this("", razon);
    }

    public String getIdTrato() {
        return idTrato;
    }

    public String getRazon() {
        return razon;
    }
}