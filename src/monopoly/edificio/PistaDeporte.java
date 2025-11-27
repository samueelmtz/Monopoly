// monopoly/edificio/PistaDeporte.java
package monopoly.edificio;

import monopoly.casilla.propiedad.Solar;
import partida.Jugador;

public class PistaDeporte extends Edificio {
    // Contador estático para IDs únicos
    private static int contadorPistas = 0;

    // Constructor
    public PistaDeporte(Solar solar) {
        super("pista_deporte", solar, solar.getPrecioPistaDeporte());
        contadorPistas++;
    }

    public PistaDeporte() {
        super();
    }

    // Implementación del método abstracto
    @Override
    public void accion() {
        System.out.println("Pista de deporte construída en " + this.getSolar().getNombre() +
                ". Aumenta el alquiler en " +
                String.format("%,.0f", this.getSolar().getAlquilerPistaDeporte()) + "€");
    }

    // Método estático para obtener el contador
    public static int getContadorPistas() {
        return contadorPistas;
    }

    public static void setContadorPistas(int contador) {
        contadorPistas = contador;
    }

    @Override
    public String toString() {
        return String.format("PistaDeporte{id='%s', solar='%s', coste=%,.0f€}",
                this.getId(), this.getSolar().getNombre(), this.getCoste());
    }
}
