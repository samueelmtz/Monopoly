package monopoly.casilla.accion;

import monopoly.casilla.Accion;
import partida.Jugador;
import monopoly.Juego;

public class CajaComunidad extends Accion {

    // Constructor
    public CajaComunidad(String nombre, int posicion, Jugador duenho) {
        super(nombre, posicion, duenho, "Comunidad");
    }

    // MÉTODO de evaluación de casilla - Específico para CajaComunidad
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.getAvatar().getLugar() == this) {
            Juego.consola.imprimir("¡Has caído en Caja de Comunidad! Se elegirá una carta de comunidad.");
            // La ejecución de la carta se manejará en el juego principal
            return true;
        }
        return false;
    }

    // MÉTODO de información - Específico para CajaComunidad
    @Override
    public void infoCasilla() {
        Juego.consola.imprimir("{");
        Juego.consola.imprimir("\tTipo: Acción");
        Juego.consola.imprimir("\tSubtipo: Caja de Comunidad");
        Juego.consola.imprimir("\tNombre: " + this.getNombre());
        Juego.consola.imprimir("\tAcción: Robar carta de Caja de Comunidad");
        Juego.consola.imprimir("}");
    }
}