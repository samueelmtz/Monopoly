package excepciones;

// Nivel 3 jerarquia - El trato ya ha sido aceptado y no puede ser modificado o eliminado
public class ExcepcionTratoYaAceptado extends ExcepcionTrato {

    private final String idTrato;

    public ExcepcionTratoYaAceptado(String idTrato) {
        super("El trato '" + idTrato + "' ya ha sido aceptado y no puede ser modificado o eliminado");
        this.idTrato = idTrato;
    }

    public String getIdTrato() {
        return idTrato;
    }
}