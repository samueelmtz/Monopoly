package monopoly.casilla.accion;

import monopoly.casilla.Accion;
import partida.Jugador;
import partida.Avatar;

public class Suerte extends Accion {

    // Constructor
    public Suerte(String nombre, int posicion, Jugador duenho) {
        super(nombre, posicion, duenho, "Suerte");
    }

    // MÉTODO de evaluación de casilla - Específico para Suerte
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.getAvatar().getLugar() == this) {
            System.out.println("¡Has caído en Suerte! Se elegirá una carta de suerte.");
            // La ejecución de la carta se manejará en el juego principal
            return true;
        }
        return false;
    }

    // MÉTODO de información - Específico para Suerte
    @Override
    public void infoCasilla() {
        System.out.println("{");
        System.out.println("\tTipo: Acción");
        System.out.println("\tSubtipo: Suerte");
        System.out.println("\tNombre: " + this.getNombre());
        System.out.println("\tAcción: Robar carta de Suerte");
        System.out.println("}");
    }
}