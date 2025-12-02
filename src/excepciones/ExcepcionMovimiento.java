package excepciones;

//Nivel 2 de jerarquía
public abstract class ExcepcionMovimiento extends ExcepcionMonopoly {

    protected ExcepcionMovimiento(String mensaje) {
        super("Movimiento: " + mensaje);
    }
}
