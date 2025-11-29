package monopoly.casilla;
import partida.Avatar;
import partida.Jugador;
import java.util.ArrayList;
import monopoly.interfaces.Consola;
import monopoly.interfaces.ConsolaNormal;

public abstract class Casilla {
    // Atributos COMUNES a todas las casillas
    private String nombre;
    private int posicion;
    private ArrayList<Avatar> avatares;
    private int contadorVisitas;
    private Jugador duenho;
    private Consola consola;

    // Constructores
    public Casilla(String nombre, int posicion, Jugador duenho) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.duenho = duenho;
        this.avatares = new ArrayList<>();
        this.contadorVisitas = 0;
        this.consola = new ConsolaNormal();
    }

    public Casilla() {
        this.avatares = new ArrayList<>();
        this.contadorVisitas = 0;
        this.consola = new ConsolaNormal();
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

    public void casEnVenta() {
        if(this.getDuenho() == null || this.getDuenho().getNombre().equals("Banca")){
            consola.imprimir("{");
            consola.imprimir("    tipo: " + this.getClass().getSimpleName() + ",");
            consola.imprimir("    nombre: " + this.getNombre() + ",");
            consola.imprimir("    valor: " + String.format("%,.0f", this.getValor()) + "€");
            consola.imprimir("}");
        } else {
            consola.imprimir("La casilla " + this.getNombre() + " ya está vendida a " + this.getDuenho().getNombre());
        }
    }

    public abstract boolean estaAvatar(Avatar avatar);
    public abstract int frecuenciaVisita();

    // GETTERS y SETTERS comunes
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPosicion() {
        return this.posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public Jugador getDuenho() {
        return this.duenho;
    }

    public void setDuenho(Jugador duenho) {
        this.duenho = duenho;
    }

    public ArrayList<Avatar> getAvatares() {
        return this.avatares;
    }

    public int getContadorVisitas() {
        return this.contadorVisitas;
    }

    // MÉTODO para verificar si es comprable - común pero puede sobrescribirse
    public boolean esTipoComprable() {
        return false; // Por defecto no es comprable, se sobrescribe en propiedades
    }
}

