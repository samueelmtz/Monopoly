package excepciones;

//Nivel 3 de jerarquía
public class ExcepcionPropiedadNoPertenece extends ExcepcionPropiedad {

    private final String nombreJugador;
    private final String nombrePropiedad;

    public ExcepcionPropiedadNoPertenece(String jugador, String propiedad) {
        super("La propiedad '" + propiedad + "' no pertenece al jugador '" + jugador + "'");
        this.nombreJugador = jugador;
        this.nombrePropiedad = propiedad;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public String getNombrePropiedad() {
        return nombrePropiedad;
    }
}
