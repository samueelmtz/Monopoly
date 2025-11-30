package monopoly.carta;

import partida.Jugador;
import monopoly.Tablero;
import java.util.ArrayList;
import java.util.Collections;

public class Suerte extends Carta {
    private static ArrayList<Suerte> mazoCartas;
    private static boolean mazoInicializado = false;

    public Suerte(int id, String descripcion, String accion) {
        super(id, descripcion, accion);
    }

    public static void inicializarCartasSuerte() {
        if (!mazoInicializado) {
            mazoCartas = new ArrayList<>();

            // Cartas de Suerte
            mazoCartas.add(new Suerte(1,"Decides hacer un viaje de placer. Avanza hasta Solar19. Si pasas por la casilla de Salida, cobra 2.000.000€.", "avanzar:33"));
            mazoCartas.add(new Suerte(2, "Los acreedores te persiguen por impago. Ve a la Cárcel.", "irCarcel"));
            mazoCartas.add(new Suerte(3, "¡Has ganado el bote de la lotería! Recibe 1.000.000€.", "recibir:1000000"));
            mazoCartas.add(new Suerte(4, "Has sido elegido presidente. Paga a cada jugador 250.000€.", "pagarTodos:250000"));
            mazoCartas.add(new Suerte(5, "¡Hora punta de trafico! Retrocede tres casillas.", "retroceder:3"));
            mazoCartas.add(new Suerte(6, "Te multan por usar el móvil mientras conduces. Paga 150.000€.", "pagar:150000"));
            mazoCartas.add(new Suerte(7, "Avanza hasta la casilla de transporte más cercana.", "transporteCercano"));

            Collections.shuffle(mazoCartas);
            mazoInicializado = true;
        }
    }

    public static Suerte sacarCarta() {
        if (!mazoInicializado) {
            inicializarCartasSuerte();
        }

        if (mazoCartas.isEmpty()) {
            inicializarCartasSuerte();
        }

        Suerte carta = mazoCartas.removeFirst();
        mazoCartas.add(carta);

        return carta;
    }

    @Override
    public void ejecutarAccion(Jugador jugador, Tablero tablero, ArrayList<Jugador> jugadores, Jugador banca) {
        ejecutarAccionComun(jugador, tablero, jugadores, banca);
    }
}