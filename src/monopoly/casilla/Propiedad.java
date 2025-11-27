package monopoly.casilla;

import monopoly.casilla.Casilla;
import partida.Jugador;
import partida.Avatar;

public abstract class Propiedad extends Casilla {
    // Atributos específicos de propiedades
    protected float valor;
    protected boolean hipotecada;
    protected float valorHipoteca;
    protected float impuesto; // Alquiler base

    // Constructores
    public Propiedad(String nombre, int posicion, float valor, float impuesto, Jugador duenho) {
        super(nombre, posicion, duenho);
        this.valor = valor;
        this.impuesto = impuesto;
        this.hipotecada = false;
        this.valorHipoteca = valor / 2; // Por defecto, la mitad del valor
    }

    public Propiedad(String nombre, int posicion, float valor, Jugador duenho) {
        this(nombre, posicion, valor, 0, duenho);
    }

    // MÉTODOS REQUERIDOS por el PDF - ABSTRACTOS
    public abstract boolean perteneceAJugador(Jugador jugador);
    public abstract boolean alquiler();
    public abstract float valor();

    // Método comprar - común para todas las propiedades
    public void comprar(Jugador jugador) {
        if (jugador.getFortuna() >= this.valor) {
            jugador.restarFortuna(this.valor);
            jugador.sumarDineroInvertido(this.valor);

            // Si tenía dueño anterior (banca), eliminarlo
            if (this.duenho != null && this.duenho.getNombre().equals("Banca")) {
                this.duenho.eliminarPropiedad(this);
            }

            jugador.anhadirPropiedad(this);
            this.duenho = jugador;

            System.out.printf("%s ha comprado la propiedad %s por el precio de %,.0f€\n",
                    jugador.getNombre(), this.nombre, this.valor);
        } else {
            System.out.printf("No tienes dinero para comprar esta propiedad. Necesitas %,.0f€ pero tienes %,.0f€\n",
                    this.valor, jugador.getFortuna());
        }
    }

    // MÉTODOS COMUNES de propiedades
    @Override
    public boolean esTipoComprable() {
        return true;
    }

    @Override
    public boolean estaAvatar(Avatar avatar) {
        return this.avatares.contains(avatar);
    }

    @Override
    public int frecuenciaVisita() {
        return this.contadorVisitas;
    }

    @Override
    public float getValor() {
        return this.valor;
    }

    // Método para comprar con banca (de tu código original)
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        if (solicitante.getAvatar().getLugar() == this) {
            // Verificar si pertenece a la banca
            if (this.duenho == null || this.duenho == banca || this.duenho.getNombre().equals("Banca")) {
                this.comprar(solicitante);
            } else {
                System.out.println("Esta propiedad no está en venta. Pertenece a: " + this.duenho.getNombre());
            }
        } else {
            System.out.println("¡Tienes que caer en la propiedad para poder comprarla!\n");
        }
    }

    // Métodos de hipoteca
    public boolean puedeHipotecar(Jugador jugador) {
        if (this.duenho == null || !this.duenho.equals(jugador)) {
            System.out.println(jugador.getNombre() + " no puede hipotecar " + this.nombre + ". No es una propiedad que le pertenece.");
            return false;
        }
        if (this.hipotecada) {
            System.out.println(jugador.getNombre() + " no puede hipotecar " + this.nombre + ". Ya está hipotecada.");
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

    // GETTERS Y SETTERS específicos de propiedades
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

    // Método toString requerido
    @Override
    public abstract String toString();
}
