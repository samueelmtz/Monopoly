package monopoly.casilla.accion;

import monopoly.casilla.Accion;
import partida.Jugador;
import partida.Avatar;

public class CajaComunidad extends Accion {

    // Constructor
    public CajaComunidad(String nombre, int posicion, Jugador duenho) {
        super(nombre, posicion, duenho, "Comunidad");
    }

    // MÉTODO de evaluación de casilla - Específico para CajaComunidad
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.getAvatar().getLugar() == this) {
            System.out.println("¡Has caído en Caja de Comunidad! Se elegirá una carta de comunidad.");
            // La ejecución de la carta se manejará en el juego principal
            return true;
        }
        return false;
    }

    // MÉTODO de información - Específico para CajaComunidad
    @Override
    public void infoCasilla() {
        System.out.println("{");
        System.out.println("\tTipo: Acción");
        System.out.println("\tSubtipo: Caja de Comunidad");
        System.out.println("\tNombre: " + this.getNombre());
        System.out.println("\tAcción: Robar carta de Caja de Comunidad");
        System.out.println("}");
    }
}