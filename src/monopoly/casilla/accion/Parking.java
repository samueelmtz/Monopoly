package monopoly.casilla.accion;

import monopoly.Juego;
import monopoly.casilla.Accion;
import partida.Jugador;

public class Parking extends Accion {
    private float bote;

    // Constructor
    public Parking(String nombre, int posicion, Jugador duenho) {
        super(nombre, posicion, duenho, "Parking");
        this.bote = 0;
    }

    // MÉTODO de evaluación de casilla - Específico para Parking
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.getAvatar().getLugar() == this) {
            Juego.consola.imprimir("¡Has caído en Parking!");
            if (this.bote > 0) {
                Juego.consola.imprimir("¡Hay un bote de %,.0f€ acumulado!\n", this.bote);
                // La reclamación del bote se manejará en el juego principal
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
        Juego.consola.imprimir("Se han añadido %,.0f€ al bote del Parking. Bote actual: %,.0f€\n",
                cantidad, this.bote);
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