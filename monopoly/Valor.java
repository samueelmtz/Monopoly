package monopoly;


public class Valor {
    //Se incluyen una serie de constantes útiles para no repetir valores.
    public static final float FORTUNA_BANCA = Float.MAX_VALUE; // Cantidad que tiene inicialmente la Banca
    public static final float FORTUNA_INICIAL = 15000000f; // Cantidad que recibe cada jugador al comenzar la partida
    public static final float SUMA_VUELTA = 2000000f; // Cantidad que recibe un jugador al pasar pos la Salida
    public static final float SALIR_CARCEL = 500000f; // Cantidad que debe pagar un jugador para salir de la cárcel
    public static final float FACTOR_SERVICIO = 50000f;
    public static final float ALQUILER_TRANSPORTE = 250000f;
    public static final float IMPUESTO = 2000000f; // Cantidad a pagar en la casilla de Impuestos
    public static final float PRECIO_SERVICIO_TRANSPORTE = 500000f;
    
    //Colores del texto:
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    //Colores de fondo para los grupos de solares:
    public static final String BG_BLACK = "\u001B[40m";
    public static final String BG_RED = "\u001B[41m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String BG_YELLOW = "\u001B[43m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_PURPLE = "\u001B[45m";
    public static final String BG_CYAN = "\u001B[46m";
    public static final String BG_WHITE = "\u001B[47m";
}
