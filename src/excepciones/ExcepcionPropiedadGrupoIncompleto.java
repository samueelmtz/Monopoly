package excepciones;

//Nivel 3 de jerarquía - Se intenta edificar en una propiedad sin tener todo el grupo
public class ExcepcionPropiedadGrupoIncompleto extends ExcepcionPropiedad {

    private final String nombrePropiedad;
    private final String nombreGrupo;

    public ExcepcionPropiedadGrupoIncompleto(String propiedad, String grupo) {
        super("No se puede edificar en '" + propiedad +
                "' porque no se tienen todas las propiedades del grupo '" + grupo + "'");
        this.nombrePropiedad = propiedad;
        this.nombreGrupo = grupo;
    }

    public String getNombrePropiedad() {
        return nombrePropiedad;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }
}