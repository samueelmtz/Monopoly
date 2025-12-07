package monopoly.casilla.propiedad;

import excepciones.ExcepcionFondosInsuficientes;
import monopoly.casilla.Propiedad;
import partida.Jugador;
import monopoly.Valor;
import monopoly.Juego;
import monopoly.Tablero;

import java.util.ArrayList;


public class Transporte extends Propiedad {

    // Constructor
    public Transporte(String nombre, int posicion, float valor, Jugador duenho) {
        super(nombre, posicion, valor, Valor.ALQUILER_TRANSPORTE, duenho);
    }

    // MÉTODOS REQUERIDOS por el PDF - IMPLEMENTACIÓN
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
        return String.format("Transporte{nombre='%s', posicion=%d, valor=%,.0f€}",
                this.getNombre(), this.getPosicion(), this.getValorPropiedad());
    }

    // MÉTODO infoCasilla() IMPLEMENTADO
    @Override
    public void infoCasilla() {
        Juego.consola.imprimir("{");
        Juego.consola.imprimir("\tTipo: Transporte");
        Juego.consola.imprimir("\tDueño: " + (this.getDuenho() != null ? this.getDuenho().getNombre() : "Banca"));
        Juego.consola.imprimir(String.format("\tPrecio: %,.0f€", this.getValorPropiedad()));
        Juego.consola.imprimir(String.format("\tPago por caer: %,.0f€ × número de transportes del dueño", Valor.ALQUILER_TRANSPORTE));

        // Mostrar información adicional si tiene dueño
        if (this.getDuenho() != null && !this.getDuenho().getNombre().equals("Banca")) {
            int transportesDelDuenho = 0;
            for (monopoly.casilla.Casilla propiedad : this.getDuenho().getPropiedades()) {
                if (propiedad instanceof Transporte) {
                    transportesDelDuenho++;
                }
            }
            Juego.consola.imprimir("\tEl dueño tiene " + transportesDelDuenho + " transporte(s)");
        }
        Juego.consola.imprimir("}");
    }

    // MÉTODO de evaluación de casilla - Polimorfismo
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, Tablero tablero, ArrayList<Jugador> jugadores, int tirada) {
        if (actual.getAvatar().getLugar() == this) {
            // Verificar si está disponible para compra
            if (this.getDuenho() == null || this.getDuenho() == banca || this.getDuenho().getNombre().equals("Banca")) {
                Juego.consola.imprimir("¡Este transporte está disponible para compra! Usa el comando 'comprar " + this.getNombre() + "' para adquirirla.");
                return true;
            }

            // Si tiene dueño y no es el jugador actual, calcular alquiler
            if (this.getDuenho() != null && this.getDuenho() != banca && this.getDuenho() != actual) {
                float aPagar = calcularAlquilerTransporte();

                // Verificar solvencia
                try {
                    if (actual.getFortuna() < aPagar) {
                        throw new ExcepcionFondosInsuficientes(actual.getNombre(), aPagar, actual.getFortuna(), "pagar alquiler");
                    }
                }catch (ExcepcionFondosInsuficientes e){
                    Juego.consola.imprimir("ERROR: " + e.getMessage());
                }

                // Aplicar pago
                actual.restarFortuna(aPagar);
                actual.sumarPagoDeAlquileres(aPagar);
                this.getDuenho().sumarFortuna(aPagar);
                this.getDuenho().sumarCobroDeAlquileres(aPagar);
                this.anhadirDineroGenerado(aPagar);

                Juego.consola.imprimir("%s ha pagado %,.0f€ de alquiler a %s\n", actual.getNombre(), aPagar, this.getDuenho().getNombre());
            }
            return true;
        }
        return false;
    }



    private float calcularAlquilerTransporte() {
        // Contar cuántos transportes tiene el dueño
        int transportesDelDuenho = 0;
        if (this.getDuenho() != null) {
            for (monopoly.casilla.Casilla propiedad : this.getDuenho().getPropiedades()) {
                if (propiedad instanceof Transporte) {
                    transportesDelDuenho++;
                }
            }
        }

        float alquiler = Valor.ALQUILER_TRANSPORTE * transportesDelDuenho;
        Juego.consola.imprimir("Alquiler de transporte: %,.0f€ (el dueño tiene %d transporte%s)\n",
                alquiler, transportesDelDuenho, transportesDelDuenho != 1 ? "s" : "");
        return alquiler;
    }
}