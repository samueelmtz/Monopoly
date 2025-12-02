package excepciones;

//Nivel 2 de jerarquía
public class ExcepcionPropiedad extends ExcepcionMonopoly {

    public ExcepcionPropiedad(String mensaje) {
        super("Propiedad: " + mensaje);
    }
}
