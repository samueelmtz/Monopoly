package monopoly.casilla.accion;

import monopoly.casilla.Accion;
import partida.Jugador;
import partida.Avatar;

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
            System.out.println("¡Has caído en Parking!");
            if (this.bote > 0) {
                System.out.printf("¡Hay un bote de %,.0f€ acumulado!\n", this.bote);
                // La reclamación del bote se manejará en el juego principal
            } else {
                System.out.println("El bote del parking está vacío.");
            }
            return true;
        }
        return false;
    }

    // MÉTODO de información - Específico para Parking
    @Override
    public void infoCasilla() {
        System.out.println("{");
        System.out.println("\tTipo: Acción");
        System.out.println("\tSubtipo: Parking");
        System.out.println("\tNombre: " + this.getNombre());
        System.out.println(String.format("\tBote acumulado: %,.0f€", this.bote));
        System.out.print("\tJugadores en parking: [");
        if (!this.getAvatares().isEmpty()) {
            for (int i = 0; i < this.getAvatares().size(); i++) {
                System.out.print(this.getAvatares().get(i).getJugador().getNombre());
                if (i < this.getAvatares().size() - 1) System.out.print(", ");
            }
        } else {
            System.out.print("-");
        }
        System.out.println("]");
        System.out.println("}");
    }

    // MÉTODOS específicos para manejar el bote
    public void añadirAlBote(float cantidad) {
        this.bote += cantidad;
        System.out.printf("Se han añadido %,.0f€ al bote del Parking. Bote actual: %,.0f€\n",
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