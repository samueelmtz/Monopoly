package partida;

import java.util.ArrayList;

import monopoly.casilla.Propiedad;
import excepciones.ExcepcionAccionNoPermitida;
import excepciones.ExcepcionTratoNoEncontrado;
import monopoly.edificio.Edificio;
import monopoly.casilla.Casilla;
import monopoly.*;


public class Jugador {

    //ATRIBUTOS
    private final String nombre; //Nombre del jugador
    private Avatar avatar; //Avatar que tiene en la partida.
    private float fortuna; //Dinero que posee.
    private boolean enCarcel; //Será true si el jugador está en la carcel
    private int tiradasCarcel; //Cuando está en la carcel, contará las tiradas sin éxito que ha hecho allí para intentar salir (se usa para limitar el número de intentos).
    private int vueltas; //Cuenta las vueltas dadas al tablero.
    private ArrayList<Casilla> propiedades; //Propiedades que posee el jugador.
    private final ArrayList<Edificio> edificios; //Edificios que posee el jugador
    private int vecesEnCarcel; //Contador del número de turnos en la cárcel
    private float dineroInvertido;
    private float pagoTasasEImpuestos;
    private float pagoDeAlquileres;
    private float cobroDeAlquileres;
    private float pasarPorCasillaDeSalida;
    private float premiosInversionesBote;
    private ArrayList<Tratos> tratosPendientes;
    private boolean yaTiroEsteTurno; //Indica si el jugador ya ha tirado los dados en el turno actual en la carcel
    private int turnosEnCarcel;
    private Jugador acreedor;
    private boolean enBancarrota;

    //CONSTRUCTORES

    //Constructor vacío. Se usará para crear la banca.
    public Jugador() {
        this.nombre = "Banca";
        this.fortuna = Valor.FORTUNA_BANCA;
        this.enCarcel = false;
        this.tiradasCarcel = 0;
        this.vueltas = 0;
        this.propiedades = new ArrayList<>();
        this.edificios = new ArrayList<>();
        this.dineroInvertido = 0;
        this.pagoTasasEImpuestos = 0;
        this.pagoDeAlquileres = 0;
        this.cobroDeAlquileres = 0;
        this.pasarPorCasillaDeSalida = 0;
        this.premiosInversionesBote = 0;
    }

    /*Constructor principal. Requiere parámetros:
     * Nombre del jugador, tipo del avatar que tendrá, casilla en la que empezará y ArrayList de
     * avatares creados (usado para dos propósitos: evitar que dos jugadores tengan el mismo nombre y
     * que dos avatares tengan mismo ID). Desde este constructor también se crea el avatar.
     */
    public Jugador(String nombre, String tipoAvatar, Casilla inicio, ArrayList<Avatar> avCreados) {
        this.nombre = nombre;
        this.avatar = new Avatar(tipoAvatar, this, inicio, avCreados);
        this.fortuna = Valor.FORTUNA_INICIAL;
        this.enCarcel = false;
        this.tiradasCarcel = 0;
        this.vueltas = 0;
        this.propiedades = new ArrayList<>();
        this.edificios = new ArrayList<>();
        this.dineroInvertido = 0;
        this.pagoTasasEImpuestos = 0;
        this.pagoDeAlquileres = 0;
        this.cobroDeAlquileres = 0;
        this.pasarPorCasillaDeSalida = 0;
        this.premiosInversionesBote = 0;
        this.tratosPendientes = new ArrayList<>();
        this.yaTiroEsteTurno = false;
        this.turnosEnCarcel = 0;
        this.enBancarrota = false;
    }

    //Otros métodos:
    //Método para añadir una propiedad al jugador. Como parámetro, la casilla a añadir.
    public void anhadirPropiedad(Casilla casilla) {
        if (!this.propiedades.contains(casilla)) {
            propiedades.add(casilla);
        }
    }

    //Método para eliminar una propiedad del arraylist de propiedades de jugador.
    public void eliminarPropiedad(Casilla casilla) {
        if (this.propiedades.contains(casilla)) {
            propiedades.remove(casilla);
        }
    }

    //Método para añadir un edificio a un jugador
    public void anhadirEdificio(Edificio edificio) {
        if (!this.edificios.contains(edificio)) {
            edificios.add(edificio);
        }
    }

    //Método para añadir fortuna a un jugador
    //Como parámetro se pide el valor a añadir.
    public void sumarFortuna(float valor) {
        this.fortuna += valor;
    }

