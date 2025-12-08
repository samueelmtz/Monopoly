package monopoly;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

import partida.Jugador;
import monopoly.interfaces.*;
import monopoly.casilla.*;
import monopoly.casilla.accion.*;
import monopoly.casilla.propiedad.*;
import monopoly.edificio.*;
import monopoly.carta.*;
import partida.*;
import excepciones.*;
import excepciones.ExcepcionComandoNoReconocido;
import excepciones.ExcepcionAvatarNoValido;
import excepciones.ExcepcionJugadorNombreDuplicado;
import excepciones.ExcepcionMaxJugadores;
import excepciones.ExcepcionPropiedadNoComprable;
import excepciones.ExcepcionCasillaNoEncontrada;


public class Juego implements Comandos{

    //Atributos
    private ArrayList<Jugador> jugadores; //Jugadores de la partida.
    private ArrayList<Avatar> avatares; //Avatares en la partida.
    private ArrayList<Edificio> edificios; //Edificios en la partida
    private int turno; //Índice correspondiente a la posición en el arrayList del jugador (y el avatar) que tienen el turno
    private int lanzamientos; //Variable para contar el número de lanzamientos de un jugador en un turno.
    private Tablero tablero; //Tablero en el que se juega.
    private Dado dado1; //Dos dados para lanzar y avanzar casillas.
    private Dado dado2;
    private Jugador banca; //El jugador banca.
    private boolean tirado; //Booleano para comprobar si el jugador que tiene el turno ha tirado o no.
    private boolean solvente; //Booleano para comprobar si el jugador que tiene el turno es solvente, es decir, si ha pagado sus deudas.
    public final static Consola consola = new ConsolaNormal(); //Atributo estático para imprimir y leer mensajes por consola

    // Constructor
    public Juego() {
        this.jugadores = new ArrayList<>();
        this.avatares = new ArrayList<>();
        this.edificios = new ArrayList<>();
        this.banca = new Jugador();
        this.tablero = new Tablero(banca);
        this.dado1 = new Dado();
        this.dado2 = new Dado();
        this.turno = 0;
        this.lanzamientos = 0;
        this.tirado = false;
        this.solvente = true;
    }

    // Método para iniciar una partida: crea los jugadores y avatares.
    public void iniciarPartida() {
        // Crear lista de jugadores, avatares y edificios
        jugadores = new ArrayList<>();
        avatares = new ArrayList<>();
        edificios = new ArrayList<>();

        // Crear la banca
        banca = new Jugador();

        // Inicializar tablero y cartas
        tablero = new Tablero(banca);

        // Inicializar dados
        dado1 = new Dado();
        dado2 = new Dado();

        // Variables de control de turno
        turno = 0;
        lanzamientos = 0;
        tirado = false;
        solvente = true;

        // Leemos el fichero txt de entrada (si lo hay)
        String respuesta = consola.leer("¿Desea cargar comandos desde un fichero? (s/n): ");
        if (respuesta.equalsIgnoreCase("s")) {
            String rutaFichero = consola.leer("Introduce la ruta del fichero de comandos (.txt): ");
            lecturaFichero(rutaFichero);
        }

        while (true) {
            try {
                // Mostrar prompt con comandos disponibles
                consola.imprimir("\uD83E\uDD11 ===== MENU MONOPOLY ===== \uD83E\uDD11\n");
                consola.imprimir("> crear jugador");
                consola.imprimir("> jugador");
                consola.imprimir("> listar jugadores");
                consola.imprimir("> lanzar dados (x+y para forzar)");
                consola.imprimir("> acabar turno");
                consola.imprimir("> salir cárcel");
                consola.imprimir("> describir casilla");
                consola.imprimir("> describir jugador");
                consola.imprimir("> estadisticas");
                consola.imprimir("> edificar");
                consola.imprimir("> comprar");
                consola.imprimir("> listar enventa");
                consola.imprimir("> edificar");
                consola.imprimir("> vender edificio");
                consola.imprimir("> listar edificios");
                consola.imprimir("> hipotecar propiedad");
                consola.imprimir("> deshipotecar propiedad");
                consola.imprimir("> proponer trato");
                consola.imprimir("> aceptar trato");
                consola.imprimir("> tratos");
                consola.imprimir("> eliminar trato");
                consola.imprimir("> ver tablero");
                consola.imprimir("> salir");
                String comando = consola.leer("Acción a ejecutar: ");

                // Salir del juego
                if (comando.equalsIgnoreCase("salir")) {
                    consola.imprimir("Saliendo del juego...");
                    break;
                }

                // Procesar comando
                if (!comando.isEmpty()) {
                    analizarComando(comando);
                }
            } catch (Exception e) {
                try {
                    throw new ExcepcionComandoNoReconocido("comando válido", e.getMessage());
                } catch (ExcepcionComandoNoReconocido ex) {
                    consola.imprimir("✗ " + ex.getMessage());
                }
            }
        }

        consola.imprimir("El juego ha terminado. Esperamos que hayáis disfrutado la experiencia!!!");
    }

