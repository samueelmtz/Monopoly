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
    //public boolean esDuenhoGrupo(Jugador jugador) {}//No se usa en esta entrega

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