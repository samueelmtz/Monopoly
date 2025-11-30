package monopoly.casilla;

import partida.Jugador;
import partida.Avatar;
import monopoly.Valor;
import monopoly.Juego;

public class Especial extends Casilla {
    private final String tipoEspecial; // "Salida", "Carcel", "IrCarcel"

    // Constructor
    public Especial(String nombre, int posicion, Jugador duenho, String tipoEspecial) {
        super(nombre, posicion, duenho);
        this.tipoEspecial = tipoEspecial;
    }

    // MÉTODOS REQUERIDOS por el PDF
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
        return String.format("Especial{nombre='%s', posicion=%d, tipo=%s}",
                this.getNombre(), this.getPosicion(), tipoEspecial);
    }

    // MÉTODO de evaluación de casilla
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.getAvatar().getLugar() == this) {
            switch (this.tipoEspecial) {
                case "Salida":
                    Juego.consola.imprimir("Estás en la casilla de Salida.");
                    break;
                case "Carcel":
                    Juego.consola.imprimir("Estás de visita en la Cárcel.");
                    break;
                case "IrCarcel":
                    Juego.consola.imprimir("¡Has caído en Ir a la Cárcel! Serás enviado a la cárcel.");
                    actual.encarcelar(null); // El tablero se pasará después
                    break;
                default:
                    Juego.consola.imprimir("Has caído en " + this.getNombre());
            }
            return true;
        }
        return false;
    }

    // MÉTODO de información
    @Override
    public void infoCasilla() {
        Juego.consola.imprimir("{");
        Juego.consola.imprimir("\tTipo: Especial");
        Juego.consola.imprimir("\tSubtipo: " + this.tipoEspecial);
        Juego.consola.imprimir("\tNombre: " + this.getNombre());

        // Información específica según el tipo
        switch (this.tipoEspecial) {
            case "Salida":
                Juego.consola.imprimir(String.format("\tRecompensa al pasar: %,.0f€", Valor.SUMA_VUELTA));
                break;
            case "Carcel":
                Juego.consola.imprimir(String.format("\tSalir pagando: %,.0f€", Valor.SALIR_CARCEL));
                Juego.consola.imprimir("\tJugadores: [");
                if (this.getAvatares() != null && !this.getAvatares().isEmpty()) {
                    for (int i = 0; i < this.getAvatares().size(); i++) {
                        Jugador j = this.getAvatares().get(i).getJugador();
                        Juego.consola.imprimir(j.getNombre() + "," + j.getTiradasCarcel());
                        if (i < this.getAvatares().size() - 1)Juego.consola.imprimir(" ");
                    }
                } else {
                    Juego.consola.imprimir("-");
                }
                Juego.consola.imprimir("]");
                break;
            case "IrCarcel":
                Juego.consola.imprimir("\tAcción: Enviar al jugador a la Cárcel");
                break;
        }
        Juego.consola.imprimir("}");
    }

    // Las casillas especiales no tienen valor monetario
    public float getValor() {
        return 0;
    }

    // Las casillas especiales no son comprables
    @Override
    public boolean esTipoComprable() {
        return false;
    }

    // GETTER específico
    public String getTipoEspecial() {
        return tipoEspecial;
    }
}