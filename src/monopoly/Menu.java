package monopoly;

import java.util.ArrayList;
import partida.*;
import java.util.Scanner;

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
    public void iniciarPartida() {
        Scanner scanner = new Scanner(System.in);
        // Crear lista de jugadores y avatares
        jugadores = new ArrayList<>();
        avatares = new ArrayList<>();

        // Inicializar tablero
        tablero = new Tablero(banca);

        // Inicializar dados
        dado1 = new Dado();
        dado2 = new Dado();

        // Crear la banca
        banca = new Jugador(); // null porque no tiene avatar


        // Variables de control de turno
        turno = 0;
        lanzamientos = 0;
        tirado = false;
        solvente = true;

        while (true) {
            try {
                // Mostrar prompt con comandos disponibles
                System.out.print("[crear jugador|jugador|listar jugadores|lanzar dados|acabar turno|salir cárcel|describir casilla|describir jugador|comprar|listar|salir|ver tablero|ayuda] > ");
                String comando = scanner.nextLine().trim();

                // Salir del juego
                if (comando.equalsIgnoreCase("salir")) {
                    System.out.println("Saliendo del juego...");
                    break;
                }

                // Procesar comando
                if (!comando.isEmpty()) {
                    analizarComando(comando);
                }
            } catch (Exception e) {
                System.out.println("Error procesando comando: " + e.getMessage());
            }
        }

        scanner.close();
        System.out.println("Juego terminado");;
    }


    /*Metodo que interpreta el comando introducido y toma la accion correspondiente.
     * Parámetro: cadena de caracteres (el comando).
     */
    private void analizarComando(String comando) {
        String[] comandos = comando.split(" ");
        switch (comandos[0]) {
            case "crear":
                if (comandos.length >= 4 && comandos[1].equals("jugador")) {
                    crearJugador(comandos[2], comandos[3]); // nombre, tipoAvatar
                } else {
                    System.out.println("Comando incorrecto. Uso: crear jugador <nombre> <tipo_avatar>");
                }
                break;
            case "describir":
                if (comandos.length < 2) {
                    System.out.println("Comando incompleto. Uso: describir <jugador|avatar|nombre_casilla> [ID|nombre]");
                } else {
                    switch (comandos[1]) {
                        case "jugador":
                            descJugador(comandos);
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
            case "tablero":
                if (comandos.length == 1) {
                    tablero.mostrarTablero();
                } else {
                    System.out.println("Comando incorrecto. Uso: tablero");
                }
                break;
            case "acabar":
                if (comandos.length == 2 && comandos[1].equals("turno")) {
                    acabarTurno();
                } else {
                    System.out.println("Comando incorrecto. Uso: acabar turno");
                }
        }
    }

    /*Método que realiza las acciones asociadas al comando 'describir jugador'.
     * Parámetro: comando introducido*/
    public void descJugador(String[] partes) {
        String nombreJugador = partes[2];
        for (Jugador jugador : jugadores) {
            if (jugador.getNombre().equalsIgnoreCase(nombreJugador)) {
                System.out.println("{");
                System.out.println("    nombre: " + jugador.getNombre() + ",");
                System.out.println("    avatar: " + jugador.getAvatar().getId() + ",");
                System.out.println("    fortuna: " + jugador.getFortuna() + ",");
                System.out.println("    propiedades: " + jugador.getPropiedades() + ",");
                //System.out.println("    hipotecas: " + jugador.getHipotecas() + ","); aun no se construyeron las hipotecas y los edificios asi que no se pueden mostrar
                //System.out.println("    edificios: " + jugador.getEdificios());
                System.out.println("}");
                return;
            }
        }
        System.out.println("Jugador no encontrado: " + nombreJugador);
    }

    /*Método que realiza las acciones asociadas al comando 'describir avatar'.
    * Parámetro: id del avatar a describir.
    NO HACER PRIMERA ENTREGA
     */

    private void descAvatar(String ID) {
    }

    /* Método que realiza las acciones asociadas al comando 'describir nombre_casilla'.
     * Parámetros: nombre de la casilla a describir.
     */
    private void descCasilla(String nombre) {
        Casilla casilla = tablero.encontrar_casilla(nombre);

        if (casilla == null) {
            System.out.println("Casilla no encontrada: " + nombre);
            return;
        }

        // Verificar si es una casilla que no tiene información detallada
        String tipo = casilla.getTipo();
        if (tipo.equals("CajaComunidad") || tipo.equals("Suerte") || tipo.equals("Carcel") || tipo.equals("Comunidad")) {
            System.out.println("La casilla " + nombre + " no tiene información detallada.");
            return;
        }

        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            for (Casilla c : lado) {
                if (c.getNombre().equalsIgnoreCase(nombre)) {
                    System.out.println("{");
                    System.out.println("    nombre: " + casilla.getNombre() + ",");
                    System.out.println("    tipo: " + casilla.getTipo() + ",");
                    System.out.println("    valor: " + casilla.getValor() + ",");
                    System.out.println("    propietario: " + (casilla.getDuenho() != null ? casilla.getDuenho().getNombre() : "Ninguno") + ",");
                    System.out.println("    impuestos: " + (casilla.getTipo().equals("Impuesto") ? casilla.getImpuesto() : "N/A") + ",");
                    System.out.println("    hipoteca: " + (casilla.getTipo().equals("Solar") || casilla.getTipo().equals("Transporte") || casilla.getTipo().equals("Servicio") ? casilla.getHipoteca() : "N/A") + ",");
                    System.out.println("    grupo: " + (casilla.getTipo().equals("Solar") && casilla.getGrupo() != null ? casilla.getGrupo().getColorGrupo() : "N/A") + ",");
                    System.out.println("    avatares: " + casilla.getAvatares());
                    System.out.println("}");
                    return;
                }
            }
        }
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
    private void lanzarDados() {
        Jugador actual = jugadores.get(turno);
        if (actual.isEnCarcel()) {
            System.out.println("No puedes lanzar los dados, estás en la cárcel.");
            return;
        }
        if (tirado) {
            System.out.println("Ya has lanzado los dados en este turno.");
            return;
        }

        int valorDado1 = dado1.hacerTirada();
        int valorDado2 = dado2.hacerTirada();
        int suma = valorDado1 + valorDado2;

        System.out.println("Has lanzado los dados: " + valorDado1 + " y " + valorDado2 + ". Total: " + suma);

        System.out.println("El avatar " + actual.getAvatar().getId() + " avanza " + (valorDado1 + valorDado2) + " posiciones");
        actual.getAvatar().moverAvatar(tablero.getPosiciones(), valorDado1 + valorDado2);
        tirado = true;
        lanzamientos++;

        // EVALUAR LA CASILLA DESPUÉS DEL MOVIMIENTO
        Casilla casillaActual = actual.getAvatar().getLugar();
        solvente = casillaActual.evaluarCasilla(actual, banca, suma);




        if (valorDado1 == valorDado2) {
            if (lanzamientos == 3) {
                System.out.println("Tercer doble consecutivo. El avatar va a la cárcel.");
                actual.encarcelar(tablero.getPosiciones());
            } else {
                System.out.println("Dados dobles. Puedes lanzar de nuevo.");
            }
        } else {
            lanzamientos = 0;
        }
    }


    /*Método que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
     * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    private void comprar(String nombre) {
        Jugador jugadorActual = jugadores.get(turno);
        Casilla casilla = tablero.encontrar_casilla(nombre);

        if (casilla != null) {
            casilla.comprarCasilla(jugadorActual, banca);
        } else {
            System.out.println("No se pudo comprar la casilla " + nombre);
        }
    }


    //Metodo que ejecuta todas las acciones relacionadas con el comando 'salir carcel'.

    private void salirCarcel() {
        Jugador jugadorActual = jugadores.get(turno);
        if(jugadorActual.salirCarcel()) {
            System.out.println(jugadorActual.getNombre() + " paga 500.000€ y sale de la cárcel. Puede lanzar los datos.");
        } else {
            System.out.println("No se pudo salir de la cárcel. Asegúrate de que estás en la cárcel y tienes suficiente dinero.");
        }
    }

    // Metodo que realiza las acciones asociadas al comando 'listar enventa'.
    private void listarVenta() {
        System.out.println("Propiedades en venta:");

        boolean hayPropiedadesEnVenta = false;

        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            for (Casilla casilla : lado) {
                // Verificar si la casilla es comprable y pertenece a la banca
                if (casilla.esTipoComprable() && casilla.getDuenho() == banca) {
                    hayPropiedadesEnVenta = true;
                    System.out.println("{");
                    System.out.println("    tipo: " + casilla.getTipo() + ",");
                    System.out.println("    nombre: " + casilla.getNombre() + ",");

                    if (casilla.getTipo().equals("Solar") && casilla.getGrupo() != null) {
                        System.out.println("    grupo: " + casilla.getGrupo().getColorGrupo() + ",");
                    }

                    System.out.println("    valor: " + String.format("%,.0f", casilla.getValor()) + "€");
                    System.out.println("}");
                }
            }
        }

        if (!hayPropiedadesEnVenta) {
            System.out.println("No hay propiedades en venta en este momento.");
        }
    }


    // Método que realiza las acciones asociadas al comando 'listar jugadores'.
    private void listarJugadores() {
        for (Jugador jugador : jugadores) {
            System.out.println("{");
            System.out.println("    nombre: " + jugador.getNombre() + ",");
            System.out.println("    avatar: " + jugador.getAvatar().getId() + ",");
            System.out.println("    fortuna: " + jugador.getFortuna() + ",");
            System.out.println("    propiedades: " + jugador.getPropiedades() + ",");
            //System.out.println("    hipotecas: " + jugador.getHipotecas() + ",");
            //System.out.println("    edificios: " + jugador.getEdificios());
            System.out.println("}");
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar avatares'. //NO HACER PRIMERA ENTREGA
    private void listarAvatares() {

    }

    // Método que realiza las acciones asociadas al comando 'acabar turno'.
    private void acabarTurno() {

        Jugador jugadorActual = jugadores.get(turno);

        tirado = false;
        lanzamientos = 0;

        // Pasar al siguiente jugador
        turno = (turno + 1) % jugadores.size();

        Jugador siguienteJugador = jugadores.get(turno);
        System.out.println("El jugador actual es " + siguienteJugador.getNombre() + ".");
    }

    private void crearJugador(String nombre, String tipoAvatar) {
        // Verificar si el jugador ya existe
        for (Jugador jugador : jugadores) {
            if (jugador.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println("Error: Ya existe un jugador con el nombre '" + nombre + "'");
                return;
            }
        }

        // Encontrar la casilla de salida
        Casilla salida = tablero.encontrar_casilla("Salida");
        if (salida == null) {
            System.out.println("Error: No se pudo encontrar la casilla de Salida");
            return;
        }

        try {
            // Crear el nuevo jugador
            Jugador nuevoJugador = new Jugador(nombre, tipoAvatar, salida, avatares);
            jugadores.add(nuevoJugador);

            // Mostrar la información como en el PDF
            System.out.println("{");
            System.out.println("    nombre: " + nombre + ",");
            System.out.println("    avatar: " + nuevoJugador.getAvatar().getId());
            System.out.println("}");

            // Mostrar el tablero actualizado
            tablero.mostrarTablero();

        } catch (Exception e) {
            System.out.println("Error al crear el jugador: " + e.getMessage());
        }
    }
}




