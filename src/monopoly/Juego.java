package monopoly;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

import monopoly.Tratos;
import monopoly.interfaces.*;
import monopoly.casilla.*;
import monopoly.casilla.accion.*;
import monopoly.casilla.propiedad.*;
import monopoly.casilla.Accion;
import monopoly.casilla.Impuesto;
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
    private String colorGrupo;
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

    // Metodo para inciar una partida: crea los jugadores y avatares.
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

        //Leemos el fichero txt de entrada (si lo hay)
        String rutaFichero = consola.leer("Introduce la ruta del fichero de comandos (.txt): ");

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
                consola.imprimir("Error procesando comando: " + e.getMessage());
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
        } catch (FileNotFoundException e) {
            consola.imprimir("Error al abrir el fichero");
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
                        verTablero();
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
                    } else {
                        consola.imprimir("Comando incorrecto. Uso: deshipotecar <nombre_casilla>");
                    }
                    break;

                case "aceptar":
                    if (comandos.length == 2) {
                        aceptarTrato(comandos[1]);
                    } else {
                        consola.imprimir("Comando incorrecto. Uso: aceptar <idTrato>");
                    }

                case "tratos":
                    if (comandos.length == 1) {
                        listarTratos();
                    }

                case "eliminar":
                    if (comandos.length == 2) {
                        eliminarTrato(comandos[1]);
                    } else {
                        consola.imprimir("Comando incorrecto. Uso: eliminar <idTrato>");
                    }

                default:
                    throw new ExcepcionComandoNoReconocido(comando);
            }
            }catch(ExcepcionComandoNoReconocido e) {
            consola.imprimir(e.getMessage());
        }
    }

    @Override
    public void verTablero(){
        consola.imprimir(tablero.toString());
    }

    /*Metodo que realiza las acciones asociadas al comando 'describir jugador'.
     * Parámetro: comando introducido*/
    @Override
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
                    if (propiedad instanceof Propiedad) {
                        Propiedad prop = (Propiedad) propiedad;
                        if (prop.isHipotecada()) {
                            consola.leer("(H)");
                        }
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
                    consola.leer(edificio.getId() + "(" + edificio.getSolar().getNombre() + ")");
                    if (i < edificiosJugador.size() - 1) {
                        consola.leer(", ");
                    }
                }
                consola.imprimir("],");

                // Mostrar propiedades hipotecadas
                consola.leer("    hipotecas: [");
                boolean primeraHipoteca = true;
                for (Casilla propiedad : propiedades) {
                    if (propiedad instanceof Propiedad) {
                        Propiedad prop = (Propiedad) propiedad;
                        if (prop.isHipotecada()) {
                            if (!primeraHipoteca) consola.leer(", ");
                            consola.leer(prop.getNombre() + ":" + String.format("%,.0f", prop.getValorHipoteca()));
                            primeraHipoteca = false;
                        }
                    }
                }
                consola.imprimir("]");

                consola.imprimir("}");
                return;
            }
        }
        consola.imprimir("Jugador no encontrado: " + nombreJugador);
    }

    /* Método que realiza las acciones asociadas al comando 'describir nombre_casilla'.
     * Parámetros: nombre de la casilla a describir.
     */
    @Override
    public void descCasilla(String nombre) {
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
    @Override
    public void lanzarDados(String valoresForzados) {
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
        // 3. Mover avatar
        actual.getAvatar().moverAvatar(tablero.getPosiciones(), valorDado1 + valorDado2);

        // 4. Obtener casilla actual
        Casilla casillaActual = actual.getAvatar().getLugar();

        // 5. EVALUAR LA CASILLA
        solvente = casillaActual.evaluarCasilla(actual, banca, tablero, jugadores, suma);

        // 6. Manejar dobles y cárcel
        if (valorDado1 == valorDado2) {
            if (lanzamientos == 3) {
                actual.encarcelar(tablero.getPosiciones());
                tirado = true;
            } else {
                // Permite otro lanzamiento
                tirado = false;
            }
        } else {
            tirado = true;
            lanzamientos = 0;
        }
    }


    /*Método que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
     * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    @Override
    public void comprar(String nombre) {
        try {
            Jugador jugadorActual = jugadores.get(turno);
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
            consola.imprimir("⚠ Error inesperado al comprar: " + e.getMessage());
        }
    }


    //Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'.
    @Override
    public void salirCarcel() {
        Jugador jugadorActual = jugadores.get(turno);
        if (jugadorActual.salirDeCarcel()) {
            System.out.println(jugadorActual.getNombre() + " paga 500.000€ y sale de la cárcel. Puede lanzar los datos.");
        } else {
            System.out.println("No se pudo salir de la cárcel. Asegúrate de que estás en la cárcel y tienes suficiente dinero.");
        }
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
            System.out.println("{");
            System.out.println("    nombre: " + jugador.getNombre() + ",");
            System.out.println("    avatar: " + jugador.getAvatar().getId() + ",");
            System.out.println("    fortuna: " + String.format("%,.0f", jugador.getFortuna()) + ",");

            // Propiedades
            System.out.print("    propiedades: [");
            ArrayList<Casilla> propiedades = jugador.getPropiedades();
            for (int i = 0; i < propiedades.size(); i++) {
                System.out.print(propiedades.get(i).getNombre());
                if (i < propiedades.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("],");

            // Edificios
            System.out.print("    edificios: [");
            ArrayList<Edificio> edificiosJugador = jugador.getEdificios();
            for (int i = 0; i < edificiosJugador.size(); i++) {
                Edificio edificio = edificiosJugador.get(i);
                System.out.print(edificio.getId() + "(" + edificio.getSolar().getNombre() + ")");
                if (i < edificiosJugador.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("],");

            // Hipotecas
            System.out.print("    hipotecas: [");
            boolean primeraHipoteca = true;
            for (Casilla propiedad : propiedades) {
                if (!(propiedad instanceof Propiedad)){
                    Propiedad prop = (Propiedad) propiedad;
                    if (prop.isHipotecada()) {
                        if (!primeraHipoteca) {
                            System.out.print(", ");
                        }
                        System.out.print(prop.getNombre());
                        primeraHipoteca = false;
                    }
                }
            }
            if (primeraHipoteca) {
                System.out.print("-");
            }
            System.out.println("]");
        }
    }

    //Método que realiza las acciones asociadas al comando 'listar edificios'
    @Override
    public void listarEdificios(String colorGrupo) {
        if(edificios.isEmpty()) {
            System.out.println("No hay edificios en este momento.");
        }
        // Mostrar todos los edificios si no se especifica un grupo
        if(colorGrupo == null) {
            for(Edificio edificio : edificios) {
                System.out.println(edificio.toString());
            }
        }

        // Filtrar edificios por grupo si se especifica
        ArrayList<Edificio> edificiosFiltrados = new ArrayList<>();
        if (colorGrupo != null) {
            for (Edificio edificio : edificios) {
                if (edificio.getSolar() != null){
                    Grupo grupo = edificio.getSolar().getGrupo();
                    if(grupo!=null && grupo.getColorGrupo().equalsIgnoreCase(colorGrupo)) {
                        edificiosFiltrados.add(edificio);
                    }
                }
            }

            if (edificiosFiltrados.isEmpty()) {
                System.out.println("No hay edificios en el grupo " + colorGrupo + ".");
                return;
            }

            // Mostrar los edificios filtrados
            System.out.println("{");
            for(Edificio edificio : edificios) {
                System.out.println(edificio.toString());
            }
        }
    }

    // Método que realiza las acciones asociadas al comando 'acabar turno'.
    @Override
    public void acabarTurno() {
        Jugador jugadorActual = jugadores.get(turno);

        // Resetear todas las variables de control del turno
        tirado = false;
        lanzamientos = 0;

        // Pasar al siguiente jugador
        turno = (turno + 1) % jugadores.size();

        Jugador siguienteJugador = jugadores.get(turno);
        System.out.println("El jugador actual es " + siguienteJugador.getNombre() + ".");
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

            // Obtener la casilla Salida del tablero (antes se usaba una variable no declarada 'salida')
            Casilla salidaCasilla = tablero.encontrar_casilla("Salida");
            if (salidaCasilla == null) {
                consola.imprimir("Aviso: no se encontró la casilla Salida; se usará null como ubicación inicial.");
            }

            // Crear el nuevo jugador
            Jugador nuevoJugador = new Jugador(nombre, tipoValidado, salidaCasilla, avatares);
            jugadores.add(nuevoJugador);

            // Mostrar la información como en el PDF
            System.out.println("{");
            System.out.println("    nombre: " + nombre + ",");
            System.out.println("    avatar: " + nuevoJugador.getAvatar().getId());
            System.out.println("}");

            // Mostrar el tablero actualizado
            tablero.toString();

        } catch (ExcepcionMonopoly e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (Exception e) {
            // Manejar errores inesperados
            consola.imprimir("⚠ Error inesperado al crear jugador: " + e.getMessage());
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

    private void ejecutarCarta(Jugador jugador, String tipoCarta) {
        Carta carta = Carta.obtenerSiguienteCarta(tipoCarta);
        carta.ejecutarAccion(jugador, tablero, jugadores, banca);
    }


    //Mostrar las estadísticas de un jugador
    @Override
    public void mostrarEstadisticas(String nombreJugador) {
        for (Jugador jugador : jugadores) {
            if (jugador.getNombre().equalsIgnoreCase(nombreJugador)) {
                System.out.println("$> estadisticas " + nombreJugador);
                System.out.println("{");
                System.out.println("  dineroInvertido: " + String.format("%,.0f", jugador.getDineroInvertido()) + ",");
                System.out.println("  pagoTasasEImpuestos: " + String.format("%,.0f", jugador.getPagoTasasEImpuestos()) + ",");
                System.out.println("  pagoDeAlquileres: " + String.format("%,.0f", jugador.getPagoDeAlquileres()) + ",");
                System.out.println("  cobroDeAlquileres: " + String.format("%,.0f", jugador.getCobroDeAlquileres()) + ",");
                System.out.println("  pasarPorCasillaDeSalida: " + String.format("%,.0f", jugador.getPasarPorCasillaDeSalida()) + ",");
                System.out.println("  premiosInversionesOBote: " + String.format("%,.0f", jugador.getPremiosInversionesBote()) + ",");
                System.out.println("  vecesEnLaCarcel: " + jugador.getVecesEnCarcel());
                System.out.println("}");
                return;
            }
        }
        System.out.println("Jugador no encontrado: " + nombreJugador);
    }

    //Mostrar las estadísticas generales
    @Override
    public void mostrarEstadisticasJuego() {
        System.out.println("$> estadisticas");
        System.out.println("{");

        String casillaMasRentable = calcularCasillaMasRentable();
        System.out.println("casillaMasRentable: " + casillaMasRentable + ",");

        String grupoMasRentable = calcularGrupoMasRentable();
        System.out.println("grupoMasRentable: " + grupoMasRentable + ",");

        String casillaMasFrecuentada = calcularCasillaMasFrecuentada();
        System.out.println("casillaMasFrecuentada: " + casillaMasFrecuentada + ",");

        String jugadorMasVueltas = calcularJugadorMasVueltas();
        System.out.println("jugadorMasVueltas: " + jugadorMasVueltas + ",");

        String jugadorEnCabeza = calcularJugadorEnCabeza();
        System.out.println("jugadorEnCabeza: " + jugadorEnCabeza);

        System.out.println("}");
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
                    if (!(casilla instanceof  Propiedad)) {
                        // Calcular rentabilidad: alquiler / valor de la casilla
                        Propiedad propiedad = (Propiedad) casilla;
                        float rentabilidad = 0;
                        if (propiedad.getValor() > 0) {
                            rentabilidad = propiedad.getImpuesto() / propiedad.getValor();
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
                    if(casilla instanceof Propiedad) {  // CAMBIAR: Verificar que SÍ ES Propiedad
                        Propiedad propiedad = (Propiedad) casilla;
                        if (propiedad.getValor() > 0) {
                            float rentabilidad = propiedad.getImpuesto() / propiedad.getValor();
                            rentabilidadTotal += rentabilidad;
                            casillasValiosas++;
                        }
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

            // Verificar que es un solar
            if (!(casillaActual instanceof Solar)) {
                throw new ExcepcionPropiedadNoEdificable(
                        casillaActual.getNombre(),
                        casillaActual.getClass().getSimpleName()
                );
            }

            Solar solar = (Solar) casillaActual;

            // DELEGAR TODA LA LÓGICA AL SOLAR
            Edificio nuevoEdificio = solar.construirEdificio(tipoEdificio, jugadorActual);

            // Registrar el edificio
            jugadorActual.anhadirEdificio(nuevoEdificio);
            edificios.add(nuevoEdificio);

            // Mostrar éxito
            consola.imprimir("✓ " + jugadorActual.getNombre() +
                    " ha construido un " + tipoEdificio +
                    " en " + solar.getNombre());

            // Obtener el coste desde el Solar en lugar de pedirlo al Edificio
            float coste = solar.obtenerCosteEdificio(tipoEdificio);
            consola.imprimir("  Coste: " + String.format("%,.0f", coste) + "€");
            consola.imprimir("  Fortuna actual: " +
                    String.format("%,.0f", jugadorActual.getFortuna()) + "€");

        } catch (ExcepcionMonopoly e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (Exception e) {
            consola.imprimir("⚠ Error inesperado: " + e.getMessage());
        }
    }


    @Override
    public void venderEdificios(String tipoVenta, String nombreCasilla, int cantidadSolicitada) {
        try {
            // 1. Obtener jugador actual
            Jugador jugadorActual = jugadores.get(turno);

            // 2. Encontrar la casilla
            Casilla casilla = tablero.encontrar_casilla(nombreCasilla);
            if (casilla == null) {
                throw new ExcepcionCasillaNoEncontrada(nombreCasilla);
            }

            // 3. Verificar que es un solar
            if (!(casilla instanceof Solar)) {
                throw new ExcepcionPropiedadNoEdificable(
                        nombreCasilla,
                        casilla.getClass().getSimpleName()
                );
            }

            Solar solar = (Solar) casilla;

            // 4. DELEGAR la venta al Solar
            int cantidadVendida = solar.venderEdificios(tipoVenta, cantidadSolicitada, jugadorActual);
            float ingresoTotal = cantidadVendida * solar.obtenerPrecioVentaEdificio(tipoVenta);

            // 5. Actualizar dinero del jugador
            jugadorActual.sumarFortuna(ingresoTotal);

            // 6. Eliminar edificios de las listas globales
            eliminarEdificiosDeListas(solar, tipoVenta, cantidadVendida);

            // 7. Mostrar resultado
            consola.imprimir("✓ " + jugadorActual.getNombre() +
                    " ha vendido " + cantidadVendida + " " + tipoVenta +
                    " en " + solar.getNombre() +
                    " por " + String.format("%,.0f", ingresoTotal) + "€");

        } catch (ExcepcionMonopoly e) {
            consola.imprimir("✗ " + e.getMessage());
        } catch (Exception e) {
            consola.imprimir("⚠ Error inesperado al vender edificios: " + e.getMessage());
        }
    }

    // Método auxiliar para eliminar edificios de las listas
    private void eliminarEdificiosDeListas(Solar solar, String tipoEdificio, int cantidad) {
        String tipo = tipoEdificio.toLowerCase();
        int eliminados = 0;

        // Eliminar de lista global
        for (int i = edificios.size() - 1; i >= 0 && eliminados < cantidad; i--) {
            Edificio e = edificios.get(i);
            if (e.getSolar() == solar && e.getTipoEdificio().equals(tipo)) {
                edificios.remove(i);
                eliminados++;
            }
        }

        // También eliminar del jugador
        // (Esto debería hacerse en el método del jugador)
    }


    /**
 * Método para hipotecar una propiedad
 */
    @Override
    public void hipotecarPropiedad(String nombreCasilla) {
        Jugador jugadorActual = jugadores.get(turno);
        Casilla casilla = tablero.encontrar_casilla(nombreCasilla);

        //Comprobamos que existe la casilla
        if (casilla == null) {
            System.out.println("Casilla no encontrada: " + nombreCasilla);
            return;
        }

        //Comprobamos que es una propiedad
        if (!(casilla instanceof Propiedad)) {
            consola.imprimir("La casilla\" + casilla.getNombre() + \"no es una propiedad.\");");
            return;
        }

        Propiedad propiedad = (Propiedad) casilla;

        // Verificar que el jugador es el dueño
        if (propiedad.getDuenho() != jugadorActual) {
            consola.imprimir("Esta propiedad no pertenece a " + jugadorActual.getNombre() + ".");
            return;
        }

        // Solo si pasa la validación, proceder con la hipoteca
        if (propiedad.esHipotecable()){ //Valor de hipotecada cambia a true

            float valorHipoteca = propiedad.getValorHipoteca();
            jugadorActual.sumarFortuna(valorHipoteca);

            consola.imprimir("%s recibe %,.0f€ por la hipoteca de %s. ",
                    jugadorActual.getNombre(), valorHipoteca, nombreCasilla);

            // Mostrar información adicional sobre restricciones
            if (propiedad.getGrupo() != null) {
                consola.imprimir("No puede recibir alquileres ni edificar en el grupo " +
                        propiedad.getGrupo().getColorGrupo() + ".");
            } else {
                consola.imprimir("No puede recibir alquileres de esta propiedad.");
            }

            consola.imprimir("Fortuna actual de %s: %,.0f€\n",
                    jugadorActual.getNombre(), jugadorActual.getFortuna());
        } else {
            consola.imprimir("No se pudo hipotecar " + nombreCasilla);
        }
    }

    @Override
    public void deshipotecarPropiedad(String nombreCasilla) {
        Jugador jugadorActual = jugadores.get(turno);
        Casilla casilla = tablero.encontrar_casilla(nombreCasilla);

        if (casilla == null) {
            consola.imprimir("Casilla no encontrada: " + nombreCasilla);
            return;
        }

        // Verificar que la casilla es una propiedad
        if (!(casilla instanceof Propiedad)) {
            consola.imprimir("La casilla" + casilla.getNombre() + "no es una propiedad.");
            return;
        }

        Propiedad propiedad = (Propiedad) casilla;

        // Calcular coste de deshipoteca (valor de hipoteca)
        float costeDeshipoteca = propiedad.getValorHipoteca();

        // Verificar que el jugador es el dueño
        if (propiedad.getDuenho() != jugadorActual) {
            consola.imprimir("Esta propiedad no pertenece a " + jugadorActual.getNombre() + ".");
            return;
        }



        // Verificar si tiene suficiente dinero
        if (jugadorActual.getFortuna() < costeDeshipoteca) {
            consola.imprimir("No tienes suficiente dinero para deshipotecar. Necesitas %,.0f€ pero tienes %,.0f€\n",
                    costeDeshipoteca, jugadorActual.getFortuna());
            return;
        }

        // Realizar la deshipoteca
        if(propiedad.puedeDeshipotecar(jugadorActual)) {
            jugadorActual.restarFortuna(costeDeshipoteca);
            jugadorActual.sumarPagoTasasEImpuestos(costeDeshipoteca);

            consola.imprimir("%s ha deshipotecado %s por %,.0f€.\n",
                    jugadorActual.getNombre(), nombreCasilla, costeDeshipoteca);
            consola.imprimir("La propiedad puede volver a recibir alquileres y edificarse.");

            consola.imprimir("Fortuna actual de %s: %,.0f€\n",
                    jugadorActual.getNombre(), jugadorActual.getFortuna());
        } else {
            consola.imprimir("No se pudo deshipotecar " + nombreCasilla);
        }
    }

    //MÉTODOS PARA LOS TRATOS
    @Override
    public void aceptarTrato(String idTrato){
        //Obtener jugador actual
        Jugador jugadorActual = jugadores.get(turno);

        // Buscar el trato por ID en los tratos pendientes del jugador
        Tratos trato = jugadorActual.buscarTratoPorId(idTrato);
        if (trato == null) {
            consola.imprimir("Trato inexistente o no estás involucrado.");
        }

        Jugador jugador2 = trato.getOfertante();

        if (jugadorActual.getNombre().equals(jugador2.getNombre())) {
            consola.imprimir("No puedes aceptar un trato que tú mismo propusiste!");
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
                mensaje.append(trato.getDineroOfrecido()).append("€");
            }

            mensaje.append(" y ").append(jugador2.getNombre()).append(" me da ");

            if (trato.getPropiedadDemandada() != null) {
                mensaje.append(trato.getPropiedadDemandada().getNombre());
            }

            if (trato.getDineroDemandado() > 0) {
                if (trato.getPropiedadDemandada() != null) {
                    mensaje.append(" y ");
                }
                mensaje.append(trato.getDineroDemandado()).append("€");
            }

            mensaje.append(".");
            consola.imprimir(mensaje.toString());
        } else {
            consola.imprimir("No se pudo aceptar el trato.");
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
            }
        }
    }

    @Override
    public void eliminarTrato(String idTrato){
        Jugador jugadorActual = jugadores.get(turno);

        Tratos trato = jugadorActual.buscarTratoPorId(idTrato);
        if (trato != null) {
            // Si no lo encuentra en el jugador actual, buscar en el otro jugador involucrado
            consola.imprimir("El trato entre "+ trato.getReceptor().getNombre()+
                    " y "+  trato.getOfertante().getNombre() + " se elimino \n");
            trato.getOfertante().eliminarTrato(trato);
            trato.getReceptor().eliminarTrato(trato);
        }
    }

    public void proponerTrato(String comando) {
        // Formato: proponer trato <jugador>: cambiar (<oferta>, <solicitud>)
        // Ejemplo: proponer trato Juan: cambiar (Solar1, Solar2)
        // Ejemplo: proponer trato Maria: cambiar (Solar1, 200000)
        // Ejemplo: proponer trato Pedro: cambiar (Solar1 y 100000, Solar2)

        try {
            // Obtener el jugador actual (ofertante)
            Jugador ofertante = jugadores.get(turno);

            // Parsear el comando
            String[] partes = comando.split(":", 2);
            if (partes.length != 2) {
                consola.imprimir("Formato incorrecto. Uso: proponer trato <jugador>: cambiar (<oferta>, <solicitud>)");
                return;
            }

            // Obtener el nombre del receptor y validar
            String nombreReceptor = partes[0].trim();
            Jugador receptor = null;
            for (Jugador j : jugadores) {
                if (j.getNombre().equalsIgnoreCase(nombreReceptor)) {
                    receptor = j;
                    break;
                }
            }

            if (receptor == null) {
                consola.imprimir("Jugador no encontrado: " + nombreReceptor);
                return;
            }

            if (receptor.equals(ofertante)) {
                consola.imprimir("No puedes hacerte un trato a ti mismo.");
                return;
            }

            // Parsear la oferta y la solicitud
            String ofertaSolicitud = partes[1].trim();
            if (!ofertaSolicitud.startsWith("cambiar (") || !ofertaSolicitud.endsWith(")")) {
                consola.imprimir("Formato incorrecto. Debe ser: cambiar (<oferta>, <solicitud>)");
                return;
            }

            String contenido = ofertaSolicitud.substring(9, ofertaSolicitud.length() - 1).trim();
            String[] ofertaYSolicitud = contenido.split(",", 2);

            if (ofertaYSolicitud.length != 2) {
                consola.imprimir("Formato incorrecto. Debe ser: cambiar (<oferta>, <solicitud>)");
                return;
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
                            dineroOfrecido = Float.parseFloat(parte);
                        } else {
                            // Es una propiedad
                            propiedadOfrecida = (Propiedad) tablero.encontrar_casilla(parte);
                            if (propiedadOfrecida == null) {
                                consola.imprimir("Propiedad no encontrada: " + parte);
                                return;
                            }
                            if (!propiedadOfrecida.perteneceAJugador(ofertante)) {
                                consola.imprimir("No eres dueño de la propiedad: " + propiedadOfrecida.getNombre());
                                return;
                            }
                        }
                    }
                } else {
                    // Solo propiedad
                    propiedadOfrecida = (Propiedad) tablero.encontrar_casilla(ofertaStr);
                    if (propiedadOfrecida == null) {
                        consola.imprimir("Propiedad no encontrada: " + ofertaStr);
                        return;
                    }
                    if (!propiedadOfrecida.perteneceAJugador(ofertante)) {
                        consola.imprimir("No eres dueño de la propiedad: " + propiedadOfrecida.getNombre());
                        return;
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
                            dineroDemandado = Float.parseFloat(parte);
                        } else {
                            // Es una propiedad
                            propiedadDemandada = (Propiedad) tablero.encontrar_casilla(parte);
                            if (propiedadDemandada == null) {
                                consola.imprimir("Propiedad no encontrada: " + parte);
                                return;
                            }
                            if (!propiedadDemandada.perteneceAJugador(receptor)) {
                                consola.imprimir("El jugador " + receptor.getNombre() + " no es dueño de la propiedad: " + propiedadDemandada.getNombre());
                                return;
                            }
                        }
                    }
                } else {
                    // Solo propiedad
                    propiedadDemandada = (Propiedad) tablero.encontrar_casilla(solicitudStr);
                    if (propiedadDemandada == null) {
                        consola.imprimir("Propiedad no encontrada: " + solicitudStr);
                        return;
                    }
                    if (!propiedadDemandada.perteneceAJugador(receptor)) {
                        consola.imprimir("El jugador " + receptor.getNombre() + " no es dueño de la propiedad: " + propiedadDemandada.getNombre());
                        return;
                    }
                }
            }

            // Verificar que al menos hay algo en oferta o en solicitud
            if (propiedadOfrecida == null && dineroOfrecido <= 0 &&
                    propiedadDemandada == null && dineroDemandado <= 0) {
                consola.imprimir("El trato debe incluir al menos una propiedad o cantidad de dinero.");
                return;
            }

            // Crear el trato
            Tratos nuevoTrato = new Tratos(ofertante, receptor, propiedadOfrecida, propiedadDemandada, dineroOfrecido, dineroDemandado);

            // Verificar que el trato es válido
            if (!nuevoTrato.esTratoValido()) {
                consola.imprimir("El trato no es válido.");
                return;
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

            mensaje.append("\nEl jugador ").append(receptor.getNombre())
                    .append(" puede aceptar el trato con el comando: aceptarTrato ").append(nuevoTrato.getId());

            consola.imprimir(mensaje.toString());

        } catch (Exception e) {
            consola.imprimir("Error al procesar el trato: " + e.getMessage());
            e.printStackTrace();
        }
        }
    }










