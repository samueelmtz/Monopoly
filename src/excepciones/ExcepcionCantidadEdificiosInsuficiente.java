package excepciones;

//Nivel 3 de jerarquia - Cuando se intenta vender mas edificios de los que existen en una propiedad
public class ExcepcionCantidadEdificiosInsuficiente extends ExcepcionEdificacion {

    private final String nombrePropiedad;
    private final String tipoEdificio;
    private final int cantidadSolicitada;
    private final int cantidadDisponible;

    public ExcepcionCantidadEdificiosInsuficiente(String propiedad, String tipo, int solicitada, int disponible) {
        super("No se pueden vender " + solicitada + " " + tipo + "(s) en '" + propiedad +
                "'. Solo hay " + disponible + " disponible(s)");
        this.nombrePropiedad = propiedad;
        this.tipoEdificio = tipo;
        this.cantidadSolicitada = solicitada;
        this.cantidadDisponible = disponible;
    }

    public String getNombrePropiedad() {
        return nombrePropiedad;
    }

    public String getTipoEdificio() {
        return tipoEdificio;
    }

    public int getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }
}