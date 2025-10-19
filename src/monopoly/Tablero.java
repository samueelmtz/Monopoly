package monopoly;

import partida.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Tablero {
    //Atributos.
    private ArrayList<ArrayList<Casilla>> posiciones; //Posiciones del tablero: se define como un arraylist de arraylists de casillas (uno por cada lado del tablero).
    private HashMap<String, Grupo> grupos; //Grupos del tablero, almacenados como un HashMap con clave String (será el color del grupo).
    private Jugador banca;//Un jugador que será la banca.
    private float boteParking; //Atributo auxiliar para acumular el bote del parking

    //Constructor: únicamente le pasamos el jugador banca (que se creará desde el menú).
    public Tablero(Jugador banca) {
        this.banca = banca;
        this.posiciones = new ArrayList<>();
        this.grupos = new HashMap<>();
        this.boteParking = 0f;
        this.generarCasillas(); //Llamamos al método que genera todas las casillas del tablero.
        this.crearGrupos(); //Llamamos al método que genera todos los grupos del tablero.
    }

    // Método para añadir dinero al bote
    public void añadirAlBote(float cantidad) {
        this.boteParking += cantidad;

        // Usar sumarValor en la casilla de Parking
        Casilla parking = encontrar_casilla("Parking");
        if (parking != null) {
            parking.sumarValor(cantidad);
        }
        System.out.printf("Se han añadido %,.0f€ al bote del Parking. Bote actual: %,.0f€\n", cantidad, this.boteParking);
    }

    // Método para que un jugador reclame el bote
    public float reclamarBote(Jugador jugador) {
        float boteActual = this.boteParking;
        if (boteActual > 0) {
            jugador.sumarFortuna(boteActual);

            // Resetear el valor de la casilla Parking usando sumarValor
            Casilla parking = encontrar_casilla("Parking");
            if (parking != null) {
                // Restar el valor actual para ponerlo a 0
                parking.sumarValor(-parking.getValor());
            }
            System.out.printf("¡%s ha ganado el bote del Parking: %,.0f€!\n", jugador.getNombre(), boteActual);
            this.boteParking = 0f; // Resetear el bote
        } else {
            System.out.println("El bote del Parking está vacío.");
        }
        return boteActual;
    }

    private void crearGrupos() {
        // Crear los grupos y añadirlos al HashMap
        grupos.put("Naranja", new Grupo(
                encontrar_casilla("Solar1"),
                encontrar_casilla("Solar2"),
                "Naranja"
        ));
        grupos.put("Celeste", new Grupo(
                encontrar_casilla("Solar3"),
                encontrar_casilla("Solar4"),
                encontrar_casilla("Solar5"),
                "Celeste"
        ));
        grupos.put("Purpura", new Grupo(
                encontrar_casilla("Solar6"),
                encontrar_casilla("Solar7"),
                encontrar_casilla("Solar8"),
                "Purpura"
        ));
        grupos.put("Negro", new Grupo(
                encontrar_casilla("Solar9"),
                encontrar_casilla("Solar10"),
                encontrar_casilla("Solar11"),
                "Negro"
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

        for (int i = 0; i < 4; i++) {
            this.posiciones.add(new ArrayList<Casilla>());
        }

        this.insertarLadoSur();
        this.insertarLadoOeste();
        this.insertarLadoNorte();
        this.insertarLadoEste();
    }

    //Metodo que inserta las casillas del lado sur.
    private void insertarLadoSur() {
        ArrayList<Casilla> ladoSur = this.posiciones.get(0);

        // Posiciones 1-10 con alquileres según PDF
        ladoSur.add(new Casilla("Salida", "Salida", 1, banca));
        ladoSur.add(new Casilla("Solar1", "Solar", 2, 600000, 20000, banca));
        ladoSur.add(new Casilla("Caja1", "Comunidad", 3, banca));
        ladoSur.add(new Casilla("Solar2", "Solar", 4, 600000, 40000, banca));
        ladoSur.add(new Casilla("Imp1", 5, 2000000, banca));
        ladoSur.add(new Casilla("Trans1", "Transporte", 6, 500000, banca));
        ladoSur.add(new Casilla("Solar3", "Solar", 7, 1000000, 60000, banca));
        ladoSur.add(new Casilla("Suerte1", "Suerte", 8, banca));
        ladoSur.add(new Casilla("Solar4", "Solar", 9, 1000000, 60000, banca));
        ladoSur.add(new Casilla("Solar5", "Solar", 10, 1200000, 80000, banca));
    }

    //Método que inserta las casillas del lado oeste.
    private void insertarLadoOeste() {
        ArrayList<Casilla> ladoOeste = this.posiciones.get(1);

        // Posiciones 11-20 con alquileres según PDF
        ladoOeste.add(new Casilla("Carcel", "Carcel", 11, banca));
        ladoOeste.add(new Casilla("Solar6", "Solar", 12, 1400000, 100000, banca));
        ladoOeste.add(new Casilla("Serv1", "Servicios", 13, 500000, banca));
        ladoOeste.add(new Casilla("Solar7", "Solar", 14, 1400000, 100000, banca));
        ladoOeste.add(new Casilla("Solar8", "Solar", 15, 1600000, 120000, banca));
        ladoOeste.add(new Casilla("Trans2", "Transporte", 16, 500000, banca));
        ladoOeste.add(new Casilla("Solar9", "Solar", 17, 1800000, 140000, banca));
        ladoOeste.add(new Casilla("Caja2", "Comunidad", 18, banca));
        ladoOeste.add(new Casilla("Solar10", "Solar", 19, 1800000, 140000, banca));
        ladoOeste.add(new Casilla("Solar11", "Solar", 20, 2200000, 160000, banca));
    }

    //Método que inserta las casillas del lado norte.
    private void insertarLadoNorte() {
        ArrayList<Casilla> ladoNorte = this.posiciones.get(2);

        // Posiciones 21-30 con alquileres según PDF
        ladoNorte.add(new Casilla("Parking", "Parking", 21, banca));
        Casilla parking = encontrar_casilla("Parking");
        if (parking != null) {
            parking.setValor(0f);
        }
        ladoNorte.add(new Casilla("Solar12", "Solar", 22, 2200000, 180000, banca));
        ladoNorte.add(new Casilla("Suerte3", "Suerte", 23, banca));
        ladoNorte.add(new Casilla("Solar13", "Solar", 24, 2200000, 180000, banca));
        ladoNorte.add(new Casilla("Solar14", "Solar", 25, 2400000, 200000, banca));
        ladoNorte.add(new Casilla("Trans3", "Transporte", 26, 500000, banca));
        ladoNorte.add(new Casilla("Solar15", "Solar", 27, 2600000, 220000, banca));
        ladoNorte.add(new Casilla("Solar16", "Solar", 28, 2600000, 220000, banca));
        ladoNorte.add(new Casilla("Serv2", "Servicios", 29, 500000, banca));
        ladoNorte.add(new Casilla("Solar17", "Solar", 30, 2800000, 240000, banca));
    }


    //Método que inserta casillas del lado este.
    private void insertarLadoEste() {
        ArrayList<Casilla> ladoEste = this.posiciones.get(3);

        // Posiciones 31-40 con alquileres según PDF
        ladoEste.add(new Casilla("IrCarcel", "IrCarcel", 31, banca));
        ladoEste.add(new Casilla("Solar18", "Solar", 32, 3000000, 260000, banca));
        ladoEste.add(new Casilla("Solar19", "Solar", 33, 3000000, 260000, banca));
        ladoEste.add(new Casilla("Caja3", "Comunidad", 34, banca));
        ladoEste.add(new Casilla("Solar20", "Solar", 35, 3200000, 280000, banca));
        ladoEste.add(new Casilla("Trans4", "Transporte", 36, 500000, banca));
        ladoEste.add(new Casilla("Suerte2", "Suerte", 37, banca));
        ladoEste.add(new Casilla("Solar21", "Solar", 38, 3500000, 350000, banca));
        ladoEste.add(new Casilla("Imp2", 39, 2000000, banca));
        ladoEste.add(new Casilla("Solar22", "Solar", 40, 4000000, 500000, banca));
    }

    //Para imprimir el tablero, modificamos el método toString().
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Valor.CYAN).append("\n========================================== TABLERO DE MONOPOLY ==========================================").append(Valor.RESET).append("\n");

        //Obtener casillas para la fila SUPERIOR (Norte)
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

        // Obtener casillas para el lado IZQUIERDO (Oeste)
        Casilla solar11 = encontrar_casilla("Solar11");
        Casilla solar10 = encontrar_casilla("Solar10");
        Casilla caja2 = encontrar_casilla("Caja2");
        Casilla solar9 = encontrar_casilla("Solar9");
        Casilla trans2 = encontrar_casilla("Trans2");
        Casilla solar8 = encontrar_casilla("Solar8");
        Casilla solar7 = encontrar_casilla("Solar7");
        Casilla serv1 = encontrar_casilla("Serv1");
        Casilla solar6 = encontrar_casilla("Solar6");

        // Obtener casillas para el lado DERECHO (Este)
        Casilla solar18 = encontrar_casilla("Solar18");
        Casilla solar19 = encontrar_casilla("Solar19");
        Casilla caja3 = encontrar_casilla("Caja3");
        Casilla solar20 = encontrar_casilla("Solar20");
        Casilla trans4 = encontrar_casilla("Trans4");
        Casilla suerte2 = encontrar_casilla("Suerte2");
        Casilla solar21 = encontrar_casilla("Solar21");
        Casilla imp2 = encontrar_casilla("Imp2");
        Casilla solar22 = encontrar_casilla("Solar22");

        // Obtener casillas para la fila INFERIOR (Sur)
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

        // Mostrar fila SUPERIOR (Norte)
        sb.append("\n");
        sb.append("| ");
        sb.append(obtenerCasillaFormateada(parking)).append(" | ");
        sb.append(obtenerCasillaFormateada(solar12)).append(" | ");
        sb.append(obtenerCasillaFormateada(suerte3)).append(" | ");
        sb.append(obtenerCasillaFormateada(solar13)).append(" | ");
        sb.append(obtenerCasillaFormateada(solar14)).append(" | ");
        sb.append(obtenerCasillaFormateada(trans3)).append(" | ");
        sb.append(obtenerCasillaFormateada(solar15)).append(" | ");
        sb.append(obtenerCasillaFormateada(solar16)).append(" | ");
        sb.append(obtenerCasillaFormateada(serv2)).append(" | ");
        sb.append(obtenerCasillaFormateada(solar17)).append(" | ");
        sb.append(obtenerCasillaFormateada(irCarcel)).append(" |\n");

        // Mostrar lados IZQUIERDO y DERECHO
        Casilla[] izquierda = {solar11, solar10, caja2, solar9, trans2, solar8, solar7, serv1, solar6};
        Casilla[] derecha = {solar18, solar19, caja3, solar20, trans4, suerte2, solar21, imp2, solar22};

        for (int i = 0; i < izquierda.length; i++) {
            sb.append("| ");
            sb.append(obtenerCasillaFormateada(izquierda[i]));
            sb.append(" |");
            sb.append(" ".repeat(116)); // Espacio central
            sb.append("| ");
            sb.append(obtenerCasillaFormateada(derecha[i]));
            sb.append(" |\n");
        }

        // Mostrar fila INFERIOR (Sur)
        sb.append("| ");
        sb.append(obtenerCasillaFormateada(carcel)).append(" | ");
        sb.append(obtenerCasillaFormateada(solar5)).append(" | ");
        sb.append(obtenerCasillaFormateada(solar4)).append(" | ");
        sb.append(obtenerCasillaFormateada(suerte1)).append(" | ");
        sb.append(obtenerCasillaFormateada(solar3)).append(" | ");
        sb.append(obtenerCasillaFormateada(trans1)).append(" | ");
        sb.append(obtenerCasillaFormateada(imp1)).append(" | ");
        sb.append(obtenerCasillaFormateada(solar2)).append(" | ");
        sb.append(obtenerCasillaFormateada(caja1)).append(" | ");
        sb.append(obtenerCasillaFormateada(solar1)).append(" | ");
        sb.append(obtenerCasillaFormateada(salida)).append(" |\n");

        // Mostrar avatares
        sb.append(Valor.PURPLE).append("\nAVATARES EN EL TABLERO:").append(Valor.RESET).append("\n");
        boolean hayAvatares = false;

        // Recorrer todas las casillas del tablero
        for (ArrayList<Casilla> lado : posiciones) {
            for (Casilla casilla : lado) {
                if (casilla != null && !casilla.getAvatares().isEmpty()) {
                    hayAvatares = true;
                    sb.append("• ").append(casilla.getNombre()).append(" (Pos ").append(casilla.getPosicion()).append("): ");
                    for (Avatar avatar : casilla.getAvatares()) {
                        sb.append(avatar.getId()).append("(").append(avatar.getTipo()).append(") ");
                    }
                    sb.append("\n");
                }
            }
        }

        if (!hayAvatares) {
            sb.append("No hay avatares en el tablero.\n");
        }

        sb.append(Valor.CYAN).append("========================================================================================================").append(Valor.RESET).append("\n");

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



    // Método auxiliar para mostrar una casilla con color
    private String obtenerCasillaFormateada(Casilla casilla) {
        if (casilla == null) {
            System.out.print("NULL     ");
            return "NULL";
        }

        String color = Valor.RESET;
        String nombre = casilla.getNombre();

        // Asignar colores según tipo
        switch (casilla.getTipo()) {
            case "Solar":
                if (casilla.getGrupo() != null) {
                    String colorGrupo = casilla.getGrupo().getColorGrupo();
                    switch (colorGrupo) {
                        case "Naranja":
                            color = Valor.BG_ORANGE + Valor.BLACK;
                            break;
                        case "Celeste":
                            color = Valor.BG_CYAN + Valor.BLACK;
                            break;
                        case "Purpura":
                            color = Valor.BG_PURPLE + Valor.BLACK;
                            break;
                        case "Negro":
                            color = Valor.BG_BLACK + Valor.WHITE;
                            break;
                        case "Rojo":
                            color = Valor.BG_RED + Valor.BLACK;
                            break;
                        case "Amarillo":
                            color = Valor.BG_YELLOW + Valor.BLACK;
                            break;
                        case "Verde":
                            color = Valor.BG_GREEN + Valor.BLACK;
                            break;
                        case "Azul":
                            color = Valor.BG_BLUE + Valor.BLACK;
                            break;
                        default:
                            color = Valor.RESET;
                    }
                }
                break;
            case "Transporte":
                color = Valor.BG_GRAY + Valor.BLACK;
                break;
            case "Servicios":
                color = Valor.BG_GRAY + Valor.BLACK;
                break;
            case "Impuesto":
                color = Valor.BG_GRAY + Valor.BLACK;
                break;
            case "Suerte":
                color = Valor.BG_GRAY + Valor.BLACK;
                break;
            case "Comunidad":
                color = Valor.BG_GRAY + Valor.BLACK;
                break;
            case "Carcel":
                color = Valor.BG_GRAY + Valor.BLACK;
                break;
            case "Parking":
                color = Valor.BG_GRAY + Valor.BLACK;
                break;
            case "Salida":
                color = Valor.BG_GRAY + Valor.BLACK;
                break;
            case "IrCarcel":
                color = Valor.BG_GRAY + Valor.BLACK;
                break;
        }

        // Mostrar nombre completo (sin abreviar)
        return color + String.format("%-10s", nombre) + Valor.RESET;
    }

    // Getters
    public ArrayList<ArrayList<Casilla>> getPosiciones() { return posiciones; }
    public HashMap<String, Grupo> getGrupos() { return grupos; }
    public Jugador getBanca() { return banca; }
    public float getBoteParking() {
        return boteParking;
    }
}




