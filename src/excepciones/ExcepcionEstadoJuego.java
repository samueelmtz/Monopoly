package excepciones;

//Nivel 2 de jerarquía
public abstract class ExcepcionEstadoJuego extends ExcepcionMonopoly {

    protected ExcepcionEstadoJuego(String mensaje) {
        super("EstadoJuego: " + mensaje);
    }
}