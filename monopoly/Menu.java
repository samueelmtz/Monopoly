package monopoly;

import java.util.ArrayList;
import partida.*;

public class Menu {

    //Atributos
    private ArrayList<Jugador> jugadores; //Jugadores de la partida.
    private ArrayList<Avatar> avatares; //Avatares en la partida.
    private int turno = 0; //Índice correspondiente a la posición en el arrayList del jugador (y el avatar) que tienen el turno
    private int lanzamientos; //Variable para contar el número de lanzamientos de un jugador en un turno.
    private Tablero tablero; //Tablero en el que se juega.
    private Dado dado1; //Dos dados para lanzar y avanzar casillas.
    private Dado dado2;
    private Jugador banca; //El jugador banca.
    private boolean tirado; //Booleano para comprobar si el jugador que tiene el turno ha tirado o no.
    private boolean solvente; //Booleano para comprobar si el jugador que tiene el turno es solvente, es decir, si ha pagado sus deudas.


    // Método para inciar una partida: crea los jugadores y avatares.
    private void iniciarPartida() {
        // Crear lista de jugadores y avatares
        jugadores = new ArrayList<>();
        avatares = new ArrayList<>();

        // Inicializar tablero
        tablero = new Tablero();

        // Inicializar dados
        dado1 = new Dado();
        dado2 = new Dado();

        // Crear la banca
        banca = new Jugador("Banca", null); // null porque no tiene avatar
        banca.setFortuna(Long.MAX_VALUE);   // o alguna representación de dinero ilimitado

        // Dar a la banca todas las propiedades del tablero
        for (Casilla c : tablero.getCasillas()) {
            if (c instanceof Propiedad) {
                ((Propiedad) c).setPropietario(banca);
            }
        }

        // Variables de control de turno
        turno = 0;
        lanzamientos = 0;
        tirado = false;
        solvente = true;

        System.out.println("Partida iniciada. Tablero preparado. Esperando jugadores...");
    }

    
    /*Método que interpreta el comando introducido y toma la accion correspondiente.
    * Parámetro: cadena de caracteres (el comando).
    */
    private void analizarComando(String comando) {
        String[] comandos = comando.split(" ");
        switch (comandos[0]) {
            case "describir":
                if (comandos.length < 2) {
                    System.out.println("Comando incompleto. Uso: describir <jugador|avatar|nombre_casilla> [ID|nombre]");
                } else {
                    switch (comandos[1]) {
                        case "jugador":
                            descJugador(comandos);
                            break;
                        case "avatar":
                            if (comandos.length < 3) {
                                System.out.println("Falta el ID del avatar. Uso: describir avatar <ID>");
                            } else {
                                descAvatar(comandos[2]);
                            }
                            break;
                        default:
                            String nombreCasilla = comando.substring(comando.indexOf(" ") + 1);
                            descCasilla(nombreCasilla);
                            break;
                    }
                }
                break;
            case "lanzar":
                if (comandos.length == 2 && comandos[1].equals("dados")) {
                    lanzarDados();
                } else {
                    System.out.println("Comando incorrecto. Uso: lanzar dados");
                }
                break;
            case "comprar":
                if (comandos.length < 2) {
                    System.out.println("Falta el nombre de la casilla. Uso: comprar <nombre_casilla>");
                } else {
                    String nombreCasilla = comando.substring(comando.indexOf(" ") + 1);
                    comprar(nombreCasilla);
                }
                break;
            case "salir":
                if (comandos.length == 2 && comandos[1].equals("carcel")) {
                    salirCarcel();
                } else {
                    System.out.println("Comando incorrecto. Uso: salir carcel");
                }
                break;
            case "listar":
                if (comandos.length < 2) {
                    System.out.println("Comando incompleto. Uso: listar <enventa|jugadores|avatares>");
                } else {
                    switch (comandos[1]) {
                        case "enventa":
                            listarVenta();
                            break;
                        case "jugadores":
                            listarJugadores();
                            break;
                        case "avatares":
                            listarAvatares();
                            break;
                        default:
                            System.out.println("Comando incorrecto. Uso: listar <enventa|jugadores|avatares>");
                            break;
                    }
                }
                break;
            case "acabar":
                if (comandos.length == 2 && comandos[1].equals("turno")) {
                    acabarTurno();
                } else {
                    System.out.println("Comando incorrecto. Uso: acabar turno");
        }
    }

    /*Método que realiza las acciones asociadas al comando 'describir jugador'.
    * Parámetro: comando introducido
     */
    private void descJugador(String[] partes) {
    }

    /*Método que realiza las acciones asociadas al comando 'describir avatar'.
    * Parámetro: id del avatar a describir.
    */
    private void descAvatar(String ID) {
    }

    /* Método que realiza las acciones asociadas al comando 'describir nombre_casilla'.
    * Parámetros: nombre de la casilla a describir.
    */
    private void descCasilla(String nombre) {
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
    private void lanzarDados() {
    }

    /*Método que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
    * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    private void comprar(String nombre) {
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'. 
    private void salirCarcel() {
    }

    // Método que realiza las acciones asociadas al comando 'listar enventa'.
    private void listarVenta() {
    }

    // Método que realiza las acciones asociadas al comando 'listar jugadores'.
    private void listarJugadores() {
    }

    // Método que realiza las acciones asociadas al comando 'listar avatares'.
    private void listarAvatares() {
    }

    // Método que realiza las acciones asociadas al comando 'acabar turno'.
    private void acabarTurno() {
    }

}
