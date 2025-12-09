package monopoly.casilla.propiedad;

import monopoly.casilla.Casilla;
import monopoly.Juego;
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

    // MÉTODO de evaluación de casilla
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
                    if (this.isHipotecada()) {
                        Juego.consola.imprimir("✗ La propiedad %s está hipotecada. No se paga alquiler.", this.getNombre());
                        return true; // No hay que pagar alquiler si está hipotecada
                    }

                float aPagar = calcularAlquilerTransporte();
                Jugador propietario = this.getDuenho();

                // Calcular recursos TOTALES disponibles (dinero + valor hipotecable)
                float dineroDisponible = actual.getFortuna();
                float valorHipotecaDisponible = calcularValorHipotecaDisponible(actual);
                float totalDisponible = dineroDisponible + valorHipotecaDisponible;

                if (totalDisponible < aPagar) {
                    // NO PUEDE PAGAR NI CON DINERO NI HIPOTECANDO → BANCARROTA INMEDIATA
                    Juego.consola.imprimir("✗ %s no puede pagar el alquiler de %,.0f€ por %s", actual.getNombre(), aPagar, this.getNombre());
                    Juego.consola.imprimir("✗ Recursos totales: %,.0f€ (Dinero: %,.0f€ + Hipoteca: %,.0f€)", totalDisponible, dineroDisponible, valorHipotecaDisponible);

                    // Declarar bancarrota automáticamente
                    actual.declararBancarrotaPorAlquiler(aPagar, propietario);
                    return false;
                }

                // Si tiene suficiente dinero, pagar normalmente
                if (actual.getFortuna() >= aPagar) {
                    actual.restarFortuna(aPagar);
                    actual.sumarPagoDeAlquileres(aPagar);
                    propietario.sumarFortuna(aPagar);
                    propietario.sumarCobroDeAlquileres(aPagar);
                    this.anhadirDineroGenerado(aPagar);

                    Juego.consola.imprimir("%s ha pagado %,.0f€ de alquiler a %s", actual.getNombre(), aPagar, propietario.getNombre());
                    Juego.consola.imprimir("Fortuna actual de %s: %,.0f€", actual.getNombre(), actual.getFortuna());
                    return true;
                } else {
                    // Tiene recursos totales pero no efectivo suficiente
                    Juego.consola.imprimir("✗ %s no tiene suficiente efectivo (% ,.0f€) para pagar alquiler de %,.0f€", actual.getNombre(), actual.getFortuna(), aPagar);
                    Juego.consola.imprimir("✓ Pero podría hipotecar propiedades por %,.0f€ para pagar", valorHipotecaDisponible);
                    Juego.consola.imprimir("Usa 'hipotecar propiedad' para obtener efectivo y pagar.");
                    return false; // No solvente por ahora
                }
            }
            return true;
        }
        return false;
    }

    // Método auxiliar para calcular valor hipotecable total
    private float calcularValorHipotecaDisponible(Jugador jugador) {
        float total = 0;
        for (Casilla propiedad : jugador.getPropiedades()) {
            if (propiedad instanceof Propiedad) {
                Propiedad prop = (Propiedad) propiedad;
                // Solo propiedades no hipotecadas y que se puedan hipotecar
                if (!prop.isHipotecada() && prop.esHipotecable()) {
                    total += prop.getValorHipoteca();
                }
            }
        }
        return total;
    }

    //Método para calcular el alquiler del transporte
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