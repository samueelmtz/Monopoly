package monopoly.casilla.propiedad;

import monopoly.casilla.Propiedad;
import monopoly.edificio.Edificio;
import partida.Jugador;
import monopoly.casilla.Grupo;
import java.util.ArrayList;
import monopoly.Juego;
import excepciones.*;
import monopoly.edificio.Casa;
import monopoly.edificio.Hotel;
import monopoly.edificio.Piscina;
import monopoly.edificio.PistaDeporte;
import monopoly.Tablero;

public class Solar extends Propiedad {
    private final ArrayList<ArrayList<Edificio>> edificios;
    private Grupo grupo;
    private int numCasas;
    private int numHoteles;
    private int numPiscinas;
    private int numPistas;

    // Constructores
    public Solar(String nombre, int posicion, float valor, float alquiler, Jugador duenho, Grupo grupo) {
        super(nombre, posicion, valor, alquiler, duenho);
        this.grupo = grupo;
        this.edificios = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            this.edificios.add(new ArrayList<>()); //Array de casas, hoteles, piscinas, pistas de deporte
        }
        this.numCasas = 0;
        this.numHoteles = 0;
        this.numPiscinas = 0;
        this.numPistas = 0;
        this.setValorHipoteca(valor / 2);
    }

    // MÉTODOS REQUERIDOS
    @Override
    public boolean perteneceAJugador(Jugador jugador) {
        return this.getDuenho() != null && this.getDuenho().equals(jugador);
    }

    @Override
    public boolean alquiler() {
        return !this.isHipotecada() && this.getDuenho() != null;
    }

    @Override
    public float valor() {
        return this.getValorPropiedad();
    }

    @Override
    public String toString() {
        return String.format("Solar{nombre='%s', posicion=%d, valor=%,.0f€, grupo=%s}",
                this.getNombre(), this.getPosicion(), this.getValorPropiedad(),
                grupo != null ? grupo.getColorGrupo() : "null");
    }

    // MÉTODO infoCasilla()
    @Override
    public void infoCasilla() {
        Juego.consola.imprimir("{");
        Juego.consola.imprimir("\tTipo: Solar");
        if (this.grupo != null) {
            Juego.consola.imprimir("\tColor del grupo: " + this.grupo.getColorGrupo());
        }
        Juego.consola.imprimir("\tDueño: " + (this.getDuenho() != null ? this.getDuenho().getNombre() : "Banca"));
        Juego.consola.imprimir(String.format("\tPrecio: %,.0f€", this.getValorPropiedad()));
        Juego.consola.imprimir(String.format("\tAlquiler: %,.0f€", this.getImpuesto()));
        Juego.consola.imprimir(String.format("\tPrecio casa: %,.0f€", getPrecioCasa()));
        Juego.consola.imprimir(String.format("\tPrecio hotel: %,.0f€", getPrecioHotel()));
        Juego.consola.imprimir(String.format("\tPrecio piscina: %,.0f€", getPrecioPiscina()));
        Juego.consola.imprimir(String.format("\tPrecio pista de deporte: %,.0f€", getPrecioPistaDeporte()));
        Juego.consola.imprimir(String.format("\tAlquiler casa: %,.0f€", getAlquilerCasa()));
        Juego.consola.imprimir(String.format("\tAlquiler hotel: %,.0f€", getAlquilerHotel()));
        Juego.consola.imprimir(String.format("\tAlquiler piscina: %,.0f€", getAlquilerPiscina()));
        Juego.consola.imprimir(String.format("\tAlquiler pista de deporte: %,.0f€", getAlquilerPistaDeporte()));
        Juego.consola.imprimir("\tEdificios: " + this.numCasas + " casas, " + this.numHoteles + " hoteles, " +
                this.numPiscinas + " piscinas, " + this.numPistas + " pistas");
        Juego.consola.imprimir("}");
    }



    // MÉTODO de evaluación de casilla
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, Tablero tablero, ArrayList<Jugador> jugadores, int tirada) {
        if (actual.getAvatar().getLugar() == this) {
            if (this.getDuenho() == null || this.getDuenho().equals(banca) || this.getDuenho().getNombre().equals("Banca")) {
                Juego.consola.imprimir("¡Este solar está disponible para compra! Usa el comando 'comprar " + this.getNombre() + "' para adquirirla.");
                return true;
            }

            if (this.getDuenho() != null && !this.getDuenho().equals(banca) && !this.getDuenho().equals(actual)) {
                if (this.isHipotecada()) {
                    Juego.consola.imprimir("El solar " + this.getNombre() + " está hipotecado. No se cobra alquiler.");
                    return true;
                }

                float aPagar = calcularAlquilerTotal();

                if (actual.getFortuna() < aPagar) {
                    Juego.consola.imprimir("¡NO ERES SOLVENTE! Debes pagar %,.0f€ pero solo tienes %,.0f€\n", aPagar, actual.getFortuna());
                    return false;
                }

                actual.restarFortuna(aPagar);
                actual.sumarPagoDeAlquileres(aPagar);
                this.getDuenho().sumarFortuna(aPagar);
                this.getDuenho().sumarCobroDeAlquileres(aPagar);


                Juego.consola.imprimir("%s ha pagado %,.0f€ de alquiler a %s\n", actual.getNombre(), aPagar, this.getDuenho().getNombre());
            }
            return true;
        }
        return false;
    }

    private float calcularAlquilerTotal() {
        float alquilerTotal = this.getImpuesto();

        if (this.numCasas > 0) {
            alquilerTotal += getAlquilerCasa() * this.numCasas;
        }
        if (this.numHoteles > 0) {
            alquilerTotal += getAlquilerHotel() * this.numHoteles;
        }
        if (this.numPiscinas > 0) {
            alquilerTotal += getAlquilerPiscina() * this.numPiscinas;
        }
        if (this.numPistas > 0) {
            alquilerTotal += getAlquilerPistaDeporte() * this.numPistas;
        }

        if (this.grupo != null && this.numCasas == 0 && this.numHoteles == 0) {
            boolean tieneTodoElGrupo = true;
            for (monopoly.casilla.Casilla casillaGrupo : this.grupo.getMiembros()) {
                if (casillaGrupo.getDuenho() != this.getDuenho()) {
                    tieneTodoElGrupo = false;
                    break;
                }
            }
            if (tieneTodoElGrupo) {
                alquilerTotal *= 2;
                Juego.consola.imprimir("¡Grupo completo! Alquiler doble.");
            }
        }

        Juego.consola.imprimir("Alquiler de solar: %,.0f€\n", alquilerTotal);
        return alquilerTotal;
    }

    // MÉTODOS de construcción
    public boolean anhadirCasa() {
        if (numCasas < 4 && numHoteles == 0) {
            numCasas++;
            return true;
        }
        return false;
    }

    public boolean anhadirHotel() {
        if (numHoteles == 0 && numCasas == 4) {
            numHoteles++;
            numCasas = 0;
            return true;
        }
        return false;
    }

    public boolean anhadirPiscina() {
        if (numPiscinas == 0 && numHoteles == 1) {
            numPiscinas++;
            return true;
        }
        return false;
    }

    public boolean anhadirPistaDeporte() {
        if (numPistas == 0 && numHoteles == 1) {
            numPistas++;
            return true;
        }
        return false;
    }

    public Edificio construirEdificio(String tipoEdificio, Jugador jugador) throws ExcepcionMonopoly {
        // 1. Validar
        validarEdificacion(tipoEdificio, jugador);

        // 2. Obtener coste
        float coste = obtenerCosteEdificio(tipoEdificio);

        // 3. Verificar fondos
        if (jugador.getFortuna() < coste) {
            throw new ExcepcionFondosInsuficientes(
                    jugador.getNombre(), coste, jugador.getFortuna(),
                    "edificar " + tipoEdificio + " en " + this.getNombre()
            );
        }

        // 4. Construir
        boolean construido = false;
        switch (tipoEdificio.toLowerCase()) {
            case "casa": construido = anhadirCasa(); break;
            case "hotel": construido = anhadirHotel(); break;
            case "piscina": construido = anhadirPiscina(); break;
            case "pista_deporte": construido = anhadirPistaDeporte(); break;
        }

        if (!construido) {
            throw new ExcepcionReglasEdificacion(
                    tipoEdificio, this.getNombre(), "error al construir"
            );
        }

        // 5. Restar dinero
        jugador.restarFortuna(coste);
        jugador.sumarDineroInvertido(coste);

        // 6. Crear instancia
        return crearInstanciaEdificio(tipoEdificio);
    }

    public int venderEdificios(String tipoEdificio, int cantidad, Jugador jugador) throws ExcepcionMonopoly {
        // 1. Verificar propiedad
        if (!this.perteneceAJugador(jugador)) {
            throw new ExcepcionPropiedadNoPertenece(jugador.getNombre(), this.getNombre());
        }

        // 2. Obtener cantidad disponible
        int disponibles = obtenerCantidadEdificios(tipoEdificio);

        // 3. Validar
        if (disponibles == 0) {
            throw new ExcepcionEdificioNoExistente(
                    tipoEdificio + "(s) en " + this.getNombre(),
                    this.getNombre()
            );
        }

        if (cantidad > disponibles) {
            throw new ExcepcionCantidadEdificiosInsuficiente(
                    this.getNombre(), tipoEdificio, cantidad, disponibles
            );
        }

        // 4. Eliminar edificios
        eliminarEdificios(tipoEdificio, cantidad);

        return cantidad;
    }

    private int obtenerCantidadEdificios(String tipoEdificio) {
        switch (tipoEdificio.toLowerCase()) {
            case "casa": return numCasas;
            case "hotel": return numHoteles;
            case "piscina": return numPiscinas;
            case "pista_deporte": return numPistas;
            default: return 0;
        }
    }

    private void eliminarEdificios(String tipoEdificio, int cantidad) {
        switch (tipoEdificio.toLowerCase()) {
            case "casa":
                numCasas = Math.max(0, numCasas - cantidad);
                break;
            case "hotel":
                numHoteles = Math.max(0, numHoteles - cantidad);
                break;
            case "piscina":
                numPiscinas = Math.max(0, numPiscinas - cantidad);
                break;
            case "pista_deporte":
                numPistas = Math.max(0, numPistas - cantidad);
                break;
        }
    }

    public float obtenerPrecioVentaEdificio(String tipoEdificio) throws ExcepcionMonopoly{
        // Según las reglas: se vende al mismo precio de compra
        return obtenerCosteEdificio(tipoEdificio);
    }

    public float obtenerCosteEdificio(String tipoEdificio) throws ExcepcionMonopoly {
        // Validamos el tipo de edificio
        if (tipoEdificio == null || tipoEdificio.trim().isEmpty()) {
            throw new ExcepcionAccionNoPermitida(
                    "edificar",
                    "tipo de edificio no especificado"
            );
        }

        // Usamos switch con los tipos exactos que esperas
        switch (tipoEdificio) {
            case "casa":
                return getPrecioCasa();

            case "hotel":
                return getPrecioHotel();

            case "piscina":
                return getPrecioPiscina();

            case "pista_deporte":
                return getPrecioPistaDeporte();

            default:
                // Si el tipo no coincide, lanzamos excepción
                throw new ExcepcionAccionNoPermitida(
                        "edificar",
                        "tipo de edificio '" + tipoEdificio + "' no válido. " +
                                "Tipos válidos: casa, hotel, piscina, pista_deporte"
                );
        }
    }

    public Edificio crearInstanciaEdificio(String tipoEdificio) throws ExcepcionMonopoly {
        switch (tipoEdificio.toLowerCase()) {
            case "casa":
                Casa casa = new Casa(this);
                añadirEdificioALista(casa, 0); // Índice 0 = casas
                return casa;

            case "hotel":
                Hotel hotel = new Hotel(this);
                añadirEdificioALista(hotel, 1); // Índice 1 = hoteles
                return hotel;

            case "piscina":
                Piscina piscina = new Piscina(this);
                añadirEdificioALista(piscina, 2); // Índice 2 = piscinas
                return piscina;

            case "pista_deporte":
                PistaDeporte pista = new PistaDeporte(this);
                añadirEdificioALista(pista, 3); // Índice 3 = pistas
                return pista;

            default:
                throw new ExcepcionAccionNoPermitida(
                        "crear edificio",
                        "tipo de edificio '" + tipoEdificio + "' no válido"
                );
        }
    }

    public void validarEdificacion(String tipoEdificio, Jugador jugador) throws ExcepcionMonopoly {
        // 1. Verificar que el jugador es el dueño
        if (!this.perteneceAJugador(jugador)) {
            throw new ExcepcionPropiedadNoPertenece(jugador.getNombre(), this.getNombre());
        }

        // 2. Verificar que la propiedad no está hipotecada
        if (this.isHipotecada()) {
            throw new ExcepcionPropiedadHipotecada(this.getNombre());
        }

        // 3. Verificar que tiene todo el grupo (si aplica)
        if (grupo != null && !grupo.tieneTodoElGrupo(jugador)) {
            throw new ExcepcionPropiedadGrupoIncompleto(
                    this.getNombre(),
                    grupo.getColorGrupo()
            );
        }

        // 4. Validar tipo de edificio y límites (TODO EN UNO)
        validarTipoYLimites(tipoEdificio);
    }

    // UN SOLO MÉTODO PARA TODAS LAS VALIDACIONES
    private void validarTipoYLimites(String tipoEdificio) throws ExcepcionMonopoly {
        String tipo = tipoEdificio.toLowerCase();

        switch (tipo) {
            case "casa":
                if (numCasas >= 4) {
                    throw new ExcepcionValidacionEdificacion(
                            "casa", this.getNombre(),
                            "Máximo 4 casas por solar (actual: " + numCasas + ")"
                    );
                }
                if (numHoteles > 0) {
                    throw new ExcepcionValidacionEdificacion(
                            "casa", this.getNombre(),
                            "No se pueden construir casas cuando hay un hotel"
                    );
                }
                break;

            case "hotel":
                if (numHoteles >= 1) {
                    throw new ExcepcionValidacionEdificacion(
                            "hotel", this.getNombre(),
                            "Máximo 1 hotel por solar (actual: " + numHoteles + ")"
                    );
                }
                if (numCasas < 4) {
                    throw new ExcepcionValidacionEdificacion(
                            "hotel", this.getNombre(),
                            "Se requieren 4 casas para construir un hotel (actual: " + numCasas + ")"
                    );
                }
                break;

            case "piscina":
                if (numPiscinas >= 1) {
                    throw new ExcepcionValidacionEdificacion(
                            "piscina", this.getNombre(),
                            "Máximo 1 piscina por solar (actual: " + numPiscinas + ")"
                    );
                }
                if (numHoteles < 1) {
                    throw new ExcepcionValidacionEdificacion(
                            "piscina", this.getNombre(),
                            "Se requiere un hotel para construir una piscina (hoteles: " + numHoteles + ")"
                    );
                }
                break;

            case "pista_deporte":
                if (numPistas >= 1) {
                    throw new ExcepcionValidacionEdificacion(
                            "pista de deporte", this.getNombre(),
                            "Máximo 1 pista de deporte por solar (actual: " + numPistas + ")"
                    );
                }
                if (numHoteles < 1) {
                    throw new ExcepcionValidacionEdificacion(
                            "pista de deporte", this.getNombre(),
                            "Se requiere un hotel para construir una pista de deporte (hoteles: " + numHoteles + ")"
                    );
                }
                break;

            default:
                throw new ExcepcionAccionNoPermitida(
                        "edificar",
                        "tipo de edificio '" + tipoEdificio + "' no válido. " +
                                "Tipos válidos: casa, hotel, piscina, pista_deporte"
                );
        }
    }

    private void añadirEdificioALista(Edificio edificio, int indice) {
        if (indice >= 0 && indice < edificios.size()) {
            edificios.get(indice).add(edificio);
        }
    }

    // MÉTODOS de precios y alquileres
    public float getPrecioCasa() {
        switch(this.getNombre()) {
            case "Solar1": case "Solar2": return 500000;
            case "Solar3": case "Solar4": case "Solar5": return 500000;
            case "Solar6": case "Solar7": case "Solar8":
            case "Solar9": case "Solar10": case "Solar11": return 1000000;
            case "Solar12": case "Solar13": case "Solar14":
            case "Solar15": case "Solar16": case "Solar17": return 1500000;
            case "Solar18": case "Solar19": case "Solar20":
            case "Solar21": case "Solar22": return 2000000;
            default: return this.getValorPropiedad() * 0.60f;
        }
    }

    public float getPrecioHotel() {
        return getPrecioCasa();
    }

    public float getPrecioPiscina() {
        switch(this.getNombre()) {
            case "Solar1": case "Solar2": return 100000;
            case "Solar3": case "Solar4": case "Solar5": return 100000;
            case "Solar6": case "Solar7": case "Solar8":
            case "Solar9": case "Solar10": case "Solar11": return 200000;
            case "Solar12": case "Solar13": case "Solar14":
            case "Solar15": case "Solar16": case "Solar17": return 300000;
            case "Solar18": case "Solar19": case "Solar20":
            case "Solar21": case "Solar22": return 400000;
            default: return this.getValorPropiedad() * 0.40f;
        }
    }

    public float getPrecioPistaDeporte() {
        switch(this.getNombre()) {
            case "Solar1": case "Solar2": return 200000;
            case "Solar3": case "Solar4": case "Solar5": return 200000;
            case "Solar6": case "Solar7": case "Solar8":
            case "Solar9": case "Solar10": case "Solar11": return 400000;
            case "Solar12": case "Solar13": case "Solar14":
            case "Solar15": case "Solar16": case "Solar17": return 600000;
            case "Solar18": case "Solar19": case "Solar20":
            case "Solar21": case "Solar22": return 800000;
            default: return this.getValorPropiedad() * 1.25f;
        }
    }

    public float getAlquilerCasa() {
        switch(this.getNombre()) {
            case "Solar1": return 400000;
            case "Solar2": return 800000;
            case "Solar3": case "Solar4": return 1000000;
            case "Solar5": return 1250000;
            case "Solar6": case "Solar7": return 1500000;
            case "Solar8": return 1750000;
            case "Solar9": case "Solar10": return 1850000;
            case "Solar11": return 2000000;
            case "Solar12": case "Solar13": return 2200000;
            case "Solar14": return 2325000;
            case "Solar15": case "Solar16": return 2450000;
            case "Solar17": return 2600000;
            case "Solar18": case "Solar19": return 2750000;
            case "Solar20": return 3000000;
            case "Solar21": return 3250000;
            case "Solar22": return 4250000;
            default: return this.getImpuesto() * 5f;
        }
    }

    public float getAlquilerHotel() {
        switch(this.getNombre()) {
            case "Solar1": return 2500000;
            case "Solar2": return 4500000;
            case "Solar3": case "Solar4": return 5500000;
            case "Solar5": return 6000000;
            case "Solar6": case "Solar7": return 7500000;
            case "Solar8": return 9000000;
            case "Solar9": case "Solar10": return 9500000;
            case "Solar11": return 10000000;
            case "Solar12": case "Solar13": return 10500000;
            case "Solar14": return 11000000;
            case "Solar15": case "Solar16": return 11500000;
            case "Solar17": return 12000000;
            case "Solar18": case "Solar19": return 12750000;
            case "Solar20": return 14000000;
            case "Solar21": return 17000000;
            case "Solar22": return 20000000;
            default: return this.getImpuesto() * 70f;
        }
    }

    public float getAlquilerPiscina() {
        switch(this.getNombre()) {
            case "Solar1": return 500000;
            case "Solar2": return 900000;
            case "Solar3": case "Solar4": return 1100000;
            case "Solar5": return 1200000;
            case "Solar6": case "Solar7": return 1500000;
            case "Solar8": return 1800000;
            case "Solar9": case "Solar10": return 1900000;
            case "Solar11": return 2000000;
            case "Solar12": case "Solar13": return 2100000;
            case "Solar14": return 2200000;
            case "Solar15": case "Solar16": return 2300000;
            case "Solar17": return 2400000;
            case "Solar18": case "Solar19": return 2550000;
            case "Solar20": return 2800000;
            case "Solar21": return 3400000;
            case "Solar22": return 4000000;
            default: return this.getImpuesto() * 25f;
        }
    }

    public float getAlquilerPistaDeporte() {
        return getAlquilerPiscina();
    }

    // GETTERS Y SETTERS específicos de Solar
    public Grupo getGrupo() { return grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }
    public int getNumCasas() { return numCasas; }
    public void setNumCasas(int numCasas) { this.numCasas = numCasas; }
    public int getNumHoteles() { return numHoteles; }
    public void setNumHoteles(int numHoteles) { this.numHoteles = numHoteles; }
    public int getNumPiscinas() { return numPiscinas; }
    public void setNumPiscinas(int numPiscinas) { this.numPiscinas = numPiscinas; }
    public int getNumPistas() { return numPistas; }
    public void setNumPistas(int numPistas) { this.numPistas = numPistas; }
    public ArrayList<ArrayList<Edificio>> getEdificios(){
        return edificios;
    }
    public int getNumeroEdificios(){
        int contador=0;

        for(ArrayList<Edificio> eds: edificios){
            contador += eds.size();
        }
        return contador;
    }
}
