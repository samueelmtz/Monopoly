package monopoly.casilla;

import partida.Jugador;
import partida.Avatar;
import monopoly.Valor;

public class Especial extends Casilla {
    private String tipoEspecial; // "Salida", "Carcel", "IrCarcel"

    // Constructor
    public Especial(String nombre, int posicion, Jugador duenho, String tipoEspecial) {
        super(nombre, posicion, duenho);
        this.tipoEspecial = tipoEspecial;
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
        return String.format("Especial{nombre='%s', posicion=%d, tipo=%s}",
                this.getNombre(), this.getPosicion(), tipoEspecial);
    }

    // MÉTODO de evaluación de casilla
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.getAvatar().getLugar() == this) {
            switch (this.tipoEspecial) {
                case "Salida":
                    System.out.println("Estás en la casilla de Salida.");
                    break;
                case "Carcel":
                    System.out.println("Estás de visita en la Cárcel.");
                    break;
                case "IrCarcel":
                    System.out.println("¡Has caído en Ir a la Cárcel! Serás enviado a la cárcel.");
                    actual.encarcelar(null); // El tablero se pasará después
                    break;
                default:
                    System.out.println("Has caído en " + this.getNombre());
            }
            return true;
        }
        return false;
    }

    // MÉTODO de información
    @Override
    public void infoCasilla() {
        System.out.println("{");
        System.out.println("\tTipo: Especial");
        System.out.println("\tSubtipo: " + this.tipoEspecial);
        System.out.println("\tNombre: " + this.getNombre());

        // Información específica según el tipo
        switch (this.tipoEspecial) {
            case "Salida":
                System.out.println(String.format("\tRecompensa al pasar: %,.0f€", Valor.SUMA_VUELTA));
                break;
            case "Carcel":
                System.out.println(String.format("\tSalir pagando: %,.0f€", Valor.SALIR_CARCEL));
                System.out.print("\tJugadores: [");
                if (this.getAvatares() != null && !this.getAvatares().isEmpty()) {
                    for (int i = 0; i < this.getAvatares().size(); i++) {
                        Jugador j = this.getAvatares().get(i).getJugador();
                        System.out.print(j.getNombre() + "," + j.getTiradasCarcel());
                        if (i < this.getAvatares().size() - 1) System.out.print(" ");
                    }
                } else {
                    System.out.print("-");
                }
                System.out.println("]");
                break;
            case "IrCarcel":
                System.out.println("\tAcción: Enviar al jugador a la Cárcel");
                break;
        }
        System.out.println("}");
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