    //Método para restar fortuna a un jugador
    //Como parámetro se pide el valor a añadir.
    public void restarFortuna(float valor) {
        this.fortuna -= valor;
    }

    /*Método para establecer al jugador en la cárcel.
     * Se requiere disponer de las casillas del tablero para ello (por eso se pasan como parámetro).*/
    public void encarcelar(ArrayList<ArrayList<Casilla>> pos) {
        this.enCarcel = true;
        this.tiradasCarcel = 0;
        this.yaTiroEsteTurno = false;
        this.turnosEnCarcel = 0;

        // Buscar la casilla de la cárcel por nombre
        for (ArrayList<Casilla> lado : pos) {
            for (Casilla cas : lado) {
                if (cas.getNombre().equals("Carcel")) {
                    this.avatar.colocar(pos, cas.getPosicion());
                    Juego.consola.imprimir(this.nombre + " ha sido enviado a la cárcel.");
                    this.vecesEnCarcel++;
                    return;
                }
            }
        }
        Juego.consola.imprimir("Error: No se encontró la casilla Carcel");
    }

    //Método para iniciar el turno de la carcel
    public void iniciarTurnoCarcel() {
        if (!enCarcel) return;

        this.yaTiroEsteTurno = false;
        this.turnosEnCarcel++;

        Juego.consola.imprimir("%s está en la cárcel (turno %d/3)",
                nombre, turnosEnCarcel);

        if (turnosEnCarcel >= 3) {
            Juego.consola.imprimir("¡Última oportunidad! Si no sales con dobles, deberás pagar 500.000€.");
        }
    }

    //Método para intentar salir de la carcel con dados
    public boolean intentarSalirCarcelConDados(int dado1, int dado2) throws ExcepcionAccionNoPermitida {
        if (!enCarcel) {
            throw new ExcepcionAccionNoPermitida(
                    "intentar salir cárcel",
                    "estado del jugador",
                    nombre + " no está en la cárcel"
            );
        }

        if (yaTiroEsteTurno) {
            throw new ExcepcionAccionNoPermitida(
                    "lanzar dados en cárcel",
                    "turno actual",
                    "ya has tirado los dados este turno. Usa 'salir carcel' para pagar o 'acabar turno'"
            );
        }

        this.yaTiroEsteTurno = true;
        this.tiradasCarcel++;

        Juego.consola.imprimir("%s intenta dobles: %d y %d", nombre, dado1, dado2);

        if (dado1 == dado2) {
            salirDeCarcelGratis();
            return true;
        }


        if (turnosEnCarcel >= 3) {
            Juego.consola.imprimir("¡3 turnos en cárcel! %s debe pagar 500.000€.", nombre);
        }

        return false;
    }

    //Método para salir de la cárcel
    public boolean salirDeCarcel() {
        try {
            if (!enCarcel) {
                throw new ExcepcionAccionNoPermitida("salir cárcel", "estado del jugador",
                        nombre + " no está en la cárcel"
                );
            }

            if (this.fortuna >= Valor.SALIR_CARCEL) {
                this.restarFortuna(Valor.SALIR_CARCEL);
                this.sumarPagoTasasEImpuestos(Valor.SALIR_CARCEL);

                this.enCarcel = false;
                this.tiradasCarcel = 0;
                this.turnosEnCarcel = 0;
                this.yaTiroEsteTurno = false;

                Juego.consola.imprimir("%s ha pagado %,.0f€ para salir de la cárcel.",
                        nombre, Valor.SALIR_CARCEL);
                Juego.consola.imprimir("Fortuna actual: %,.0f€", fortuna);
                return true;
            } else {
                throw new ExcepcionAccionNoPermitida(
                        "salir cárcel",
                        "fondos disponibles",
                        nombre + " no tiene suficientes fondos (necesita " + Valor.SALIR_CARCEL +
                                "€, tiene " + fortuna + "€)"
                );
            }
        } catch (ExcepcionAccionNoPermitida e) {
            Juego.consola.imprimir("ERROR: " + e.getMessage());
            return false;
        }
    }

    //Método para salir de la cárcel gratis (por ejemplo al sacar dobles)
    private void salirDeCarcelGratis() {
        this.enCarcel = false;
        this.tiradasCarcel = 0;
        this.turnosEnCarcel = 0;
        this.yaTiroEsteTurno = false;
    }

