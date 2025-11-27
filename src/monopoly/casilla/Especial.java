package monopoly.casilla;

import partida.Jugador;
import partida.Avatar;
import monopoly.Valor;

public class Especial extends Casilla {

    // Constructor
    public Especial(String nombre, int posicion, Jugador duenho) {
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
        return String.format("Especial{nombre='%s', posicion=%d, tipo=%s}",
                nombre, posicion, this.nombre);
    }

    // MÉTODO de evaluación de casilla
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.getAvatar().getLugar() == this) {
            switch (this.nombre) {
                case "Salida":
                    System.out.println("Estás en la casilla de Salida.");
                    // No se cobra aquí, se cobra al pasar por ella
                    break;

                case "Carcel":
                    System.out.println("Estás de visita en la Cárcel.");
                    break;

                case "Parking":
                    System.out.println("Has caído en Parking.");
                    // El bote se maneja en el juego principal
                    break;

                case "IrCarcel":
                    System.out.println("¡Has caído en Ir a la Cárcel!");
                    // El envío a la cárcel se maneja en el juego principal
                    break;

                default:
                    System.out.println("Has caído en " + this.nombre);
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
        System.out.println("\tNombre: " + this.nombre);

        switch (this.nombre) {
            case "Salida":
                System.out.println(String.format("\tRecompensa al pasar: %,.0f€", Valor.SUMA_VUELTA));
                break;

            case "Carcel":
                System.out.println(String.format("\tSalir pagando: %,.0f€", Valor.SALIR_CARCEL));
                System.out.print("\tJugadores: [");
                if (!this.avatares.isEmpty()) {
                    for (int i = 0; i < this.avatares.size(); i++) {
                        Jugador j = this.avatares.get(i).getJugador();
                        System.out.print(j.getNombre() + "," + j.getTiradasCarcel());
                        if (i < this.avatares.size() - 1) System.out.print(" ");
                    }
                } else {
                    System.out.print("-");
                }
                System.out.println("]");
                break;

            case "Parking":
                System.out.println("\tBote: Se acumula con impuestos y multas");
                System.out.print("\tJugadores: [");
                if (!this.avatares.isEmpty()) {
                    for (int i = 0; i < this.avatares.size(); i++) {
                        System.out.print(this.avatares.get(i).getJugador().getNombre());
                        if (i < this.avatares.size() - 1) System.out.print(", ");
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
    @Override
    public float getValor() {
        return 0;
    }

    // Las casillas especiales no son comprables
    @Override
    public boolean esTipoComprable() {
        return false;
    }
}
