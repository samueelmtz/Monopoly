package excepciones;

public class ExcepcionFormatoDadosIncorrecto extends ExcepcionMonopoly {

    // Constructor general (para cualquier mensaje)
    public ExcepcionFormatoDadosIncorrecto(String mensaje) {
        super(" " + mensaje);
    }
}