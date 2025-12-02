package excepciones;

// Nivel 3 jerarquia - El jugador no es propietario del elemento especificado
public class ExcepcionJugadorNoPropietario extends ExcepcionJugador {

    private final String nombreJugador;
    private final String elemento;
    private final String tipoElemento;

    public ExcepcionJugadorNoPropietario(String jugador, String elemento, String tipo) {
        super("El jugador '" + jugador + "' no es propietario del " + tipo + " '" + elemento + "'");
        this.nombreJugador = jugador;
        this.elemento = elemento;
        this.tipoElemento = tipo;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public String getElemento() {
        return elemento;
    }

    public String getTipoElemento() {
        return tipoElemento;
    }
}