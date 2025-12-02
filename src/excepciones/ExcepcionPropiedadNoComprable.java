package excepciones;

//Nivel 3 de jerarquía - Se intenta comprar una casilla que no es una propiedad
public class ExcepcionPropiedadNoComprable extends ExcepcionPropiedad {

    private final String nombreCasilla;

    public ExcepcionPropiedadNoComprable(String casilla) {
        super("La casilla '" + casilla + "' no es una propiedad comprable");
        this.nombreCasilla = casilla;
    }

    public String getNombreCasilla() {
        return nombreCasilla;
    }
}