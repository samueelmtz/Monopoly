package excepciones;

//Nivel 3 de jerarquía - Se intenta realizar una operación no permitida en una propiedad hipotecada
public class ExcepcionPropiedadHipotecada extends ExcepcionPropiedad {

    private final String nombrePropiedad;

    public ExcepcionPropiedadHipotecada(String propiedad) {
        super("La propiedad '" + propiedad + "' está hipotecada. Operación no permitida.");
        this.nombrePropiedad = propiedad;
    }

    public String getNombrePropiedad() {
        return nombrePropiedad;
    }
}