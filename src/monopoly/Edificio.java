package monopoly;

import partida.*;
import monopoly.*;

import java.util.HashMap;
import java.util.Map;

public class Edificio {

    //ATRIBUTOS
    private String id; //Identificador
    private String tipoEdificio; //Casa, hotel, piscina, pista de deporte
    private Jugador duenho; //Dueño del edificio
    private Casilla casilla; //Casilla en la que se edifica
    private float coste; //Precio del edificio
    private Grupo grupo; //Grupo de la casilla edificada

    private static int contadorCasas = 0;
    private static int contadorHoteles = 0;
    private static int contadorPiscinas = 0;
    private static int contadorPistas = 0;

    //CONSTRUCTORES
    public Edificio(){
        this.id = null;
        this.tipoEdificio = null;
        this.casilla = null;
        this.coste = 0;
        this.grupo = null;
    } //Parámetros vacíos

    /*Constructor principal
    * @param tipo Tipo de edificio (casa, hotel, piscina, pista de deporte)
    * @param lugar Casilla en la que está construído el edificio
    */
    public Edificio(String tipo, Casilla lugar){
        this.id = generarID(tipo);
        this.tipoEdificio = tipo;
        this.casilla = lugar;
        this.duenho = casilla.getDuenho();
        this.grupo = casilla.getGrupo();
        this.coste = calcularCoste(tipo, lugar);
    }

    //MÉTODOS
    /* Genera IDs únicos como casa-1, hotel-2, etc.
    * @param tipo Tipo de edificio
    */
    public String generarID(String tipo) {
        switch (tipo) {
            case "casa":
                contadorCasas++;
                return "casa-" + contadorCasas;
            case "hotel":
                contadorHoteles++;
                return "hotel-" + contadorHoteles;
            case "piscina":
                contadorPiscinas++;
                return "piscina-" + contadorPiscinas;
            case "pista_deporte":
                contadorPistas++;
                return "pista_deporte" + contadorPistas;
            default:
                System.out.println("Tipo de edificio desconocido: " + tipo);
                break;
        }
        return "";
    }

    /* Método que comprueba si se trata de un tipo válido de edificio
    * @param tipo Tipo de edificio
    */
    public boolean EsEdificioValido(String tipo){
        return tipo.equals("casa") || tipo.equals("hotel") || tipo.equals("piscina")  || tipo.equals("pista_deporte");
    }

    /* Método para calcular el coste de construcción de un tipo de edificio
    * @param lugar Casilla en la que se construirá el edificio
    * @param tipo Tipo de edificio a construír
    */
    public static float calcularCoste(String tipoEdificio, Casilla casilla) {

        return switch (tipoEdificio) {
            case "casa", "hotel" -> casilla.getPrecioCasa();
            case "piscina" -> casilla.getPrecioPiscina();
            case "pista" -> casilla.getPrecioPistaDeporte();
            default -> 0f;
        };
    }
    // Lista todas las edificaciones en su respectivo array
    public String infoEdificio() {
        String cadena = "";

        cadena += "{\n";
        cadena += "\tid: " + this.id + ",\n";
        cadena += "\tpropietario: " + this.duenho.getNombre() + ",\n";
        cadena += "\tcasilla: " + this.casilla.getNombre() + ",\n";
        cadena += "\tgrupo: " + this.grupo.getColorGrupo() + ",\n";
        cadena += "\tcoste: " + this.coste + "\n";
        cadena += "},\n";

        return cadena;
    }

    //GETTERS
    public String getId() {
        return id;
    }

    public String getTipo() {
        return tipoEdificio;
    }

    public Jugador getDuenho() {
        return duenho;
    }

    public Casilla getCasilla() {
        return casilla;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public float getCoste() {
        return coste;
    }

}

