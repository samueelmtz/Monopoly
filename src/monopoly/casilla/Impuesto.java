package monopoly.casilla;

import partida.Jugador;
import partida.Avatar;
import monopoly.Juego;
import monopoly.Tablero;
import java.util.ArrayList;

public class Impuesto extends Casilla {
    private float cantidadImpuesto;

    // Constructor
    public Impuesto(String nombre, int posicion, Jugador duenho, float cantidadImpuesto) {
        super(nombre, posicion, duenho);
        this.cantidadImpuesto = cantidadImpuesto;
    }

    // MÉTODOS REQUERIDOS
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
        return String.format("Impuesto{nombre='%s', posición=%d, impuesto=%,.0f€}",
                this.getNombre(), this.getPosicion(), cantidadImpuesto);
    }


    // MÉTODO de evaluación de casilla
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, Tablero tablero, ArrayList<Jugador> jugadores, int tirada) {
        try {
            if (actual.getAvatar().getLugar() == this) {
                float impuestoAPagar = this.cantidadImpuesto;
                Juego.consola.imprimir("Impuesto a pagar: %,.0f€", impuestoAPagar);

                // Calcular recursos TOTALES disponibles (dinero + valor hipotecable)
                float dineroDisponible = actual.getFortuna();
                float valorHipotecaDisponible = calcularValorHipotecaDisponible(actual);
                float totalDisponible = dineroDisponible + valorHipotecaDisponible;

                if (totalDisponible < impuestoAPagar) {
                    // NO PUEDE PAGAR NI CON DINERO NI HIPOTECANDO → BANCARROTA INMEDIATA
                    Juego.consola.imprimir("✗ %s no puede pagar el impuesto de %,.0f€ en %s",
                            actual.getNombre(), impuestoAPagar, this.getNombre());
                    Juego.consola.imprimir("✗ Recursos totales: %,.0f€ (Dinero: %,.0f€ + Hipoteca: %,.0f€)",
                            totalDisponible, dineroDisponible, valorHipotecaDisponible);

                    // Declarar bancarrota automáticamente (la banca es el acreedor)
                    actual.declararBancarrotaPorImpuesto(impuestoAPagar);
                    return false;
                }

                // Si tiene suficiente dinero paga normalmente
                if (actual.getFortuna() >= impuestoAPagar) {
                    actual.restarFortuna(impuestoAPagar);
                    actual.sumarPagoTasasEImpuestos(impuestoAPagar);
                    tablero.añadirAlBote(impuestoAPagar);

                    Juego.consola.imprimir("Se han añadido %,.0f€ al bote del Parking. Bote actual: %,.0f€",
                            impuestoAPagar, tablero.getBoteParking());
                    Juego.consola.imprimir("%s ha pagado %,.0f€ de impuestos",
                            actual.getNombre(), impuestoAPagar);
                    return true;
                } else {
                    // Tiene recursos totales pero no efectivo suficiente
                    Juego.consola.imprimir("✗ %s no tiene suficiente efectivo (% ,.0f€) para pagar impuesto de %,.0f€",
                            actual.getNombre(), actual.getFortuna(), impuestoAPagar);
                    Juego.consola.imprimir("✓ Pero podría hipotecar propiedades por %,.0f€ para pagar",
                            valorHipotecaDisponible);
                    Juego.consola.imprimir("Usa 'hipotecar propiedad' para obtener efectivo y pagar.");
                    return false; // No solvente por ahora
                }
            }
            return false;

        } catch (Exception e) {
            Juego.consola.imprimir("⚠ Error inesperado en casilla de impuestos: " + e.getMessage());
            return false;
        }
    }

    // Método auxiliar para calcular valor hipotecable total (si no lo tienes en Jugador)
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


    // GETTER específico
    public float getCantidadImpuesto() {
        return cantidadImpuesto;
    }

    public void setCantidadImpuesto(float cantidadImpuesto) {
        this.cantidadImpuesto = cantidadImpuesto;
    }
}