package excepciones;

//Nivel 3 de jerarquía - Se intenta hipotecar una propiedad ya hipotecada
public class ExcepcionPropiedadYaHipotecada extends ExcepcionPropiedad {

    private final String nombrePropiedad;

    public ExcepcionPropiedadYaHipotecada(String propiedad) {
        super("La propiedad '" + propiedad + "' ya está hipotecada");
        this.nombrePropiedad = propiedad;
    }

    public String getNombrePropiedad() {
        return nombrePropiedad;
    }
}