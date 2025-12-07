package excepciones;

// Nivel 3 jerarquia - Comando no reconocido por el sistema
public class ExcepcionComandoNoReconocido extends ExcepcionAccion {

    // Nuevo constructor con formato sugerido
    public ExcepcionComandoNoReconocido(String formatoCorrecto, String comandoIncorrecto) {
        super("Comando incorrecto: '" + comandoIncorrecto + "'. " +
                "Formato correcto: " + formatoCorrecto + ".");
    }
}