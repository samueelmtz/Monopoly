package monopoly.casilla.accion;

import monopoly.casilla.Accion;
import partida.Jugador;
import monopoly.Juego;

public class Suerte extends Accion {

    // Constructor
    public Suerte(String nombre, int posicion, Jugador duenho) {
        super(nombre, posicion, duenho, "Suerte");
    }

    // MÉTODO de evaluación de casilla - Específico para Suerte
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.getAvatar().getLugar() == this) {
            Juego.consola.imprimir("¡Has caído en Suerte! Se elegirá una carta de suerte.");
            // La ejecución de la carta se manejará en el juego principal
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