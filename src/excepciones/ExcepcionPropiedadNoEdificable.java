package excepciones;

//Nivel 3 de jerarquía - Se intenta edificar en una propiedad no edificable (Solar)
public class ExcepcionPropiedadNoEdificable extends ExcepcionPropiedad {

    private final String nombrePropiedad;
    private final String tipoPropiedad;

    public ExcepcionPropiedadNoEdificable(String propiedad, String tipo) {
        super("No se puede edificar en '" + propiedad +
                "' porque es de tipo '" + tipo + "'");
        this.nombrePropiedad = propiedad;
        this.tipoPropiedad = tipo;
    }

    public String getNombrePropiedad() {
        return nombrePropiedad;
    }

    public String getTipoPropiedad() {
        return tipoPropiedad;
    }
}
