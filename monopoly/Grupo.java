package monopoly;

import partida.*;
import java.util.ArrayList;


class Grupo {

    //Atributos
    private ArrayList<Casilla> miembros; //Casillas miembros del grupo.
    private String colorGrupo; //Color del grupo
    private int numCasillas; //Número de casillas del grupo.

    //Constructor vacío.
    public Grupo() {
        this.miembros = new ArrayList<>();
        this.numCasillas = 0;
    }

    /*Constructor para cuando el grupo está formado por DOS CASILLAS:
    * Requiere como parámetros las dos casillas miembro y el color del grupo.
     */
    public Grupo(Casilla cas1, Casilla cas2, String colorGrupo) {
        this.miembros = new ArrayList<>();
        this.colorGrupo = colorGrupo;
        this.numCasillas = 2;
        anhadirCasilla(cas1);
        anhadirCasilla(cas2);
    }

    /*Constructor para cuando el grupo está formado por TRES CASILLAS:
    * Requiere como parámetros las tres casillas miembro y el color del grupo.
     */
    public Grupo(Casilla cas1, Casilla cas2, Casilla cas3, String colorGrupo) {
        this.miembros = new ArrayList<>();
        this.colorGrupo = colorGrupo;
        this.numCasillas = 3;
        anhadirCasilla(cas1);
        anhadirCasilla(cas2);
        anhadirCasilla(cas3);

    }

    /* Método que añade una casilla al array de casillas miembro de un grupo.
    * Parámetro: casilla que se quiere añadir.
     */
    public void anhadirCasilla(Casilla miembro) {
        this.miembros.add(miembro);
        miembro.setGrupo(this);       //Establecer este grupo en la casilla añadida.
    }

    /*Método que comprueba si el jugador pasado tiene en su haber todas las casillas del grupo:
    * Parámetro: jugador que se quiere evaluar.
    * Valor devuelto: true si es dueño de todas las casillas del grupo, false en otro caso.
     */
    public boolean esDuenhoGrupo(Jugador jugador) {
        for (Casilla casilla : miembros) {
            if (casilla.getDuenho() != jugador) {
                return false; // Si alguna casilla no pertenece al jugador, retorna false
            }
        }
        return true; // Si todas las casillas pertenecen al jugador, retorna true
    }

    private void crearGrupos() {
        // Grupo Marrón (2 casillas) - Solar1, Solar2
        Grupo grupoMarron = new Grupo(
                encontrar_casilla("Solar1"),
                encontrar_casilla("Solar2"),
                "Marrón"
        );
        grupos.put("Marrón", grupoMarron);

        // Grupo Celeste (3 casillas) - Solar3, Solar4, Solar5
        Grupo grupoCeleste = new Grupo(
                encontrar_casilla("Solar3"),
                encontrar_casilla("Solar4"),
                encontrar_casilla("Solar5"),
                "Celeste"
        );
        grupos.put("Celeste", grupoCeleste);

        // Grupo Rosa (3 casillas) - Solar6, Solar7, Solar8
        Grupo grupoRosa = new Grupo(
                encontrar_casilla("Solar6"),
                encontrar_casilla("Solar7"),
                encontrar_casilla("Solar8"),
                "Rosa"
        );
        grupos.put("Rosa", grupoRosa);

        // Grupo Naranja (3 casillas) - Solar9, Solar10, Solar11
        Grupo grupoNaranja = new Grupo(
                encontrar_casilla("Solar9"),
                encontrar_casilla("Solar10"),
                encontrar_casilla("Solar11"),
                "Naranja"
        );
        grupos.put("Naranja", grupoNaranja);

        // Grupo Rojo (3 casillas) - Solar12, Solar13, Solar14
        Grupo grupoRojo = new Grupo(
                encontrar_casilla("Solar12"),
                encontrar_casilla("Solar13"),
                encontrar_casilla("Solar14"),
                "Rojo"
        );
        grupos.put("Rojo", grupoRojo);

        // Grupo Amarillo (3 casillas) - Solar15, Solar16, Solar17
        Grupo grupoAmarillo = new Grupo(
                encontrar_casilla("Solar15"),
                encontrar_casilla("Solar16"),
                encontrar_casilla("Solar17"),
                "Amarillo"
        );
        grupos.put("Amarillo", grupoAmarillo);

        // Grupo Verde (3 casillas) - Solar18, Solar19, Solar20
        Grupo grupoVerde = new Grupo(
                encontrar_casilla("Solar18"),
                encontrar_casilla("Solar19"),
                encontrar_casilla("Solar20"),
                "Verde"
        );
        grupos.put("Verde", grupoVerde);

        // Grupo Azul (2 casillas) - Solar21, Solar22
        Grupo grupoAzul = new Grupo(
                encontrar_casilla("Solar21"),
                encontrar_casilla("Solar22"),
                "Azul"
        );
        grupos.put("Azul", grupoAzul);
    }


    //Getters y setters.
    public ArrayList<Casilla> getMiembros() {
        return miembros;
    }
    public String getColorGrupo() {
        return colorGrupo;
    }
    public int getNumCasillas() {
        return numCasillas;
    }
    public void setColorGrupo(String colorGrupo) {
        this.colorGrupo = colorGrupo;
    }
    public void setNumCasillas(int numCasillas) {
        this.numCasillas = numCasillas;
    }
}
