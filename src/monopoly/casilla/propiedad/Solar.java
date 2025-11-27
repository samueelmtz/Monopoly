package monopoly.casilla.propiedad;

import monopoly.casilla.Propiedad;
import partida.Jugador;
import partida.Avatar;
import monopoly.Valor;
import monopoly.Grupo;
import java.util.ArrayList;

public class Solar extends Propiedad {
    // Atributos específicos de Solar - PRIVADOS
    private Grupo grupo;
    private int numCasas;
    private int numHoteles;
    private int numPiscinas;
    private int numPistas;

    // Constructores
    public Solar(String nombre, int posicion, float valor, float alquiler, Jugador duenho, Grupo grupo) {
        super(nombre, posicion, valor, alquiler, duenho);
        this.grupo = grupo;
        this.numCasas = 0;
        this.numHoteles = 0;
        this.numPiscinas = 0;
        this.numPistas = 0;
        this.setValorHipoteca(valor / 2);
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
        return String.format("Solar{nombre='%s', posicion=%d, valor=%,.0f€, grupo=%s}",
                this.getNombre(), this.getPosicion(), this.getValorPropiedad(), grupo != null ? grupo.getColorGrupo() : "null");
    }

    // MÉTODOS ESPECÍFICOS
    public void edificar(String tipoEdificio) {
        switch (tipoEdificio.toLowerCase()) {
            case "casa":
                if (anhadirCasa()) {
                    System.out.println("Casa construída en " + this.getNombre());
                }
                break;
            case "hotel":
                if (anhadirHotel()) {
                    System.out.println("Hotel construído en " + this.getNombre());
                }
                break;
            case "piscina":
                if (anhadirPiscina()) {
                    System.out.println("Piscina construída en " + this.getNombre());
                }
                break;
            case "pista_deporte":
                if (anhadirPistaDeporte()) {
                    System.out.println("Pista de deporte construída en " + this.getNombre());
                }
                break;
        }
    }

    public void hipotecar() {
        if (puedeHipotecar(this.getDuenho())) {
            this.setHipotecada(true);
            System.out.println(this.getDuenho().getNombre() + " ha hipotecado " + this.getNombre() + " por " +
                    String.format("%,.0f", this.getValorHipoteca()) + "€");
        }
    }

    public boolean estaHipotecada() {
        return this.isHipotecada();
    }

    // MÉTODO de evaluación de casilla
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.getAvatar().getLugar() == this) {
            if (this.getDuenho() == null || this.getDuenho() == banca || this.getDuenho().getNombre().equals("Banca")) {
                System.out.println("¡Este solar está disponible para compra! Usa el comando 'comprar " + this.getNombre() + "' para adquirirla.");
                return true;
            }

            if (this.getDuenho() != null && this.getDuenho() != banca && this.getDuenho() != actual) {
                if (this.isHipotecada()) {
                    System.out.println("El solar " + this.getNombre() + " está hipotecado. No se cobra alquiler.");
                    return true;
                }

                float aPagar = calcularAlquilerTotal();

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

    private float calcularAlquilerTotal() {
        float alquilerTotal = this.getImpuesto();

        if (this.numCasas > 0) {
            alquilerTotal += getAlquilerCasa() * this.numCasas;
        }
        if (this.numHoteles > 0) {
            alquilerTotal += getAlquilerHotel() * this.numHoteles;
        }
        if (this.numPiscinas > 0) {
            alquilerTotal += getAlquilerPiscina() * this.numPiscinas;
        }
        if (this.numPistas > 0) {
            alquilerTotal += getAlquilerPistaDeporte() * this.numPistas;
        }

        if (this.grupo != null && this.numCasas == 0 && this.numHoteles == 0) {
            boolean tieneTodoElGrupo = true;
            for (monopoly.casilla.Casilla casillaGrupo : this.grupo.getMiembros()) {
                if (casillaGrupo.getDuenho() != this.getDuenho()) {
                    tieneTodoElGrupo = false;
                    break;
                }
            }
            if (tieneTodoElGrupo) {
                alquilerTotal *= 2;
                System.out.println("¡Grupo completo! Alquiler doble.");
            }
        }

        System.out.printf("Alquiler de solar: %,.0f€\n", alquilerTotal);
        return alquilerTotal;
    }

    // GETTERS Y SETTERS específicos de Solar
    public Grupo getGrupo() { return grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }
    public int getNumCasas() { return numCasas; }
    public void setNumCasas(int numCasas) { this.numCasas = numCasas; }
    public int getNumHoteles() { return numHoteles; }
    public void setNumHoteles(int numHoteles) { this.numHoteles = numHoteles; }
    public int getNumPiscinas() { return numPiscinas; }
    public void setNumPiscinas(int numPiscinas) { this.numPiscinas = numPiscinas; }
    public int getNumPistas() { return numPistas; }
    public void setNumPistas(int numPistas) { this.numPistas = numPistas; }

    // Resto de métodos (anhadirCasa, getPrecioCasa, etc.) se mantienen igual
    // pero usando los getters en lugar de acceder directamente a los atributos
}