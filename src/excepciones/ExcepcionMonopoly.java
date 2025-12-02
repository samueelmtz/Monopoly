package excepciones;

//Nivel 1 de jerarquía
public abstract class ExcepcionMonopoly extends Exception {
    protected ExcepcionMonopoly(String mensaje) {
        super("Excepcion " +mensaje);
    }
}