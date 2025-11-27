// monopoly/edificio/Hotel.java
package monopoly.edificio;

import monopoly.casilla.propiedad.Solar;
import partida.Jugador;

public class Hotel extends Edificio {
    // Contador estático para IDs únicos
    private static int contadorHoteles = 0;

    // Constructor
    public Hotel(Solar solar) {
        super("hotel", solar, solar.getPrecioHotel());
        contadorHoteles++;
    }

    public Hotel() {
        super();
    }

    // Implementación del método abstracto
    @Override
    public void accion() {
        System.out.println("Hotel construído en " + this.getSolar().getNombre() +
                ". Aumenta el alquiler en " +
                String.format("%,.0f", this.getSolar().getAlquilerHotel()) + "€");

        // El hotel reemplaza las casas
        System.out.println("Nota: El hotel reemplaza las 4 casas existentes.");
    }

    // Método estático para obtener el contador
    public static int getContadorHoteles() {
        return contadorHoteles;
    }

    public static void setContadorHoteles(int contador) {
        contadorHoteles = contador;
    }

    @Override
    public String toString() {
        return String.format("Hotel{id='%s', solar='%s', coste=%,.0f€}",
                this.getId(), this.getSolar().getNombre(), this.getCoste());
    }
}
