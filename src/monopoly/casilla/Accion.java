// monopoly/casilla/Accion.java
package monopoly.casilla;

import monopoly.carta.Carta;
import partida.Jugador;
import partida.Avatar;
import monopoly.Juego;
import monopoly.Tablero;
import java.util.ArrayList;

public class Accion extends Casilla {
    private final String tipoAccion; // "Suerte", "Comunidad", "Parking"

    // Constructor
    public Accion(String nombre, int posicion, Jugador duenho, String tipoAccion) {
        super(nombre, posicion, duenho);
        this.tipoAccion = tipoAccion;
    }

    // MÉTODOS REQUERIDOS por el PDF - IMPLEMENTACIÓN
    @Override
    public boolean estaAvatar(Avatar avatar) {
        return this.getAvatares().contains(avatar);
    }

    @Override
    public int frecuenciaVisita() {
        return this.getContadorVisitas();
    }

    @Override
    public String toString() {
        return String.format("Accion{nombre='%s', posicion=%d, tipo=%s}", this.getNombre(), this.getPosicion(), tipoAccion);
    }

    // MÉTODO de evaluación de casilla
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, Tablero tablero, ArrayList<Jugador> jugadores, int tirada) {
        if (actual.getAvatar().getLugar() == this) {
            Juego.consola.imprimir("Has caído en " + this.getTipoAccion() +
                    ". Se procesará la acción correspondiente.");
            try {
                Carta carta = Carta.obtenerSiguienteCarta(this.getTipoAccion());
                carta.ejecutarAccion(actual, tablero, jugadores, banca);

            } catch (Exception e) {
                Juego.consola.imprimir("Error ejecutando carta: " + e.getClass().getSimpleName() + " - " + e.getMessage());
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }


    // MÉTODO de información
    @Override
    public void infoCasilla() {
        Juego.consola.imprimir("{");
        Juego.consola.imprimir("\tTipo: Acción");
        Juego.consola.imprimir("\tSubtipo: " + this.tipoAccion);
        Juego.consola.imprimir("\tNombre: " + this.getNombre());
        Juego.consola.imprimir("}");
    }

    // Las casillas de acción no tienen valor monetario
    public float getValor() {
        return 0;
    }

    // Las casillas de acción no son comprables
    @Override
    public boolean esTipoComprable() {
        return false;
    }

    // GETTER específico
    public String getTipoAccion() {
        return tipoAccion;
    }
}