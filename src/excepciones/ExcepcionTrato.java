package excepciones;

//Nivel 2 de jerarquía
public abstract class ExcepcionTrato extends ExcepcionMonopoly {

    protected ExcepcionTrato(String mensaje) {
        super("Trato: " + mensaje);
    }
}