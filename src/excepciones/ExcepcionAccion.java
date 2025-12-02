package excepciones;

//Nivel 2 de jerarquía
public abstract class ExcepcionAccion extends ExcepcionMonopoly {

    protected ExcepcionAccion(String mensaje) {
        super("Accion: " + mensaje);
    }
}