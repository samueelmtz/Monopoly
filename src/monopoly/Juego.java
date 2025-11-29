package monopoly;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import monopoly.interfaces.Comandos;
import monopoly.interfaces.Consola;
import monopoly.interfaces.ConsolaNormal;
import partida.Jugador;
import partida.Avatar;
import partida.Dado;
import monopoly.casilla.Casilla;
import monopoly.casilla.propiedad.Solar;
import monopoly.casilla.propiedad.Transporte;
import monopoly.casilla.propiedad.Servicio;
import monopoly.casilla.accion.CajaComunidad;
import monopoly.casilla.accion.Parking;
import monopoly.casilla.accion.Suerte;
import monopoly.edificio.Edificio;
import monopoly.edificio.Casa;
import monopoly.edificio.Hotel;
import monopoly.edificio.Piscina;
import monopoly.edificio.PistaDeporte;
import monopoly.carta.Carta;

import java.util.Scanner;
import java.util.HashMap;


public class Juego {

    //Atributos
    private ArrayList<Jugador> jugadores; //Jugadores de la partida.
    private ArrayList<Avatar> avatares; //Avatares en la partida.
    private ArrayList<Edificio> edificios; //Edificios en la partida
    private String colorGrupo;
    private int turno = 0; //Índice correspondiente a la posición en el arrayList del jugador (y el avatar) que tienen el turno
    private int lanzamientos; //Variable para contar el número de lanzamientos de un jugador en un turno.
    private Tablero tablero; //Tablero en el que se juega.
    private Dado dado1; //Dos dados para lanzar y avanzar casillas.
    private Dado dado2;
    private Jugador banca; //El jugador banca.
    private boolean tirado; //Booleano para comprobar si el jugador que tiene el turno ha tirado o no.
    private boolean solvente; //Booleano para comprobar si el jugador que tiene el turno es solvente, es decir, si ha pagado sus deudas.
    //Gestion de cartas
    private ArrayList<Carta> cartasSuerte;
    private ArrayList<Carta> cartasComunidad;
    private int contadorSuerte;
    private int contadorComunidad;
    public Consola consola;

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
        this.consola = new ConsolaNormal();
    }

    // Metodo para inciar una partida: crea los jugadores y avatares.
    public void iniciarPartida() {
        Scanner scanner = new Scanner(System.in);
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

        //Leemos el fichero txt de entrada (si lo hay)
        consola.leer("Introduce la ruta del fichero de comandos (.txt): ");
        String rutaFichero = scanner.nextLine().trim();

        lecturaFichero(rutaFichero);

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
                consola.imprimir("> ver tablero");
                consola.imprimir("> salir");
                consola.imprimir("Acción a ejecutar: ");
                String comando = scanner.nextLine().trim();

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
                consola.imprimir("Error procesando comando: " + e.getMessage());
            }
        }

        scanner.close();
        consola.imprimir("Juego terminado");
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
        } catch (FileNotFoundException e) {
            consola.imprimir("Error al abrir el fichero");
        }
    }

    /*Metodo que interpreta el comando introducido y toma la accion correspondiente.
     * Parámetro: cadena de caracteres (el comando).
     */
    private void analizarComando(String comando) {
        String[] comandos = comando.split(" ");
        if (comandos.length == 0) return;

        switch (comandos[0]) {
            case "crear":
                if (comandos.length >= 4 && comandos[1].equals("jugador")) {
                    crearJugador(comandos[2], comandos[3]);
                } else {
                    consola.imprimir("Comando incorrecto. Uso: crear jugador <nombre> <tipo_avatar>");
                }
                break;

            case "jugador":
                if (comandos.length == 1) {
                    turnoJugador();
                } else {
                    consola.imprimir("Comando incorrecto. Uso: jugador");
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
                    consola.imprimir("Comando erróneo. Uso: describir <jugador> [nombre] o describir [nombre_casilla]");

                }
                break;

            case "lanzar":
                if (comandos.length == 2 && comandos[1].equals("dados")) {
                    lanzarDados(null); // Lanzamiento normal
                } else if (comandos.length == 3 && comandos[1].equals("dados")) {
                    lanzarDados(comandos[2]); // Lanzamiento con dados forzados
                } else {
                    consola.imprimir("Comando incorrecto. Uso: lanzar dados [valor1+valor2]");
                }
                break;

            case "comprar":
                if (comandos.length < 2) {
                    consola.imprimir("Falta el nombre de la casilla. Uso: comprar <nombre_casilla>");
                } else {
                    String nombreCasilla = comando.substring(comando.indexOf(" ") + 1);
                    comprar(nombreCasilla);
                }
                break;

            case "salir":
                if (comandos.length >= 2) {
                    String subcomando = comandos[1].toLowerCase();
                    // Aceptar ambas versiones
                    if (subcomando.equals("carcel") || subcomando.equals("cárcel")) {
                        salirCarcel();
                    } else {
                        consola.imprimir("Comando incorrecto. Uso: salir cárcel");
                    }
                } else {
                    consola.imprimir("Comando incorrecto. Uso: salir cárcel");
                }
                break;

            case "listar":
                if (comandos.length < 2) {
                    consola.imprimir("Comando incompleto. Uso: listar <enventa|jugadores|avatares>");
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
                        case "edificios":
                            if (comandos.length == 2) {
                                listarEdificios(null);
                            } else if (comandos.length == 3) {
                                listarEdificios(comandos[2]);
                            }
                            break;
                        default:
                            consola.imprimir("Comando incorrecto. Uso: listar <enventa|jugadores|avatares|edificios>");
                            break;
                    }
                }
                break;

            case "ver":
                if (comandos.length == 2 && comandos[1].equals("tablero")) {
                    // Usar toString()
                    consola.imprimir(tablero.toString());
                } else {
                    consola.imprimir("Comando incorrecto. Uso: ver tablero");
                }
                break;

            case "acabar":
                if (comandos.length == 2 && comandos[1].equals("turno")) {
                    acabarTurno();
                } else {
                    consola.imprimir("Comando incorrecto. Uso: acabar turno");
                }
                break;

            case "estadisticas":
                if (comandos.length == 2) {
                    mostrarEstadisticas(comandos[1]);
                } else if (comandos.length == 1) {
                    mostrarEstadisticasJuego();
                } else {
                    consola.imprimir("Comando incorrecto. Uso: estadisticas <nombre_jugador> o estadisticas");
                }
                break;

            case "edificar":
                if (comandos.length == 2) {
                    edificar(comandos[1]);
                } else {
                    consola.imprimir("Comando incorrecto. Uso: edificar <tipo_edificio>");
                }
                break;

            case "vender":
                // vender <casas|hoteles|piscina|pista_deporte> <nombre_casilla> <cantidad>
                if (comandos.length >= 4) {
                    String tipoVenta = comandos[1].toLowerCase();
                    String nombreCasilla = comandos[2];
                    int cantidad;
                    try {
                        cantidad = Integer.parseInt(comandos[3]);
                    } catch (NumberFormatException e) {
                        consola.imprimir("Cantidad inválida. Uso: vender <tipo> <nombre_casilla> <cantidad>");
                        break;
                    }
                    venderEdificios(tipoVenta, nombreCasilla, cantidad);
                } else {
                    consola.imprimir("Comando incorrecto. Uso: vender <casas|hoteles|piscina|pista_deporte> <nombre_casilla> <cantidad>");
                }
                break;

            case "hipotecar":
                if (comandos.length == 2) {
                    hipotecarPropiedad(comandos[1]);
                } else {
                    consola.imprimir("Comando incorrecto. Uso: hipotecar <nombre_casilla>");
                }
                break;

            case "deshipotecar":
                if (comandos.length == 2) {
                    deshipotecarPropiedad(comandos[1]);
                } else{
                    consola.imprimir("Comando incorrecto. Uso: deshipotecar <nombre_casilla>");
                }
                break;

            default:
                consola.imprimir("Comando no reconocido: " + comando);
                break;
        }
    }

    /*Metodo que realiza las acciones asociadas al comando 'describir jugador'.
     * Parámetro: comando introducido*/
    public void descJugador(String[] partes) {
        // Verificar que hay suficientes partes en el comando
        if (partes.length < 3) {
            consola.imprimir("Error: Comando incompleto. Uso: describir jugador <nombre_jugador>");
            return;
        }

        String nombreJugador = partes[2];
        for (Jugador jugador : jugadores) {
            if (jugador.getNombre().equalsIgnoreCase(nombreJugador)) {
                consola.imprimir("{");
                consola.imprimir("    nombre: " + jugador.getNombre() + ",");
                consola.imprimir("    avatar: " + (jugador.getAvatar() != null ? jugador.getAvatar().getId() : "-") + ",");
                consola.imprimir("    fortuna: " + String.format("%,.0f", jugador.getFortuna()) + ",");

                // Mostrar propiedades
                consola.leer("    propiedades: [");
                ArrayList<Casilla> propiedades = jugador.getPropiedades();
                for (int i = 0; i < propiedades.size(); i++) {
                    Casilla propiedad = propiedades.get(i);
                    consola.leer(propiedad.getNombre());
                    if (propiedad.isHipotecada()) {
                        consola.leer("(H)");
                    }
                    if (i < propiedades.size() - 1) {
                        consola.leer(", ");
                    }
                }
                consola.imprimir("],");

                // Mostrar edificios
                consola.leer("    edificios: [");
                ArrayList<Edificio> edificiosJugador = jugador.getEdificios();
                for (int i = 0; i < edificiosJugador.size(); i++) {
                    Edificio edificio = edificiosJugador.get(i);
                    consola.leer(edificio.getId() + "(" + edificio.getCasilla().getNombre() + ")");
                    if (i < edificiosJugador.size() - 1) {
                        consola.leer(", ");
                    }
                }
                consola.imprimir("],");

                // Mostrar propiedades hipotecadas
                consola.leer("    hipotecas: [");
                boolean primeraHipoteca = true;
                for (Casilla propiedad : propiedades) {
                    if (propiedad.isHipotecada()) {
                        if (!primeraHipoteca) {
                            consola.leer(", ");
                        }
                        consola.leer(propiedad.getNombre() + ":" + String.format("%,.0f", propiedad.getValorHipoteca()) + "€");
                        primeraHipoteca = false;
                    }
                }
                consola.imprimir("]");

                consola.imprimir("}");
                return;
            }
        }
        consola.imprimir("Jugador no encontrado: " + nombreJugador);
    }

    /*Metodo que realiza las acciones asociadas al comando 'describir avatar'.
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
            consola.imprimir("Casilla no encontrada: " + nombre);
            return;
        }

        // En lugar de mostrar la información manualmente, usar infoCasilla
        consola.imprimir("Información de la casilla " + nombre + ":");
        casilla.infoCasilla();
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
    private void lanzarDados(String valoresForzados) {
        Jugador actual = jugadores.get(turno);
        if (actual.isEnCarcel()) {
            consola.imprimir("No puedes lanzar los dados, estás en la cárcel.");
            return;
        }

        // Permitir lanzar si no ha tirado O si tiene dados dobles y menos de 3 lanzamientos
        if (tirado && lanzamientos > 3) {
            consola.imprimir("Ya has lanzado los dados en este turno.");
            return;
        }

        int valorDado1, valorDado2;

        if (valoresForzados != null && valoresForzados.contains("+")) {
            // Procesar dados forzados
            try {
                String[] valoresArray = valoresForzados.split("\\+");
                if (valoresArray.length == 2) {
                    valorDado1 = Integer.parseInt(valoresArray[0].trim());
                    valorDado2 = Integer.parseInt(valoresArray[1].trim());

                    // Validar que los valores estén entre 1 y 6
                    if (valorDado1 >= 1 && valorDado1 <= 6 && valorDado2 >= 1 && valorDado2 <= 6) {
                        consola.imprimir("Dados forzados a: " + valorDado1 + " y " + valorDado2);
                    } else {
                        consola.imprimir("Error: Los valores deben estar entre 1 y 6. Usando valores aleatorios...");
                        valorDado1 = dado1.hacerTirada();
                        valorDado2 = dado2.hacerTirada();
                    }
                } else {
                    consola.imprimir("Error en formato. Usando valores aleatorios...");
                    valorDado1 = dado1.hacerTirada();
                    valorDado2 = dado2.hacerTirada();
                }
            } catch (NumberFormatException e) {
                consola.imprimir("Error en valores de dados. Usando valores aleatorios.");
                valorDado1 = dado1.hacerTirada();
                valorDado2 = dado2.hacerTirada();
            }
        } else {
            // Lanzamiento normal
            valorDado1 = dado1.hacerTirada();
            valorDado2 = dado2.hacerTirada();
        }

        int suma = valorDado1 + valorDado2;

        consola.imprimir("Has lanzado los dados: " + valorDado1 + " y " + valorDado2 + ". Total: " + suma);

        consola.imprimir("El avatar " + actual.getAvatar().getId() + " avanza " + (valorDado1 + valorDado2) + " posiciones");
        actual.getAvatar().moverAvatar(tablero.getPosiciones(), valorDado1 + valorDado2);

        // NO establecer tirado = true aquí cuando hay dados dobles
        if (valorDado1 != valorDado2) {
            tirado = true;
        }

        lanzamientos++;

        // EVALUAR LA CASILLA DESPUÉS DEL MOVIMIENTO
        Casilla casillaActual = actual.getAvatar().getLugar();
        solvente = casillaActual.evaluarCasilla(actual, banca, suma);


        // En el manejo de cartas:
        if (casillaActual.getTipo().equals("Suerte") || casillaActual.getTipo().equals("Comunidad")) {
            ejecutarCarta(actual, casillaActual.getTipo());
        }


        // MANEJO ESPECIAL PARA CASILLAS DE IMPUESTOS
        if (casillaActual.getTipo().equals("Impuesto") && solvente) {
            float impuesto = casillaActual.getImpuesto();

            // El dinero ya se restó en evaluarCasilla, pero fue a la banca
            // Lo quitamos de la banca y lo ponemos en el bote
            actual.restarFortuna(impuesto);
            tablero.añadirAlBote(impuesto);

            consola.imprimir("Se han transferido %,.0f€ del impuesto al bote del Parking\n", impuesto);
        }

        // MANEJO ESPECIAL PARA PARKING DESPUÉS DE EVALUAR
        if (casillaActual.getNombre().equals("Parking")) {
            float boteGanado = tablero.reclamarBote(actual);
            if (boteGanado > 0) {
                consola.imprimir("Fortuna actual de %s: %,.0f€\n", actual.getNombre(), actual.getFortuna());
            }
        }

        // MANEJO ESPECIAL PARA IRCARCEL DESPUÉS DE EVALUAR
        if (casillaActual.getNombre().equals("IrCarcel")) {
            consola.imprimir("¡Has caído en Ir a la Cárcel! Moviendo a la cárcel...");
            actual.encarcelar(tablero.getPosiciones());
        }

        if (valorDado1 == valorDado2) {
            if (lanzamientos == 3) {
                consola.imprimir("Tercer doble consecutivo. El avatar va a la cárcel.");
                actual.encarcelar(tablero.getPosiciones());
                tirado = true; // Terminar turno después de ir a la cárcel
            } else {
                consola.imprimir("Dados dobles. Puedes lanzar de nuevo.");
                // NO establecer tirado = true para permitir otro lanzamiento
            }
        } else {
            lanzamientos = 0;
            // tirado ya se estableció arriba para casos no dobles
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
            consola.imprimir("No se pudo comprar la casilla " + nombre);
        }
    }


    //Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'.
    private void salirCarcel() {
        Jugador jugadorActual = jugadores.get(turno);
        if (jugadorActual.salirDeCarcel()) {
            consola.imprimir(jugadorActual.getNombre() + " paga 500.000€ y sale de la cárcel. Puede lanzar los datos.");
        } else {
            consola.imprimir("No se pudo salir de la cárcel. Asegúrate de que estás en la cárcel y tienes suficiente dinero.");
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar enventa'.
    private void listarVenta() {
        consola.imprimir("Propiedades en venta:");

        boolean hayPropiedadesEnVenta = false;

        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            for (Casilla casilla : lado) {
                // Verificar si la casilla es comprable y pertenece a la banca
                if (casilla.esTipoComprable() && casilla.getDuenho() == banca) {
                    hayPropiedadesEnVenta = true;
                    // Llamar casEnVenta sin println - ya imprime internamente
                    casilla.casEnVenta();
                }
            }
        }

        if (!hayPropiedadesEnVenta) {
            consola.imprimir("No hay propiedades en venta en este momento.");
        }
    }


    // Método que realiza las acciones asociadas al comando 'listar jugadores'.
    private void listarJugadores() {
        for (Jugador jugador : jugadores) {
            consola.imprimir("{");
            consola.imprimir("    nombre: " + jugador.getNombre() + ",");
            consola.imprimir("    avatar: " + jugador.getAvatar().getId() + ",");
            consola.imprimir("    fortuna: " + String.format("%,.0f", jugador.getFortuna()) + ",");

            // Propiedades
            consola.leer("    propiedades: [");
            ArrayList<Casilla> propiedades = jugador.getPropiedades();
            for (int i = 0; i < propiedades.size(); i++) {
                consola.leer(propiedades.get(i).getNombre());
                if (i < propiedades.size() - 1) {
                    consola.leer(", ");
                }
            }
            consola.imprimir("],");

            // Edificios
            consola.leer("    edificios: [");
            ArrayList<Edificio> edificiosJugador = jugador.getEdificios();
            for (int i = 0; i < edificiosJugador.size(); i++) {
                Edificio edificio = edificiosJugador.get(i);
                consola.leer(edificio.getId() + "(" + edificio.getCasilla().getNombre() + ")");
                if (i < edificiosJugador.size() - 1) {
                    consola.leer(", ");
                }
            }
            consola.imprimir("],");

            // Hipotecas
            consola.leer("    hipotecas: [");
            boolean primeraHipoteca = true;
            for (Casilla propiedad : propiedades) {
                if (propiedad.isHipotecada()) {
                    if (!primeraHipoteca) {
                        consola.leer(", ");
                    }
                    consola.leer(propiedad.getNombre());
                    primeraHipoteca = false;
                }
            }
            if (primeraHipoteca) {
                consola.leer("-");
            }
            consola.imprimir("]");

            consola.imprimir("}");
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar avatares'. //NO HACER PRIMERA ENTREGA
    private void listarAvatares() {

    }

    //Método que realiza las acciones asociadas al comando 'listar edificios'
    private void listarEdificios(String colorGrupo) {
        if(edificios.isEmpty()) {
            consola.imprimir("No hay edificios en este momento.");
        }
        // Mostrar todos los edificios si no se especifica un grupo
        if(colorGrupo == null) {
            for (int i = 0; i < edificios.size(); i++) {
                Edificio edificio = edificios.get(i);
                String info = edificio.infoEdificio();
                consola.imprimir(info);
            }
        }

        // Filtrar edificios por grupo si se especifica
        ArrayList<Edificio> edificiosFiltrados = new ArrayList<>();
        if (colorGrupo != null) {
            for (Edificio edificio : edificios) {
                if (edificio.getGrupo() != null &&
                        edificio.getGrupo().getColorGrupo().equalsIgnoreCase(colorGrupo)) {
                    edificiosFiltrados.add(edificio);
                }
            }

            if (edificiosFiltrados.isEmpty()) {
                consola.imprimir("No hay edificios en el grupo " + colorGrupo + ".");
                return;
            }

            // Mostrar los edificios filtrados
            consola.imprimir("{");
            for (int i = 0; i < edificiosFiltrados.size(); i++) {
                Edificio edificio = edificiosFiltrados.get(i);
                String info = edificio.infoEdificio();
                consola.imprimir(info);
            }
        }
    }

    // Método que realiza las acciones asociadas al comando 'acabar turno'.
    private void acabarTurno() {
        Jugador jugadorActual = jugadores.get(turno);

        // Resetear todas las variables de control del turno
        tirado = false;
        lanzamientos = 0;

        // Pasar al siguiente jugador
        turno = (turno + 1) % jugadores.size();

        Jugador siguienteJugador = jugadores.get(turno);
        consola.imprimir("El jugador actual es " + siguienteJugador.getNombre() + ".");
    }

    private void crearJugador(String nombre, String tipoAvatar) {
        // Validar primero el tipo de avatar
        String tipoValidado = validarTipoAvatar(tipoAvatar);
        if (tipoValidado == null) {
            consola.imprimir("Error: '" + tipoAvatar + "' no es un avatar permitido.");
            consola.imprimir("Avatares válidos: sombrero, esfinge, pelota, coche");
            return;
        }

        // Verificar si el jugador ya existe
        for (Jugador jugador : jugadores) {
            if (jugador.getNombre().equalsIgnoreCase(nombre)) {
                consola.imprimir("Error: Ya existe un jugador con el nombre '" + nombre + "'");
                return;
            }
        }

        // Encontrar la casilla de salida
        Casilla salida = tablero.encontrar_casilla("Salida");
        if (salida == null) {
            consola.imprimir("Error: No se pudo encontrar la casilla de Salida"
            return;
        }

        try {
            // Crear el nuevo jugador
            Jugador nuevoJugador = new Jugador(nombre, tipoValidado, salida, avatares);
            jugadores.add(nuevoJugador);

            // Mostrar la información como en el PDF
            consola.imprimir("{");
            consola.imprimir("    nombre: " + nombre + ",");
            consola.imprimir("    avatar: " + nuevoJugador.getAvatar().getId());
            consola.imprimir("}");

            // Mostrar el tablero actualizado
            tablero.toString();

        } catch (Exception e) {
            consola.imprimir("Error al crear el jugador: " + e.getMessage());
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



    public void ejecutarCarta(Jugador jugador, String tipoCarta) {
        Carta carta = Carta.obtenerSiguienteCarta(tipoCarta);
        carta.ejecutarAccion(jugador, tablero, jugadores, banca);
    }


    //Mostrar las estadísticas de un jugador
    private void mostrarEstadisticas(String nombreJugador) {
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
    private void mostrarEstadisticasJuego() {
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
        float maxRentabilidad = -1;

        // Recorrer todas las casillas del tablero
        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            for (Casilla casilla : lado) {
                // SOLO considerar casillas compradas (que no son de la banca)
                if (casilla.getDuenho() != null && !casilla.getDuenho().getNombre().equals("Banca") &&
                        casilla.getDuenho() != banca) {

                    // Solo considerar tipos que pueden generar renta
                    if (casilla.getTipo().equals("Solar") || casilla.getTipo().equals("Transporte") || casilla.getTipo().equals("Servicios")) {
                        // Calcular rentabilidad: alquiler / valor de la casilla
                        float rentabilidad = 0;
                        if (casilla.getValor() > 0) {
                            rentabilidad = casilla.getImpuesto() / casilla.getValor();
                        }

                        if (rentabilidad > maxRentabilidad) {
                            maxRentabilidad = rentabilidad;
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
        HashMap<String, Float> rentabilidadGrupos = new HashMap<>(); //Usar HashMap porque no sabemos cuántos grupos hay comprados

        // Calcular rentabilidad solo de grupos con propiedades compradas
        for (Grupo grupo : tablero.getGrupos().values()) {
            float rentabilidadTotal = 0;
            int casillasValiosas = 0;

            for (Casilla casilla : grupo.getMiembros()) {
                // SOLO considerar casillas compradas
                if (casilla.getDuenho() != null && !casilla.getDuenho().getNombre().equals("Banca") &&
                        casilla.getDuenho() != banca) {

                    if (casilla.getValor() > 0) {
                        float rentabilidad = casilla.getImpuesto() / casilla.getValor();
                        rentabilidadTotal += rentabilidad;
                        casillasValiosas++;
                    }
                }
            }

            if (casillasValiosas > 0) {
                rentabilidadGrupos.put(grupo.getColorGrupo(), rentabilidadTotal / casillasValiosas); //Meter en el HashMap la rentabilidad media del grupo
            }
        }

        // Encontrar el grupo más rentable entre los comprados
        String grupoMasRentable = "Ninguno";
        float maxRentabilidad = -1;
        //Recorrer el HashMap
        for (String color : rentabilidadGrupos.keySet()) { //Para cada color de grupo en el HashMap
            float rentabilidad = rentabilidadGrupos.get(color);
            if (rentabilidad > maxRentabilidad) {
                maxRentabilidad = rentabilidad;
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
            for (Casilla propiedad : jugador.getPropiedades()) {
                fortunaTotal += propiedad.getValor();
            }

            if (fortunaTotal > maxFortuna) {
                maxFortuna = fortunaTotal;
                jugadorEnCabeza = jugador;
            }
        }

        return jugadorEnCabeza != null ? jugadorEnCabeza.getNombre() : "Ninguno";
    }

    //Metodo que verifica si se puede construir el edificio y llama a la función de construcción
    private void edificar(String tipoEdificio) {
        Jugador jugadorActual = jugadores.get(turno);
        Casilla casillaActual = jugadorActual.getAvatar().getLugar();

        // Verificar que la casilla es un solar
        if (!casillaActual.getTipo().equals("Solar")) {
            consola.imprimir("Solo se pueden construir edificios en solares.");
            return;
        }

        // Verificar que el jugador es dueño de la casilla
        if (casillaActual.getDuenho() != jugadorActual) {
            consola.imprimir("No eres el dueño de esta casilla.");
            return;
        }

        // Verificar que el jugador tiene todas las casillas del grupo
        Grupo grupo = casillaActual.getGrupo();
        boolean tieneTodoElGrupo = true;
        for (Casilla casillaGrupo : grupo.getMiembros()) { //Para cada casilla del grupo
            if (casillaGrupo.getDuenho() != jugadorActual) { //Si no es dueño de alguna casilla del grupo
                tieneTodoElGrupo = false;
                break;
            }
        }

        // Verificar si la casilla está hipotecada
        if (casillaActual.isHipotecada()) {
            consola.imprimir("No se puede edificar en " + casillaActual.getNombre() + " porque está hipotecada.");
            return;
        }

        if (!tieneTodoElGrupo) {
            consola.imprimir("No puedes edificar hasta que no seas dueño de todas las casillas del grupo " + grupo.getColorGrupo() + ".");
            return;
        }

        // Obtener contadores de edificios actuales
        int casasEnCasilla = casillaActual.getNumCasas();
        int hotelesEnCasilla = casillaActual.getNumHoteles();
        int piscinasEnCasilla = casillaActual.getNumPiscinas();
        int pistasEnCasilla = casillaActual.getNumPistasDeporte();

        // Verificar límites de construcción
        if (!puedeConstruir(tipoEdificio, casasEnCasilla, hotelesEnCasilla, piscinasEnCasilla, pistasEnCasilla, grupo, casillaActual)) {
            return;
        }

        // Calcular coste del edificio
        float coste = Edificio.calcularCoste(tipoEdificio, casillaActual);


        // Verificar si tiene suficiente dinero
        if (jugadorActual.getFortuna() < coste) {
            consola.imprimir(String.format("La fortuna de %s no es suficiente...\n", jugadorActual.getNombre()));
            return;
        }
        // Construir (el Edificio calculará el mismo coste internamente)
        construirEdificio(jugadorActual, casillaActual, tipoEdificio, coste);
    }

    /**
     * Construye el edificio y actualiza la fortuna del jugador
     */
    private void construirEdificio(Jugador jugador, Casilla casilla, String tipoEdificio, float coste) {
        boolean construido = false;

        switch (tipoEdificio) {
            case "casa":
                construido = casilla.anhadirCasa();
                break;
            case "hotel":
                // PARA HOTEL: Primero eliminar las casas existentes antes de construir
                if (casilla.getNumCasas() == 4) {
                    // ELIMINAR TODAS LAS CASAS DE ESTA CASILLA
                    // Eliminar de la lista global de edificios (recorrer en orden inverso para evitar problemas de indices al eliminar)
                    for (int i = edificios.size() - 1; i >= 0; i--) {
                        Edificio edificio = edificios.get(i);
                        if (edificio.getCasilla() == casilla && edificio.getTipo().equals("casa")) {
                            edificios.remove(i);
                        }
                    }

                    // Eliminar de la lista del jugador (recorrer en orden inverso para evitar problemas de indices al eliminar)
                    ArrayList<Edificio> edificiosJugador = jugador.getEdificios();
                    for (int i = edificiosJugador.size() - 1; i >= 0; i--) {
                        Edificio edificio = edificiosJugador.get(i);
                        if (edificio.getCasilla() == casilla && edificio.getTipo().equals("casa")) {
                            edificiosJugador.remove(i);
                        }
                    }

                    construido = casilla.anhadirHotel();
                }
                break;
            case "piscina":
                construido = casilla.anhadirPiscina();
                break;
            case "pista_deporte":
                construido = casilla.anhadirPistaDeporte();
                break;
            default:
                consola.imprimir("Tipo de edificio no reconocido: " + tipoEdificio);
                return;
        }

        if (construido) {
            // Restar el coste de la fortuna del jugador
            jugador.restarFortuna(coste);
            jugador.sumarDineroInvertido(coste);


            // CREAR EL NUEVO OBJETO EDIFICIO
            Edificio nuevoEdificio = new Edificio(tipoEdificio, casilla);

            // Añadir el edificio al jugador
            jugador.anhadirEdificio(nuevoEdificio);

            // Añadir el edificio a la lista global de edificios
            edificios.add(nuevoEdificio);

            consola.imprimir(String.format("Se ha edificado un %s en %s.\n", tipoEdificio, casilla.getNombre()));
            consola.imprimir(String.format("La fortuna de %s se reduce en %,.0f€.\n", jugador.getNombre(), coste));
            consola.imprimir(String.format("Fortuna actual: %,.0f€\n", jugador.getFortuna()));

            // Mostrar estado actual de edificios en la casilla
            mostrarEstadoEdificios(casilla);
        } else {
            consola.imprimir("Error: No se pudo construir el " + tipoEdificio + " en " + casilla.getNombre());
        }
    }

    /**
     * Muestra el estado actual de los edificios en una casilla
     */

    private void mostrarEstadoEdificios(Casilla casilla) {
        consola.imprimir(String.format("Edificios en %s: %d casas, %d hoteles, %d piscinas, %d pistas de deporte\n", casilla.getNombre(), casilla.getNumCasas(), casilla.getNumHoteles(), casilla.getNumPiscinas(), casilla.getNumPistasDeporte()));
    }

    /**
     * Verifica si se puede construir un tipo específico de edificio
     */
    private boolean puedeConstruir(String tipoEdificio, int casas, int hoteles, int piscinas, int pistas, Grupo grupo, Casilla casillaActual) {
        switch (tipoEdificio) {
            case "casa":
                if (casas >= 4) {
                    consola.imprimir("No se pueden construir más de 4 casas por casilla.");
                    return false;
                }
                if (hoteles > 0) {
                    consola.imprimir("No se pueden construir casas cuando hay un hotel.");
                    return false;
                }
                break;

            case "hotel":
                if (hoteles >= 1) {
                    consola.imprimir("No se pueden construir más de 1 hotel por casilla.");
                    return false;
                }
                if (casas < 4) {
                    consola.imprimir("Se necesitan 4 casas para construir un hotel.");
                    return false;
                }
                break;

            case "piscina":
                if (piscinas >= 1) {
                    consola.imprimir("No se pueden construir más de 1 piscina por casilla.");
                    return false;
                }
                if (hoteles < 1) {
                    consola.imprimir("No se puede edificar una piscina, ya que no se dispone de un hotel.");
                    return false;
                }
                break;

            case "pista_deporte":
                if (pistas >= 1) {
                    consola.imprimir("No se pueden construir más de 1 pista de deporte por casilla.");
                    return false;
                }
                if (hoteles < 1) {
                    consola.imprimir("No se puede edificar una pista de deporte, ya que no se dispone de un hotel.");
                    return false;
                }
                break;

            default:
                consola.imprimir("Tipo de edificio no reconocido: " + tipoEdificio);
                return false;
        }

        //Verificar si hay edificios en el resto de casillas del grupo, si hay no se puede edificar
        boolean hayEdificiosEnGrupo = false;
        for (Casilla casilla : grupo.getMiembros()) {
            if (casilla != casillaActual) {
                int totalEdificios = casilla.getNumCasas() + casilla.getNumHoteles() + casilla.getNumPiscinas() + casilla.getNumPistasDeporte();
                if (totalEdificios > 0) {
                    hayEdificiosEnGrupo = true;
                    break;
                }
            }
        }

        if (hayEdificiosEnGrupo) {
            consola.imprimir("No se puede edificar ningún edificio más en esta casilla ni en el grupo al que la casilla pertenece porque ya hay un edificio en este grupo.");
            return false;
        }

        return true;
    }


    //Metodo para vender un edificio
    private void venderEdificios(String tipoVenta, String nombreCasilla, int cantidadSolicitada) {
        Jugador jugadorActual = jugadores.get(turno);

        // La cantidad a vender debe ser positiva
        if (cantidadSolicitada <= 0) {
            consola.imprimir("La cantidad a vender debe ser positiva.");
            return;
        }

        // Solo se aceptan tipos de edificio válidos
        if (!(tipoVenta.equals("casas") || tipoVenta.equals("hoteles") || tipoVenta.equals("piscina") || tipoVenta.equals("pista_deporte"))) {
            consola.imprimir("Tipo de edificio no reconocido para vender: " + tipoVenta);
            return;
        }

        // Encontrar la casilla por nombre
        Casilla casilla = tablero.encontrar_casilla(nombreCasilla);
        if (casilla == null) {
            consola.imprimir("Casilla no encontrada: " + nombreCasilla);
            return;
        }

        // Debe ser un solar para tener edificios
        if (!casilla.getTipo().equals("Solar")) {
            consola.imprimir("Solo se pueden vender edificios en solares.");
            return;
        }

        // Comprobar propiedad
        if (casilla.getDuenho() != jugadorActual) {
            consola.imprimir("Esta propiedad no pertenece a " + jugadorActual.getNombre() + ".");
            return;
        }

        // Obtener disponibles y precio unitario
        int disponibles;
        float precioUnitario;
        String tipoParaEliminar; // Tipo para eliminar de la lista de edificios

        switch (tipoVenta) {
            case "casas":
                disponibles = casilla.getNumCasas();
                precioUnitario = casilla.getPrecioCasa();
                tipoParaEliminar = "casa";
                break;
            case "hoteles":
                disponibles = casilla.getNumHoteles();
                precioUnitario = casilla.getPrecioHotel();
                tipoParaEliminar = "hotel";
                break;
            case "piscina":
                disponibles = casilla.getNumPiscinas();
                precioUnitario = casilla.getPrecioPiscina();
                tipoParaEliminar = "piscina";
                break;
            case "pista_deporte":
                disponibles = casilla.getNumPistasDeporte();
                precioUnitario = casilla.getPrecioPistaDeporte();
                tipoParaEliminar = "pista_deporte";
                break;
            default:
                return;
        }

        //Si no hay edificios disponibles
        if (disponibles == 0) {
            consola.imprimir("No hay " + tipoParaEliminar + " para vender en " + casilla.getNombre() + ".");
            return;
        }

        // Determinar cuántos se pueden vender
        int aVender = Math.min(cantidadSolicitada, disponibles); //Elige el mínimo entre lo solicitado y lo disponible
        float ingreso = precioUnitario * aVender; // Calcular ingreso total

        // ELIMINAR LOS EDIFICIOS DE LA LISTA DEL JUGADOR
        ArrayList<Edificio> edificiosJugador = jugadorActual.getEdificios();
        int eliminados = 0;

        // Buscar y eliminar los edificios de este tipo en esta casilla (recorrer en orden inverso para evitar problemas de indices al eliminar)
        for (int i = edificiosJugador.size() - 1; i >= 0 && eliminados < aVender; i--) {
            Edificio edificio = edificiosJugador.get(i);
            if (edificio.getCasilla() == casilla && edificio.getTipo().equals(tipoParaEliminar)) {
                edificiosJugador.remove(i);
                eliminados++;
            }
        }

        // ELIMINAR TAMBIÉN DE LA LISTA GLOBAL DE EDIFICIOS (recorrer en orden inverso para evitar problemas de indices al eliminar)
        int eliminadosGlobal = 0;
        for (int i = edificios.size() - 1; i >= 0 && eliminadosGlobal < aVender; i--) {
            Edificio edificio = edificios.get(i);
            if (edificio.getCasilla() == casilla && edificio.getTipo().equals(tipoParaEliminar)) {
                edificios.remove(i);
                eliminadosGlobal++;
            }
        }

        // Actualizar contadores en la casilla
        switch (tipoVenta) {
            case "casas":
                casilla.setNumCasas(disponibles - aVender); //Actualizar (casas disponibles - minimo de (disponibles, solicitadas))
                break;
            case "hoteles":
                casilla.setNumHoteles(disponibles - aVender);
                break;
            case "piscina":
                casilla.setNumPiscinas(disponibles - aVender);
                break;
            case "pista_deporte":
                casilla.setNumPistasDeporte(disponibles - aVender);
                break;
        }

        // Actualizar fortuna del jugador
        jugadorActual.sumarFortuna(ingreso);

        // Mostrar resultado de la venta
        consola.imprimir(String.format("%s ha recibido %,.0f€ por vender %d %s de %s.", jugadorActual.getNombre(), ingreso, aVender, tipoParaEliminar, casilla.getNombre()));

        // Estado restante
        int quedan;
        switch (tipoVenta) {
            case "casas":
                quedan = casilla.getNumCasas();
                break;
            case "hoteles":
                quedan = casilla.getNumHoteles();
                break;
            case "piscina":
                quedan = casilla.getNumPiscinas();
                break;
            default:
                quedan = casilla.getNumPistasDeporte();
                break;
        }
        consola.imprimir(String.format("En la propiedad queda %d %s.", quedan, tipoParaEliminar));
    }

    /**
     * Método para hipotecar una propiedad
     */
    private void hipotecarPropiedad(String nombreCasilla) {
        Jugador jugadorActual = jugadores.get(turno);
        Casilla casilla = tablero.encontrar_casilla(nombreCasilla);

        if (casilla == null) {
            consola.imprimir("Casilla no encontrada: " + nombreCasilla);
            return;
        }

        // Validar con puedeHipotecar()
        if (!casilla.puedeHipotecar(jugadorActual)) {
            // El mensaje de error ya se muestra en puedeHipotecar()
            return;
        }


        // Solo si pasa la validación, proceder con la hipoteca
        if (casilla.hipotecar()) {  //Valor de hipotecada cambia a true
            float valorHipoteca = casilla.getValorHipoteca();
            jugadorActual.sumarFortuna(valorHipoteca);

            consola.imprimir(String.format("%s recibe %,.0f€ por la hipoteca de %s. ",
                    jugadorActual.getNombre(), valorHipoteca, nombreCasilla));

            // Mostrar información adicional sobre restricciones
            if (casilla.getGrupo() != null) {
                consola.imprimir("No puede recibir alquileres ni edificar en el grupo " +
                        casilla.getGrupo().getColorGrupo() + ".");
            } else {
                consola.imprimir("No puede recibir alquileres de esta propiedad.");
            }

            consola.imprimir("Fortuna actual de %s: %,.0f€\n",
                    jugadorActual.getNombre(), jugadorActual.getFortuna());
        } else {
            consola.imprimir("No se pudo hipotecar " + nombreCasilla);
        }
    }

    private void deshipotecarPropiedad(String nombreCasilla) {
        Jugador jugadorActual = jugadores.get(turno);
        Casilla casilla = tablero.encontrar_casilla(nombreCasilla);

        if (casilla == null) {
            consola.imprimir("Casilla no encontrada: " + nombreCasilla);
            return;
        }

        // Verificar que el jugador es el dueño
        if (casilla.getDuenho() != jugadorActual) {
            consola.imprimir("Esta propiedad no pertenece a " + jugadorActual.getNombre() + ".");
            return;
        }

        // Verificar que la propiedad está hipotecada
        if (!casilla.isHipotecada()) {
            consola.imprimir("La propiedad " + nombreCasilla + " no está hipotecada.");
            return;
        }

        // Calcular coste de deshipoteca (valor de hipoteca)
        float costeDeshipoteca = casilla.getValorHipoteca();

        // Verificar si tiene suficiente dinero
        if (jugadorActual.getFortuna() < costeDeshipoteca) {
            consola.imprimir(String.format("No tienes suficiente dinero para deshipotecar. Necesitas %,.0f€ pero tienes %,.0f€",
                    costeDeshipoteca, jugadorActual.getFortuna()));
            return;
        }

        // Realizar la deshipoteca
        if (casilla.deshipotecar()) {
            jugadorActual.restarFortuna(costeDeshipoteca);
            jugadorActual.sumarPagoTasasEImpuestos(costeDeshipoteca);

            consola.imprimir(String.format("%s ha deshipotecado %s por %,.0f€.",
                    jugadorActual.getNombre(), nombreCasilla, costeDeshipoteca));
            consola.imprimir("La propiedad puede volver a recibir alquileres y edificarse.");

            consola.imprimir("Fortuna actual de %s: %,.0f€\n",
                    jugadorActual.getNombre(), jugadorActual.getFortuna());
        } else {
            consola.imprimir("No se pudo deshipotecar " + nombreCasilla);
        }
    }
}