    //Método que verifica que acciones se puedes realizar en la cárcel
    public void verificarAccionPermitidaEnCarcel(String comandoCompleto) throws ExcepcionAccionNoPermitida {
        if (!enCarcel) return;

        // Extraer acción básica (primera palabra del comando)
        String[] partes = comandoCompleto.split(" ");
        String accion = partes[0].toLowerCase();

        // Definir acciones bloqueadas en cárcel
        switch(accion) {
            case "comprar":
            case "edificar":
            case "vender":
            case "hipotecar":
            case "deshipotecar":
            case "trato":
            case "aceptar":
            case "eliminar":
                throw new ExcepcionAccionNoPermitida(
                        accion,
                        "estado del jugador",
                        nombre + " no puede realizar esta acción mientras está en la cárcel. " +
                                "Acciones permitidas: 'salir carcel', 'lanzar dados' (1 vez/turno), 'acabar turno'"
                );

            case "lanzar":
                // Verificar si ya tiró este turno (se maneja en intentarSalirCarcelConDados)
                break;

            // "salir", "acabar", "usar", "describir", "estadisticas", "listar", "tratos", "ver"
            // están permitidos
        }
    }

    //Método que verifica si se tiene que pagar para salir de la cárcel
    public boolean debePagarForzosamenteCarcel() {
        return enCarcel && turnosEnCarcel >= 3;
    }

    //Método que verifica si el jugador está en la cárcel
    public boolean isEnCarcel() {
        return enCarcel;
    }

    //Método que añade dinero invertido
    public void sumarDineroInvertido(float cantidad) {
        this.dineroInvertido += cantidad;
    }

    //Método que suma pago de tasas e impuestos
    public void sumarPagoTasasEImpuestos(float cantidad) {
        this.pagoTasasEImpuestos += cantidad;
    }

    //Método que suma pago de alquileres
    public void sumarPagoDeAlquileres(float cantidad) {
        this.pagoDeAlquileres += cantidad;
    }

    //Método que suma cobro de alquileres
    public void sumarCobroDeAlquileres(float cantidad) {
        this.cobroDeAlquileres += cantidad;
    }

    //Método que suma pasar por casilla de salida
    public void sumarPasarPorCasillaDeSalida(float cantidad) {
        this.pasarPorCasillaDeSalida += cantidad;
    }

    //Método que suma premios de inversiones o bote
    public void sumarPremiosInversionesOBote(float cantidad) {
        this.premiosInversionesBote += cantidad;
    }

    // Métodos bancarrota
    public void declararBancarrota(float deuda, Jugador acreedor) {

        this.enBancarrota = true;
        this.acreedor = acreedor;

        Juego.consola.imprimir("\n BANCARROTA ");
        Juego.consola.imprimir("%s no puede pagar %,.0f€ y declara bancarrota.",
                nombre, deuda);

        if (acreedor == null) {
            Juego.consola.imprimir("Todas las propiedades pasan a la BANCA.");
            transferirPropiedadesABanca();
        } else {
            Juego.consola.imprimir("Todas las propiedades pasan a %s.", acreedor.getNombre());
            transferirPropiedadesAAcreedor(acreedor);
        }

    }

    public void declararBancarrotaPorCarcel() {
        float deudaCarcel = 500000;
        declararBancarrota(deudaCarcel, null); // A la banca
    }

    //Método que declara bancarrota por no poder pagar alquiler a otro jugador
    public void declararBancarrotaPorAlquiler(float alquiler, Jugador propietario) {
        declararBancarrota(alquiler, propietario);
    }

    // Método que declara bancarrota por no poder pagar impuestos
    public void declararBancarrotaPorImpuesto(float impuesto) {
        declararBancarrota(impuesto, null); // A la banca
    }

    //Método que transfiere todas las propiedades a la banca
    private void transferirPropiedadesABanca() {
        if (propiedades.isEmpty()) {
            Juego.consola.imprimir("%s no tenía propiedades.", nombre);
            return;
        }

        Juego.consola.imprimir("Propiedades transferidas a la BANCA:");
        for (Casilla casilla : new ArrayList<>(propiedades)) {
            if (casilla instanceof Propiedad) {
                Propiedad propiedad = (Propiedad) casilla;
                propiedad.setDuenho(null);
                Juego.consola.imprimir("  • %s (valor: %,.0f€)",
                        propiedad.getNombre(), propiedad.getValor());
            }
        }

        // Vaciar lista de propiedades
        propiedades.clear();
    }

