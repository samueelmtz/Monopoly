package excepciones;

//Nivel 3 de jerarquia - Cuando se intenta vender un edificio que no existe en una propiedad
public class ExcepcionEdificioNoExistente extends ExcepcionEdificacion {

    private final String idEdificio;
    private final String nombrePropiedad;

    public ExcepcionEdificioNoExistente(String idEdificio, String propiedad) {
        super("El edificio '" + idEdificio + "' no existe en la propiedad '" + propiedad + "'");
        this.idEdificio = idEdificio;
        this.nombrePropiedad = propiedad;
    }

    public String getIdEdificio() {
        return idEdificio;
    }

    public String getNombrePropiedad() {
        return nombrePropiedad;
    }
}