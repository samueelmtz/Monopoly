package excepciones;

//Nivel 3 de jerarquía - Se intenta hipotecar una propiedad que tiene edificios
public class ExcepcionPropiedadConEdificios extends ExcepcionPropiedad {

    private final String nombrePropiedad;
    private final int cantidadEdificios;

    public ExcepcionPropiedadConEdificios(String propiedad, int edificios) {
        super("No se puede hipotecar '" + propiedad +
                "' porque tiene " + edificios + " edificio(s). Debe venderlos primero.");
        this.nombrePropiedad = propiedad;
        this.cantidadEdificios = edificios;
    }

    public String getNombrePropiedad() {
        return nombrePropiedad;
    }

    public int getCantidadEdificios() {
        return cantidadEdificios;
    }
}