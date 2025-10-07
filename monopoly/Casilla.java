package monopoly;

import partida.*;
import java.util.ArrayList;

public class Casilla {

    //Atributos:
    private String nombre; //Nombre de la casilla
    private String tipo; //Tipo de casilla (Solar, Especial, Transporte, Servicios, Comunidad, Suerte y Impuesto).
    private float valor; //Valor de esa casilla (en la mayoría será valor de compra, en la casilla parking se usará como el bote).
    private int posicion; //Posición que ocupa la casilla en el tablero (entero entre 1 y 40).
    private Jugador duenho; //Dueño de la casilla (por defecto sería la banca).
    private Grupo grupo; //Grupo al que pertenece la casilla (si es solar).
    private float impuesto; //Cantidad a pagar por caer en la casilla: el alquiler en solares/servicios/transportes o impuestos.
    private float hipoteca; //Valor otorgado por hipotecar una casilla
    private ArrayList<Avatar> avatares; //Avatares que están situados en la casilla.

    //Constructores:
    public Casilla() {
    }//Parámetros vacíos

    /*Constructor para casillas tipo Solar, Servicios o Transporte:
     * Parámetros: nombre casilla, tipo (debe ser solar, serv. o transporte), posición en el tablero, valor y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, float valor, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.valor = valor;
        this.duenho = duenho;
        this.avatares = new ArrayList<Avatar>();
    }

    /*Constructor utilizado para inicializar las casillas de tipo IMPUESTOS.
     * Parámetros: nombre, posición en el tablero, impuesto establecido y dueño.
     */
    public Casilla(String nombre, int posicion, float impuesto, Jugador duenho) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.tipo = "IMPUESTOS";
        this.impuesto = impuesto;
        this.duenho = duenho;
        this.avatares = new ArrayList<Avatar>();
    }

    /*Constructor utilizado para crear las otras casillas (Suerte, Caja de comunidad y Especiales):
     * Parámetros: nombre, tipo de la casilla (será uno de los que queda), posición en el tablero y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.duenho = duenho;
        this.avatares = new ArrayList<Avatar>();
    }

    //Método utilizado para añadir un avatar al array de avatares en casilla.
    public void anhadirAvatar(Avatar av) {
        this.avatares.add(av);
    }

    //Método utilizado para eliminar un avatar del array de avatares en casilla.
    public void eliminarAvatar(Avatar av) {
        this.avatares.remove(av);
    }

    /*Método para evaluar qué hacer en una casilla concreta. Parámetros:
     * - Jugador cuyo avatar está en esa casilla.
     * - La banca (para ciertas comprobaciones).
     * - El valor de la tirada: para determinar impuesto a pagar en casillas de servicios.
     * Valor devuelto: true en caso de ser solvente (es decir, de cumplir las deudas), y false
     * en caso de no cumplirlas.*/
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {

    }

    /*Método usado para comprar una casilla determinada. Parámetros:
     * - Jugador que solicita la compra de la casilla.
     * - Banca del monopoly (es el dueño de las casillas no compradas aún).*/
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        if(this.esComprable()){ //Comprobar si es la casilla es de un tipo que se pueda comprar
            if(solicitante.getAvatar().getLugar() == this){ //Comprobar que el jugador está encima de la casilla
                if(this.duenho == banca){ //Comprobar si la propiedad pertenece a la banca
                    if(solicitante.getFortuna() >= this.valor){ //Comprobar si el jugador tiene suficiente dinero para comprar la propiedad
                        solicitante.restarFortuna();
                        solicitante.sumarGastos();
                        banca.eliminarPropiedad(this);
                        this.duenho = solicitante;

                        System.out.printf("%s ha comprado la propiedad %s por el precio de &.0f€.\n",
                                solicitante.getNombre(), this.nombre, this.valor);
                    }
                    else{
                        System.out.println("No tiene suficiente ahorrado para comprar esta casilla!\n");
                    }
                }
                else{
                    System.out.printf("Esta casilla ya fue comprada!\n");
                }
            }
            else{
                System.out.printf("El jugador no está sobre la casilla!\n");
            }
        }
        else{
            System.out.println("Esta casilla NO se puede comprar!\n");
        }
    }

    //Método auxiliar para verificar si se puede comprar una casilla.
    public boolean esComprable() {
        return (this.tipo.equals("Solar") || this.tipo.equals("Servicio") || this.tipo.equals("Transporte"));
    }

    /*Método para añadir valor a una casilla. Utilidad:
     * - Sumar valor a la casilla de parking.
     * - Sumar valor a las casillas de solar al no comprarlas tras cuatro vueltas de todos los jugadores.
     * Este método toma como argumento la cantidad a añadir del valor de la casilla.*/
    public void sumarValor(float suma) {
        this.valor += suma;
    }

    /*Método para mostrar información sobre una casilla.
     * Devuelve una cadena con información específica de cada tipo de casilla.*/
    public String infoCasilla() {

    }

    /* Método para mostrar información de una casilla en venta.
     * Valor devuelto: texto con esa información.
     */
    public String casEnVenta() {
        if(this.duenho == NULL || this.duenho.equals("Banca")){
            System.out.println("La casilla" + this.nombre + "está en venta por un precio de" + this.valor + "\n");
        }
        else{
            System.out.printf("La casilla no está en venta");
        }
    }


    //GETTERS Y SETTERS

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public float getValor() {
        return valor; }

    public int getPosicion() {
        return posicion;
    }

    public Jugador getDuenho() {
        return duenho;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public float getImpuesto() {
        return impuesto;
    }

    public float getHipoteca() {
        return hipoteca;
    }

    public ArrayList<Avatar> getAvatares() {
        return avatares;
    }

    public void setTipo(String tipo) {
       this.tipo = tipo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public void setImpuesto(float impuesto) {
        this.impuesto = impuesto;
    }

    public void setHipoteca(float hipoteca) {
        this.hipoteca = hipoteca;
    }

    public void setDuenho(Jugador duenho) {
        this.duenho = duenho;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }
}
