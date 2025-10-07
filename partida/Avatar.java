package partida;

import monopoly.*;

import java.util.Ramdom;
import java.util.ArrayList;


public class Avatar {

    ///Atributos
    private String id; ///Identificador: una letra generada aleatoriamente.
    private String tipo; ///Sombrero, Esfinge, Pelota, Coche
    private Jugador jugador; ///Un jugador al que pertenece ese avatar.
    private Casilla lugar; ///Los avatares se sitúan en casillas del tablero.

    ///Constructor vacío
    public Avatar() {}

    /**Constructor principal. Requiere éstos parámetros:
    * Tipo del avatar, jugador al que pertenece, lugar en el que estará ubicado, y un arraylist con los
    * avatares creados (usado para crear un ID distinto del de los demás avatares).
     */
    public Avatar(String tipo, Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) {
        this.tipo = tipo;
        this.jugador = jugador;
        this.lugar = lugar;
        this.avCreados = avCreados;
        generarId(avCreados);
        this.avCreados.add(this);
    }

    ///A continuación, tenemos otros métodos útiles para el desarrollo del juego.
    /**Método que permite mover a un avatar a una casilla concreta. Parámetros:
    * - Un array con las casillas del tablero. Se trata de un arrayList de arrayList de casillas (uno por lado).
    * - Un entero que indica el numero de casillas a moverse (será el valor sacado en la tirada de los dados).
    * EN ESTA VERSIÓN SUPONEMOS QUE valorTirada siempre es positivo.
     */
    public void moverAvatar(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {

        // PASO 1: calcular la nueva posición
        int posicionActual = this.lugar.getPosicion();

        // Calcular la nueva posición en el tablero después de la tirada
        int nuevaPosicion;
        nuevaPosicion = (posicionActual + valorTirada) % 40;

        // PASO 2: colocar el avatar en la nueva posicion
        this.colocar(casillas, nuevaPosicion);
    }

    /**Método que permite generar un ID para un avatar. Sólo lo usamos en esta clase (por ello es privado).
    * El ID generado será una letra mayúscula. Parámetros:
    * - Un arraylist de los avatares ya creados, con el objetivo de evitar que se generen dos ID iguales.
     */
    private void generarId(ArrayList<Avatar> avCreados) {
        Ramdom num = new.Ramdom();
        String id;
        boolean repetido = true;
        while (repetido) {
            repetido = false;
            ID = String.valueOf((char) (num.nextInt(26) + 'A')); ///Obtiene como ID una letra entre la a

            for (Avatar a : avCreados) {
                if (a != null && a.getId().equals(ID)) {
                    repetido = true;
                    break;  ///Si uno es igual no hace falta comprobar el resto
                }
            }
            ///Si no es repetido, se asigna el ID
            if (!repetido) {
                this.id = ID;
            }
        }
    }

    ///GETTERS, SETTERS
    public String getId() {
        return id;
    }
    //El id no necesita setter porque se le asigna un valor al crear el Avatar y no hay que modificarlo nunca
    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador_avatar) {
        this.jugador = jugador_avatar;
    }

    public Casilla getLugar() {
        return lugar;
    }

    public void setLugar(Casilla casilla_avatar) {
        this.lugar = casilla_avatar;
    }
}

