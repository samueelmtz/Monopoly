package excepciones;

// Nivel 3 jerarquia - Avatar no válido para el jugador
public class ExcepcionAvatarNoValido extends ExcepcionJugador {

    private final String tipoAvatar;
    private final String[] avataresPermitidos;

    public ExcepcionAvatarNoValido(String tipoAvatar, String[] permitidos) {
        super("Avatar '" + tipoAvatar + "' no válido. " +
                "Avatares permitidos: " + String.join(", ", permitidos));
        this.tipoAvatar = tipoAvatar;
        this.avataresPermitidos = permitidos;
    }

    public ExcepcionAvatarNoValido(String tipoAvatar) {
        this(tipoAvatar, new String[]{"sombrero", "esfinge", "pelota", "coche"});
    }

    public String getTipoAvatar() {
        return tipoAvatar;
    }

    public String[] getAvataresPermitidos() {
        return avataresPermitidos;
    }
}
