package monopoly.interfaces;

import partida.Jugador;
import monopoly.casilla.Casilla;

public interface comando{
    void crearJugador(String nombre, String tipoAvatar);
    void turnoJugador();
    void descJugador(String[] partes);
    void descCasilla(String nombre);
    void lanzarDados(String valoresForzados);
    void comprar(String nombre);
    void salirCarcel();
    void listarVenta();
    void listarJugadores();
    void listarEdificios(String colorGrupo);
    void acabarTurno();
    void mostrarEstadisticas(String nombreJugador);
    void mostrarEstadisticasJuego();
    void edificar(String tipoEdificio);
    void venderEdificios(String tipoVenta, String nombreCasilla, int cantidadSolicitada);
    void hipotecarPropiedad(String nombreCasilla);
    void deshipotecarPropiedad(String nombreCasilla);
}
