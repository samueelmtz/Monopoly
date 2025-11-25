package monopoly;

public class Carta {
    private int id; //Identificador para cada tipo de carta
    private String tipo; //Tipo de carta
    private String descripcion; //Descripción de uso de la carta
    private String accion; //Efecto de la carta

    //CONSTRUCTOR PRINCIPAL
    public Carta(int id, String tipo, String descripcion, String accion) {
        this.id = id;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.accion = accion;
    }


    //GETTERS
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
