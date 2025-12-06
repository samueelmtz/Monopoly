package monopoly.casilla.accion;

import monopoly.casilla.Accion;
import partida.Jugador;
import monopoly.Juego;
import monopoly.Tablero;
import monopoly.carta.Carta;
import java.util.ArrayList;

public class Suerte extends Accion {

    // Constructor
    public Suerte(String nombre, int posicion, Jugador duenho) {
        super(nombre, posicion, duenho, "Suerte");
    }

    // MÉTODO de evaluación de casilla - Específico para Suerte
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca,
                                  Tablero tablero, ArrayList<Jugador> jugadores,
                                  int tirada) {
        if (actual.getAvatar().getLugar() == this) {
            Juego.consola.imprimir("¡Has caído en " + this.getTipoAccion() + "!");

            // Ahora tenemos acceso a tablero y jugadores
            Carta carta = Carta.obtenerSiguienteCarta(this.getTipoAccion());
            carta.ejecutarAccion(actual, tablero, jugadores, banca);

            return true;
        }
        return false;
    }


    // MÉTODO de información - Específico para Suerte
    @Override
    public void infoCasilla() {
        Juego.consola.imprimir("{");
        Juego.consola.imprimir("\tTipo: Acción");
        Juego.consola.imprimir("\tSubtipo: Suerte");
        Juego.consola.imprimir("\tNombre: " + this.getNombre());
        Juego.consola.imprimir("\tAcción: Robar carta de Suerte");
        Juego.consola.imprimir("}");
    }
}