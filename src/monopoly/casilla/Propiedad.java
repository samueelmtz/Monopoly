// monopoly/casilla/Propiedad.java
package monopoly.casilla;

import partida.Jugador;
import partida.Avatar;


public abstract class Propiedad extends Casilla {
    // Atributos específicos de propiedades - PRIVADOS
    private float valor;
    private boolean hipotecada;
    private float valorHipoteca;
    private float impuesto;
    private Jugador duenho;

    // Constructores
    public Propiedad(String nombre, int posicion, float valor, float impuesto, Jugador duenho) {
        super(nombre, posicion, duenho);
        this.valor = valor;
        this.impuesto = impuesto;
        this.hipotecada = false;
        this.duenho = duenho;
        this.valorHipoteca = valor / 2;
    }

    public Propiedad(String nombre, int posicion, float valor, Jugador duenho) {
        this(nombre, posicion, valor, 0, duenho);
    }

    // MÉTODOS REQUERIDOS
    public abstract boolean perteneceAJugador(Jugador jugador);
    public abstract boolean alquiler();
    public abstract float valor();

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
    public boolean esTipoComprable() { return true; }

    @Override
    public boolean estaAvatar(Avatar avatar) {
        return this.getAvatares().contains(avatar);
    }

    @Override
    public int frecuenciaVisita() {
        return this.getContadorVisitas();
    }

    @Override
    public float getValor() { return this.valor; }

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

    // GETTERS Y SETTERS
    public float getValorPropiedad() { return valor; }
    public void setValor(float valor) { this.valor = valor; }
    public float getImpuesto() { return impuesto; }
    public void setImpuesto(float impuesto) { this.impuesto = impuesto; }
    public boolean isHipotecada() { return hipotecada; }
    public void setHipotecada(boolean hipotecada) { this.hipotecada = hipotecada; }
    public float getValorHipoteca() { return valorHipoteca; }
    public void setValorHipoteca(float valorHipoteca) { this.valorHipoteca = valorHipoteca; }
    public Jugador getDuenho() { return duenho; }
    public void setDuenho(Jugador duenho) { this.duenho = duenho; }

    @Override
    public abstract String toString();
}