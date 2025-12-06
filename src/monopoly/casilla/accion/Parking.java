package monopoly.casilla.accion;

import monopoly.Juego;
import monopoly.casilla.Accion;
import monopoly.Tablero;
import partida.Jugador;
import java.util.ArrayList;

public class Parking extends Accion {
    private float bote;

    // Constructor
    public Parking(String nombre, int posicion, Jugador duenho) {
        super(nombre, posicion, duenho, "Parking");
        this.bote = 0;
    }

    // MÉTODO de evaluación de casilla - Específico para Parking
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, Tablero tablero, ArrayList<Jugador> jugadores, int tirada) {
        if (actual.getAvatar().getLugar() == this) {
            Juego.consola.imprimir("¡Has caído en Parking!");

            if (this.bote > 0) {
                float boteGanado = reclamarBote();
                actual.sumarFortuna(boteGanado);
                actual.sumarPremiosInversionesOBote(boteGanado);
                Juego.consola.imprimir("¡Has ganado el bote de %,.0f€!\n", boteGanado);
                Juego.consola.imprimir("Fortuna actual: %,.0f€\n", actual.getFortuna());
            } else {
                Juego.consola.imprimir("El bote del parking está vacío.");
            }
            return true;
        }
        return false;
    }

    // MÉTODO de información - Específico para Parking
    @Override
    public void infoCasilla() {
        Juego.consola.imprimir("{");
        Juego.consola.imprimir("\tTipo: Acción");
        Juego.consola.imprimir("\tSubtipo: Parking");
        Juego.consola.imprimir("\tNombre: " + this.getNombre());
        Juego.consola.imprimir(String.format("\tBote acumulado: %,.0f€", this.bote));
        Juego.consola.imprimir("\tJugadores en parking: [");
        if (!this.getAvatares().isEmpty()) {
            for (int i = 0; i < this.getAvatares().size(); i++) {
                Juego.consola.imprimir(this.getAvatares().get(i).getJugador().getNombre());
                if (i < this.getAvatares().size() - 1) System.out.print(", ");
            }
        } else {
            Juego.consola.imprimir("-");
        }
        Juego.consola.imprimir("]");
        Juego.consola.imprimir("}");
    }

    // MÉTODOS específicos para manejar el bote
    public void añadirAlBote(float cantidad) {
        this.bote += cantidad;
    }

    public float reclamarBote() {
        float boteActual = this.bote;
        this.bote = 0;
        return boteActual;
    }

    public float getBote() {
        return bote;
    }
}