package excepciones;

//Nivel 3 de jerarquía - Casilla de propiedad ya comprada por otro jugador
public class ExcepcionPropiedadYaComprada extends ExcepcionPropiedad {

    private final String nombrePropiedad;
    private final String nombrePropietario;

    public ExcepcionPropiedadYaComprada(String propiedad, String propietario) {
        super("La propiedad '" + propiedad + "' ya pertenece a '" + propietario + "'");
        this.nombrePropiedad = propiedad;
        this.nombrePropietario = propietario;
    }

    public String getNombrePropiedad() {
        return nombrePropiedad;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }
}