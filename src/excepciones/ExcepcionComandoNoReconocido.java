package excepciones;

// Nivel 3 jerarquia - Comando no reconocido por el sistema
public class ExcepcionComandoNoReconocido extends ExcepcionAccion {

    private final String comando;

    public ExcepcionComandoNoReconocido(String comando) {
        super("Comando no reconocido: '" + comando + "'");
        this.comando = comando;
    }

    public String getComando() {
        return comando;
    }
}