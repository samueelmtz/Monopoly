package excepciones;

public class ExcepcionValidacionEdificacion extends ExcepcionEdificacion {

    private final String tipoEdificio;
    private final String nombrePropiedad;
    private final String reglaViolada;

    public ExcepcionValidacionEdificacion(String tipoEdificio, String propiedad, String reglaViolada) {
        super("No se puede construir " + tipoEdificio + " en '" + propiedad + "'. " + reglaViolada);
        this.tipoEdificio = tipoEdificio;
        this.nombrePropiedad = propiedad;
        this.reglaViolada = reglaViolada;
    }

    public String getTipoEdificio() {
        return tipoEdificio;
    }

    public String getNombrePropiedad() {
        return nombrePropiedad;
    }

    public String getReglaViolada() {
        return reglaViolada;
    }
}