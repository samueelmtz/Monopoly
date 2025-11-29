package monopoly.carta;

import monopoly.casilla.Casilla;
import partida.Jugador;
import monopoly.Valor;
import monopoly.Tablero;

import java.util.ArrayList;

public abstract class Carta {
    // Atributos comunes
    private int id;
    private String descripcion;
    private String accion;

    public Carta(int id, String descripcion, String accion) {
        this.id = id;
        this.descripcion = descripcion;
        this.accion = accion;
    }

    // Método abstracto para ejecutar la acción (debe implementarse en subclases)
    public abstract void ejecutarAccion(Jugador jugador, Tablero tablero, ArrayList<Jugador> jugadores, Jugador banca);

    // Método auxiliar protegido para reutilizar lógica común
    protected boolean pasaPorSalida(int desde, int hasta) {
        return hasta < desde;
    }

    // Método protegido para ejecutar acciones comunes (las subclases pueden usarlo)
    protected void ejecutarAccionComun(Jugador jugador, Tablero tablero, ArrayList<Jugador> jugadores, Jugador banca) {
        System.out.println("Carta elegida: " + this.id);
        System.out.println("Descripción: " + this.descripcion);

        String accion = this.accion;

        if (accion.startsWith("avanzar:")) {
            int posicionDestino = Integer.parseInt(accion.split(":")[1]);
            int posicionActual = jugador.getAvatar().getLugar().getPosicion();

            if (pasaPorSalida(posicionActual, posicionDestino)) {
                jugador.sumarFortuna(Valor.SUMA_VUELTA);
                jugador.sumarPasarPorCasillaDeSalida(Valor.SUMA_VUELTA);
                System.out.printf("¡Has pasado por Salida y cobrado %,.0f€!\n", Valor.SUMA_VUELTA);
            }

            jugador.getAvatar().colocar(tablero.getPosiciones(), posicionDestino);

        } else if (accion.equals("irCarcel")) {
            jugador.encarcelar(tablero.getPosiciones());

        } else if (accion.startsWith("recibir:")) {
            float cantidad = Float.parseFloat(accion.split(":")[1]);
            jugador.sumarFortuna(cantidad);
            jugador.sumarPremiosInversionesOBote(cantidad);
            System.out.printf("¡Has recibido %,.0f€!\n", cantidad);

        } else if (accion.startsWith("pagarTodos:")) {
            float cantidad = Float.parseFloat(accion.split(":")[1]);
            boolean puedePagar = true;

            float totalAPagar = cantidad * (jugadores.size() - 1);
            if (jugador.getFortuna() < totalAPagar) {
                System.out.printf("No tienes suficiente dinero para pagar a todos los jugadores. Necesitas %,.0f€ pero tienes %,.0f€\n",
                        totalAPagar, jugador.getFortuna());
                puedePagar = false;
            }

            if (puedePagar) {
                System.out.printf("%s paga %,.0f€ a cada jugador:\n", jugador.getNombre(), cantidad);
                for (Jugador otro : jugadores) {
                    if (otro != jugador && otro != banca) {
                        jugador.restarFortuna(cantidad);
                        jugador.sumarPagoTasasEImpuestos(cantidad);
                        otro.sumarFortuna(cantidad);
                        System.out.printf("  - Paga %,.0f€ a %s\n", cantidad, otro.getNombre());
                    }
                }
            }

        } else if (accion.startsWith("retroceder:")) {
            int casillas = Integer.parseInt(accion.split(":")[1]);
            int posicionActual = jugador.getAvatar().getLugar().getPosicion();
            int nuevaPosicion = (posicionActual - casillas + 40) % 40;
            jugador.getAvatar().colocar(tablero.getPosiciones(), nuevaPosicion);
            System.out.println("Has retrocedido " + casillas + " casillas.");

        } else if (accion.startsWith("pagar:")) {
            float cantidadPago = Float.parseFloat(accion.split(":")[1]);
            if (jugador.getFortuna() >= cantidadPago) {
                jugador.restarFortuna(cantidadPago);
                jugador.sumarPagoTasasEImpuestos(cantidadPago);
                // tablero.añadirAlBote(cantidadPago); // Descomenta si existe este método
                System.out.printf("Has pagado %,.0f€\n", cantidadPago);
            } else {
                System.out.println("No tienes suficiente dinero para pagar.");
            }

        } else if (accion.equals("transporteCercano")) {
            int posicionActual = jugador.getAvatar().getLugar().getPosicion();
            String[] nombresTransporte = {"Trans1", "Trans2", "Trans3", "Trans4"};
            int[] posicionesTransporte = {6, 16, 26, 36};

            String transporteCercano = null;
            int menorDistancia = 50;

            for (int i = 0; i < posicionesTransporte.length; i++) {
                int distancia = (posicionesTransporte[i] - posicionActual + 40) % 40;
                if (distancia < menorDistancia && distancia > 0) {
                    menorDistancia = distancia;
                    transporteCercano = nombresTransporte[i];
                }
            }

            if (transporteCercano != null) {
                Casilla destino = tablero.encontrar_casilla(transporteCercano);
                if (destino != null) {
                    jugador.getAvatar().colocar(tablero.getPosiciones(), destino.getPosicion());
                    System.out.println("Te has movido a " + destino.getNombre());
                    destino.evaluarCasilla(jugador, banca, 0);
                }
            }

        } else if (accion.equals("irSalida")) {
            jugador.getAvatar().colocar(tablero.getPosiciones(), 1);
            jugador.sumarFortuna(Valor.SUMA_VUELTA);
            jugador.sumarPasarPorCasillaDeSalida(Valor.SUMA_VUELTA);
            System.out.printf("¡Has cobrado %,.0f€ por pasar por salida!\n", Valor.SUMA_VUELTA);
        }

        System.out.printf("Fortuna actual de %s: %,.0f€\n", jugador.getNombre(), jugador.getFortuna());
    }

    public static Carta obtenerSiguienteCarta(String tipo) {
        if (tipo == null) throw new IllegalArgumentException("Tipo de carta nulo");
        if (tipo.equalsIgnoreCase("Suerte")) {
            Suerte.inicializarMazo(); // garantiza inicialización si es necesario
            return Suerte.sacarCarta();
        } else if (tipo.equalsIgnoreCase("Comunidad") || tipo.equalsIgnoreCase("CajaComunidad")) {
            CajaComunidad.inicializarMazo();
            return CajaComunidad.sacarCarta();
        } else {
            throw new IllegalArgumentException("Tipo de carta desconocido: " + tipo);
        }
    }
    // Getters
    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getAccion() {
        return accion;
    }
}