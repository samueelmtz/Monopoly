package monopoly.casilla;

import partida.Jugador;
import partida.Avatar;
import java.util.ArrayList;
import monopoly.interfaces.*;
import monopoly.edificio.*;
import monopoly.casilla.propiedad.*;
import monopoly.Juego;

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

            Juego.consola.imprimir("%s ha comprado la propiedad %s por el precio de %,.0f€\n",
                    jugador.getNombre(), this.getNombre(), this.valor);
        } else {
            Juego.consola.imprimir("No tienes dinero para comprar esta propiedad. Necesitas %,.0f€ pero tienes %,.0f€\n",
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
                Juego.consola.imprimir("Esta propiedad no está en venta. Pertenece a: " + this.getDuenho().getNombre());
            }
        } else {
            Juego.consola.imprimir("¡Tienes que caer en la propiedad para poder comprarla!\n");
        }
    }

    public boolean isHipotecada() {
        return this.hipotecada;
    }

    public boolean esHipotecable() {
        if (!hipotecada) { // Verifica si la propiedad no está hipotecada
            boolean sinEdificios = true; // Inicializa como que no hay edificaciones

            // Verifica si la propiedad es una instancia de Solar
            if (this instanceof Solar) {
                Solar solar = (Solar) this;  // Hacemos un cast a Solar para acceder a los atributos específicos de Solar
                for (ArrayList<Edificio> tipoEdificio : solar.getEdificios()) {  // Accede a la lista de edificios
                    if (!tipoEdificio.isEmpty()) {  // Si alguna lista de edificios no está vacía
                        sinEdificios = false;  // Marca que no está vacío, por lo tanto, no puede hipotecarse
                        break;
                    }
                }
            }

            // Si hay edificaciones, no se puede hipotecar
            if (!sinEdificios) {
                Juego.consola.imprimir("No puedes hipotecar la casilla " + this.getNombre() + " porque tienes que vender todas tus edificaciones.");
                return false;  // Retorna false indicando que no puede hipotecarse
            } else {
                hipotecada = true;  // Marca como hipotecada
                return true;  // Retorna true indicando que sí se puede hipotecar
            }
        } else {
            Juego.consola.imprimir("No puedes hipotecar esta propiedad porque ya está hipotecada.");
            return false;  // Retorna false si ya está hipotecada
        }
    }

    public boolean puedeDeshipotecar(Jugador jugador) {
        if (this.getDuenho() == null || !this.getDuenho().equals(jugador)) {
            Juego.consola.imprimir(jugador.getNombre() + " no puede hipotecar " + this.getNombre() + ". No es una propiedad que le pertenece.");
            return false;
        }
        if(!this.hipotecada) {
            Juego.consola.imprimir(jugador.getNombre() + " no puede deshipotecar " + this.getNombre() + ". No está hipotecada.");
            return false;
       }
        return true;
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
                Juego.consola.imprimir("¡Esta propiedad está disponible para compra! Usa el comando 'comprar " + this.getNombre() + "' para adquirirla.");
            }
            return true;
        }
        return false;
    }

    @Override
    public void infoCasilla() {
        // Implementación por defecto - será sobrescrita en subclases
        Juego.consola.imprimir("{");
        Juego.consola.imprimir("\tTipo: Propiedad");
        Juego.consola.imprimir("\tDueño: " + (this.getDuenho() != null ? this.getDuenho().getNombre() : "Banca"));
        Juego.consola.imprimir(String.format("\tPrecio: %,.0f€", this.valor));
        Juego.consola.imprimir("}");
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }
}
