package excepciones;

//Nivel 3 de jerarquia - Cuando ya hay un edificio en otra casilla del mismo grupo.
public class ExcepcionLimiteGrupoEdificios extends ExcepcionEdificacion {

    private final String nombreGrupo;
    private final String propiedadYaEdificada;
    private final String propiedadIntentada;

    public ExcepcionLimiteGrupoEdificios(String grupo, String yaEdificada, String intentada) {
        super("No se puede edificar en '" + intentada + "' porque ya hay edificios en '" +
                yaEdificada + "' del grupo '" + grupo + "'. Solo se permite edificar en una propiedad por grupo.");
        this.nombreGrupo = grupo;
        this.propiedadYaEdificada = yaEdificada;
        this.propiedadIntentada = intentada;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public String getPropiedadYaEdificada() {
        return propiedadYaEdificada;
    }

    public String getPropiedadIntentada() {
        return propiedadIntentada;
    }
}