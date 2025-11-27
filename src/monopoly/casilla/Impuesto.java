package monopoly.casilla;

import partida.Jugador;
import partida.Avatar;

public class Impuesto extends Casilla {
    private float cantidadImpuesto;

    // Constructor
    public Impuesto(String nombre, int posicion, float cantidadImpuesto, Jugador duenho) {
        super(nombre, posicion, duenho);
        this.cantidadImpuesto = cantidadImpuesto;
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
        return String.format("Impuesto{nombre='%s', posicion=%d, impuesto=%,.0f€}",
                nombre, posicion, cantidadImpuesto);
    }

    // MÉTODO de evaluación de casilla
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.getAvatar().getLugar() == this) {
            System.out.printf("Has caído en %s. Impuesto a pagar: %,.0f€\n", this.nombre, this.cantidadImpuesto);

            // Verificar solvencia
            if (actual.getFortuna() < this.cantidadImpuesto) {
                System.out.printf("¡NO ERES SOLVENTE! Debes pagar %,.0f€ pero solo tienes %,.0f€\n",
                        this.cantidadImpuesto, actual.getFortuna());
                return false;
            }

            // El pago se procesará en el menú/juego principal
            actual.sumarPagoTasasEImpuestos(this.cantidadImpuesto);
            return true;
        }
        return false;
    }

    // MÉTODO de información
    @Override
    public void infoCasilla() {
        System.out.println("{");
        System.out.println("\tTipo: Impuesto");
        System.out.println(String.format("\tA pagar: %,.0f€", this.cantidadImpuesto));
        System.out.println("}");
    }

    // Las casillas de impuesto no tienen valor monetario
    @Override
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