package monopoly;

import monopoly.casilla.*;
import monopoly.casilla.accion.CajaComunidad;
import monopoly.casilla.accion.Parking;
import monopoly.casilla.accion.Suerte;
import partida.*;
import java.util.ArrayList;
import java.util.HashMap;
import monopoly.casilla.propiedad.Solar;
import monopoly.casilla.propiedad.Transporte;
import monopoly.casilla.propiedad.Servicio;

public class Tablero {
    //Atributos
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

        Casilla parking = encontrar_casilla("Parking");
        if (parking != null) {
            if (parking instanceof Parking) {
                ((Parking) parking).añadirAlBote(cantidad);
                Juego.consola.imprimir("Se han añadido %,.0f€ al bote del Parking. Bote actual: %,.0f€\n",
                        cantidad, this.boteParking);
            } else {
                Juego.consola.imprimir("Advertencia: La casilla Parking no es del tipo correcto");
            }
        } else {
            Juego.consola.imprimir("Error: No se encontró la casilla Parking");
        }
    }
    // Método para que un jugador reclame el bote
    public float reclamarBote(Jugador jugador) {
        float boteActual = this.boteParking;
        if (boteActual > 0) {
            jugador.sumarFortuna(boteActual);
            jugador.sumarPremiosInversionesOBote(boteActual);

            // Resetear el valor de la casilla Parking usando sumarValor
            Casilla parking = encontrar_casilla("Parking");
            if (parking != null) {
                // Restar el valor actual para ponerlo a 0
                ((Parking) parking).añadirAlBote(-boteActual);
            }
            Juego.consola.imprimir("¡%s ha ganado el bote del Parking: %,.0f€!\n", jugador.getNombre(), boteActual);
            this.boteParking = 0f; // Resetear el bote
        } else {
            Juego.consola.imprimir("El bote del Parking está vacío.");
        }
        return boteActual;
    }

    private void crearGrupos() {
        try {
            // Crear los grupos y añadirlos al HashMap
            crearGrupo("Naranja", "Solar1", "Solar2");
            crearGrupo("Celeste", "Solar3", "Solar4", "Solar5");
            crearGrupo("Purpura", "Solar6", "Solar7", "Solar8");
            crearGrupo("Negro", "Solar9", "Solar10", "Solar11");
            crearGrupo("Rojo", "Solar12", "Solar13", "Solar14");
            crearGrupo("Amarillo", "Solar15", "Solar16", "Solar17");
            crearGrupo("Verde", "Solar18", "Solar19", "Solar20");
            crearGrupo("Azul", "Solar21", "Solar22");
        } catch (ClassCastException e) {
            throw new RuntimeException("Error de tipo: Asegúrate de que las casillas son de tipo Propiedad", e);
        } catch (RuntimeException e) {
            System.err.println("Error al crear los grupos: " + e.getMessage());
            throw e;
        }
    }

    private void crearGrupo(String color, String... nombresCasillas) {
        ArrayList<Propiedad> propiedadesGrupo = new ArrayList<>();

        for (String nombre : nombresCasillas) {
            Casilla casilla = encontrar_casilla(nombre);
            if (casilla == null) {
                Juego.consola.imprimir("No se encontró la casilla: " + nombre);
            }
            if (!(casilla instanceof Propiedad)) {
                Juego.consola.imprimir("La casilla " + nombre + " no es una propiedad");
            }
            propiedadesGrupo.add((Propiedad) casilla);
        }

        // Crear el grupo con el número correcto de propiedades
        switch (propiedadesGrupo.size()) {
            case 2:
                grupos.put(color, new Grupo(
                        propiedadesGrupo.get(0),
                        propiedadesGrupo.get(1),
                        color
                ));
                break;
            case 3:
                grupos.put(color, new Grupo(
                        propiedadesGrupo.get(0),
                        propiedadesGrupo.get(1),
                        propiedadesGrupo.get(2),
                        color
                ));
                break;
            default:
                throw new RuntimeException("Número incorrecto de propiedades para el grupo " + color);
        }
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


    //Método que inserta las casillas del lado sur.
    private void insertarLadoSur() {
        ArrayList<Casilla> ladoSur = this.posiciones.get(0);

        // Posiciones 1-10 con alquileres según PDF
        ladoSur.add(new Especial("Salida", 1, banca, "Salida"));
        // Propiedades (solares)
        ladoSur.add(new Solar("Solar1", 2, 600000, 20000, banca, null));
        ladoSur.add(new CajaComunidad("Caja1", 3, banca));
        ladoSur.add(new Solar("Solar2", 4, 600000, 40000, banca, null));
        ladoSur.add(new Impuesto("Imp1", 5, banca, 2000000));
        ladoSur.add(new Transporte("Trans1", 6, 500000, banca)); // Transporte
        ladoSur.add(new Solar("Solar3", 7, 1000000, 60000, banca, null));
        ladoSur.add(new Suerte("Suerte1", 8, banca));
        ladoSur.add(new Solar("Solar4", 9, 1000000, 60000, banca, null));
        ladoSur.add(new Solar("Solar5", 10, 1200000, 80000, banca, null));
    }

    //Método que inserta las casillas del lado oeste.
    private void insertarLadoOeste() {
        ArrayList<Casilla> ladoOeste = this.posiciones.get(1);

        // Posiciones 11-20 con alquileres según PDF
        ladoOeste.add(new Especial("Carcel", 11, banca, "Carcel"));
        ladoOeste.add(new Solar("Solar6",12, 1400000, 100000, banca, null));
        ladoOeste.add(new Servicio("Serv1", 13, 500000, banca));
        ladoOeste.add(new Solar("Solar7", 14, 1400000, 100000, banca, null));
        ladoOeste.add(new Solar("Solar8", 15, 1600000, 120000, banca, null));
        ladoOeste.add(new Transporte("Trans2", 16, 500000,banca));
        ladoOeste.add(new Solar("Solar9", 17, 1800000, 140000, banca, null));
        ladoOeste.add(new CajaComunidad("Caja2", 18, banca));
        ladoOeste.add(new Solar("Solar10", 19, 1800000, 140000, banca, null));
        ladoOeste.add(new Solar("Solar11", 20, 2200000, 160000, banca, null));
    }

    //Método que inserta las casillas del lado norte.
    private void insertarLadoNorte() {
        ArrayList<Casilla> ladoNorte = this.posiciones.get(2);

        // Posiciones 21-30 con alquileres según PDF
        ladoNorte.add(new Parking("Parking", 21, banca));
        ladoNorte.add(new Solar("Solar12", 22, 2200000, 180000, banca, null));
        ladoNorte.add(new Suerte("Suerte3", 23, banca));
        ladoNorte.add(new Solar("Solar13", 24, 2200000, 180000, banca, null));
        ladoNorte.add(new Solar("Solar14", 25, 2400000, 200000, banca, null));
        ladoNorte.add(new Transporte("Trans3", 26, 500000, banca));
        ladoNorte.add(new Solar("Solar15", 27, 2600000, 220000, banca, null));
        ladoNorte.add(new Solar("Solar16", 28, 2600000, 220000, banca, null));
        ladoNorte.add(new Servicio("Serv2", 29, 500000, banca));
        ladoNorte.add(new Solar("Solar17", 30, 2800000, 240000, banca, null));
    }


    //Método que inserta casillas del lado este.
    private void insertarLadoEste() {
        ArrayList<Casilla> ladoEste = this.posiciones.get(3);

        // Posiciones 31-40 con alquileres según PDF
        ladoEste.add(new Especial("IrCarcel", 31, banca, "IrCarcel"));
        ladoEste.add(new Solar("Solar18", 32, 3000000, 260000, banca, null));
        ladoEste.add(new Solar("Solar19", 33, 3000000, 260000, banca, null));
        ladoEste.add(new CajaComunidad("Caja3", 34, banca));
        ladoEste.add(new Solar("Solar20", 35, 3200000, 280000, banca, null));
        ladoEste.add(new Transporte("Trans4", 36, 500000, banca));
        ladoEste.add(new Suerte("Suerte2", 37, banca));
        ladoEste.add(new Solar("Solar21", 38, 3500000, 350000, banca, null));
        ladoEste.add(new Impuesto("Imp2", 39, banca,2000000));
        ladoEste.add(new Solar("Solar22", 40, 4000000, 500000, banca, null));
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
            return String.format("%-10s", "NULL");
        }

        // Color por defecto para todas las casillas no propiedad
        String color = Valor.BG_GRAY + Valor.BLACK;
        String nombre = casilla.getNombre();
        
        // Limitar el nombre a 8 caracteres para dejar espacio para los avatares
        String nombreRecortado = nombre.length() > 8 ? nombre.substring(0, 8) : nombre;

        // Obtener avatares en esta casilla (máximo 2 caracteres)
        String avataresStr = "";
        if (!casilla.getAvatares().isEmpty()) {
            StringBuilder avataresBuilder = new StringBuilder();
            int maxAvatares = Math.min(2, casilla.getAvatares().size());
            for (int i = 0; i < maxAvatares; i++) {
                avataresBuilder.append(casilla.getAvatares().get(i).getId());
            }
            avataresStr = avataresBuilder.toString();
        }

        // Determinar el color según el tipo de casilla
        // Solo las propiedades tienen colores específicos, el resto se quedan en gris
        if (casilla instanceof Propiedad) {
            Propiedad propiedad = (Propiedad) casilla;
            if (propiedad.getGrupo() != null) {
                String colorGrupo = propiedad.getGrupo().getColorGrupo();
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
                        // Si no coincide con ningún color conocido, se queda en gris
                        break;
                }
            }
        }
        
        // Construir la cadena de salida con el color, nombre y avatares, todo en 10 caracteres
        String contenido = String.format("%-8s%-2s", nombreRecortado, avataresStr);
        return color + contenido.substring(0, Math.min(10, contenido.length())) + Valor.RESET;
    }
    
    // Getters
    public ArrayList<ArrayList<Casilla>> getPosiciones() {
        return posiciones;
    }
    
    public HashMap<String, Grupo> getGrupos() {
        return grupos;
    }
    
    public Jugador getBanca() {
        return banca;
    }
    
    public float getBoteParking() { return boteParking;}
}