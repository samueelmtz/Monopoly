// monopoly/casilla/Accion.java
package monopoly.casilla;

import partida.Jugador;
import partida.Avatar;

public class Accion extends Casilla {
    private String tipoAccion; // "Suerte", "Comunidad", "Parking"

    // Constructor
    public Accion(String nombre, int posicion, Jugador duenho, String tipoAccion) {
        super(nombre, posicion, duenho);
        this.tipoAccion = tipoAccion;
    }

    // MÉTODOS REQUERIDOS por el PDF - IMPLEMENTACIÓN
    @Override
    public boolean estaAvatar(Avatar avatar) {
        return this.avatares.contains(avatar);
    }

    @Override
    public int frecuenciaVisita() {
        return this.contadorVisitas;
    }

    @Override
    public String toString() {
        return String.format("Accion{nombre='%s', posicion=%d, tipo=%s}",
                nombre, posicion, tipoAccion);
    }

    // MÉTODO de evaluación de casilla
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.getAvatar().getLugar() == this) {
            System.out.println("Has caído en " + this.tipoAccion + ". Se procesará la acción correspondiente.");
            return true;
        }
        return false;
    }

    // MÉTODO de información
    @Override
    public void infoCasilla() {
        System.out.println("{");
        System.out.println("\tTipo: Acción");
        System.out.println("\tSubtipo: " + this.tipoAccion);
        System.out.println("\tNombre: " + this.nombre);
        System.out.println("}");
    }

    // Las casillas de acción no tienen valor monetario
    @Override
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