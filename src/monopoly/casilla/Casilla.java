package monopoly.casilla;

import partida.Avatar;
import partida.Jugador;
import java.util.ArrayList;

public abstract class Casilla {
    // Atributos COMUNES a todas las casillas
    protected String nombre;
    protected int posicion;
    protected ArrayList<Avatar> avatares;
    protected int contadorVisitas;

    // Constructores
    public Casilla(String nombre, int posicion) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.avatares = new ArrayList<>();
        this.contadorVisitas = 0;
    }

    public Casilla() {
        this.avatares = new ArrayList<>();
        this.contadorVisitas = 0;
    }

    //Métodos abstractos comunes
    public abstract boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada);
    public abstract void infoCasilla();
    public abstract String toString();

    // MÉTODOS COMUNES que ya tienes en tu clase original
    public void eliminarAvatar(Avatar avatar) {
        this.avatares.remove(avatar);
    }

    public void anhadirAvatar(Avatar avatar) {
        this.avatares.add(avatar);
    }

    public void registrarVisita() {
        this.contadorVisitas++;
    }

    public boolean estaAvatar(Avatar avatar){
        return avatar.getLugar().equals(this);
    }

    // GETTERS comunes
    public String getNombre() {
        return nombre;
    }

    public int getPosicion() {
        return posicion;
    }

    public ArrayList<Avatar> getAvatares() {
        return avatares;
    }

    public int getContadorVisitas() {
        return contadorVisitas;
    }

    // MÉTODO para verificar si es comprable - común pero puede sobrescribirse
    public boolean esTipoComprable() {
        return false; // Por defecto no es comprable, se sobrescribe en propiedades
    }
}

