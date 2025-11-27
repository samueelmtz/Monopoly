package monopoly.casilla;

import partida.Avatar;
import partida.Jugador;
import java.util.ArrayList;

public abstract class Casilla {
    // Atributos COMUNES a todas las casillas
    private String nombre;
    private int posicion;
    private ArrayList<Avatar> avatares;
    private int contadorVisitas;
    private Jugador duenho; // Dueño (puede ser null para casillas no comprables)

    // Constructores
    public Casilla(String nombre, int posicion, Jugador duenho) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.duenho = duenho;
        this.avatares = new ArrayList<>();
        this.contadorVisitas = 0;
    }

    public Casilla() {
        this.avatares = new ArrayList<>();
        this.contadorVisitas = 0;
    }

    //metodos abstractos comunes
    public abstract boolean estaAvatar(Avatar avatar);
    public abstract int frecuenciaVisita();
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

    // MÉTODO COMÚN para evaluación de casilla - será sobrescrito en subclases
    public abstract boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada);

    // MÉTODO para mostrar información - será sobrescrito en subclases
    public abstract void infoCasilla();

    // MÉTODO para casilla en venta - común pero puede sobrescribirse
    public void casEnVenta() {
        if(this.duenho == null || this.duenho.getNombre().equals("Banca")){
            System.out.println("{");
            System.out.println("    tipo: " + this.getClass().getSimpleName() + ",");
            System.out.println("    nombre: " + this.nombre + ",");
            System.out.println("    valor: " + String.format("%,.0f", this.getValor()) + "€");
            System.out.println("}");
        } else {
            System.out.println("La casilla " + this.nombre + " ya está vendida a " + this.duenho.getNombre());
        }
    }

    // MÉTODO auxiliar para valor - será implementado en subclases
    public abstract float getValor();

    // GETTERS Y SETTERS comunes
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public Jugador getDuenho() {
        return duenho;
    }

    public void setDuenho(Jugador duenho) {
        this.duenho = duenho;
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