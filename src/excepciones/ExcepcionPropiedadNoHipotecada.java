package excepciones;

//Nivel 3 de jerarquía - Se intenta deshipotecar una propiedad que no está hipotecada
public class ExcepcionPropiedadNoHipotecada extends ExcepcionPropiedad {

    private final String nombrePropiedad;

    public ExcepcionPropiedadNoHipotecada(String propiedad) {
        super("La propiedad '" + propiedad + "' no está hipotecada");
        this.nombrePropiedad = propiedad;
    }

    public String getNombrePropiedad() {
        return nombrePropiedad;
    }
}