package excepciones;

//Nivel 3 de jerarquía - Acción sobre propiedad que no pertenece al jugador
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
