package monopoly.casilla.propiedad;

import partida.Jugador;
import partida.Avatar;
import monopoly.Valor;
import java.util.ArrayList;

public class Transporte extends Propiedad {

    // Constructor
    public Transporte(String nombre, int posicion, float valor, Jugador duenho) {
        super(nombre, posicion, valor, Valor.ALQUILER_TRANSPORTE, duenho);
    }

    // MÉTODOS REQUERIDOS por el PDF - IMPLEMENTACIÓN
    @Override
    public boolean perteneceAJugador(Jugador jugador) {
        return this.duenho != null && this.duenho.equals(jugador);
    }

    @Override
    public boolean alquiler() {
        return !this.hipotecada && this.duenho != null;
    }

    @Override
    public float valor() {
        return this.valor;
    }

    @Override
    public String toString() {
        return String.format("Transporte{nombre='%s', posicion=%d, valor=%,.0f€}",
                nombre, posicion, valor);
    }

    // MÉTODO de evaluación de casilla - Polimorfismo
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.getAvatar().getLugar() == this) {
            // Verificar si está disponible para compra
            if (this.duenho == null || this.duenho == banca || this.duenho.getNombre().equals("Banca")) {
                System.out.println("¡Este transporte está disponible para compra! Usa el comando 'comprar " + this.nombre + "' para adquirirla.");
                return true;
            }

            // Si tiene dueño y no es el jugador actual, calcular alquiler
            if (this.duenho != null && this.duenho != banca && this.duenho != actual) {
                float aPagar = calcularAlquilerTransporte();

                // Verificar solvencia
                if (actual.getFortuna() < aPagar) {
                    System.out.printf("¡NO ERES SOLVENTE! Debes pagar %,.0f€ pero solo tienes %,.0f€\n", aPagar, actual.getFortuna());
                    return false;
                }

                // Aplicar pago
                actual.restarFortuna(aPagar);
                actual.sumarPagoDeAlquileres(aPagar);
                this.duenho.sumarFortuna(aPagar);
                this.duenho.sumarCobroDeAlquileres(aPagar);

                System.out.printf("%s ha pagado %,.0f€ de alquiler a %s\n", actual.getNombre(), aPagar, this.duenho.getNombre());
            }
            return true;
        }
        return false;
    }

    private float calcularAlquilerTransporte() {
        // Contar cuántos transportes tiene el dueño
        int transportesDelDuenho = 0;
        if (this.duenho != null) {
            for (monopoly.casilla.Casilla propiedad : this.duenho.getPropiedades()) {
                if (propiedad instanceof Transporte) {
                    transportesDelDuenho++;
                }
            }
        }

        float alquiler = Valor.ALQUILER_TRANSPORTE * transportesDelDuenho;
        System.out.printf("Alquiler de transporte: %,.0f€ (el dueño tiene %d transporte%s)\n",
                alquiler, transportesDelDuenho, transportesDelDuenho != 1 ? "s" : "");
        return alquiler;
    }

    // MÉTODO de información
    @Override
    public void infoCasilla() {
        System.out.println("{");
        System.out.println("\tTipo: Transporte");
        System.out.println("\tDueño: " + (this.duenho != null ? this.duenho.getNombre() : "Banca"));
        System.out.println(String.format("\tPrecio: %,.0f€", this.valor));
        System.out.println(String.format("\tPago por caer: %,.0f€ × número de transportes del dueño", Valor.ALQUILER_TRANSPORTE));

        // Mostrar información adicional si tiene dueño
        if (this.duenho != null && !this.duenho.getNombre().equals("Banca")) {
            int transportesDelDuenho = 0;
            for (monopoly.casilla.Casilla propiedad : this.duenho.getPropiedades()) {
                if (propiedad instanceof Transporte) {
                    transportesDelDuenho++;
                }
            }
            System.out.println("\tEl dueño tiene " + transportesDelDuenho + " transporte(s)");
        }
        System.out.println("}");
    }

    // Los transportes no se pueden hipotecar - sobrescribir métodos relevantes
    @Override
    public boolean puedeHipotecar(Jugador jugador) {
        System.out.println("Los transportes no se pueden hipotecar.");
        return false;
    }

    @Override
    public boolean hipotecar() {
        System.out.println("Los transportes no se pueden hipotecar.");
        return false;
    }
}


