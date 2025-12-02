package excepciones;

//Nivel 3 de jerarquia - Cuando no se cumplen los requisitos previos para construir un edificio
public class ExcepcionRequisitosPrevios extends ExcepcionEdificacion {

    private final String tipoEdificio;
    private final String requisitoFaltante;

    public ExcepcionRequisitosPrevios(String edificio, String requisito) {
        super("Para construir " + edificio + " se requiere primero: " + requisito);
        this.tipoEdificio = edificio;
        this.requisitoFaltante = requisito;
    }

    public String getTipoEdificio() {
        return tipoEdificio;
    }

    public String getRequisitoFaltante() {
        return requisitoFaltante;
    }
}
