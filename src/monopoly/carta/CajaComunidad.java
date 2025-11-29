package monopoly.carta;

import partida.Jugador;
import monopoly.Tablero;
import java.util.ArrayList;
import java.util.Collections;

public class CajaComunidad extends Carta {
    private static ArrayList<CajaComunidad> mazoCartas;
    private static boolean mazoInicializado = false;

    // Constructor para cartas individuales
    public CajaComunidad(int id, String descripcion, String accion) {
        super(id, descripcion, accion);
    }

    // Método para inicializar el mazo (debe llamarse una vez al inicio del juego)
    public static void inicializarCartasComunidad() {
        if (!mazoInicializado) {
            mazoCartas = new ArrayList<>();

            // Añadir todas las cartas de Caja de Comunidad
            mazoCartas.add(new CajaComunidad(1, "Paga 500.000€ por un fin de semana en balneario.", "pagar:500000"));
            mazoCartas.add(new CajaComunidad(2, "Te investigan por fraude. Ve a la Cárcel.", "irCarcel"));
            mazoCartas.add(new CajaComunidad(3, "Colócate en la casilla de Salida.", "irSalida"));
            mazoCartas.add(new CajaComunidad(4, "Devolución de Hacienda. Cobra 500.000€.", "recibir:500000"));
            mazoCartas.add(new CajaComunidad(5, "Retrocede hasta Solar1 para comprar antigüedades exóticas.", "retroceder:16"));
            mazoCartas.add(new CajaComunidad(6, "Ve a Solar20 para disfrutar del San Fermín.", "avanzar:35"));

            // Barajar el mazo
            Collections.shuffle(mazoCartas);
            mazoInicializado = true;
        }
    }

    // Método para sacar una carta del mazo
    public static CajaComunidad sacarCarta() {
        if (!mazoInicializado) {
            inicializarCartasComunidad();
        }

        if (mazoCartas.isEmpty()) {
            // Si el mazo está vacío, reinicializar
            inicializarCartasComunidad();
        }

        CajaComunidad carta = mazoCartas.remove(0);
        // Poner la carta usada al final del mazo
        mazoCartas.add(carta);

        return carta;
    }

    @Override
    public void ejecutarAccion(Jugador jugador, Tablero tablero, ArrayList<Jugador> jugadores, Jugador banca) {
        // Usar el método común de la clase base
        ejecutarAccionComun(jugador, tablero, jugadores, banca);
    }
}