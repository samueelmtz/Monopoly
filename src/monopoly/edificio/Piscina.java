// monopoly/edificio/Piscina.java
package monopoly.edificio;

import monopoly.casilla.propiedad.Solar;
import partida.Jugador;

public class Piscina extends Edificio {
    // Contador estático para IDs únicos
    private static int contadorPiscinas = 0;

    // Constructor
    public Piscina(Solar solar) {
        super("piscina", solar, solar.getPrecioPiscina());
        contadorPiscinas++;
    }

    public Piscina() {
        super();
    }

    // Implementación del método abstracto
    @Override
    public void accion() {
        System.out.println("Piscina construída en " + this.getSolar().getNombre() +
                ". Aumenta el alquiler en " +
                String.format("%,.0f", this.getSolar().getAlquilerPiscina()) + "€");
    }

    // Método estático para obtener el contador
    public static int getContadorPiscinas() {
        return contadorPiscinas;
    }

    public static void setContadorPiscinas(int contador) {
        contadorPiscinas = contador;
    }

    @Override
    public String toString() {
        return String.format("Piscina{id='%s', solar='%s', coste=%,.0f€}",
                this.getId(), this.getSolar().getNombre(), this.getCoste());
    }
}
