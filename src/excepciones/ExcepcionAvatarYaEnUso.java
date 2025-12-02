package excepciones;

// Nivel 3 jerarquia - El avatar ya está en uso por otro jugador
public class ExcepcionAvatarYaEnUso extends ExcepcionJugador {

    private final char avatar;
    private final String jugadorPropietario;

    public ExcepcionAvatarYaEnUso(char avatar, String propietario) {
        super("El avatar '" + avatar + "' ya está en uso por el jugador '" + propietario + "'");
        this.avatar = avatar;
        this.jugadorPropietario = propietario;
    }

    public char getAvatar() {
        return avatar;
    }

    public String getJugadorPropietario() {
        return jugadorPropietario;
    }
}