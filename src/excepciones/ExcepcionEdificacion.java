package excepciones;

//Nivel 2 de jerarquía
public abstract class ExcepcionEdificacion extends ExcepcionMonopoly {

    protected ExcepcionEdificacion(String mensaje) {
        super("Edificacion: " + mensaje);
    }
}
