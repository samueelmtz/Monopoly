// monopoly/casilla/propiedad/Servicio.java
package monopoly.casilla.propiedad;

import monopoly.casilla.Propiedad;
import partida.Jugador;
import partida.Avatar;
import monopoly.Valor;

public class Servicio extends Propiedad {

    // Constructor
    public Servicio(String nombre, int posicion, float valor, Jugador duenho) {
        super(nombre, posicion, valor, 0, duenho);
    }

    // MÉTODOS REQUERIDOS
    @Override
    public boolean perteneceAJugador(Jugador jugador) {
        return this.getDuenho() != null && this.getDuenho().equals(jugador);
    }

    @Override
    public boolean alquiler() {
        return !this.isHipotecada() && this.getDuenho() != null;
    }

    @Override
    public float valor() {
        return this.getValorPropiedad();
    }

    @Override
    public String toString() {
        return String.format("Servicio{nombre='%s', posicion=%d, valor=%,.0f€}",
                this.getNombre(), this.getPosicion(), this.getValorPropiedad());
    }

    // MÉTODO de evaluación de casilla
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.getAvatar().getLugar() == this) {
            if (this.getDuenho() == null || this.getDuenho() == banca || this.getDuenho().getNombre().equals("Banca")) {
                System.out.println("¡Este servicio está disponible para compra! Usa el comando 'comprar " + this.getNombre() + "' para adquirirla.");
                return true;
            }

            if (this.getDuenho() != null && this.getDuenho() != banca && this.getDuenho() != actual) {
                if (this.isHipotecada()) {
                    System.out.println("El servicio " + this.getNombre() + " está hipotecado. No se cobra alquiler.");
                    return true;
                }

                float aPagar = calcularAlquilerServicio(tirada);

                if (actual.getFortuna() < aPagar) {
                    System.out.printf("¡NO ERES SOLVENTE! Debes pagar %,.0f€ pero solo tienes %,.0f€\n", aPagar, actual.getFortuna());
                    return false;
                }

                actual.restarFortuna(aPagar);
                actual.sumarPagoDeAlquileres(aPagar);
                this.getDuenho().sumarFortuna(aPagar);
                this.getDuenho().sumarCobroDeAlquileres(aPagar);

                System.out.printf("%s ha pagado %,.0f€ de alquiler a %s\n", actual.getNombre(), aPagar, this.getDuenho().getNombre());
            }
            return true;
        }
        return false;
    }

    private float calcularAlquilerServicio(int tirada) {
        int serviciosDelDuenho = 0;
        if (this.getDuenho() != null) {
            for (monopoly.casilla.Casilla propiedad : this.getDuenho().getPropiedades()) {
                if (propiedad instanceof Servicio) {
                    serviciosDelDuenho++;
                }
            }
        }

        int multiplicador;
        if (serviciosDelDuenho == 1) {
            multiplicador = 4;
        } else if (serviciosDelDuenho == 2) {
            multiplicador = 10;
        } else {
            multiplicador = 4;
        }

        float aPagar = (float) tirada * multiplicador * Valor.FACTOR_SERVICIO;

        System.out.printf("Alquiler de servicio: dados(%d) × %d × %,.0f€ = %,.0f€\n",
                tirada, multiplicador, Valor.FACTOR_SERVICIO, aPagar);
        System.out.printf("El dueño tiene %d servicio(s)\n", serviciosDelDuenho);

        return aPagar;
    }

    // MÉTODO de información
    @Override
    public void infoCasilla() {
        System.out.println("{");
        System.out.println("\tTipo: Servicio");
        System.out.println("\tDueño: " + (this.getDuenho() != null ? this.getDuenho().getNombre() : "Banca"));
        System.out.println(String.format("\tPrecio: %,.0f€", this.getValorPropiedad()));
        System.out.println(String.format("\tPago por caer: dados × multiplicador × %,.0f€", Valor.FACTOR_SERVICIO));
        System.out.println("\t\t(multiplicador=4 si se posee un servicio, multiplicador=10 si se poseen 2)");

        if (this.getDuenho() != null && !this.getDuenho().getNombre().equals("Banca")) {
            int serviciosDelDuenho = 0;
            for (monopoly.casilla.Casilla propiedad : this.getDuenho().getPropiedades()) {
                if (propiedad instanceof Servicio) {
                    serviciosDelDuenho++;
                }
            }
            System.out.println("\tEl dueño tiene " + serviciosDelDuenho + " servicio(s)");
        }
        System.out.println("}");
    }

    // Los servicios no se pueden hipotecar
    @Override
    public boolean puedeHipotecar(Jugador jugador) {
        System.out.println("Los servicios no se pueden hipotecar.");
        return false;
    }

    @Override
    public boolean hipotecar() {
        System.out.println("Los servicios no se pueden hipotecar.");
        return false;
    }

    @Override
    public boolean deshipotecar() {
        System.out.println("Los servicios no se pueden hipotecar.");
        return false;
    }
}