package partida;

import java.util.ArrayList;

import monopoly.*;


public class Jugador {

    //Atributos:
    private String nombre; //Nombre del jugador
    private Avatar avatar; //Avatar que tiene en la partida.
    private float fortuna; //Dinero que posee.
    private float gastos; //Gastos realizados a lo largo del juego.
    private boolean enCarcel; //Será true si el jugador está en la carcel
    private int tiradasCarcel; //Cuando está en la carcel, contará las tiradas sin éxito que ha hecho allí para intentar salir (se usa para limitar el numero de intentos).
    private int vueltas; //Cuenta las vueltas dadas al tablero.
    private ArrayList<Casilla> propiedades; //Propiedades que posee el jugador.
    private int vecesEnCarcel;

    //Constructor vacío. Se usará para crear la banca.
    public Jugador() {
        this.nombre = "Banca";
        this.fortuna = Valor.FORTUNA_BANCA;
        this.gastos = 0;
        this.enCarcel = false;
        this.tiradasCarcel = 0;
        this.vueltas = 0;
        this.propiedades = new ArrayList<>();
    }

    /*Constructor principal. Requiere parámetros:
    * Nombre del jugador, tipo del avatar que tendrá, casilla en la que empezará y ArrayList de
    * avatares creados (usado para dos propósitos: evitar que dos jugadores tengan el mismo nombre y
    * que dos avatares tengan mismo ID). Desde este constructor también se crea el avatar.
     */
    public Jugador(String nombre, String tipoAvatar, Casilla inicio, ArrayList<Avatar> avCreados) {
        this.nombre = nombre;
        this.avatar = new Avatar(tipoAvatar,this, inicio, avCreados);
        this.fortuna = Valor.FORTUNA_INICIAL;
        this.gastos = 0;
        this.enCarcel = false;
        this.tiradasCarcel = 0;
        this.vueltas = 0;
        this.propiedades = new ArrayList<>();
    }

    //Otros métodos:
    //Metodo para añadir una propiedad al jugador. Como parámetro, la casilla a añadir.
    public void anhadirPropiedad(Casilla casilla) {
        if(!this.propiedades.contains(casilla)) {
            propiedades.add(casilla);
        }
    }

    //Metodo para eliminar una propiedad del arraylist de propiedades de jugador.
    public void eliminarPropiedad(Casilla casilla) {
        if(this.propiedades.contains(casilla)) {
            propiedades.remove(casilla);
        }
    }

    //Metodo para añadir fortuna a un jugador
    //Como parámetro se pide el valor a añadir.
    public void sumarFortuna(float valor) {
        this.fortuna += valor;
    }

    //Metodo para restar fortuna a un jugador
    //Como parámetro se pide el valor a añadir.
    public void restarFortuna(float valor) {
        this.fortuna -= valor;
    }

    //Metodo para sumar gastos a un jugador.
    //Parámetro: valor a añadir a los gastos del jugador (será el precio de un solar, impuestos pagados...).
    public void sumarGastos(float valor) {
        this.gastos += valor;
    }

    /*Metodo para establecer al jugador en la cárcel.
    * Se requiere disponer de las casillas del tablero para ello (por eso se pasan como parámetro).*/
    public void encarcelar(ArrayList<ArrayList<Casilla>> pos) {
        this.enCarcel = true;
        this.tiradasCarcel = 0;

        // Buscar la casilla de la cárcel por nombre
        for(ArrayList<Casilla> lado : pos) {
            for(Casilla cas : lado) {
                if(cas.getNombre().equals("Carcel")) {
                    this.avatar.colocar(pos, cas.getPosicion());
                    System.out.println(this.nombre + " ha sido enviado a la cárcel.");
                    this.vecesEnCarcel++;
                    return;
                }
            }
        }
        System.out.println("Error: No se encontró la casilla Carcel");
    }

    public boolean salirCarcel() {
        float PRECIO_SALIDA_CARCEL = 500000;
        if (!this.enCarcel) {
            System.out.println(this.nombre + " no está en la cárcel.");
            return true; // ya libre
        }
        if (this.fortuna >= PRECIO_SALIDA_CARCEL) {
            this.restarFortuna(PRECIO_SALIDA_CARCEL);
            this.enCarcel = false;
            this.tiradasCarcel = 0;
            System.out.println(this.nombre + " ha pagado " + PRECIO_SALIDA_CARCEL + " para salir de la cárcel.");
            return true;
        } else {
            System.out.println(this.nombre + " no tiene suficiente dinero para salir de la cárcel ("
                    + this.fortuna + " < " + PRECIO_SALIDA_CARCEL + ").");
            return false;
        }
    }

    public boolean isEnCarcel() {
        return enCarcel;
    }

    //Getters y setters:
    public String getNombre() {
        return nombre;
    }
    public Avatar getAvatar() {
        return avatar;
    }
    public float getFortuna() {
        return fortuna;
    }
    public float getGastos() {
        return gastos;
    }

    public int getTiradasCarcel() {
        return tiradasCarcel;
    }
    public int getVueltas() {
        return vueltas;
    }
    public int getVecesEnCarcel() {
        return vecesEnCarcel;
    }
    public ArrayList<Casilla> getPropiedades() {
        return propiedades;
    }
    public void setEnCarcel(boolean enCarcel) {
        this.enCarcel = enCarcel;
    }
    public void setTiradasCarcel(int tiradasCarcel) {
        this.tiradasCarcel = tiradasCarcel;
    }
    public void setVueltas(int vueltas) {
        this.vueltas = vueltas++;
    }
    public void setVecesCarcel(int vecesEnCarcel) {
        this.vecesEnCarcel = vecesEnCarcel;
    }
}
