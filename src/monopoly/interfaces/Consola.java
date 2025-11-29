package monopoly.interfaces;

public interface Consola {
    void imprimir(String mensaje);
    String leer(String descripcion);

    // NUEVO MÉTODO para printf
    default void imprimir(String formato, Object... args) {
        String mensaje = String.format(formato, args);
        imprimir(mensaje);
    }
}
