package excepciones;

//Nivel 3 de jerarquia - Cuando se incumplen las reglas de edificacion en una propiedad
public class ExcepcionReglasEdificacion extends ExcepcionEdificacion {

    private final String tipoEdificio;
    private final String nombrePropiedad;
    private final String reglaIncumplida;

    public ExcepcionReglasEdificacion(String tipoEdificio, String propiedad, String reglaIncumplida) {
        super("No se puede construir " + tipoEdificio + " en '" + propiedad +
                "'. Regla violada: " + reglaIncumplida);
        this.tipoEdificio = tipoEdificio;
        this.nombrePropiedad = propiedad;
        this.reglaIncumplida = reglaIncumplida;
    }

    public ExcepcionReglasEdificacion(String mensaje) {
        super(mensaje);
        this.tipoEdificio = "";
        this.nombrePropiedad = "";
        this.reglaIncumplida = "";
    }

    public String getTipoEdificio() {
        return tipoEdificio;
    }

    public String getNombrePropiedad() {
        return nombrePropiedad;
    }

    public String getReglaViolada() {
        return reglaIncumplida;
    }
}
