package monopoly.casilla;

import monopoly.casilla.Propiedad;
import partida.Jugador;
import java.util.ArrayList;

public class Grupo {
    private ArrayList<Propiedad> propiedades;
    private String colorGrupo;
    private int numCasillas;

    /**
     * Constructor para un grupo con dos propiedades.
     */
    public Grupo(Propiedad prop1, Propiedad prop2, String colorGrupo) {
        this.propiedades = new ArrayList<>();
        this.colorGrupo = colorGrupo;
        this.propiedades.add(prop1);
        this.propiedades.add(prop2);
        this.numCasillas = 2;

        // Establecer este grupo en las propiedades
        prop1.setGrupo(this);
        prop2.setGrupo(this);
    }

    /**
     * Constructor para un grupo con tres propiedades.
     */
    public Grupo(Propiedad prop1, Propiedad prop2, Propiedad prop3, String colorGrupo) {
        this.propiedades = new ArrayList<>();
        this.colorGrupo = colorGrupo;
        this.propiedades.add(prop1);
        this.propiedades.add(prop2);
        this.propiedades.add(prop3);
        this.numCasillas = 3;

        // Establecer este grupo en las propiedades
        prop1.setGrupo(this);
        prop2.setGrupo(this);
        prop3.setGrupo(this);
    }

    public void anhadirPropiedad(Propiedad propiedad) {
        this.propiedades.add(propiedad);
        this.numCasillas++;
        propiedad.setGrupo(this);
    }

    /**
     * Verifica si un jugador tiene todas las propiedades del grupo.
     */
    public boolean jugadorTieneGrupoCompleto(Jugador jugador) {
        for (Propiedad propiedad : propiedades) {
            if (!propiedad.perteneceAJugador(jugador)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Muestra información del grupo.
     */
    public void mostrarInfo() {
        System.out.println("{");
        System.out.println("    Grupo: " + colorGrupo);
        System.out.println("    Número de propiedades: " + numCasillas);
        System.out.println("    Propiedades: [");
        for (Propiedad prop : propiedades) {
            System.out.println("        " + prop.getNombre() +
                    " (Dueño: " + (prop.getDuenho() != null ? prop.getDuenho().getNombre() : "Banca") + ")");
        }
        System.out.println("    ]");
        System.out.println("}");
    }

    // GETTERS Y SETTERS
    public ArrayList<Propiedad> getPropiedades() {
        return propiedades;
    }

    public String getColorGrupo() {
        return colorGrupo;
    }

    public void setColorGrupo(String colorGrupo) {
        this.colorGrupo = colorGrupo;
    }

    public int getNumCasillas() {
        return numCasillas;
    }

    @Override
    public String toString() {
        return String.format("Grupo{color='%s', propiedades=%d}", colorGrupo, numCasillas);
    }
}