// monopoly/casilla/Impuesto.java
package monopoly.casilla;

import partida.Jugador;
import partida.Avatar;
import monopoly.Juego;

public class Impuesto extends Casilla {
    private float cantidadImpuesto;

    // Constructor
    public Impuesto(String nombre, int posicion, Jugador duenho, float cantidadImpuesto) {
        super(nombre, posicion, duenho);
        this.cantidadImpuesto = cantidadImpuesto;
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
        return String.format("Impuesto{nombre='%s', posicion=%d, impuesto=%,.0f€}",
                this.getNombre(), this.getPosicion(), cantidadImpuesto);
    }

    // MÉTODO de evaluación de casilla
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.getAvatar().getLugar() == this) {
            Juego.consola.imprimir("Impuesto a pagar: %,.0f€\n", this.cantidadImpuesto);

            // Verificar solvencia
            if (actual.getFortuna() < this.cantidadImpuesto) {
                Juego.consola.imprimir("¡NO ERES SOLVENTE! Debes pagar %,.0f€ pero solo tienes %,.0f€\n",
                        this.cantidadImpuesto, actual.getFortuna());
                return false;
            }

            // Aplicar pago del impuesto
            actual.restarFortuna(this.cantidadImpuesto);
            actual.sumarPagoTasasEImpuestos(this.cantidadImpuesto);

            // El dinero va al bote del Parking
            // Esto se manejará desde el Tablero/Juego
            Juego.consola.imprimir("%s ha pagado %,.0f€ de impuestos\n",
                    actual.getNombre(), this.cantidadImpuesto);
            return true;
        }
        return false;
    }

    // MÉTODO de información
    @Override
    public void infoCasilla() {
        Juego.consola.imprimir("{");
        Juego.consola.imprimir("\tTipo: Impuesto");
        Juego.consola.imprimir("\tNombre: " + this.getNombre());
        Juego.consola.imprimir(String.format("\tA pagar: %,.0f€", this.cantidadImpuesto));
        Juego.consola.imprimir("}");
    }

    // Las casillas de impuesto no tienen valor de compra
    public float getValor() {
        return 0;
    }

    // Las casillas de impuesto no son comprables
    @Override
    public boolean esTipoComprable() {
        return false;
    }

    // GETTER específico
    public float getCantidadImpuesto() {
        return cantidadImpuesto;
    }

    public void setCantidadImpuesto(float cantidadImpuesto) {
        this.cantidadImpuesto = cantidadImpuesto;
    }
}