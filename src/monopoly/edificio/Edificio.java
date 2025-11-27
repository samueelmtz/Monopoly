// monopoly/edificio/Edificio.java
package monopoly.edificio;

import partida.Jugador;
import monopoly.casilla.propiedad.Solar;

public abstract class Edificio {
    // Atributos COMUNES a todos los edificios - PRIVADOS
    private String id;
    private String tipoEdificio;
    private Jugador duenho;
    private Solar solar;
    private float coste;

    // Constructores
    public Edificio(String tipoEdificio, Solar solar, float coste) {
        this.tipoEdificio = tipoEdificio;
        this.solar = solar;
        this.duenho = solar.getDuenho();
        this.coste = coste;
        this.id = generarID(tipoEdificio);
    }

    public Edificio() {
        // Constructor vacío
    }

    // MÉTODO ABSTRACTO requerido por el PDF
    public abstract void accion();

    // MÉTODOS COMUNES
    protected String generarID(String tipo) {
        // Contadores estáticos para IDs únicos
        switch (tipo) {
            case "casa":
                return "casa-" + (Casa.getContadorCasas() + 1);
            case "hotel":
                return "hotel-" + (Hotel.getContadorHoteles() + 1);
            case "piscina":
                return "piscina-" + (Piscina.getContadorPiscinas() + 1);
            case "pista_deporte":
                return "pista_deporte-" + (PistaDeporte.getContadorPistas() + 1);
            default:
                return "edificio-unknown";
        }
    }

    // GETTERS Y SETTERS
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTipoEdificio() { return tipoEdificio; }
    public void setTipoEdificio(String tipoEdificio) { this.tipoEdificio = tipoEdificio; }
    public Jugador getDuenho() { return duenho; }
    public void setDuenho(Jugador duenho) { this.duenho = duenho; }
    public Solar getSolar() { return solar; }
    public void setSolar(Solar solar) { this.solar = solar; }
    public float getCoste() { return coste; }
    public void setCoste(float coste) { this.coste = coste; }

    @Override
    public String toString() {
        return String.format("Edificio{id='%s', tipo='%s', solar='%s', coste=%,.0f€}",
                id, tipoEdificio, solar.getNombre(), coste);
    }
}

