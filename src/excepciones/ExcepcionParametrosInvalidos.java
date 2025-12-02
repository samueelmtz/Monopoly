package excepciones;

// Nivel 3 jerarquia - Parámetros inválidos para un comando
public class ExcepcionParametrosInvalidos extends ExcepcionAccion {

    private final String comando;
    private final String parametros;
    private final String error;

    public ExcepcionParametrosInvalidos(String comando, String parametros, String error) {
        super("Parámetros inválidos para el comando '" + comando +
                "' con parámetros: " + parametros + ". Error: " + error);
        this.comando = comando;
        this.parametros = parametros;
        this.error = error;
    }

    public String getComando() {
        return comando;
    }

    public String getParametros() {
        return parametros;
    }

    public String getError() {
        return error;
    }
}