package excepciones;

//Nivel 3 de jerarquia - Cuando se alcanza el maximo de edificios permitidos en una propiedad
public class ExcepcionMaxEdificiosAlcanzado extends ExcepcionEdificacion {

    private final String nombrePropiedad;
    private final String tipoEdificio;
    private final int maxPermitido;
    private final int cantidadActual;

    public ExcepcionMaxEdificiosAlcanzado(String propiedad, String tipo, int max, int actual) {
        super("No se pueden construir más " + tipo + " en '" + propiedad +
                "'. Máximo permitido: " + max + " (actual: " + actual + ")");
        this.nombrePropiedad = propiedad;
        this.tipoEdificio = tipo;
        this.maxPermitido = max;
        this.cantidadActual = actual;
    }

    public String getNombrePropiedad() {
        return nombrePropiedad;
    }

    public String getTipoEdificio() {
        return tipoEdificio;
    }

    public int getMaxPermitido() {
        return maxPermitido;
    }

    public int getCantidadActual() {
        return cantidadActual;
    }
}
