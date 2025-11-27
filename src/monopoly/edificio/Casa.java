// monopoly/edificio/Casa.java
package monopoly.edificio;

import monopoly.casilla.propiedad.Solar;
import partida.Jugador;

public class Casa extends Edificio {
    // Contador estático para IDs únicos
    private static int contadorCasas = 0;

    // Constructor
    public Casa(Solar solar) {
        super("casa", solar, solar.getPrecioCasa());
        contadorCasas++;
    }

    public Casa() {
        super();
    }

    // Implementación del método abstracto
    @Override
    public void accion() {
        System.out.println("Casa construída en " + this.getSolar().getNombre() +
                ". Aumenta el alquiler en " +
                String.format("%,.0f", this.getSolar().getAlquilerCasa()) + "€");
    }

    // Método estático para obtener el contador
    public static int getContadorCasas() {
        return contadorCasas;
    }

    public static void setContadorCasas(int contador) {
        contadorCasas = contador;
    }

    @Override
    public String toString() {
        return String.format("Casa{id='%s', solar='%s', coste=%,.0f€}",
                this.getId(), this.getSolar().getNombre(), this.getCoste());
    }
}
