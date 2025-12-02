package excepciones;

//Nivel 2 de jerarquía
public abstract class ExcepcionJugador extends ExcepcionMonopoly {

    protected ExcepcionJugador(String mensaje) {
        super("Jugador: " + mensaje);
    }
}