    /*Metodo que lee un fichero de texto con comandos y los ejecuta.
     * Parámetro: cadena de caracteres (ruta del fichero).
     */
    public void lecturaFichero(String fichero) {
        File file = new File(fichero);
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                consola.imprimir(line);
                analizarComando(line);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            try {
                throw new ExcepcionComandosFichero(fichero, "Archivo no encontrado: " + e.getMessage());
            } catch (ExcepcionComandosFichero ex) {
                consola.imprimir("✗ " + ex.getMessage());
            }
        } catch (Exception e) {
            try {
                throw new ExcepcionComandosFichero(fichero, "Error al procesar archivo: " + e.getMessage());
            } catch (ExcepcionComandosFichero ex) {
                consola.imprimir("✗ " + ex.getMessage());
            }
        }
    }

    /*Método que interpreta el comando introducido y toma la accion correspondiente.
     * Parámetro: cadena de caracteres (el comando).
     */
    private void analizarComando(String comando) {
        String[] comandos = comando.split(" ");
        if (comandos.length == 0) return;

        try {
            switch (comandos[0]) {
                case "crear":
                    if (comandos.length >= 4 && comandos[1].equals("jugador")) {
                        crearJugador(comandos[2], comandos[3]);
                    } else {
                        throw new ExcepcionComandoNoReconocido("crear jugador <nombre> <tipo_avatar>", comando);
                    }
                    break;

                case "jugador":
                    if (comandos.length == 1) {
                        turnoJugador();
                    } else {
                        throw new ExcepcionComandoNoReconocido("jugador", comando);
                    }
                    break;

                case "describir":
                    if (comandos.length == 2 || comandos.length == 3) {
                        switch (comandos[1]) {
                            case "jugador":
                                descJugador(comandos);
                                break;
                            default:
                                descCasilla(comandos[1]);
                                break;
                        }
                    } else {
                        throw new ExcepcionComandoNoReconocido("describir <jugador> [nombre] o describir [nombre_casilla]", comando);
                    }
                    break;

                case "lanzar":
                    if (comandos.length == 2 && comandos[1].equals("dados")) {
                        lanzarDados(null);
                    } else if (comandos.length == 3 && comandos[1].equals("dados")) {
                        lanzarDados(comandos[2]);
                    } else {
                        throw new ExcepcionComandoNoReconocido(
                                "lanzar dados [valor1+valor2]",
                                comando
                        );
                    }
                    break;

                case "comprar":
                    if (comandos.length < 2) {
                        throw new ExcepcionComandoNoReconocido("comprar <nombre_casilla>", comando);
                    } else {
                        String nombreCasilla = comando.substring(comando.indexOf(" ") + 1);
                        comprar(nombreCasilla);
                    }
                    break;

                case "salir":
                    if (comandos.length >= 2) {
                        String subcomando = comandos[1].toLowerCase();
                        if (subcomando.equals("carcel") || subcomando.equals("cárcel")) {
                            salirCarcel();
                        } else {
                            throw new ExcepcionComandoNoReconocido("salir cárcel", comando);
                        }
                    } else {
                        throw new ExcepcionComandoNoReconocido("salir cárcel", comando);
                    }
                    break;

                case "listar":
                    if (comandos.length < 2) {
                        throw new ExcepcionComandoNoReconocido("listar <enventa|jugadores|edificios>", comando);
                    } else {
                        switch (comandos[1]) {
                            case "enventa":
                                listarVenta();
                                break;
                            case "jugadores":
                                listarJugadores();
                                break;
                            case "edificios":
                                if (comandos.length == 2) {
                                    listarEdificios(null);
                                } else if (comandos.length == 3) {
                                    listarEdificios(comandos[2]);
                                }
                                break;
                            default:
                                throw new ExcepcionComandoNoReconocido("listar <enventa|jugadores|edificios>", comando);
                        }
                    }
                    break;

                case "ver":
                    if (comandos.length == 2 && comandos[1].equals("tablero")) {
                        verTablero();
                    } else {
                        throw new ExcepcionComandoNoReconocido("ver tablero", comando);
                    }
                    break;

                case "acabar":
                    if (comandos.length == 2 && comandos[1].equals("turno")) {
                        acabarTurno();
                    } else {
                        throw new ExcepcionComandoNoReconocido("acabar turno", comando);
                    }
                    break;

                case "estadisticas":
                    if (comandos.length == 2) {
                        mostrarEstadisticas(comandos[1]);
                    } else if (comandos.length == 1) {
                        mostrarEstadisticasJuego();
                    } else {
                        throw new ExcepcionComandoNoReconocido("estadisticas <nombre_jugador> o estadisticas", comando);
                    }
                    break;

                case "edificar":
                    if (comandos.length == 2) {
                        edificar(comandos[1]);
                    } else {
                        throw new ExcepcionComandoNoReconocido("edificar <tipo_edificio>", comando);
                    }
                    break;

                case "vender":
                    if (comandos.length >= 4) {
                        String tipoVenta = comandos[1].toLowerCase();
                        String nombreCasilla = comandos[2];
                        int cantidad;
                        try {
                            cantidad = Integer.parseInt(comandos[3]);
                        } catch (NumberFormatException e) {
                            throw new ExcepcionComandoNoReconocido("vender <casas|hoteles|piscina|pista_deporte> <nombre_casilla> <cantidad>", comando);
                        }
                        venderEdificios(tipoVenta, nombreCasilla, cantidad);
                    } else {
                        throw new ExcepcionComandoNoReconocido("vender <casas|hoteles|piscina|pista_deporte> <nombre_casilla> <cantidad>", comando);
                    }
                    break;

                case "trato":
                    // Get the rest of the command after "trato"
                    String restoComando = comando.substring(comando.indexOf("trato") + "trato".length()).trim();
                    
                    // Split the command into player name and trade details
                    String[] partes = restoComando.split(":", 2);
                    if (partes.length != 2) {
                        throw new ExcepcionComandoNoReconocido("trato <jugador>: cambiar (<oferta>, <solicitud>)", comando);
                    }
                    
                    // Extract player name and trade details
                    String nombreJugador = partes[0].trim();
                    String ofertaSolicitud = partes[1].trim();
                    
                    // Pass to proponerTrato as a single string
                    proponerTrato(new String[]{nombreJugador, ofertaSolicitud});
                    break;

                case "hipotecar":
                    if (comandos.length == 2) {
                        hipotecarPropiedad(comandos[1]);
                    } else {
                        throw new ExcepcionComandoNoReconocido("hipotecar <nombre_casilla>", comando);
                    }
                    break;

                case "deshipotecar":
                    if (comandos.length == 2) {
                        deshipotecarPropiedad(comandos[1]);
                    } else {
                        throw new ExcepcionComandoNoReconocido("deshipotecar <nombre_casilla>", comando);
                    }
                    break;

                case "aceptar":
                    if (comandos.length == 2) {
                        aceptarTrato(comandos[1]);
                    } else {
                        throw new ExcepcionComandoNoReconocido("aceptar <idTrato>", comando);
                    }
                    break;

                case "tratos":
                    if (comandos.length == 1) {
                        listarTratos();
                    } else {
                        throw new ExcepcionComandoNoReconocido("tratos", comando);
                    }
                    break;

                case "eliminar":
                    if (comandos.length == 2) {
                        eliminarTrato(comandos[1]);
                    } else {
                        throw new ExcepcionComandoNoReconocido("eliminar <idTrato>", comando);
                    }
                    break;

                default:
                    throw new ExcepcionComandoNoReconocido("" , comando);
            }

        } catch (ExcepcionComandoNoReconocido e) {
            consola.imprimir("✗ " + e.getMessage());

        } catch (ExcepcionMonopoly e) {
            consola.imprimir("✗ " + e.getMessage());

        } catch (Exception e) {
            consola.imprimir("Error inesperado procesando comando: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void verTablero(){
        consola.imprimir(tablero.toString());
    }

    /*Método que realiza las acciones asociadas al comando 'describir jugador'.
     * Parámetro: comando introducido*/
    @Override
    public void descJugador(String[] partes) {
        try {
            if (partes.length < 3) {
                throw new ExcepcionComandoNoReconocido("describir jugador <nombre_jugador>", String.join(" ", partes));
            }

            String nombreJugador = partes[2];
            boolean jugadorEncontrado = false;

            for (Jugador jugador : jugadores) {
                if (jugador.getNombre().equalsIgnoreCase(nombreJugador)) {
                    jugadorEncontrado = true;

                    // Encabezado
                    consola.imprimir("{");
                    consola.imprimir("    nombre: " + jugador.getNombre() + ",");
                    consola.imprimir("    avatar: " + (jugador.getAvatar() != null ? jugador.getAvatar().getId() : "-") + ",");
                    consola.imprimir("    fortuna: " + String.format("%,.0f", jugador.getFortuna()) + ",");

                    // Propiedades en una línea
                    String props = "";
                    for (Casilla c : jugador.getPropiedades()) {
                        if (!props.isEmpty()) props += ", ";
                        props += c.getNombre();
                        if (c instanceof Propiedad && ((Propiedad)c).isHipotecada()) {
                            props += "(H)";
                        }
                    }
                    consola.imprimir("    propiedades: [" + props + "],");

                    // Edificios en una línea
                    String edifs = "";
                    for (Edificio e : jugador.getEdificios()) {
                        if (!edifs.isEmpty()) edifs += ", ";
                        edifs += e.getId() + "(" + e.getSolar().getNombre() + ")";
                    }
                    consola.imprimir("    edificios: [" + edifs + "],");

                    // Hipotecas en una línea
                    String hips = "";
                    for (Casilla c : jugador.getPropiedades()) {
                        if (c instanceof Propiedad && ((Propiedad)c).isHipotecada()) {
                            if (!hips.isEmpty()) hips += ", ";
                            hips += c.getNombre() + ":" +
                                    String.format("%,.0f", ((Propiedad)c).getValorHipoteca());
                        }
                    }
                    if (hips.isEmpty()) hips = "-";
                    consola.imprimir("    hipotecas: [" + hips + "]");

                    consola.imprimir("}");
                    break;
                }
            }

            if (!jugadorEncontrado) {
                throw new ExcepcionJugadorNoExistente(nombreJugador);
            }

        } catch (ExcepcionComandoNoReconocido e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (ExcepcionJugadorNoExistente e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (Exception e) {
            consola.imprimir("Error inesperado al describir jugador: " + e.getMessage());
        }
    }

    /* Método que realiza las acciones asociadas al comando 'describir nombre_casilla'.
     * Parámetros: nombre de la casilla a describir.
     */
    @Override
    public void descCasilla(String nombre) {
        try {
            Casilla casilla = tablero.encontrar_casilla(nombre);

            if (casilla == null) {
                throw new ExcepcionCasillaNoEncontrada(nombre);
            }

            // En lugar de mostrar la información manualmente, usar infoCasilla
            consola.imprimir("Información de la casilla " + nombre + ":");
            casilla.infoCasilla();
        } catch (ExcepcionCasillaNoEncontrada e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (Exception e) {
            consola.imprimir("Error inesperado al describir casilla: " + e.getMessage());
        }
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
    @Override
    public void lanzarDados(String valoresForzados) {
        try {
            Jugador actual = jugadores.get(turno);

            //Jugador está en la cárcel
            if (actual.estaEnCarcel()) {
                int valorDado1 = 0, valorDado2 = 0;

                // Procesar valores forzados si los hay
                if (valoresForzados != null && valoresForzados.contains("+")) {
                    try {
                        String[] valoresArray = valoresForzados.split("\\+");
                        if (valoresArray.length == 2) {
                            valorDado1 = Integer.parseInt(valoresArray[0].trim());
                            valorDado2 = Integer.parseInt(valoresArray[1].trim());

                            // Validar valores entre 1 y 6
                            if (valorDado1 < 1 || valorDado1 > 6 || valorDado2 < 1 || valorDado2 > 6) {
                                throw new ExcepcionFormatoDadosIncorrecto("Valores de dados forzados inválidos: " + valorDado1 + " y " + valorDado2 + ". Deben estar entre 1 y 6.");
                            }
                            consola.imprimir("Dados forzados a: " + valorDado1 + " y " + valorDado2);
                        } else {
                            // Formato incorrecto, usar dados normales
                            valorDado1 = dado1.hacerTirada();
                            valorDado2 = dado2.hacerTirada();
                            consola.imprimir("Formato de dados forzados incorrecto. Lanzamiento normal: " + valorDado1 + " y " + valorDado2);
                        }
                    } catch (NumberFormatException e) {
                        // Error en el formato, usar dados normales
                        valorDado1 = dado1.hacerTirada();
                        valorDado2 = dado2.hacerTirada();
                        consola.imprimir("Formato de dados incorrecto. Lanzamiento normal: " + valorDado1 + " y " + valorDado2);
                    }
                } else {
                    // Lanzamiento normal desde cárcel
                    valorDado1 = dado1.hacerTirada();
                    valorDado2 = dado2.hacerTirada();
                    consola.imprimir("Lanzamiento normal: " + valorDado1 + " y " + valorDado2);
                }

                consola.imprimir(actual.getNombre() + " intenta dobles: " + valorDado1 + " y " + valorDado2);
                boolean salio = actual.intentarSalirCarcelConDados(valorDado1, valorDado2);

                if (salio) {
                    consola.imprimir("¡Dobles! " + actual.getNombre() + " sale de la cárcel.");
                    int suma = valorDado1 + valorDado2;
                    actual.getAvatar().moverAvatar(tablero.getPosiciones(), suma);

                    // Mover y evaluar la casilla destino
                    Casilla casillaActual = actual.getAvatar().getLugar();
                    solvente = casillaActual.evaluarCasilla(actual, banca, tablero, jugadores, suma);

                    // Manejar dobles para salir de cárcel
                    if (valorDado1 == valorDado2) {
                        consola.imprimir("¡Dobles (" + valorDado1 + "," + valorDado2 + ")! Puedes lanzar de nuevo.");
                        lanzamientos++;

                        if (lanzamientos == 3) {
                            consola.imprimir("¡Tercer doble consecutivo! Vas a la cárcel.");
                            actual.encarcelar(tablero.getPosiciones());
                            tirado = true;
                            lanzamientos = 0;
                            consola.imprimir("Turno finalizado automáticamente por tercer doble.");
                            acabarTurno(); // Cambiar turno
                        } else {
                            tirado = false; // Permite otro lanzamiento
                        }
                    } else {
                        // No son dobles
                        tirado = true;
                        lanzamientos = 0;
                        consola.imprimir("No son dobles. Puedes realizar otras acciones antes de terminar tu turno.");
                    }
                } else {
                    consola.imprimir("No sacó dobles. " + actual.getNombre() + " sigue en la cárcel.");
                    tirado = true; // Termina el turno
                    lanzamientos = 0;
                }
                return;
            }

            //Jugador NO está en la cárcel

            // Verificar si ya ha tirado (y no tiene dobles pendientes)
            if (tirado && lanzamientos == 0) {
                throw new ExcepcionJugadorNoTurno(actual.getNombre());
            }

            // Verificar límite de 3 lanzamientos con dobles
            if (lanzamientos >= 3) {
                throw new ExcepcionDadosDoblesTresVeces(actual.getNombre());
            }

            int valorDado1 = 0, valorDado2 = 0;

            // Procesamiento de dados forzados o aleatorios
            if (valoresForzados != null && valoresForzados.contains("+")) {
                try {
                    String[] valoresArray = valoresForzados.split("\\+");
                    if (valoresArray.length == 2) {
                        valorDado1 = Integer.parseInt(valoresArray[0].trim());
                        valorDado2 = Integer.parseInt(valoresArray[1].trim());

                        // Validar valores entre 1 y 6
                        if (valorDado1 < 1 || valorDado1 > 6 || valorDado2 < 1 || valorDado2 > 6) {
                            throw new ExcepcionFormatoDadosIncorrecto("Valores de dados forzados inválidos: " + valorDado1 + " y " + valorDado2 + ". Deben estar entre 1 y 6.");
                        }

                        consola.imprimir("Dados forzados a: " + valorDado1 + " y " + valorDado2);
                    } else {
                        throw new ExcepcionFormatoDadosIncorrecto("Formato de dados forzados incorrecto: '" + valoresForzados + "'. Formato correcto: valor1+valor2 (ej: 3+4).");
                    }
                } catch (NumberFormatException e) {
                    throw new ExcepcionFormatoDadosIncorrecto("Formato de dados incorrecto: '" + valoresForzados + "'. Formato correcto: valor1+valor2 (ej: 3+4).");
                }
            } else {
                // Lanzamiento normal
                valorDado1 = dado1.hacerTirada();
                valorDado2 = dado2.hacerTirada();
                consola.imprimir("Lanzamiento normal: " + valorDado1 + " y " + valorDado2);
            }

            int suma = valorDado1 + valorDado2;

            consola.imprimir("Has lanzado los dados: " + valorDado1 + " y " + valorDado2 + ". Total: " + suma);
            consola.imprimir("El avatar " + actual.getAvatar().getId() + " avanza " + suma + " posiciones");

            // Mover avatar
            actual.getAvatar().moverAvatar(tablero.getPosiciones(), suma);

            // Obtener casilla actual
            Casilla casillaActual = actual.getAvatar().getLugar();

            // EVALUAR LA CASILLA
            solvente = casillaActual.evaluarCasilla(actual, banca, tablero, jugadores, suma);

            // Manejar dobles y cárcel
            if (valorDado1 == valorDado2) {
                consola.imprimir("¡Dobles (" + valorDado1 + "," + valorDado2 + ")! Puedes lanzar de nuevo.");
                lanzamientos++;

                if (lanzamientos == 3) {
                    consola.imprimir("¡Tercer doble consecutivo! Vas a la cárcel.");
                    actual.encarcelar(tablero.getPosiciones());
                    tirado = true;
                    lanzamientos = 0;
                    consola.imprimir("Turno finalizado automáticamente por tercer doble.");
                    acabarTurno(); // Cambiar turno automáticamente
                } else {
                    tirado = false; // Permite otro lanzamiento
                }
            } else {
                // No son dobles
                tirado = true;
                lanzamientos = 0;
                consola.imprimir("No son dobles. Puedes realizar otras acciones antes de terminar tu turno.");
            }

        } catch (ExcepcionMonopoly e) {
            // Capturar excepciones personalizadas del Monopoly
            consola.imprimir("✗ " + e.getMessage());

        } catch (Exception e) {
            consola.imprimir("Error inesperado al lanzar dados: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /*Método que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
     * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    @Override
    public void comprar(String nombre) {
        try {
            Jugador jugadorActual = jugadores.get(turno);
            jugadorActual.verificarAccionPermitidaEnCarcel("comprar " + nombre);//No puede comprar si está en la cárcel.
            Casilla casilla = tablero.encontrar_casilla(nombre);

            if (casilla == null) {
                throw new ExcepcionCasillaNoEncontrada(nombre);
            }

            if (!(casilla instanceof Propiedad)) {
                throw new ExcepcionPropiedadNoComprable(nombre);
            }

            Propiedad propiedad = (Propiedad) casilla;
            propiedad.comprarCasilla(jugadorActual, banca);
        } catch (ExcepcionMonopoly e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (Exception e) {
            consola.imprimir("Error inesperado al comprar: " + e.getMessage());
        }
    }


    //Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'.
    @Override
    public void salirCarcel(){
        Jugador jugadorActual = jugadores.get(turno);
        jugadorActual.salirDeCarcel();
    }

    // Método que realiza las acciones asociadas al comando 'listar enventa'.
    @Override
    public void listarVenta() {
        consola.imprimir("Propiedades en venta:");

        boolean hayPropiedadesEnVenta = false;

        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            for (Casilla casilla : lado) {
                // Verificar si la casilla es una Propiedad y pertenece a la banca
                if (casilla instanceof Propiedad && casilla.getDuenho() == banca) {
                    hayPropiedadesEnVenta = true;
                    ((Propiedad) casilla).casEnVenta();
                }
            }
        }

        if (!hayPropiedadesEnVenta) {
            consola.imprimir("No hay propiedades en venta en este momento.");
        }
    }


    // Método que realiza las acciones asociadas al comando 'listar jugadores'.
    @Override
    public void listarJugadores() {
        for (Jugador jugador : jugadores) {
            consola.imprimir("{");
            consola.imprimir("    nombre: " + jugador.getNombre() + ",");
            consola.imprimir("    avatar: " + jugador.getAvatar().getId() + ",");
            consola.imprimir("    fortuna: " + String.format("%,.0f", jugador.getFortuna()) + ",");

            // Propiedades en una línea
            String props = "";
            for (Casilla c : jugador.getPropiedades()) {
                if (!props.isEmpty()) props += ", ";
                props += c.getNombre();
                if (c instanceof Propiedad && ((Propiedad)c).isHipotecada()) {
                    props += "(H)";
                }
            }
            consola.imprimir("    propiedades: [" + props + "],");

            // Edificios en una línea
            String edifs = "";
            for (Edificio e : jugador.getEdificios()) {
                if (!edifs.isEmpty()) edifs += ", ";
                edifs += e.getId() + "(" + e.getSolar().getNombre() + ")";
            }
            consola.imprimir("    edificios: [" + edifs + "],");

            // Hipotecas en una línea
            String hips = "";
            for (Casilla c : jugador.getPropiedades()) {
                if (c instanceof Propiedad && ((Propiedad)c).isHipotecada()) {
                    if (!hips.isEmpty()) hips += ", ";
                    hips += c.getNombre() + ":" +
                            String.format("%,.0f", ((Propiedad)c).getValorHipoteca());
                }
            }
            if (hips.isEmpty()) hips = "-";
            consola.imprimir("    hipotecas: [" + hips + "]");

            consola.imprimir("}");
            break;
        }
    }

    //Método que realiza las acciones asociadas al comando 'listar edificios'
    @Override
    public void listarEdificios(String edif) {
        String filtro = (edif == null) ? "" : edif.trim();
        boolean hayEdificios = false;

        if (tablero == null || tablero.getPosiciones() == null) {
            consola.imprimir("\t(no hay edificios construidos)");
            return;
        }

        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            if (lado == null) continue;
            for (Casilla cas : lado) {
                if (!(cas instanceof Solar)) continue;
                Solar solar = (Solar) cas;

                // Si hay filtro, saltar solares cuyo grupo no coincida
                if (!filtro.isEmpty()) {
                    if (solar.getGrupo() == null || !solar.getGrupo().getColorGrupo().equalsIgnoreCase(filtro)) {
                        continue;
                    }
                }

                // recorrer cada lista de edificios del solar (casas, hoteles, piscinas, pistas)
                for (ArrayList<Edificio> lista : solar.getEdificios()) {
                    if (lista == null) continue;
                    for (Edificio ed : lista) {
                        consola.imprimir(ed.toString());
                        hayEdificios = true;
                    }
                }
            }
        }

        if (!hayEdificios) {
            if (filtro.isEmpty()) {
                consola.imprimir("\t(no hay edificios construidos)");
            } else {
                consola.imprimir("\t(no hay edificios construidos para el grupo " + filtro + ")");
            }
        }
    }

    // Método que realiza las acciones asociadas al comando 'acabar turno'.
    @Override
    public void acabarTurno() {
        Jugador jugadorActual = jugadores.get(turno);

        try {
            // Verificar pago forzoso por 3 turnos en cárcel
            if (jugadorActual.debePagarForzosamenteCarcel()) {
                consola.imprimir("¡%s lleva 3 turnos en cárcel! Pago forzoso de 500.000€.", jugadorActual.getNombre());

                // Intentar salir pagando
                boolean pudoSalir = jugadorActual.salirDeCarcel();

                if (!pudoSalir) {
                    // No pudo pagar la fianza - Verificar si puede hipotecar
                    float deuda = 500000;
                    float falta = deuda - jugadorActual.getFortuna();
                    boolean puedeHipotecar = jugadorActual.puedeHipotecarAlgoParaPagar(falta);

                    if (!puedeHipotecar) {
                        // NO puede pagar NI hipotecar - BANCARROTA INMEDIATA
                        consola.imprimir("✗ %s no tiene propiedades hipotecables para cubrir la fianza.", jugadorActual.getNombre());

                        // Declarar bancarrota por cárcel
                        jugadorActual.declararBancarrotaPorCarcel();

                        // Eliminar jugador del juego
                        eliminarJugadorBancarrota(jugadorActual);
                        return; //
                    } else {
                        // Puede hipotecar pero NO LO HIZO - BANCARROTA FORZADA
                        consola.imprimir("✗ %s tuvo 3 turnos para hipotecar y no lo hizo. BANCARROTA FORZADA.", jugadorActual.getNombre());

                        // Declarar bancarrota por cárcel
                        jugadorActual.declararBancarrotaPorCarcel();

                        // Eliminar jugador del juego
                        eliminarJugadorBancarrota(jugadorActual);
                        return; //
                    }
                }
            }

        } catch (Exception e) {
            consola.imprimir("Error inesperado en acabarTurno: " + e.getMessage());
        } finally {
            // Reiniciar estado del turno
            tirado = false;
            lanzamientos = 0;
            solvente = true;
        }

        // Cambiar turno (solo si no hubo bancarrota)
        turno = (turno + 1) % jugadores.size();
        Jugador siguiente = jugadores.get(turno);

        // Iniciar turno si está en cárcel
        if (siguiente.estaEnCarcel()) {
            siguiente.iniciarTurnoCarcel();
        }

        consola.imprimir("\n===== Turno de %s =====", siguiente.getNombre());
    }

    // Método para eliminar un jugador en bancarrota
    private void eliminarJugadorBancarrota(Jugador jugadorBancarrota) {
        try {
            // Remover el jugador de la lista
            int indiceBancarrota = jugadores.indexOf(jugadorBancarrota);
            jugadores.remove(indiceBancarrota);

            // Remover su avatar
            if (jugadorBancarrota.getAvatar() != null) {
                avatares.remove(jugadorBancarrota.getAvatar());
            }

            consola.imprimir("\nHA SIDO ELIMINADO DEL JUEGO POR BANCARROTA\n", jugadorBancarrota.getNombre());

            // Verificar si queda solo un jugador (fin del juego)
            if (jugadores.size() == 1) {
                consola.imprimir("¡FIN DEL JUEGO!");
                consola.imprimir("¡%s ES EL GANADOR!", jugadores.get(0).getNombre());
                System.exit(0); // Terminar el programa
            }

            // Ajustar el índice del turno si el jugador eliminado estaba antes del actual
            if (indiceBancarrota < turno) {
                turno--; // Ajustar para que no se salte un jugador
            } else if (turno >= jugadores.size()) {
                turno = 0; // Volver al inicio si está al final
            }

            // Mostrar jugadores restantes
            consola.imprimir("Jugadores restantes:");
            for (Jugador j : jugadores) {
                consola.imprimir("• %s (Fortuna: %,.0f€)", j.getNombre(), j.getFortuna());
            }

        } catch (Exception e) {
            consola.imprimir("Error al eliminar jugador en bancarrota: " + e.getMessage());
        }
    }

    @Override
    public void crearJugador(String nombre, String tipoAvatar) {
        try{
        // Validar primero el tipo de avatar
        String tipoValidado = validarTipoAvatar(tipoAvatar);
        if (tipoValidado == null) {
            throw new ExcepcionAvatarNoValido(tipoAvatar);
        }

        // Verificar si el jugador ya existe
        for (Jugador jugador : jugadores) {
            if (jugador.getNombre().equalsIgnoreCase(nombre)) {
                throw new ExcepcionJugadorNombreDuplicado(nombre);
            }
        }
        // Verificar el número máximo de jugadores
            if (jugadores.size() >= 4) {
                 throw new ExcepcionMaxJugadores(4, jugadores.size());
            }

            // Obtener la casilla Salida del tablero
            Casilla salidaCasilla = tablero.encontrar_casilla("Salida");
            if (salidaCasilla == null) {
                throw new ExcepcionCasillaNoEncontrada("Salida");
            }

            // Crear el nuevo jugador
            Jugador nuevoJugador = new Jugador(nombre, tipoValidado, salidaCasilla, avatares);
            jugadores.add(nuevoJugador);

            // Mostrar la información
            consola.imprimir("{");
            consola.imprimir("    nombre: " + nombre + ",");
            consola.imprimir("    avatar: " + nuevoJugador.getAvatar().getId());
            consola.imprimir("}");

            // Mostrar el tablero actualizado
            verTablero();

        } catch (ExcepcionMonopoly e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (Exception e) {
            // Manejar errores inesperados
            consola.imprimir("Error inesperado al crear jugador: " + e.getMessage());
        }
    }

    // Método auxiliar para validar el tipo de avatar
    private String validarTipoAvatar(String tipoAvatar) {
        if (tipoAvatar == null) return null;

        switch (tipoAvatar.toLowerCase()) {
            case "sombrero":
            case "esfinge":
            case "pelota":
            case "coche":
                return tipoAvatar.toLowerCase();
            default:
                return null;
        }
    }

    // Método auxiliar que permite ver de quien es turno
    @Override
    public void turnoJugador() {
        if (jugadores == null || jugadores.isEmpty()) {
            consola.imprimir("No hay jugadores creados.");
            return;
        }

        Jugador actual = jugadores.get(turno);
        String avatarId = "-";
        if (actual.getAvatar() != null && actual.getAvatar().getId() != null) {
            avatarId = actual.getAvatar().getId();
        }

        consola.imprimir("> jugador");
        consola.imprimir("{");
        consola.imprimir("nombre: " + actual.getNombre() + ",");
        consola.imprimir("avatar: " + avatarId);
        consola.imprimir("}");
    }


    //Mostrar las estadísticas de un jugador
    @Override
    public void mostrarEstadisticas(String nombreJugador) {
        for (Jugador jugador : jugadores) {
            if (jugador.getNombre().equalsIgnoreCase(nombreJugador)) {
                consola.imprimir("$> estadisticas " + nombreJugador);
                consola.imprimir("{");
                consola.imprimir("  dineroInvertido: " + String.format("%,.0f", jugador.getDineroInvertido()) + ",");
                consola.imprimir("  pagoTasasEImpuestos: " + String.format("%,.0f", jugador.getPagoTasasEImpuestos()) + ",");
                consola.imprimir("  pagoDeAlquileres: " + String.format("%,.0f", jugador.getPagoDeAlquileres()) + ",");
                consola.imprimir("  cobroDeAlquileres: " + String.format("%,.0f", jugador.getCobroDeAlquileres()) + ",");
                consola.imprimir("  pasarPorCasillaDeSalida: " + String.format("%,.0f", jugador.getPasarPorCasillaDeSalida()) + ",");
                consola.imprimir("  premiosInversionesOBote: " + String.format("%,.0f", jugador.getPremiosInversionesBote()) + ",");
                consola.imprimir("  vecesEnLaCarcel: " + jugador.getVecesEnCarcel());
                consola.imprimir("}");
                return;
            }
        }
        consola.imprimir("Jugador no encontrado: " + nombreJugador);
    }

    //Mostrar las estadísticas generales
    @Override
    public void mostrarEstadisticasJuego() {
        consola.imprimir("$> estadisticas");
        consola.imprimir("{");

        String casillaMasRentable = calcularCasillaMasRentable();
        consola.imprimir("casillaMasRentable: " + casillaMasRentable + ",");

        String grupoMasRentable = calcularGrupoMasRentable();
        consola.imprimir("grupoMasRentable: " + grupoMasRentable + ",");

        String casillaMasFrecuentada = calcularCasillaMasFrecuentada();
        consola.imprimir("casillaMasFrecuentada: " + casillaMasFrecuentada + ",");

        String jugadorMasVueltas = calcularJugadorMasVueltas();
        consola.imprimir("jugadorMasVueltas: " + jugadorMasVueltas + ",");

        String jugadorEnCabeza = calcularJugadorEnCabeza();
        consola.imprimir("jugadorEnCabeza: " + jugadorEnCabeza);

        consola.imprimir("}");
    }



    private String calcularCasillaMasRentable() {
        Casilla masRentable = null;
        float maxDineroGenerado = -1;

        // Recorrer todas las casillas del tablero
        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            for (Casilla casilla : lado) {
                // SOLO considerar casillas compradas (que no son de la banca)
                if (casilla.getDuenho() != null && !casilla.getDuenho().getNombre().equals("Banca") &&
                        casilla.getDuenho() != banca) {

                    // Solo considerar tipos que pueden generar renta (Propiedad)
                    if (casilla instanceof Propiedad) {
                        // Usar dinero generado acumulado
                        Propiedad propiedad = (Propiedad) casilla;
                        float dineroGenerado = propiedad.getDineroGenerado();

                        if (dineroGenerado > maxDineroGenerado) {
                            maxDineroGenerado = dineroGenerado;
                            masRentable = casilla;
                        }
                    }
                }
            }
        }

        // Si no hay casillas compradas, devolver "Ninguna"
        return masRentable != null ? masRentable.getNombre() : "Ninguna";
    }


    private String calcularGrupoMasRentable() {
        HashMap<String, Float> dineroPorGrupo = new HashMap<>();

        // Calcular dinero total generado por cada grupo
        for (Grupo grupo : tablero.getGrupos().values()) {
            float dineroTotalGrupo = 0;

            for (Casilla casilla : grupo.getMiembros()) {
                if (casilla instanceof Propiedad) {
                    Propiedad propiedad = (Propiedad) casilla;
                    dineroTotalGrupo += propiedad.getDineroGenerado();
                }
            }

            // Solo considerar grupos que han generado dinero
            if (dineroTotalGrupo > 0) {
                dineroPorGrupo.put(grupo.getColorGrupo(), dineroTotalGrupo);
            }
        }

        // Encontrar el grupo que más dinero ha generado
        String grupoMasRentable = "Ninguno";
        float maxDinero = -1;

        for (String color : dineroPorGrupo.keySet()) {
            float dinero = dineroPorGrupo.get(color);
            if (dinero > maxDinero) {
                maxDinero = dinero;
                grupoMasRentable = color;
            }
        }

        return grupoMasRentable;
    }


    private String calcularCasillaMasFrecuentada() {
        Casilla masFrecuentada = null;
        int maxVisitas = 0;

        // Recorrer todas las casillas del tablero
        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            for (Casilla casilla : lado) {
                int visitas = casilla.getContadorVisitas();
                if (visitas > maxVisitas) {
                    maxVisitas = visitas;
                    masFrecuentada = casilla;
                }
            }
        }

        return masFrecuentada != null ? masFrecuentada.getNombre() : "Salida";
    }

    private String calcularJugadorMasVueltas() {
        Jugador jugadorMasVueltas = null;
        int maxVueltas = -1;

        // Recorrer todos los jugadores
        for (Jugador jugador : jugadores) {
            if (jugador.getVueltas() > maxVueltas) {
                maxVueltas = jugador.getVueltas();
                jugadorMasVueltas = jugador;
            }
        }

        return jugadorMasVueltas != null ? jugadorMasVueltas.getNombre() : "Ninguno";
    }

    private String calcularJugadorEnCabeza() {
        Jugador jugadorEnCabeza = null;
        float maxFortuna = -1;

        // Recorrer todos los jugadores
        for (Jugador jugador : jugadores) {
            // Calcular fortuna total: dinero + valor de propiedades
            float fortunaTotal = jugador.getFortuna();
            for (Casilla casilla : jugador.getPropiedades()) {
                if(!(casilla instanceof Propiedad)) {
                    Propiedad prop = (Propiedad) casilla;
                    fortunaTotal += prop.getValor();
                }
            }

            if (fortunaTotal > maxFortuna) {
                maxFortuna = fortunaTotal;
                jugadorEnCabeza = jugador;
            }
        }

        return jugadorEnCabeza != null ? jugadorEnCabeza.getNombre() : "Ninguno";
    }

    //Método que verifica si se puede construir el edificio y llama a la función de construcción
    @Override
    public void edificar(String tipoEdificio) {
        try {
            Jugador jugadorActual = jugadores.get(turno);
            Casilla casillaActual = jugadorActual.getAvatar().getLugar();
            jugadorActual.verificarAccionPermitidaEnCarcel("comprar " + casillaActual);

            // Verificar que es un solar
            if (!(casillaActual instanceof Solar)) {
                throw new ExcepcionPropiedadNoEdificable(casillaActual.getNombre(), casillaActual.getClass().getSimpleName());
            }

            Solar solar = (Solar) casillaActual;

            // Logica de edificar en solar
            Edificio nuevoEdificio = solar.construirEdificio(tipoEdificio, jugadorActual);

            // Registrar el edificio
            jugadorActual.anhadirEdificio(nuevoEdificio);
            edificios.add(nuevoEdificio);

            // Mostrar éxito
            consola.imprimir("✓ " + jugadorActual.getNombre() + " ha construido un " + tipoEdificio + " en " + solar.getNombre());

            // Obtener el coste desde el Solar
            float coste = solar.obtenerCosteEdificio(tipoEdificio, "edificar");
            consola.imprimir("  Coste: " + String.format("%,.0f", coste) + "€");
            consola.imprimir("  Fortuna actual: " + String.format("%,.0f", jugadorActual.getFortuna()) + "€");

        } catch (ExcepcionMonopoly e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (Exception e) {
            consola.imprimir("Error inesperado: " + e.getMessage());
        }
    }



    @Override
    public void venderEdificios(String tipoVenta, String nombreCasilla, int cantidadSolicitada) {
        try {
            //Obtener jugador actual
            Jugador jugadorActual = jugadores.get(turno);
            jugadorActual.verificarAccionPermitidaEnCarcel("comprar " + nombreCasilla);

            // Encontrar la casilla
            Casilla casilla = tablero.encontrar_casilla(nombreCasilla);
            if (casilla == null) {
                throw new ExcepcionCasillaNoEncontrada(nombreCasilla);
            }

            // Verificar que es un solar
            if (!(casilla instanceof Solar)) {
                throw new ExcepcionPropiedadNoEdificable(nombreCasilla, casilla.getClass().getSimpleName());
            }

            Solar solar = (Solar) casilla;

            if (tipoVenta.equals("hotel")) {
                if (solar.getNumPiscinas() > 0 || solar.getNumPistas() > 0) {
                    throw new ExcepcionVenderEdificios("vender hotel", solar.getNombre(),
                            "no se puede vender el hotel mientras haya piscinas (" + solar.getNumPiscinas() + ") o pistas de deporte (" + solar.getNumPistas() + ") construidas. Vende primero las piscinas y pistas."
                    );
                }
            }

            // Venta desde el solar
            int cantidadVendida = solar.venderEdificios(tipoVenta, cantidadSolicitada, jugadorActual);
            float ingresoTotal = cantidadVendida * solar.obtenerPrecioVentaEdificio(tipoVenta);

            // Actualizar dinero del jugador
            jugadorActual.sumarFortuna(ingresoTotal);

            // Eliminar edificios de las listas globales
            solar.eliminarEdificiosDeListasGlobales(edificios, jugadorActual, tipoVenta, cantidadVendida);

            // Mostrar resultado
            consola.imprimir("✓ " + jugadorActual.getNombre() + " ha vendido " + cantidadVendida + " " + tipoVenta + " en " + solar.getNombre() + " por " + String.format("%,.0f", ingresoTotal) + "€");
        } catch (ExcepcionVenderEdificios e){
            consola.imprimir("✗ " + e.getMessage());
        } catch (ExcepcionMonopoly e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (Exception e) {
            consola.imprimir("Error inesperado al vender edificios: " + e.getMessage());
        }
    }

    /**
 * Método para hipotecar una propiedad
 */
    @Override
    public void hipotecarPropiedad(String nombreCasilla) {
        try {
            Jugador jugadorActual = jugadores.get(turno);
            jugadorActual.verificarAccionPermitidaEnCarcel("comprar " + nombreCasilla);
            Casilla casilla = tablero.encontrar_casilla(nombreCasilla);

            // Verificar que existe la casilla
            if (casilla == null) {
                throw new ExcepcionCasillaNoEncontrada(nombreCasilla);
            }

            // Verificar que es una propiedad
            if (!(casilla instanceof Propiedad)) {
                throw new ExcepcionAccionNoPermitida("hipotecar", "la casilla '" + casilla.getNombre() + "' no es una propiedad");
            }

            Propiedad propiedad = (Propiedad) casilla;

            // Verificar que el jugador es el dueño
            if (propiedad.getDuenho() != jugadorActual) {
                throw new ExcepcionJugadorNoPropietario(jugadorActual.getNombre(), nombreCasilla, "propiedad");
            }

            // Intentar hipotecar
            if (propiedad.esHipotecable() && propiedad.ejecutarHipoteca()) {
                float valorHipoteca = propiedad.getValorHipoteca();
                jugadorActual.sumarFortuna(valorHipoteca);

                consola.imprimir("%s recibe %,.0f€ por la hipoteca de %s. ", jugadorActual.getNombre(), valorHipoteca, nombreCasilla);

                // Mostrar información adicional sobre restricciones
                if (propiedad.getGrupo() != null) {
                    consola.imprimir("No puede recibir alquileres ni edificar en el grupo " + propiedad.getGrupo().getColorGrupo() + ".");
                } else {
                    consola.imprimir("No puede recibir alquileres de esta propiedad.");
                }

                consola.imprimir("Fortuna actual de %s: %,.0f€\n", jugadorActual.getNombre(), jugadorActual.getFortuna());
            }

        } catch (ExcepcionCasillaNoEncontrada e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (ExcepcionJugadorNoPropietario e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (ExcepcionAccionNoPermitida e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (ExcepcionMonopoly e) {
            consola.imprimir("✗ Error al hipotecar propiedad: " + e.getMessage());
        } catch (Exception e) {
            consola.imprimir("Error inesperado al hipotecar: " + e.getMessage());
        }
    }

    @Override
    public void deshipotecarPropiedad(String nombreCasilla) {
        try {
            Jugador jugadorActual = jugadores.get(turno);
            jugadorActual.verificarAccionPermitidaEnCarcel("comprar " + nombreCasilla);
            Casilla casilla = tablero.encontrar_casilla(nombreCasilla);

            if (casilla == null) {
                throw new ExcepcionCasillaNoEncontrada(nombreCasilla);
            }

            // Verificar que la casilla es una propiedad
            if (!(casilla instanceof Propiedad)) {
                throw new ExcepcionAccionNoPermitida("deshipotecar", "la casilla '" + casilla.getNombre() + "' no es una propiedad");
            }

            Propiedad propiedad = (Propiedad) casilla;

            // Calcular coste de deshipoteca (valor de hipoteca)
            float costeDeshipoteca = propiedad.getValorHipoteca();

            // Verificar que el jugador es el dueño
            if (propiedad.getDuenho() != jugadorActual) {
                throw new ExcepcionJugadorNoPropietario(jugadorActual.getNombre(), nombreCasilla, "propiedad");
            }

            // Verificar si tiene suficiente dinero
            if (jugadorActual.getFortuna() < costeDeshipoteca) {
                throw new ExcepcionFondosInsuficientes(jugadorActual.getNombre(), costeDeshipoteca, jugadorActual.getFortuna(), "deshipotecar " + nombreCasilla);
            }

            // Realizar la deshipoteca
            if (propiedad.puedeDeshipotecar(jugadorActual) && propiedad.ejecutarDeshipoteca()) {
                jugadorActual.restarFortuna(costeDeshipoteca);
                jugadorActual.sumarPagoTasasEImpuestos(costeDeshipoteca);

                consola.imprimir("%s ha deshipotecado %s por %,.0f€.\n", jugadorActual.getNombre(), nombreCasilla, costeDeshipoteca);
                consola.imprimir("La propiedad puede volver a recibir alquileres y edificarse.");

                consola.imprimir("Fortuna actual de %s: %,.0f€\n", jugadorActual.getNombre(), jugadorActual.getFortuna());
            } else {
                throw new ExcepcionAccionNoPermitida("deshipotecar", "no se pudo deshipotecar " + nombreCasilla + " (la propiedad no está hipotecada o no pertenece al jugador)"
                );
            }

        } catch (ExcepcionCasillaNoEncontrada e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (ExcepcionAccionNoPermitida e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (ExcepcionJugadorNoPropietario e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (ExcepcionFondosInsuficientes e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (ExcepcionMonopoly e) {
            consola.imprimir("✗ Error al deshipotecar propiedad: " + e.getMessage());
        } catch (Exception e) {
            consola.imprimir("⚠ Error inesperado al deshipotecar: " + e.getMessage());
        }
    }

    //MÉTODOS PARA LOS TRATOS
    @Override
    public void aceptarTrato(String idTrato) {
        try {
            // Obtener jugador actual
            Jugador jugadorActual = jugadores.get(turno);

            // Buscar el trato por ID en los tratos pendientes del jugador
            Tratos trato = jugadorActual.buscarTratoPorId(idTrato);
            if (trato == null) {
                throw new ExcepcionTratoNoEncontrado(idTrato);
            }

            Jugador jugador2 = trato.getOfertante();

            if (jugadorActual.getNombre().equals(jugador2.getNombre())) {
                throw new ExcepcionTratoInvalido(
                        idTrato,
                        "no puedes aceptar un trato que tú mismo propusiste"
                );
            }

            // Intentar aceptar el trato
            if (trato.aceptar()) {
                // Eliminar el trato tras ser aceptado
                jugadorActual.eliminarTrato(trato);
                trato.getOfertante().eliminarTrato(trato);

                // Construir el mensaje detallado
                StringBuilder mensaje = new StringBuilder();
                mensaje.append("Se ha aceptado el siguiente trato con ")
                        .append(jugador2.getNombre()).append(": ");

                if (trato.getPropiedadOfrecida() != null) {
                    mensaje.append("le doy ").append(trato.getPropiedadOfrecida().getNombre());
                }

                if (trato.getDineroOfrecido() > 0) {
                    if (trato.getPropiedadOfrecida() != null) {
                        mensaje.append(" y ");
                    }
                    mensaje.append(String.format("%,.0f€", trato.getDineroOfrecido()));
                }

                mensaje.append(" y ").append(jugador2.getNombre()).append(" me da ");

                if (trato.getPropiedadDemandada() != null) {
                    mensaje.append(trato.getPropiedadDemandada().getNombre());
                }

                if (trato.getDineroDemandado() > 0) {
                    if (trato.getPropiedadDemandada() != null) {
                        mensaje.append(" y ");
                    }
                    mensaje.append(String.format("%,.0f€", trato.getDineroDemandado()));
                }

                mensaje.append(".");
                consola.imprimir(mensaje.toString());
            } else {
                throw new ExcepcionAccionNoPermitida(
                        "aceptar trato",
                        "aceptar el trato '" + idTrato + "'",
                        "el trato no es válido o no se pueden cumplir las condiciones"
                );
            }

        } catch (ExcepcionTratoNoEncontrado e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (ExcepcionTratoInvalido e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (ExcepcionAccionNoPermitida e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (ExcepcionMonopoly e) {
            consola.imprimir("✗ Error al aceptar trato: " + e.getMessage());
        } catch (Exception e) {
            consola.imprimir("⚠ Error inesperado al aceptar trato: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void listarTratos(){
        //Obtener el jugador
        Jugador jugadorActual = jugadores.get(turno);

        ArrayList<Tratos> tratosPendientes = jugadorActual.getTratosPendientes();

        // Imprimir los tratos pendientes
        if (tratosPendientes.isEmpty()) {
            consola.imprimir("No tienes tratos pendientes.");
        } else {
            consola.imprimir("Tus tratos pendientes:");
            for (Tratos trato : tratosPendientes) {
                StringBuilder sb = new StringBuilder();
                sb.append("{\n");
                sb.append(" id: ").append(trato.getId()).append(",\n");
                sb.append(" jugadorPropone: ").append(trato.getOfertante().getNombre()).append(",\n");
                sb.append(" trato: cambiar ");

                if (trato.getPropiedadOfrecida() != null) {
                    sb.append(trato.getPropiedadOfrecida().getNombre());
                }
                if (trato.getDineroOfrecido() > 0) {
                    if (trato.getPropiedadOfrecida() != null) {
                        sb.append(" y ");
                    }
                    sb.append(String.format("%,.2f€", trato.getDineroDemandado()));
                }

                sb.append(" por ");

                if (trato.getPropiedadDemandada() != null) {
                    sb.append(trato.getPropiedadDemandada().getNombre());
                }
                if(trato.getDineroDemandado() > 0) {
                    if (trato.getPropiedadDemandada() != null) {
                        sb.append(" y ");
                    }
                    sb.append(String.format("%,.2f€", trato.getDineroDemandado()));
                }

                sb.append("\n}");
                consola.imprimir(sb.toString());
            }
        }
    }

    @Override
    public void eliminarTrato(String idTrato){
        Jugador jugadorActual = jugadores.get(turno);

        Tratos trato = jugadorActual.buscarTratoPorId(idTrato);
        if (trato != null) {
            // Si no lo encuentra en el jugador actual, buscar en el otro jugador involucrado
            consola.imprimir("El trato entre "+ trato.getReceptor().getNombre()+ " y "+  trato.getOfertante().getNombre() + " se elimino \n");
            trato.getOfertante().eliminarTrato(trato);
            trato.getReceptor().eliminarTrato(trato);
        }
    }

    @Override
    public void proponerTrato(String[] partes) {
        try {
            // Formato: trato <jugador>: cambiar (<oferta>, <solicitud>)
            // Ejemplo: trato Juan: cambiar (Solar1, Solar2)
            // Ejemplo: trato Maria: cambiar (Solar1, 200000)
            // Ejemplo: trato Pedro: cambiar (Solar1 y 100000, Solar2)

            if (partes == null || partes.length < 2) {
                throw new ExcepcionComandoNoReconocido(
                        "trato <jugador>: cambiar (<oferta>, <solicitud>)", String.join(" ", partes != null ? partes : new String[]{"null"}));
            }

            // Obtener el jugador actual (ofertante)
            Jugador ofertante = jugadores.get(turno);

            // El primer elemento es el nombre del jugador receptor
            String nombreReceptor = partes[0].trim();
            String ofertaSolicitud = partes[1].trim();

            // Validar que el receptor existe
            Jugador receptor = null;
            for (Jugador j : jugadores) {
                if (j.getNombre().equalsIgnoreCase(nombreReceptor)) {
                    receptor = j;
                    break;
                }
            }

            if (receptor == null) {
                throw new ExcepcionJugadorNoExistente(nombreReceptor);
            }

            if (receptor.equals(ofertante)) {
                throw new ExcepcionTratoInvalido("", "no puedes hacerte un trato a ti mismo"
                );
            }

            if (!ofertaSolicitud.startsWith("cambiar (") || !ofertaSolicitud.endsWith(")")) {
                throw new ExcepcionComandoNoReconocido("trato <jugador>: cambiar (<oferta>, <solicitud>)",
                        "trato " + nombreReceptor + ": " + ofertaSolicitud
                );
            }

            String contenido = ofertaSolicitud.substring(9, ofertaSolicitud.length() - 1).trim();
            String[] ofertaYSolicitud = contenido.split(",", 2);

            if (ofertaYSolicitud.length != 2) {
                throw new ExcepcionComandoNoReconocido("trato <jugador>: cambiar (<oferta>, <solicitud>)", "trato " + nombreReceptor + ": " + ofertaSolicitud);
            }

            String ofertaStr = ofertaYSolicitud[0].trim();
            String solicitudStr = ofertaYSolicitud[1].trim();

            // Procesar oferta (lo que el ofertante da)
            Propiedad propiedadOfrecida = null;
            float dineroOfrecido = 0;

            if (!ofertaStr.equalsIgnoreCase("nada")) {
                // Verificar si hay dinero en la oferta
                if (ofertaStr.matches(".*\\d+.*")) {
                    // Extraer el número de la oferta
                    String[] partesOferta = ofertaStr.split(" y ");
                    for (String parte : partesOferta) {
                        parte = parte.trim();
                        if (parte.matches("\\d+")) {
                            try {
                                dineroOfrecido = Float.parseFloat(parte);
                            } catch (NumberFormatException e) {
                                throw new ExcepcionComandoNoReconocido(
                                        "trato <jugador>: cambiar (<propiedad>, <cantidad>) donde cantidad es un número",
                                        "trato " + nombreReceptor + ": cambiar (" + ofertaStr + ", " + solicitudStr + ")"
                                );
                            }
                        } else {
                            // Es una propiedad
                            propiedadOfrecida = (Propiedad) tablero.encontrar_casilla(parte);
                            if (propiedadOfrecida == null) {
                                throw new ExcepcionCasillaNoEncontrada(parte);
                            }
                            if (!propiedadOfrecida.perteneceAJugador(ofertante)) {
                                throw new ExcepcionJugadorNoPropietario(
                                        ofertante.getNombre(),
                                        parte,
                                        "propiedad"
                                );
                            }
                        }
                    }
                } else {
                    // Solo propiedad
                    propiedadOfrecida = (Propiedad) tablero.encontrar_casilla(ofertaStr);
                    if (propiedadOfrecida == null) {
                        throw new ExcepcionCasillaNoEncontrada(ofertaStr);
                    }
                    if (!propiedadOfrecida.perteneceAJugador(ofertante)) {
                        throw new ExcepcionJugadorNoPropietario(ofertante.getNombre(), ofertaStr, "propiedad");
                    }
                }
            }

            // Procesar solicitud (lo que el ofertante pide)
            Propiedad propiedadDemandada = null;
            float dineroDemandado = 0;

            if (!solicitudStr.equalsIgnoreCase("nada")) {
                // Verificar si hay dinero en la solicitud
                if (solicitudStr.matches(".*\\d+.*")) {
                    // Extraer el número de la solicitud
                    String[] partesSolicitud = solicitudStr.split(" y ");
                    for (String parte : partesSolicitud) {
                        parte = parte.trim();
                        if (parte.matches("\\d+")) {
                            try {
                                dineroDemandado = Float.parseFloat(parte);
                            } catch (NumberFormatException e) {
                                throw new ExcepcionComandoNoReconocido("trato <jugador>: cambiar (<propiedad>, <cantidad>) donde cantidad es un número",
                                        "trato " + nombreReceptor + ": cambiar (" + ofertaStr + ", " + solicitudStr + ")");
                            }
                        } else {
                            // Es una propiedad
                            propiedadDemandada = (Propiedad) tablero.encontrar_casilla(parte);
                            if (propiedadDemandada == null) {
                                throw new ExcepcionCasillaNoEncontrada(parte);
                            }
                            if (!propiedadDemandada.perteneceAJugador(receptor)) {
                                throw new ExcepcionJugadorNoPropietario(receptor.getNombre(), parte, "propiedad");
                            }
                        }
                    }
                } else {
                    // Solo propiedad
                    propiedadDemandada = (Propiedad) tablero.encontrar_casilla(solicitudStr);
                    if (propiedadDemandada == null) {
                        throw new ExcepcionCasillaNoEncontrada(solicitudStr);
                    }
                    if (!propiedadDemandada.perteneceAJugador(receptor)) {
                        throw new ExcepcionJugadorNoPropietario(receptor.getNombre(), solicitudStr, "propiedad");
                    }
                }
            }

            // Verificar que al menos hay algo en oferta o en solicitud
            if (propiedadOfrecida == null && dineroOfrecido <= 0 &&
                    propiedadDemandada == null && dineroDemandado <= 0) {
                throw new ExcepcionTratoInvalido("", "el trato debe incluir al menos una propiedad o cantidad de dinero");
            }

            // Crear el trato
            Tratos nuevoTrato = new Tratos(ofertante, receptor, propiedadOfrecida,
                    propiedadDemandada, dineroOfrecido, dineroDemandado);

            // Verificar que el trato es válido
            if (!nuevoTrato.esTratoValido()) {
                throw new ExcepcionTratoInvalido("", "el trato propuesto no es válido según las reglas del juego");
            }

            // Añadir el trato a ambos jugadores
            ofertante.agregarTrato(nuevoTrato);
            receptor.agregarTrato(nuevoTrato);

            // Mostrar confirmación
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Has propuesto el siguiente trato a ").append(receptor.getNombre()).append(":\n");
            mensaje.append("Ofreces: ");

            if (propiedadOfrecida != null) {
                mensaje.append(propiedadOfrecida.getNombre());
                if (dineroOfrecido > 0) {
                    mensaje.append(" y ").append(String.format("%,.0f", dineroOfrecido)).append("€");
                }
            } else if (dineroOfrecido > 0) {
                mensaje.append(String.format("%,.0f", dineroOfrecido)).append("€");
            } else {
                mensaje.append("nada");
            }

            mensaje.append("\nA cambio de: ");

            if (propiedadDemandada != null) {
                mensaje.append(propiedadDemandada.getNombre());
                if (dineroDemandado > 0) {
                    mensaje.append(" y ").append(String.format("%,.0f", dineroDemandado)).append("€");
                }
            } else if (dineroDemandado > 0) {
                mensaje.append(String.format("%,.0f", dineroDemandado)).append("€");
            } else {
                mensaje.append("nada");
            }

            mensaje.append("\nEl jugador ").append(receptor.getNombre()).append(" puede aceptar el trato con el comando: aceptarTrato ").append(nuevoTrato.getId());

            consola.imprimir(mensaje.toString());

        } catch (ExcepcionComandoNoReconocido e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (ExcepcionJugadorNoExistente e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (ExcepcionJugadorNoPropietario e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (ExcepcionCasillaNoEncontrada e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (ExcepcionTratoInvalido e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (ExcepcionMonopoly e) {
            consola.imprimir("✗ Error al proponer trato: " + e.getMessage());
        } catch (Exception e) {
            consola.imprimir("⚠ Error inesperado al proponer trato: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

