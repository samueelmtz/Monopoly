package excepciones;

// Nivel 3 de jerarquía - Excepción específica para venta de edificios
public class ExcepcionVenderEdificios extends ExcepcionMonopoly {

    private final String tipoEdificio;
    private final String nombreSolar;
    private final String motivo;

    public ExcepcionVenderEdificios(String tipoEdificio, String nombreSolar, String motivo) {
        super("No se puede vender " + tipoEdificio + " en '" + nombreSolar + "': " + motivo);
        this.tipoEdificio = tipoEdificio;
        this.nombreSolar = nombreSolar;
        this.motivo = motivo;
    }

    // Getters
    public String getTipoEdificio() {
        return tipoEdificio;
    }

    public String getNombreSolar() {
        return nombreSolar;
    }

    public String getMotivo() {
        return motivo;
    }
}