package monopoly;

import partida.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Tablero {
    //Atributos.
    private ArrayList<ArrayList<Casilla>> posiciones; //Posiciones del tablero: se define como un arraylist de arraylists de casillas (uno por cada lado del tablero).
    private HashMap<String, Grupo> grupos; //Grupos del tablero, almacenados como un HashMap con clave String (será el color del grupo).
    private Jugador banca; //Un jugador que será la banca.

    //Constructor: únicamente le pasamos el jugador banca (que se creará desde el menú).
    public Tablero(Jugador banca) {
        this.banca = banca;
        this.posiciones = new ArrayList<>();
        this.grupos = new HashMap<>();
        this.generarCasillas(); //Llamamos al método que genera todas las casillas del tablero.
        this.crearGrupos(); //Llamamos al método que genera todos los grupos del tablero.
    }

    private void crearGrupos() {
        // Crear los grupos y añadirlos al HashMap
        grupos.put("Marrón", new Grupo(
                encontrar_casilla("Solar1"),
                encontrar_casilla("Solar2"),
                "Marrón"
        ));
        grupos.put("Celeste", new Grupo(
                encontrar_casilla("Solar3"),
                encontrar_casilla("Solar4"),
                encontrar_casilla("Solar5"),
                "Celeste"
        ));
        grupos.put("Rosa", new Grupo(
                encontrar_casilla("Solar6"),
                encontrar_casilla("Solar7"),
                encontrar_casilla("Solar8"),
                "Rosa"
        ));
        grupos.put("Naranja", new Grupo(
                encontrar_casilla("Solar9"),
                encontrar_casilla("Solar10"),
                encontrar_casilla("Solar11"),
                "Naranja"
        ));
        grupos.put("Rojo", new Grupo(
                encontrar_casilla("Solar12"),
                encontrar_casilla("Solar13"),
                encontrar_casilla("Solar14"),
                "Rojo"
        ));
        grupos.put("Amarillo", new Grupo(
                encontrar_casilla("Solar15"),
                encontrar_casilla("Solar16"),
                encontrar_casilla("Solar17"),
                "Amarillo"
        ));
        grupos.put("Verde", new Grupo(
                encontrar_casilla("Solar18"),
                encontrar_casilla("Solar19"),
                encontrar_casilla("Solar20"),
                "Verde"
        ));
        grupos.put("Azul", new Grupo(
                encontrar_casilla("Solar21"),
                encontrar_casilla("Solar22"),
                "Azul"
        ));
    }


    //Método para crear todas las casillas del tablero. Formado a su vez por cuatro métodos (1/lado).
    private void generarCasillas() {
        this.posiciones = new ArrayList<ArrayList<Casilla>>();
        this.insertarLadoSur();
        this.insertarLadoOeste();
        this.insertarLadoNorte();
        this.insertarLadoEste();
    }

    //Método para insertar las casillas del lado norte.
    private void insertarLadoNorte() {
        ArrayList<Casilla> ladoNorte = this.posiciones.get(2);

        // Posiciones 21-30
        ladoNorte.add(new Casilla("Solar13", "Solar", 21, 2200000, banca)); // 2.200.000€
        ladoNorte.add(new Casilla("Solar14", "Solar", 22, 2400000, banca)); // 2.400.000€
        ladoNorte.add(new Casilla("Serv2", "Servicios", 23, 500000, banca)); // 500.000€
        ladoNorte.add(new Casilla("Solar15", "Solar", 24, 2600000, banca)); // 2.600.000€
        ladoNorte.add(new Casilla("Solar16", "Solar", 25, 2600000, banca)); // 2.600.000€
        ladoNorte.add(new Casilla("Trans3", "Transporte", 26, 500000, banca)); // 500.000€
        ladoNorte.add(new Casilla("Solar17", "Solar", 27, 2800000, banca)); // 2.800.000€
        ladoNorte.add(new Casilla("Suerte3", "Suerte", 28, banca));
        ladoNorte.add(new Casilla("Solar18", "Solar", 29, 3000000, banca)); // 3.000.000€
        ladoNorte.add(new Casilla("Solar19", "Solar", 30, 3000000, banca)); // 3.000.000€
    }

    //Método para insertar las casillas del lado sur.
    private void insertarLadoSur() {

        ArrayList<Casilla> ladoSur = this.posiciones.get(0);

        // Posiciones 1-10
        ladoSur.add(new Casilla("Salida", "Salida", 1, banca));
        ladoSur.add(new Casilla("Solar1", "Solar", 2, 600000, banca));    // 600.000€
        ladoSur.add(new Casilla("Caja1", "Comunidad", 3, banca));
        ladoSur.add(new Casilla("Solar2", "Solar", 4, 600000, banca));    // 600.000€
        ladoSur.add(new Casilla("Imp1", "Impuesto", 5, 2000000, banca));
        ladoSur.add(new Casilla("Trans1", "Transporte", 6, 500000, banca)); // 500.000€
        ladoSur.add(new Casilla("Solar3", "Solar", 7, 1000000, banca));   // 1.000.000€
        ladoSur.add(new Casilla("Suerte1", "Suerte", 8, banca));
        ladoSur.add(new Casilla("Solar4", "Solar", 9, 1000000, banca));   // 1.000.000€
        ladoSur.add(new Casilla("Solar5", "Solar", 10, 1200000, banca));  // 1.200.000€
    }

    //Método que inserta casillas del lado oeste.
    private void insertarLadoOeste() {
        ArrayList<Casilla> ladoOeste = this.posiciones.get(3);

        // Posiciones 31-40
        ladoOeste.add(new Casilla("Carcel", "Carcel", 31, banca));
        ladoOeste.add(new Casilla("Solar20", "Solar", 32, 3200000, banca)); // 3.200.000€
        ladoOeste.add(new Casilla("Solar21", "Solar", 33, 3500000, banca)); // 3.500.000€
        ladoOeste.add(new Casilla("Serv3", "Servicios", 34, 500000, banca)); // 500.000€
        ladoOeste.add(new Casilla("Solar22", "Solar", 35, 4000000, banca)); // 4.000.000€
        ladoOeste.add(new Casilla("Trans4", "Transporte", 36, 500000, banca)); // 500.000€
        ladoOeste.add(new Casilla("Caja3", "Comunidad", 37, banca));
        ladoOeste.add(new Casilla("Parking", "Parking", 38, banca));
        ladoOeste.add(new Casilla("Imp2", "Impuesto", 39, 2000000, banca));
        ladoOeste.add(new Casilla("IrCarcel", "IrCarcel", 40, banca));
    }

    //Método que inserta las casillas del lado este.
    private void insertarLadoEste() {
        ArrayList<Casilla> ladoEste = this.posiciones.get(1);

        // Posiciones 11-20
        ladoEste.add(new Casilla("Solar6", "Solar", 11, 1400000, banca));  // 1.400.000€
        ladoEste.add(new Casilla("Trans2", "Transporte", 12, 500000, banca)); // 500.000€
        ladoEste.add(new Casilla("Solar7", "Solar", 13, 1400000, banca));  // 1.400.000€
        ladoEste.add(new Casilla("Solar8", "Solar", 14, 1600000, banca));  // 1.600.000€
        ladoEste.add(new Casilla("Solar9", "Solar", 15, 1800000, banca));  // 1.800.000€
        ladoEste.add(new Casilla("Caja2", "Comunidad", 16, banca));
        ladoEste.add(new Casilla("Solar10", "Solar", 17, 1800000, banca)); // 1.800.000€
        ladoEste.add(new Casilla("Solar11", "Solar", 18, 2200000, banca)); // 2.200.000€
        ladoEste.add(new Casilla("Suerte2", "Suerte", 19, banca));
        ladoEste.add(new Casilla("Solar12", "Solar", 20, 2200000, banca)); // 2.200.000€
    }

    // En Tablero.java - método para obtener un grupo por color
    public Grupo getGrupo(String color) {
        return grupos.get(color);
    }

    // Método para verificar si un jugador tiene todo un grupo
    public boolean tieneTodoElGrupo(Jugador jugador, String colorGrupo) {
        Grupo grupo = grupos.get(colorGrupo);
        return grupo != null && grupo.esDuenhoGrupo(jugador);
    }


    //Para imprimir el tablero, modificamos el método toString().
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tablero de Monopoly:\n");
        for (int i = 0; i < posiciones.size(); i++) {
            sb.append("Lado ").append(i + 1).append(":\n");
            for (Casilla casilla : posiciones.get(i)) {
                sb.append(casilla.toString()).append("\n");
            }
        }
        return sb.toString();
    }

    //Método usado para buscar la casilla con el nombre pasado como argumento:
    public Casilla encontrar_casilla(String nombre){
        for (ArrayList<Casilla> lado : posiciones) {
            for (Casilla casilla : lado) {
                if (casilla.getNombre().equalsIgnoreCase(nombre)) {
                    return casilla;
                }
            }
        }
        return null; // Si no se encuentra la casilla, retorna null
    }
    // En Tablero.java - añade este método
    public void mostrarTablero() {
        System.out.println(Valor.CYAN + "========================================== TABLERO DE MONOPOLY ==========================================" + Valor.RESET);

        // Mostrar todas las casillas en orden de posición
        for (int i = 1; i <= 40; i++) {
            Casilla casilla = encontrarCasillaPorPosicion(i);
            if (casilla != null) {
                String colorFondo = Valor.RESET;
                String colorTexto = Valor.WHITE;

                // Asignar colores según el tipo de casilla
                switch (casilla.getTipo()) {
                    case "Solar":
                        if (casilla.getGrupo() != null) {
                            // Usar colores de grupo si están disponibles
                            String colorGrupo = casilla.getGrupo().getColorGrupo();
                            switch (colorGrupo) {
                                case "Marron": colorFondo = Valor.BG_RED; colorTexto = Valor.BLACK; break;
                                case "Celeste": colorFondo = Valor.BG_CYAN; colorTexto = Valor.BLACK; break;
                                case "Purpura": colorFondo = Valor.BG_PURPLE; colorTexto = Valor.WHITE; break;
                                case "Naranja": colorFondo = Valor.BG_YELLOW; colorTexto = Valor.BLACK; break;
                                case "Rojo": colorFondo = Valor.BG_RED; colorTexto = Valor.WHITE; break;
                                case "Amarillo": colorFondo = Valor.BG_YELLOW; colorTexto = Valor.BLACK; break;
                                case "Verde": colorFondo = Valor.BG_GREEN; colorTexto = Valor.BLACK; break;
                                case "Azul": colorFondo = Valor.BG_BLUE; colorTexto = Valor.WHITE; break;
                                case "Cian": colorFondo = Valor.BG_CYAN; colorTexto = Valor.BLACK; break;
                                case "Rosa": colorFondo = Valor.BG_PURPLE; colorTexto = Valor.WHITE; break;
                                default: colorFondo = Valor.RESET; colorTexto = Valor.WHITE;
                            }
                        }
                        break;
                    case "Transporte":
                        colorFondo = Valor.BG_BLACK;
                        colorTexto = Valor.WHITE;
                        break;
                    case "Servicio":
                        colorFondo = Valor.BG_WHITE;
                        colorTexto = Valor.BLACK;
                        break;
                    case "Impuesto":
                        colorFondo = Valor.BG_RED;
                        colorTexto = Valor.WHITE;
                        break;
                    case "Suerte":
                        colorFondo = Valor.BG_YELLOW;
                        colorTexto = Valor.BLACK;
                        break;
                    case "Comunidad":
                        colorFondo = Valor.BG_BLUE;
                        colorTexto = Valor.WHITE;
                        break;
                    case "Carcel":
                        colorFondo = Valor.BG_BLACK;
                        colorTexto = Valor.WHITE;
                        break;
                    case "Parking":
                        colorFondo = Valor.BG_GREEN;
                        colorTexto = Valor.BLACK;
                        break;
                    case "Salida":
                        colorFondo = Valor.BG_GREEN;
                        colorTexto = Valor.WHITE;
                        break;
                    case "IrCarcel":
                        colorFondo = Valor.BG_RED;
                        colorTexto = Valor.WHITE;
                        break;
                    default:
                        colorFondo = Valor.RESET;
                        colorTexto = Valor.WHITE;
                }

                // Construir información de avatares
                String avataresStr = "";
                if (!casilla.getAvatares().isEmpty()) {
                    avataresStr = Valor.PURPLE + " [Avatares: ";
                    for (Avatar avatar : casilla.getAvatares()) {
                        avataresStr += avatar.getId() + " ";
                    }
                    avataresStr = avataresStr.trim() + "]" + Valor.RESET;
                }

                // Mostrar la casilla
                System.out.printf(colorFondo + colorTexto + "│ Pos %2d: %-15s " + Valor.RESET,
                        casilla.getPosicion(), casilla.getNombre());

                // Mostrar dueño si no es la banca
                if (casilla.getDuenho() != null && !casilla.getDuenho().getNombre().equals("Banca")) {
                    System.out.printf(Valor.YELLOW + "| Dueño: %-10s " + Valor.RESET, casilla.getDuenho().getNombre());
                }

                // Mostrar avatares
                System.out.println(avataresStr);

                // Línea separadora cada 10 casillas
                if (i % 10 == 0 && i < 40) {
                    System.out.println(Valor.WHITE + "├────────────────────────────────────────────────────────────────────────────────────────┤" + Valor.RESET);
                }
            }
        }

        System.out.println(Valor.WHITE + "└────────────────────────────────────────────────────────────────────────────────────────┘" + Valor.RESET);

        // Mostrar leyenda
        System.out.println(Valor.CYAN + "\nLEYENDA:" + Valor.RESET);
        System.out.println(Valor.BG_RED + Valor.BLACK + " Marrón " + Valor.RESET + " " +
                Valor.BG_CYAN + Valor.BLACK + " Celeste " + Valor.RESET + " " +
                Valor.BG_PURPLE + Valor.WHITE + " Púrpura " + Valor.RESET + " - Grupos de Solares");
        System.out.println(Valor.BG_BLACK + Valor.WHITE + " Transportes " + Valor.RESET + " " +
                Valor.BG_WHITE + Valor.BLACK + " Servicios " + Valor.RESET + " " +
                Valor.BG_RED + Valor.WHITE + " Impuestos " + Valor.RESET + " - Propiedades especiales");
        System.out.println(Valor.PURPLE + "● Avatares en color púrpura" + Valor.RESET);
        System.out.println(Valor.CYAN + "========================================================================================================" + Valor.RESET);
    }

    // Método auxiliar para encontrar casilla por posición
    private Casilla encontrarCasillaPorPosicion(int posicion) {
        for (ArrayList<Casilla> lado : posiciones) {
            for (Casilla casilla : lado) {
                if (casilla.getPosicion() == posicion) {
                    return casilla;
                }
            }
        }
        return null;
    }

    // Getters
    public ArrayList<ArrayList<Casilla>> getPosiciones() { return posiciones; }
    public HashMap<String, Grupo> getGrupos() { return grupos; }
    public Jugador getBanca() { return banca; }
}