    //Método que transfiere todas las propiedades a un acreedor
    private void transferirPropiedadesAAcreedor(Jugador acreedor) {
        if (propiedades.isEmpty()) {
            Juego.consola.imprimir("%s no tenía propiedades para transferir.", nombre);
            return;
        }

        Juego.consola.imprimir("Propiedades transferidas a %s:", acreedor.getNombre());
        for (Casilla casilla : new ArrayList<>(propiedades)) {
            if (casilla instanceof Propiedad) {
                Propiedad propiedad = (Propiedad) casilla;
                propiedad.setDuenho(acreedor);
                acreedor.anadirPropiedad(propiedad);
                Juego.consola.imprimir("  • %s (valor: %,.0f€)", propiedad.getNombre(), propiedad.getValor());
            }
        }

        // Vaciar lista de propiedades
        propiedades.clear();
    }

    //Método que verifica si el jugador puede hipotecar propiedades para conseguir cierta cantidad
    public boolean puedeHipotecarAlgoParaPagar(float cantidadNecesaria) {
        if (cantidadNecesaria <= 0) return true;

        // Sumar dinero que podría obtener hipotecando
        float totalPotencial = 0;

        for (Casilla casilla : propiedades) {
            if (casilla instanceof Propiedad) {
                Propiedad propiedad = (Propiedad) casilla;
                // Verificar si es hipotecable y no está ya hipotecada
                if (propiedad.esHipotecable() && !propiedad.isHipotecada()) {
                    totalPotencial += propiedad.getValorHipoteca();

                    // Si ya alcanzamos la cantidad necesaria, no seguir contando
                    if (totalPotencial >= cantidadNecesaria) {
                        return true;
                    }
                }
            }
        }

        // Verificar si con su fortuna + lo que puede hipotecar alcanza
        return (this.fortuna + totalPotencial) >= cantidadNecesaria;
    }

    // Método para añadir una propiedad
    public void anadirPropiedad(Propiedad propiedad) {
        if (propiedad == null) return;
        if (this.propiedades == null) {
            this.propiedades = new ArrayList<>();
        }
        this.propiedades.add(propiedad);
    }

    // Métodos de tratos

    //Método para agregar un trato pendiente
    public void agregarTrato(Tratos trato) {
        this.tratosPendientes.add(trato);

    }

    //Método para eliminar un trato
    public void eliminarTrato(Tratos trato) {
        try {
            if (!this.tratosPendientes.remove(trato)) {
                throw new ExcepcionTratoNoEncontrado(trato.getId());
            } else {
                this.tratosPendientes.remove(trato);
            }
        }catch (ExcepcionTratoNoEncontrado e){
            Juego.consola.imprimir("ERROR: " + e.getMessage());
        }
    }

    //Método para buscar un trato en pendientes
    public Tratos buscarTratoPorId(String id) {
        for (Tratos trato : tratosPendientes) {
            if (trato.getId().equals(id)) {
                return trato;
            }
        }
        return null; // Devuelve null si no encuentra el trato
    }

    //Getters y setters:
    public boolean estaEnCarcel() { return enCarcel; }

    public String getNombre() {
        return nombre;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public float getFortuna() {
        return fortuna;
    }

    public int getTiradasCarcel() {
        return tiradasCarcel;
    }

    public int getVueltas() { return vueltas;}

    public int getVecesEnCarcel() {
        return vecesEnCarcel;
    }

    public int getTurnosEnCarcel() {
        return turnosEnCarcel;
    }
    public boolean getYaTiroEsteTurno() { return yaTiroEsteTurno; }

    public ArrayList<Casilla> getPropiedades() {
        return propiedades;
    }

    public void setVueltas(int vueltas) {
        this.vueltas = vueltas++;
    }

    public float getDineroInvertido() {
        return dineroInvertido;
    }

    public float getPagoTasasEImpuestos() {
        return pagoTasasEImpuestos;
    }

    public float getPagoDeAlquileres() {
        return pagoDeAlquileres;
    }

    public float getCobroDeAlquileres() {
        return cobroDeAlquileres;
    }

    public float getPasarPorCasillaDeSalida() {
        return pasarPorCasillaDeSalida;
    }

    public float getPremiosInversionesBote() {
        return premiosInversionesBote;
    }

    public ArrayList<Edificio> getEdificios() {
        return edificios;
    }
    // Getters
    public boolean estaEnBancarrota() { return enBancarrota; }
    public Jugador getAcreedor() { return acreedor; }
    public ArrayList<Tratos> getTratosPendientes() {return this.tratosPendientes;}
}