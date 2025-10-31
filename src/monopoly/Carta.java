package monopoly;

public class Carta {
    private int id;
    private String tipo;
    private String descripcion;
    private String accion;

    public Carta(int id, String tipo, String descripcion, String accion) {
        this.id = id;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.accion = accion;
    }

    //Getters
    public int getId() {
        return id;
    }
    public String getTipo() {
        return tipo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public String getAccion() {
        return accion;
    }
}
