package monopoly;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import partida.*;
import java.util.Scanner;
import java.util.HashMap;

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
    //Gestion de cartas
    private ArrayList<Carta> cartasSuerte;
    private ArrayList<Carta> cartasComunidad;
    private int contadorSuerte;
    private int contadorComunidad;


    // Metodo para inciar una partida: crea los jugadores y avatares.
    public void iniciarPartida() {
        Scanner scanner = new Scanner(System.in);
        // Crear lista de jugadores y avatares
        jugadores = new ArrayList<>();
        avatares = new ArrayList<>();

        // Crear la banca
        banca = new Jugador(); //

        // Inicializar tablero
        tablero = new Tablero(banca);

        inicializarCartas();

        // Inicializar dados
        dado1 = new Dado();
        dado2 = new Dado();

        // Variables de control de turno
        turno = 0;
        lanzamientos = 0;
        tirado = false;
        solvente = true;

        System.out.print("Introduce la ruta del fichero de comandos (.txt): ");
        String rutaFichero = scanner.nextLine().trim();

        lecturaFichero(rutaFichero);

        while (true) {
            try {
                // Mostrar prompt con comandos disponibles
                System.out.println("\uD83E\uDD11 ===== MENU MONOPOLY ===== \uD83E\uDD11\n");
                System.out.println("> crear jugador");
                System.out.println("> jugador");
                System.out.println("> listar jugadores");
                System.out.println("> lanzar dados (x+y para forzar)");
                System.out.println("> acabar turno");
                System.out.println("> salir cárcel");
                System.out.println("> describir casilla");
                System.out.println("> describir jugador");
                System.out.println("> estadisticas");
                System.out.println("> comprar");
                System.out.println("> listar enventa");
                System.out.println("> ver tablero");
                System.out.println("> salir");
                System.out.println("Acción a ejecutar: ");
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
        System.out.println("Juego terminado");
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
                System.out.println(line);
                analizarComando(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error al abrir el fichero");
            return;
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
                    System.out.println("Comando incorrecto. Uso: crear jugador <nombre> <tipo_avatar>");
                }
                break;

            case "jugador":
                if (comandos.length == 1) {
                    turnoJugador();
                } else {
                    System.out.println("Comando incorrecto. Uso: turno");
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
                    System.out.println("Comando erróneo. Uso: describir <jugador> [nombre] o describir [nombre_casilla]");

                }
                break;

            case "lanzar":
                if (comandos.length == 2 && comandos[1].equals("dados")) {
                    lanzarDados(null); // Lanzamiento normal
                } else if (comandos.length == 3 && comandos[1].equals("dados")) {
                    lanzarDados(comandos[2]); // Lanzamiento con dados forzados
                } else {
                    System.out.println("Comando incorrecto. Uso: lanzar dados [valor1+valor2]");
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
                if (comandos.length == 2 && comandos[1].equals("cárcel")) {
                    salirCarcel();
                } else {
                    System.out.println("Comando incorrecto. Uso: salir cárcel");
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

            case "ver":
                if (comandos.length == 2 && comandos[1].equals("tablero")) {
                    // Usar toString() directamente
                    System.out.println(tablero.toString());
                } else {
                    System.out.println("Comando incorrecto. Uso: ver tablero");
                }
                break;

            case "acabar":
                if (comandos.length == 2 && comandos[1].equals("turno")) {
                    acabarTurno();
                } else {
                    System.out.println("Comando incorrecto. Uso: acabar turno");
                }
                break;

            case "estadisticas":
                if (comandos.length == 2) {
                    mostrarEstadisticas(comandos[1]);
                } else if (comandos.length == 1) {
                    mostrarEstadisticasJuego();
                } else {
                    System.out.println("Comando incorrecto. Uso: estadisticas <nombre_jugador> o estadisticas");
                }
                break;

            default:
                System.out.println("Comando no reconocido: " + comando);
                break;
        }
    }

    /*Metodo que realiza las acciones asociadas al comando 'describir jugador'.
     * Parámetro: comando introducido*/
    public void descJugador(String[] partes) {
        String nombreJugador = partes[2];
        for (Jugador jugador : jugadores) {
            if (jugador.getNombre().equalsIgnoreCase(nombreJugador)) {
                System.out.println("{");
                System.out.println("    nombre: " + jugador.getNombre() + ",");
                System.out.println("    avatar: " + jugador.getAvatar().getId() + ",");
                System.out.println("    fortuna: " + String.format("%,.0f", jugador.getFortuna()) + ",");

                // Mostrar nombres de propiedades en lugar de objetos
                System.out.print("    propiedades: [");
                ArrayList<Casilla> propiedades = jugador.getPropiedades();
                for (int i = 0; i < propiedades.size(); i++) {
                    System.out.print(propiedades.get(i).getNombre());
                    if (i < propiedades.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println("],");

                //System.out.println("    hipotecas: " + jugador.getHipotecas() + ","); // aun no se construyeron las hipotecas
                //System.out.println("    edificios: " + jugador.getEdificios()); // aun no se construyeron los edificios
                System.out.println("}");
                return;
            }
        }
        System.out.println("Jugador no encontrado: " + nombreJugador);
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
            System.out.println("Casilla no encontrada: " + nombre);
            return;
        }

        // En lugar de mostrar la información manualmente, usar infoCasilla
        System.out.println("Información de la casilla " + nombre + ":");
        casilla.infoCasilla();
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
    private void lanzarDados(String valoresForzados) {
        Jugador actual = jugadores.get(turno);
        if (actual.isEnCarcel()) {
            System.out.println("No puedes lanzar los dados, estás en la cárcel.");
            return;
        }

        // Permitir lanzar si no ha tirado O si tiene dados dobles y menos de 3 lanzamientos
        if (tirado && lanzamientos < 3) {
            System.out.println("Ya has lanzado los dados en este turno.");
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
                        System.out.println("✓ Dados forzados a: " + valorDado1 + " y " + valorDado2);
                    } else {
                        System.out.println("Error: Los valores deben estar entre 1 y 6. Usando valores aleatorios.");
                        valorDado1 = dado1.hacerTirada();
                        valorDado2 = dado2.hacerTirada();
                    }
                } else {
                    System.out.println("Error en formato. Usando valores aleatorios.");
                    valorDado1 = dado1.hacerTirada();
                    valorDado2 = dado2.hacerTirada();
                }
            } catch (NumberFormatException e) {
                System.out.println("Error en valores de dados. Usando valores aleatorios.");
                valorDado1 = dado1.hacerTirada();
                valorDado2 = dado2.hacerTirada();
            }
        } else {
            // Lanzamiento normal
            valorDado1 = dado1.hacerTirada();
            valorDado2 = dado2.hacerTirada();
        }

        int suma = valorDado1 + valorDado2;

        System.out.println("Has lanzado los dados: " + valorDado1 + " y " + valorDado2 + ". Total: " + suma);

        System.out.println("El avatar " + actual.getAvatar().getId() + " avanza " + (valorDado1 + valorDado2) + " posiciones");
        actual.getAvatar().moverAvatar(tablero.getPosiciones(), valorDado1 + valorDado2);

        // NO establecer tirado = true aquí cuando hay dados dobles
        if (valorDado1 != valorDado2) {
            tirado = true;
        }

        lanzamientos++;

        // EVALUAR LA CASILLA DESPUÉS DEL MOVIMIENTO
        Casilla casillaActual = actual.getAvatar().getLugar();
        solvente = casillaActual.evaluarCasilla(actual, banca, suma);

        // MANEJAR CARTAS DE SUERTE Y COMUNIDAD DESPUÉS DE EVALUAR
        if (casillaActual.getTipo().equals("Suerte") || casillaActual.getTipo().equals("Comunidad")) {
            System.out.println("Ejecutando acción de carta...");
            ejecutarCarta(actual, casillaActual.getTipo());
        }


        // MANEJO ESPECIAL PARA CASILLAS DE IMPUESTOS
        if (casillaActual.getTipo().equals("Impuesto") && solvente) {
            float impuesto = casillaActual.getImpuesto();

            // El dinero ya se restó en evaluarCasilla, pero fue a la banca
            // Lo quitamos de la banca y lo ponemos en el bote
            actual.restarFortuna(impuesto);
            tablero.añadirAlBote(impuesto);

            System.out.printf("Se han transferido %,.0f€ del impuesto al bote del Parking\n", impuesto);
        }

        // MANEJO ESPECIAL PARA PARKING DESPUÉS DE EVALUAR
        if (casillaActual.getNombre().equals("Parking")) {
            float boteGanado = tablero.reclamarBote(actual);
            if (boteGanado > 0) {
                System.out.printf("¡%s ha ganado el bote del Parking: %,.0f€!\n", actual.getNombre(), boteGanado);
                System.out.printf("Fortuna actual de %s: %,.0f€\n", actual.getNombre(), actual.getFortuna());
            }
        }

        // MANEJO ESPECIAL PARA IRCARCEL DESPUÉS DE EVALUAR
        if (casillaActual.getNombre().equals("IrCarcel")) {
            System.out.println("¡Has caído en Ir a la Cárcel! Moviendo a la cárcel...");
            actual.encarcelar(tablero.getPosiciones());
        }

        if (valorDado1 == valorDado2) {
            if (lanzamientos == 3) {
                System.out.println("Tercer doble consecutivo. El avatar va a la cárcel.");
                actual.encarcelar(tablero.getPosiciones());
                tirado = true; // Terminar turno después de ir a la cárcel
            } else {
                System.out.println("Dados dobles. Puedes lanzar de nuevo.");
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
            System.out.println("No se pudo comprar la casilla " + nombre);
        }
    }


    //Metodo que ejecuta todas las acciones relacionadas con el comando 'salir carcel'.
    private void salirCarcel() {
        Jugador jugadorActual = jugadores.get(turno);
        if (jugadorActual.salirDeCarcel()) {
            System.out.println(jugadorActual.getNombre() + " paga 500.000€ y sale de la cárcel. Puede lanzar los datos.");
        } else {
            System.out.println("No se pudo salir de la cárcel. Asegúrate de que estás en la cárcel y tienes suficiente dinero.");
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar enventa'.
    private void listarVenta() {
        System.out.println("Propiedades en venta:");

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
            System.out.println("No hay propiedades en venta en este momento.");
        }
    }


    // Método que realiza las acciones asociadas al comando 'listar jugadores'.
    private void listarJugadores() {
        for (Jugador jugador : jugadores) {
            System.out.println("{");
            System.out.println("    nombre: " + jugador.getNombre() + ",");
            System.out.println("    avatar: " + jugador.getAvatar().getId() + ",");
            System.out.println("    fortuna: " + String.format("%,.0f", jugador.getFortuna()) + ",");

            // Mostrar nombres de propiedades en lugar de objetos
            System.out.print("    propiedades: [");
            ArrayList<Casilla> propiedades = jugador.getPropiedades();
            for (int i = 0; i < propiedades.size(); i++) {
                System.out.print(propiedades.get(i).getNombre());
                if (i < propiedades.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("],");

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

        // Resetear todas las variables de control del turno
        tirado = false;
        lanzamientos = 0;

        // Pasar al siguiente jugador
        turno = (turno + 1) % jugadores.size();

        Jugador siguienteJugador = jugadores.get(turno);
        System.out.println("El jugador actual es " + siguienteJugador.getNombre() + ".");
    }

    //FIN ESQUELETO

    private void crearJugador(String nombre, String tipoAvatar) {
        // Validar primero el tipo de avatar
        String tipoValidado = validarTipoAvatar(tipoAvatar);
        if (tipoValidado == null) {
            System.out.println("Error: '" + tipoAvatar + "' no es un avatar permitido.");
            System.out.println("Avatares válidos: sombrero, esfinge, pelota, coche");
            return;
        }

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
            Jugador nuevoJugador = new Jugador(nombre, tipoValidado, salida, avatares);
            jugadores.add(nuevoJugador);

            // Mostrar la información como en el PDF
            System.out.println("{");
            System.out.println("    nombre: " + nombre + ",");
            System.out.println("    avatar: " + nuevoJugador.getAvatar().getId());
            System.out.println("}");

            // Mostrar el tablero actualizado
            tablero.toString();

        } catch (Exception e) {
            System.out.println("Error al crear el jugador: " + e.getMessage());
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

    // Metodo auxiliar que permite ver de quien es turno
    public void turnoJugador() {
        if (jugadores == null || jugadores.isEmpty()) {
            System.out.println("No hay jugadores creados.");
            return;
        }
        // Asegura que el índice esté dentro de rango
        if (turno < 0 || turno >= jugadores.size()) {
            turno = 0;
        }

        Jugador actual = jugadores.get(turno);
        String avatarId = "-";
        if (actual.getAvatar() != null && actual.getAvatar().getId() != null) {
            avatarId = actual.getAvatar().getId();
        }

        System.out.println("> jugador");
        System.out.println("{");
        System.out.println("nombre: " + actual.getNombre() + ",");
        System.out.println("avatar: " + avatarId);
        System.out.println("}");
    }

    private void inicializarCartas() {
        cartasSuerte = new ArrayList<>();
        cartasComunidad = new ArrayList<>();

        // Cartas de Suerte
        cartasSuerte.add(new Carta(1, "Suerte", "Decides hacer un viaje de placer. Avanza hasta Soalr19. Si pasas por la casilla de Salida, cobra 2.000.000€.", "avanzar:19"));
        cartasSuerte.add(new Carta(2, "Suerte", "Los acreedores te persiguen por impago. Ve a la Cárcel.", "irCarcel"));
        cartasSuerte.add(new Carta(3, "Suerte", "¡Has ganado el bote de la loteria! Recibe 1.000.000€.", "recibir:1000000"));
        cartasSuerte.add(new Carta(4, "Suerte", "Has sido elegido presidente. Paga a cada jugador 250.000€.", "pagarTodos:250000"));
        cartasSuerte.add(new Carta(5, "Suerte", "¡Hora punta de trafico! Retrocede tres casillas.", "retroceder:3"));
        cartasSuerte.add(new Carta(6, "Suerte", "Te multan por usar el móvil mientras conduces. Paga 150.000€.", "pagar:150000"));
        cartasSuerte.add(new Carta(7, "Suerte", "Avanza hasta la casilla de transporte más cercana.", "transporteCercano"));

        // Cartas de Comunidad
        cartasComunidad.add(new Carta(1, "Comunidad", "Paga 500.000€ por un fin de semana en balneario.", "pagar:500000"));
        cartasComunidad.add(new Carta(2, "Comunidad", "Te investigan por fraude. Ve a la Cárcel.", "irCarcel"));
        cartasComunidad.add(new Carta(3, "Comunidad", "Colócate en la casilla de Salida.", "irSalida"));
        cartasComunidad.add(new Carta(4, "Comunidad", "Devolución de Hacienda. Cobra 500.000€.", "recibir:500000"));
        cartasComunidad.add(new Carta(5, "Comunidad", "Retrocede hasta Solar1 para comprar antigüedades exóticas.", "avanzar:1"));
        cartasComunidad.add(new Carta(6, "Comunidad", "Ve a Solar20 para disfrutar del San Fermín.", "avanzar:20"));
    }

    // Método para ejecutar acción de carta
    private void ejecutarAccionCarta(Jugador jugador, Carta carta) {
        System.out.println("Carta elegida: " + carta.getId());
        System.out.println("Descripción: " + carta.getDescripcion());

        String accion = carta.getAccion();

        if (accion.startsWith("avanzar:")) {
            int posicion = Integer.parseInt(accion.split(":")[1]);
            jugador.getAvatar().colocar(tablero.getPosiciones(), posicion);

        } else if (accion.equals("irCarcel")) {
            jugador.encarcelar(tablero.getPosiciones());

        } else if (accion.startsWith("recibir:")) {
            float cantidad = Float.parseFloat(accion.split(":")[1]);
            jugador.sumarFortuna(cantidad);
            System.out.printf("¡Has recibido %,.0f€!\n", cantidad);

        } else if (accion.startsWith("pagarTodos:")) {
            float cantidad = Float.parseFloat(accion.split(":")[1]);
            boolean puedePagar = true;

            // Verificar si tiene suficiente dinero para pagar a todos
            float totalAPagar = cantidad * (jugadores.size() - 1); // -1 porque no se paga a sí mismo
            if (jugador.getFortuna() < totalAPagar) {
                System.out.printf("No tienes suficiente dinero para pagar a todos los jugadores. Necesitas %,.0f€ pero tienes %,.0f€\n",
                        totalAPagar, jugador.getFortuna());
                puedePagar = false;

            } else if (accion.startsWith("retroceder:")) {
                int casillas = Integer.parseInt(accion.split(":")[1]);
                int posicionActual = jugador.getAvatar().getLugar().getPosicion();
                int nuevaPosicion = (posicionActual - casillas + 40) % 40;
                // AQUÍ ESTOY USANDO colocar() DIRECTAMENTE:
                jugador.getAvatar().colocar(tablero.getPosiciones(), nuevaPosicion);
                System.out.println("Has retrocedido " + casillas + " casillas.");

            } else if (accion.startsWith("pagar:")) {
                float cantidadPago = Float.parseFloat(accion.split(":")[1]);
                if (jugador.getFortuna() >= cantidadPago) {
                    jugador.restarFortuna(cantidadPago);
                    tablero.añadirAlBote(cantidadPago);
                    System.out.printf("Has pagado %,.0f€\n", cantidadPago);
                } else {
                    System.out.println("No tienes suficiente dinero para pagar.");
                }

            } else if (accion.equals("transporteCercano")) {
                int posicionActual = jugador.getAvatar().getLugar().getPosicion();
                String[] nombresTransporte = {"Trans1", "Trans2", "Trans3", "Trans4"};
                int[] posicionesTransporte = {6, 16, 26, 36};

                String transporteCercano = null;
                int menorDistancia = Integer.MAX_VALUE;

                for (int i = 0; i < posicionesTransporte.length; i++) {
                    int distancia = (posicionesTransporte[i] - posicionActual + 40) % 40;
                    if (distancia < menorDistancia && distancia > 0) {
                        menorDistancia = distancia;
                        transporteCercano = nombresTransporte[i];
                    }
                }

                if (transporteCercano != null) {
                    // USAR MÉTODO EXISTENTE: colocar()
                    // USAR MÉTODO EXISTENTE encontrar_casilla(String nombre)
                    Casilla destino = tablero.encontrar_casilla(transporteCercano);
                    if (destino != null) {
                        jugador.getAvatar().colocar(tablero.getPosiciones(), destino.getPosicion());
                        System.out.println("Te has movido a " + destino.getNombre());
                        destino.evaluarCasilla(jugador, banca, 0);
                    }
                }

            } else if (accion.equals("irSalida")) {
                jugador.getAvatar().colocar(tablero.getPosiciones(), 1); // Posición 1 = Salida
                jugador.sumarFortuna(Valor.SUMA_VUELTA);
                System.out.printf("¡Has cobrado %,.0f€ por pasar por salida!\n", Valor.SUMA_VUELTA);
            }
            System.out.printf("Fortuna actual de %s: %,.0f€\n", jugador.getNombre(), jugador.getFortuna());
        }
    }

    public void ejecutarCarta(Jugador jugador, String tipoCarta) {
        Carta carta = obtenerSiguienteCarta(tipoCarta);
        ejecutarAccionCarta(jugador, carta);
    }


    private Carta obtenerSiguienteCarta(String tipo) {
        Carta carta;

        if (tipo.equals("Suerte")) {
            carta = cartasSuerte.get(contadorSuerte);
            contadorSuerte = (contadorSuerte + 1) % cartasSuerte.size();
        } else {
            carta = cartasComunidad.get(contadorComunidad);
            contadorComunidad = (contadorComunidad + 1) % cartasComunidad.size();
        }

        return carta;
    }

    private void mostrarEstadisticas(String nombreJugador) {
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

    private void mostrarEstadisticasJuego() {
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

        return masRentable != null ? masRentable.getNombre() : "Solar3";
    }

    private String calcularGrupoMasRentable() {
        HashMap<String, Float> rentabilidadGrupos = new HashMap<>();

        // Calcular rentabilidad de cada grupo
        for (Grupo grupo : tablero.getGrupos().values()) {
            float rentabilidadTotal = 0;
            int casillasValiosas = 0;

            for (Casilla casilla : grupo.getMiembros()) {
                if (casilla.getValor() > 0) {
                    float rentabilidad = casilla.getImpuesto() / casilla.getValor();
                    rentabilidadTotal += rentabilidad;
                    casillasValiosas++;
                }
            }

            if (casillasValiosas > 0) {
                rentabilidadGrupos.put(grupo.getColorGrupo(), rentabilidadTotal / casillasValiosas);
            }
        }

        // Encontrar el grupo más rentable
        String grupoMasRentable = "Verde";
        float maxRentabilidad = -1;

        for (String color : rentabilidadGrupos.keySet()) {
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

        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            for (Casilla casilla : lado) {
                int visitas = casilla.getAvatares().size();
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
}








