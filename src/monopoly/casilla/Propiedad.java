package monopoly.casilla;

import excepciones.*;
import partida.Jugador;
import partida.Avatar;
import java.util.ArrayList;
import monopoly.interfaces.*;
import monopoly.edificio.*;
import monopoly.casilla.propiedad.*;
import monopoly.Juego;
import monopoly.Tablero;

public class Propiedad extends Casilla {
    // Atributos específicos de propiedades - PRIVADOS
    private float valor;
    private boolean hipotecada;
    private float valorHipoteca;
    private float impuesto;
    private Grupo grupo;
    private float dineroGenerado;

    // Constructores
    public Propiedad(String nombre, int posicion, float valor, float impuesto, Jugador duenho) {
        super(nombre, posicion, duenho);
        this.valor = valor;
        this.impuesto = impuesto;
        this.hipotecada = false;
        this.grupo = null;
        this.valorHipoteca = valor / 2;
        this.dineroGenerado = 0;
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
        try {
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
                throw new ExcepcionFondosInsuficientes(jugador.getNombre(), this.valor, jugador.getFortuna(), "comprar propiedad");
            }
        }catch (ExcepcionFondosInsuficientes e){
            Juego.consola.imprimir("ERROR: " + e.getMessage());
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
        try {
            if (solicitante.getAvatar().getLugar() == this) {
                try {
                    if (this.getDuenho() == null || this.getDuenho() == banca || this.getDuenho().getNombre().equals("Banca")) {
                        this.comprar(solicitante);
                    } else {
                        throw new ExcepcionPropiedadYaComprada(this.getNombre(), this.getDuenho().getNombre());
                    }
                } catch (ExcepcionPropiedadYaComprada e) {
                    Juego.consola.imprimir("ERROR: " + e.getMessage());
                }
            } else {
                throw new ExcepcionPropiedadNoValida(this.getNombre(), solicitante.getNombre());
            }
        }catch (ExcepcionPropiedadNoValida e){
            Juego.consola.imprimir("ERROR: " + e.getMessage());
        }
    }

    public boolean isHipotecada() {
        return this.hipotecada;
    }

    public boolean esHipotecable() {
        if (hipotecada) {
            Juego.consola.imprimir("No puedes hipotecar esta propiedad porque ya está hipotecada.");
            return false;
        }

        // Verificar si es Solar y tiene edificios
        if (this instanceof Solar) {
            Solar solar = (Solar) this;
            for (ArrayList<Edificio> tipoEdificio : solar.getEdificios()) {
                if (!tipoEdificio.isEmpty()) {
                    Juego.consola.imprimir("No puedes hipotecar la casilla " + this.getNombre() +
                            " porque tienes que vender todas tus edificaciones.");
                    return false;
                }
            }
        }

        return true;  // Solo verifica, NO cambia estado
    }

    // Nuevo método para EJECUTAR la hipoteca
    public boolean ejecutarHipoteca() {
        if (esHipotecable()) {
            hipotecada = true;
            return true;
        }
        return false;
    }


    public boolean puedeDeshipotecar(Jugador jugador) {
        if (this.getDuenho() == null || !this.getDuenho().equals(jugador)) {
            Juego.consola.imprimir(jugador.getNombre() + " no puede DESHIPOTECAR " +
                    this.getNombre() + ". No es una propiedad que le pertenece.");
            return false;
        }
        if (!this.hipotecada) {
            Juego.consola.imprimir(jugador.getNombre() + " no puede deshipotecar " +
                    this.getNombre() + ". No está hipotecada.");
            return false;
        }
        return true;
    }

    // Nuevo método para EJECUTAR la deshipoteca
    public boolean ejecutarDeshipoteca() {
        if (hipotecada) {
            hipotecada = false;
            return true;
        }
        return false;
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


    public void setHipotecada(boolean hipotecada) {
        this.hipotecada = hipotecada;
    }

    @Override
    public String toString() {
        return String.format("Propiedad{nombre='%s', posicion=%d, valor=%,.0f€}",
                this.getNombre(), this.getPosicion(), this.valor);
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, Tablero tablero, ArrayList<Jugador> jugadores, int tirada) {
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

    public void anhadirDineroGenerado(float cantidad) {
        this.dineroGenerado += cantidad;
    }

    // Getter
    public float getDineroGenerado() {
        return dineroGenerado;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }
}
