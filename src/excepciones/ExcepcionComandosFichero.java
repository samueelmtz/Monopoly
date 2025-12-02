package excepciones;

// Nivel 3 jerarquia - Error en el fichero de comandos
public class ExcepcionComandosFichero extends ExcepcionAccion {

    private final String nombreFichero;
    private final int numeroLinea;
    private final String error;

    public ExcepcionComandosFichero(String fichero, int linea, String error) {
        super("Error en fichero de comandos '" + fichero +
                "' en línea " + linea + ": " + error);
        this.nombreFichero = fichero;
        this.numeroLinea = linea;
        this.error = error;
    }

    public String getNombreFichero() {
        return nombreFichero;
    }

    public int getNumeroLinea() {
        return numeroLinea;
    }

    public String getError() {
        return error;
    }
}
