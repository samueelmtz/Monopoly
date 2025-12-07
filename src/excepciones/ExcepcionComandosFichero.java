package excepciones;

// Nivel 3 jerarquia - Error en el fichero de comandos
public class ExcepcionComandosFichero extends ExcepcionAccion {

    private final String nombreFichero;
    private final String error;

    public ExcepcionComandosFichero(String fichero, String error) {
        super("Error en fichero de comandos '" + fichero + "': " + error);
        this.nombreFichero = fichero;
        this.error = error;
    }

    public String getNombreFichero() {
        return nombreFichero;
    }

    public String getError() {
        return error;
    }
}
