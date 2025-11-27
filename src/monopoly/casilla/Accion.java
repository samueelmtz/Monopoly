package monopoly.casilla;

import partida.Jugador;
import partida.Avatar;

public abstract class Accion extends Casilla {

    // Constructor
    public Accion(String nombre, int posicion, Jugador duenho) {
        super(nombre, posicion, duenho);
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
                nombre, posicion, this.getClass().getSimpleName());
    }

    // MÉTODO COMÚN para acciones - será sobrescrito
    @Override
    public abstract boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada);

    // MÉTODO de información - será sobrescrito
    @Override
    public abstract void infoCasilla();

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
}