package excepciones;

// Nivel 3 de jerarquía - Cuando se intenta usar una carta que no es válida
public class ExcepcionCartaNoValida extends ExcepcionAccion {
    private final String tipoCarta;
    private final String motivo;

    public ExcepcionCartaNoValida(String tipoCarta, String motivo) {
        super("La carta del tipo '" + tipoCarta + "' no es válida: " + motivo);
        this.tipoCarta = tipoCarta;
        this.motivo = motivo;
    }

    public ExcepcionCartaNoValida(int idCarta, String tipoCarta) {
        this(tipoCarta, "motivo desconocido");
    }

    // Getters para acceder a la información detallada de la excepción
    public String getTipoCarta() {
        return tipoCarta;
    }

    public String getMotivo() {
        return motivo;
    }
}