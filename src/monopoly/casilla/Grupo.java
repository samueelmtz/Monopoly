package monopoly.casilla;

import partida.*;
import monopoly.casilla.propiedad.*;
import java.util.ArrayList;

public class Grupo extends Casilla {
    private ArrayList<Propiedad> propiedades;
    private String colorGrupo;
    private int numCasillas;

    /**
     * Constructor para un grupo con dos propiedades.
     *
     * @param prop1      miembro 1
     * @param prop2      miembro 2
     * @param colorGrupo Color del grupo
     */
    public Grupo(Propiedad prop1, Propiedad prop2, String colorGrupo) {
        this.propiedades = new ArrayList<>();
        this.colorGrupo = colorGrupo;
        this.propiedades.add(prop1);
        this.propiedades.add(prop2);
        this.numCasillas = 2;
    }

    /**
     * Constructor para un grupo con tres propiedades.
     *
     * @param prop1      Solar miembro 1
     * @param prop2      Solar miembro 2
     * @param prop3      Solar miembro 3
     * @param colorGrupo Color del grupo
     */
    public Grupo(Propiedad prop1, Propiedad prop2, Propiedad prop3, String colorGrupo) {
        this.propiedades = new ArrayList<>();
        this.colorGrupo = colorGrupo;
        this.propiedades.add(prop1);
        this.propiedades.add(prop2);
        this.propiedades.add(prop3);
        this.numCasillas = 3;
    }

    public void anhadirPropiedad(Propiedad propiedad) {
        this.propiedades.add(propiedad);
        this.numCasillas++;
    }

    // Implementación de métodos abstractos
    @Override
    public boolean estaAvatar(Avatar avatar) {
        return false; // Los grupos no son casillas físicas
    }

    @Override
    public String toString() {
        return "Grupo{" + "color='" + colorGrupo + "', propiedades=" + propiedades.size() + "}";
    }

    @Override
    public void infoCasilla() {
        System.out.println("Grupo: " + getNombre() + " - Color: " + colorGrupo);
        System.out.println("Propiedades del grupo:");
        for (Propiedad prop : propiedades) {
            System.out.println("  - " + prop.getNombre());
        }
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        return false; // Los grupos no se evalúan
    }

    @Override
    public boolean esTipoComprable() {
        return false;
    }

    // GETTERS Y SETTERS
    public ArrayList<Propiedad> getPropiedades() {
        return propiedades;
    }

    public String getColor() {
        return colorGrupo;
    }

    public void setColor(String color) {
        this.colorGrupo = color;
    }
}