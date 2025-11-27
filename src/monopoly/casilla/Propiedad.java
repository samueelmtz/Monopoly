package monopoly.casilla;

import partida.Jugador;
import partida.Avatar;

public class Propiedad extends Casilla {
    // Atributos específicos de propiedades - PRIVADOS
    private float valor;
    private boolean hipotecada;
    private float valorHipoteca;
    private float impuesto;
    private Grupo grupo;

    // Constructores
    public Propiedad(String nombre, int posicion, float valor, float impuesto, Jugador duenho) {
        super(nombre, posicion, duenho);
        this.valor = valor;
        this.impuesto = impuesto;
        this.hipotecada = false;
        this.grupo = null;
        this.valorHipoteca = valor / 2;
    }

    public Propiedad(String nombre, int posicion, float valor, Jugador duenho) {
        this(nombre, posicion, valor, 0, duenho);
    }

    // MÉTODOS REQUERIDOS - IMPLEMENTACIÓN POR DEFECTO
    public boolean perteneceAJugador(Jugador jugador) {
        return super.getDuenho() != null && super.getDuenho().equals(jugador);
    }

    public boolean alquiler() {
        return !this.hipotecada && this.getDuenho() != null;
    }

    public float valor() {
        return this.valor;
    }

    public void comprar(Jugador jugador) {
        if (jugador.getFortuna() >= this.valor) {
            jugador.restarFortuna(this.valor);
            jugador.sumarDineroInvertido(this.valor);

            if (this.getDuenho() != null && this.getDuenho().getNombre().equals("Banca")) {
                this.getDuenho().eliminarPropiedad(this);
            }

            jugador.anhadirPropiedad(this);
            this.setDuenho(jugador);

            System.out.printf("%s ha comprado la propiedad %s por el precio de %,.0f€\n",
                    jugador.getNombre(), this.getNombre(), this.valor);
        } else {
            System.out.printf("No tienes dinero para comprar esta propiedad. Necesitas %,.0f€ pero tienes %,.0f€\n",
                    this.valor, jugador.getFortuna());
        }
    }

    // MÉTODOS COMUNES
    @Override
    public boolean esTipoComprable() {
        return true;
    }

    @Override
    public boolean estaAvatar(Avatar avatar) {
        return this.getAvatares().contains(avatar);
    }

    @Override
    public int frecuenciaVisita() {
        return this.getContadorVisitas();
    }

    public float getValor() {
        return this.valor;
    }

    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        if (solicitante.getAvatar().getLugar() == this) {
            if (this.getDuenho() == null || this.getDuenho() == banca || this.getDuenho().getNombre().equals("Banca")) {
                this.comprar(solicitante);
            } else {
                System.out.println("Esta propiedad no está en venta. Pertenece a: " + this.getDuenho().getNombre());
            }
        } else {
            System.out.println("¡Tienes que caer en la propiedad para poder comprarla!\n");
        }
    }

    // Métodos de hipoteca
    public boolean puedeHipotecar(Jugador jugador) {
        if (this.getDuenho() == null || !this.getDuenho().equals(jugador)) {
            System.out.println(jugador.getNombre() + " no puede hipotecar " + this.getNombre() + ". No es una propiedad que le pertenece.");
            return false;
        }
        if (this.hipotecada) {
            System.out.println(jugador.getNombre() + " no puede hipotecar " + this.getNombre() + ". Ya está hipotecada.");
            return false;
        }
        return true;
    }

    public boolean hipotecar() {
        if (!this.hipotecada) {
            this.hipotecada = true;
            return true;
        }
        return false;
    }

    public boolean deshipotecar() {
        if (this.hipotecada) {
            this.hipotecada = false;
            return true;
        }
        return false;
    }

    public boolean isHipotecada() {
        return this.hipotecada;
    }

    // GETTERS Y SETTERS
    public float getValorPropiedad() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public float getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(float impuesto) {
        this.impuesto = impuesto;
    }

    public float getValorHipoteca() {
        return valorHipoteca;
    }

    public void setValorHipoteca(float valorHipoteca) {
        this.valorHipoteca = valorHipoteca;
    }

    // monopoly/casilla/Propiedad.java - Añadir setHipotecada()
    public void setHipotecada(boolean hipotecada) {
        this.hipotecada = hipotecada;
    }

    @Override
    public String toString() {
        return String.format("Propiedad{nombre='%s', posicion=%d, valor=%,.0f€}",
                this.getNombre(), this.getPosicion(), this.valor);
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        // Implementación por defecto - será sobrescrita en subclases
        if (actual.getAvatar().getLugar() == this) {
            if (this.getDuenho() == null || this.getDuenho() == banca || this.getDuenho().getNombre().equals("Banca")) {
                System.out.println("¡Esta propiedad está disponible para compra! Usa el comando 'comprar " + this.getNombre() + "' para adquirirla.");
            }
            return true;
        }
        return false;
    }

    @Override
    public void infoCasilla() {
        // Implementación por defecto - será sobrescrita en subclases
        System.out.println("{");
        System.out.println("\tTipo: Propiedad");
        System.out.println("\tDueño: " + (this.getDuenho() != null ? this.getDuenho().getNombre() : "Banca"));
        System.out.println(String.format("\tPrecio: %,.0f€", this.valor));
        System.out.println("}");
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }
}
