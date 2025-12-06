package excepciones;

//Nivel 3 de jerarquía - No se encuentra la casilla en el tablero
public class ExcepcionCasillaNoEncontrada extends ExcepcionAccion {

    private final String nombreCasilla;

    public ExcepcionCasillaNoEncontrada(String casilla) {
        super("Casilla no encontrada: '" + casilla + "'");
        this.nombreCasilla = casilla;
    }

    public String getNombreCasilla() {
        return nombreCasilla;
    }
}