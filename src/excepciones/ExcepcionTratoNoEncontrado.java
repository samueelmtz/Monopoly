package excepciones;

public class ExcepcionTratoNoEncontrado extends ExcepcionTrato {

    private final String idTrato;

    public ExcepcionTratoNoEncontrado(String idTrato) {
        super("No se encontró el trato con ID: '" + idTrato + "'");
        this.idTrato = idTrato;
    }

    public String getIdTrato() {
        return idTrato;
    }
}