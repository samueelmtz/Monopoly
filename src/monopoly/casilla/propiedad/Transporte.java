package monopoly.casilla.propiedad;

import partida.Jugador;
import partida.Avatar;
import monopoly.Valor;

public class Servicio extends Propiedad {

    // Constructor
    public Servicio(String nombre, int posicion, float valor, Jugador duenho) {
        super(nombre, posicion, valor, 0, duenho); // El impuesto se calcula dinámicamente
    }

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
        return String.format("Servicio{nombre='%s', posicion=%d, valor=%,.0f€}",
                nombre, posicion, valor);
    }

    // MÉTODO de evaluación de casilla - Polimorfismo
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.getAvatar().getLugar() == this) {
            // Verificar si está disponible para compra
            if (this.duenho == null || this.duenho == banca || this.duenho.getNombre().equals("Banca")) {
                System.out.println("¡Este servicio está disponible para compra! Usa el comando 'comprar " + this.nombre + "' para adquirirla.");
                return true;
            }

            // Si tiene dueño y no es el jugador actual, calcular alquiler
            if (this.duenho != null && this.duenho != banca && this.duenho != actual) {
                if (this.hipotecada) {
                    System.out.println("El servicio " + this.nombre + " está hipotecado. No se cobra alquiler.");
                    return true;
                }

                float aPagar = calcularAlquilerServicio(tirada);

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

    private float calcularAlquilerServicio(int tirada) {
        // Contar cuántos servicios tiene el dueño
        int serviciosDelDuenho = 0;
        if (this.duenho != null) {
            for (monopoly.casilla.Casilla propiedad : this.duenho.getPropiedades()) {
                if (propiedad instanceof Servicio) {
                    serviciosDelDuenho++;
                }
            }
        }

        // Determinar multiplicador según cantidad de servicios
        int multiplicador;
        if (serviciosDelDuenho == 1) {
            multiplicador = 4;
        } else if (serviciosDelDuenho == 2) {
            multiplicador = 10;
        } else {
            multiplicador = 4; // Por defecto
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
        System.out.println("\tDueño: " + (this.duenho != null ? this.duenho.getNombre() : "Banca"));
        System.out.println(String.format("\tPrecio: %,.0f€", this.valor));
        System.out.println(String.format("\tPago por caer: dados × multiplicador × %,.0f€", Valor.FACTOR_SERVICIO));
        System.out.println("\t\t(multiplicador=4 si se posee un servicio, multiplicador=10 si se poseen 2)");

        // Mostrar información adicional si tiene dueño
        if (this.duenho != null && !this.duenho.getNombre().equals("Banca")) {
            int serviciosDelDuenho = 0;
            for (monopoly.casilla.Casilla propiedad : this.duenho.getPropiedades()) {
                if (propiedad instanceof Servicio) {
                    serviciosDelDuenho++;
                }
            }
            System.out.println("\tEl dueño tiene " + serviciosDelDuenho + " servicio(s)");
        }
        System.out.println("}");
    }

    // Los servicios no se pueden hipotecar - sobrescribir métodos relevantes
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