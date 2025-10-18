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
    // En Tablero.java - añade este método
    // En Tablero.java - añade este método
    public void mostrarTablero() {
        System.out.println(Valor.CYAN + "\n========================================== TABLERO DE MONOPOLY ==========================================" + Valor.RESET);

        // Obtener todas las casillas
        Casilla parking = encontrar_casilla("Parking");
        Casilla solar12 = encontrar_casilla("Solar12");
        Casilla suerte3 = encontrar_casilla("Suerte3");
        Casilla solar13 = encontrar_casilla("Solar13");
        Casilla solar14 = encontrar_casilla("Solar14");
        Casilla trans3 = encontrar_casilla("Trans3");
        Casilla solar15 = encontrar_casilla("Solar15");
        Casilla solar16 = encontrar_casilla("Solar16");
        Casilla serv2 = encontrar_casilla("Serv2");
        Casilla solar17 = encontrar_casilla("Solar17");
        Casilla irCarcel = encontrar_casilla("IrCarcel");

        Casilla solar11 = encontrar_casilla("Solar11");
        Casilla solar10 = encontrar_casilla("Solar10");
        Casilla caja2 = encontrar_casilla("Caja2");
        Casilla solar9 = encontrar_casilla("Solar9");
        Casilla trans2 = encontrar_casilla("Trans2");
        Casilla solar8 = encontrar_casilla("Solar8");
        Casilla solar7 = encontrar_casilla("Solar7");
        Casilla serv1 = encontrar_casilla("Serv1");
        Casilla solar6 = encontrar_casilla("Solar6");

        Casilla solar18 = encontrar_casilla("Solar18");
        Casilla solar19 = encontrar_casilla("Solar19");
        Casilla caja3 = encontrar_casilla("Caja3");
        Casilla solar20 = encontrar_casilla("Solar20");
        Casilla trans4 = encontrar_casilla("Trans4");
        Casilla suerte2 = encontrar_casilla("Suerte2");
        Casilla solar21 = encontrar_casilla("Solar21");
        Casilla imp2 = encontrar_casilla("Imp2");
        Casilla solar22 = encontrar_casilla("Solar22");

        Casilla carcel = encontrar_casilla("Carcel");
        Casilla solar5 = encontrar_casilla("Solar5");
        Casilla solar4 = encontrar_casilla("Solar4");
        Casilla suerte1 = encontrar_casilla("Suerte1");
        Casilla solar3 = encontrar_casilla("Solar3");
        Casilla trans1 = encontrar_casilla("Trans1");
        Casilla imp1 = encontrar_casilla("Imp1");
        Casilla solar2 = encontrar_casilla("Solar2");
        Casilla caja1 = encontrar_casilla("Caja1");
        Casilla solar1 = encontrar_casilla("Solar1");
        Casilla salida = encontrar_casilla("Salida");

        // Mapeo de colores
        java.util.Map<String, String> coloresGrupo = new java.util.HashMap<>();
        coloresGrupo.put("Marron", Valor.BG_RED + Valor.BLACK);
        coloresGrupo.put("Celeste", Valor.BG_CYAN + Valor.BLACK);
        coloresGrupo.put("Purpura", Valor.BG_PURPLE + Valor.WHITE);
        coloresGrupo.put("Naranja", Valor.BG_YELLOW + Valor.BLACK);
        coloresGrupo.put("Rojo", Valor.BG_RED + Valor.WHITE);
        coloresGrupo.put("Amarillo", Valor.BG_YELLOW + Valor.BLACK);
        coloresGrupo.put("Verde", Valor.BG_GREEN + Valor.BLACK);
        coloresGrupo.put("Azul", Valor.BG_BLUE + Valor.WHITE);
        coloresGrupo.put("Cian", Valor.BG_CYAN + Valor.BLACK);
        coloresGrupo.put("Rosa", Valor.BG_PURPLE + Valor.WHITE);

        // Función para obtener el color
        java.util.function.Function<Casilla, String> getColorCasilla = (casilla) -> {
            if (casilla.getGrupo() != null && casilla.getTipo().equals("Solar")) {
                String colorGrupo = casilla.getGrupo().getColorGrupo();
                return coloresGrupo.getOrDefault(colorGrupo, Valor.RESET);
            }

            switch (casilla.getTipo()) {
                case "Transporte": return Valor.BG_BLACK + Valor.WHITE;
                case "Servicio": return Valor.BG_WHITE + Valor.BLACK;
                case "Impuesto": return Valor.BG_RED + Valor.WHITE;
                case "Suerte": return Valor.BG_YELLOW + Valor.BLACK;
                case "Comunidad": return Valor.BG_BLUE + Valor.WHITE;
                case "Carcel": return Valor.BG_BLACK + Valor.WHITE;
                case "Parking": return Valor.BG_GREEN + Valor.BLACK;
                case "Salida": return Valor.BG_GREEN + Valor.WHITE;
                case "IrCarcel": return Valor.BG_RED + Valor.WHITE;
                default: return Valor.RESET;
            }
        };

        // Función para formatear nombre
        java.util.function.Function<Casilla, String> formatearNombre = (casilla) -> {
            String nombre = casilla.getNombre();
            if (nombre.startsWith("Solar")) {
                return "Solar" + nombre.substring(5);
            } else if (nombre.startsWith("Trans")) {
                return "Trans" + nombre.substring(5);
            } else if (nombre.startsWith("Serv")) {
                return "Serv" + nombre.substring(4);
            } else if (nombre.startsWith("Caja")) {
                return "Caja";
            } else if (nombre.startsWith("Suerte")) {
                return "Suerte";
            } else if (nombre.equals("IrCarcel")) {
                return "IrCarcel";
            } else if (nombre.equals("Parking")) {
                return "Parking";
            } else if (nombre.equals("Carcel")) {
                return "Cárcel";
            } else if (nombre.equals("Salida")) {
                return "Salida";
            } else if (nombre.startsWith("Imp")) {
                return "Impuesto";
            }
            return nombre;
        };

        // Mostrar fila superior
        System.out.println();
        System.out.print("| ");
        Casilla[] filaSuperior = {parking, solar12, suerte3, solar13, solar14, trans3, solar15, solar16, serv2, solar17, irCarcel};
        for (Casilla casilla : filaSuperior) {
            String color = getColorCasilla.apply(casilla);
            String nombre = formatearNombre.apply(casilla);
            System.out.print(color + nombre + Valor.RESET + " | ");
        }
        System.out.println();

        // Mostrar lados izquierdo y derecho
        Casilla[] ladoIzquierdo = {solar11, solar10, caja2, solar9, trans2, solar8, solar7, serv1, solar6};
        Casilla[] ladoDerecho = {solar18, solar19, caja3, solar20, trans4, suerte2, solar21, imp2, solar22};

        for (int i = 0; i < ladoIzquierdo.length; i++) {
            System.out.print("| ");
            String colorIzq = getColorCasilla.apply(ladoIzquierdo[i]);
            String nombreIzq = formatearNombre.apply(ladoIzquierdo[i]);
            System.out.print(colorIzq + nombreIzq + Valor.RESET + " |");

            // Espacio para el centro
            System.out.print(" ".repeat(50));

            String colorDer = getColorCasilla.apply(ladoDerecho[i]);
            String nombreDer = formatearNombre.apply(ladoDerecho[i]);
            System.out.println("| " + colorDer + nombreDer + Valor.RESET + " |");
        }

        // Mostrar fila inferior
        System.out.print("| ");
        Casilla[] filaInferior = {carcel, solar5, solar4, suerte1, solar3, trans1, imp1, solar2, caja1, solar1, salida};
        for (Casilla casilla : filaInferior) {
            String color = getColorCasilla.apply(casilla);
            String nombre = formatearNombre.apply(casilla);
            System.out.print(color + nombre + Valor.RESET + " | ");
        }
        System.out.println();

        // Mostrar avatares
        System.out.println(Valor.PURPLE + "\nAVATARES EN EL TABLERO:" + Valor.RESET);
        boolean hayAvatares = false;
        for (ArrayList<Casilla> lado : posiciones) {
            for (Casilla casilla : lado) {
                if (!casilla.getAvatares().isEmpty()) {
                    hayAvatares = true;
                    System.out.print("• " + casilla.getNombre() + ": ");
                    for (Avatar avatar : casilla.getAvatares()) {
                        System.out.print(avatar.getId() + " ");
                    }
                    System.out.println();
                }
            }
        }
        if (!hayAvatares) {
            System.out.println("No hay avatares en el tablero.");
        }

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

