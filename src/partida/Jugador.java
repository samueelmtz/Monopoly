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
        this.avatar = new Avatar(tipoAvatar, inicio, avCreados);
        this.fortuna = Valor.FORTUNA_INICIAL;
        this.gastos = 0;
        this.enCarcel = false;
        this.tiradasCarcel = 0;
        this.vueltas = 0;
        this.propiedades = new ArrayList<>();
    }

    //Otros métodos:
    //Método para añadir una propiedad al jugador. Como parámetro, la casilla a añadir.
    public void anhadirPropiedad(Casilla casilla) {
        propiedades.add(casilla);
    }

    //Método para eliminar una propiedad del arraylist de propiedades de jugador.
    public void eliminarPropiedad(Casilla casilla) {
        propiedades.remove(casilla);
    }

    //Método para añadir fortuna a un jugador
    //Como parámetro se pide el valor a añadir.
    public void sumarFortuna(float valor) {

        this.fortuna += valor;

    }

    //Método para restar fortuna a un jugador
    //Como parámetro se pide el valor a añadir.
    public void restarFortuna(float valor) {
        this.fortuna -= valor;
    }

    //Método para sumar gastos a un jugador.
    //Parámetro: valor a añadir a los gastos del jugador (será el precio de un solar, impuestos pagados...).
    public void sumarGastos(float valor) {
        this.gastos += valor;
        this.fortuna -= valor;
    }

    /*Método para establecer al jugador en la cárcel.
     * Se requiere disponer de las casillas del tablero para ello (por eso se pasan como parámetro).*/
    public void encarcelar(ArrayList<ArrayList<Casilla>> pos) {
        this.enCarcel = true;
        this.tiradasCarcel = 0;

        // Buscar la casilla de la cárcel y mover el avatar allí
        for(ArrayList<Casilla> lado : pos) {
            for(Casilla cas : lado) {
                if(cas.getTipo().equals("Carcel")) { // Corregido: sin tilde
                    this.avatar.moveravatar(cas);
                    System.out.println(this.nombre + " ha sido enviado a la cárcel.");
                    return;
                }
            }
        }
        System.out.println("Error: No se pudo encontrar la casilla de la cárcel.");
    }

    /*Método para contar cuántas casillas posee un jugador de un tipo determinado
     * Solo se usa para las propiedades de tipo Transportes de momento
     * @param tipo Tipo de propiedad
     */
    public int numeroCasillasTipo(String tipo){
        int contador=0;
        for(Casilla c: propiedades){
            if(c.getTipo().equals(tipo)){
                contador++;
            }
        }
        return contador;
    }

    /**
     * Método para que un jugador salga de la cárcel pagando 500.000€
     * @return true si pudo salir, false si no pudo (no está en cárcel o no tiene dinero)
     */
    public boolean salirDeCarcel() {
        if (!this.enCarcel) {
            System.out.println("El jugador no está en la cárcel.");
            return false;
        }

        if (this.fortuna >= Valor.SALIR_CARCEL) {
            this.restarFortuna(Valor.SALIR_CARCEL);
            this.sumarGastos(Valor.SALIR_CARCEL);
            this.enCarcel = false;
            this.tiradasCarcel = 0;
            System.out.println(this.nombre + " paga " + Valor.SALIR_CARCEL + "€ para salir de la cárcel.");
            return true;
        } else {
            System.out.println(this.nombre + " no tiene suficiente dinero para salir de la cárcel.");
            return false;
        }
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
    public boolean isEnCarcel() {
        return enCarcel;
    }
    public int getTiradasCarcel() {
        return tiradasCarcel;
    }
    public int getVueltas() {
        return vueltas;
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


}