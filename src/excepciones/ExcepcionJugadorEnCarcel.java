package excepciones;

// Nivel 3 jerarquia - El jugador está en la cárcel y no puede lanzar dados
public class ExcepcionJugadorEnCarcel extends ExcepcionMonopoly {
    public ExcepcionJugadorEnCarcel(String nombreJugador) {
        super("El jugador " + nombreJugador + " está en la cárcel. " +
                "Usa 'salir carcel' para pagar 500.000€ o espera 3 turnos para intentar dobles.");
    }
}
