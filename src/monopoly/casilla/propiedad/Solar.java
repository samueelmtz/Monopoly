// monopoly/casilla/propiedad/Solar.java
package monopoly.casilla.propiedad;

import monopoly.casilla.Propiedad;
import partida.Jugador;
import partida.Avatar;
import monopoly.Valor;
import monopoly.casilla.Grupo;
import java.util.ArrayList;

public class Solar extends Propiedad {
    // Atributos específicos de Solar - PRIVADOS
    private Grupo grupo;
    private int numCasas;
    private int numHoteles;
    private int numPiscinas;
    private int numPistas;

    // Constructores
    public Solar(String nombre, int posicion, float valor, float alquiler, Jugador duenho, Grupo grupo) {
        super(nombre, posicion, valor, alquiler, duenho);
        this.grupo = grupo;
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

    // MÉTODO infoCasilla() IMPLEMENTADO
    @Override
    public void infoCasilla() {
        System.out.println("{");
        System.out.println("\tTipo: Solar");
        if (this.grupo != null) {
            System.out.println("\tColor del grupo: " + this.grupo.getColorGrupo());
        }
        System.out.println("\tDueño: " + (this.getDuenho() != null ? this.getDuenho().getNombre() : "Banca"));
        System.out.println(String.format("\tPrecio: %,.0f€", this.getValorPropiedad()));
        System.out.println(String.format("\tAlquiler: %,.0f€", this.getImpuesto()));
        System.out.println(String.format("\tPrecio casa: %,.0f€", getPrecioCasa()));
        System.out.println(String.format("\tPrecio hotel: %,.0f€", getPrecioHotel()));
        System.out.println(String.format("\tPrecio piscina: %,.0f€", getPrecioPiscina()));
        System.out.println(String.format("\tPrecio pista de deporte: %,.0f€", getPrecioPistaDeporte()));
        System.out.println(String.format("\tAlquiler casa: %,.0f€", getAlquilerCasa()));
        System.out.println(String.format("\tAlquiler hotel: %,.0f€", getAlquilerHotel()));
        System.out.println(String.format("\tAlquiler piscina: %,.0f€", getAlquilerPiscina()));
        System.out.println(String.format("\tAlquiler pista de deporte: %,.0f€", getAlquilerPistaDeporte()));
        System.out.println("\tEdificios: " + this.numCasas + " casas, " + this.numHoteles + " hoteles, " +
                this.numPiscinas + " piscinas, " + this.numPistas + " pistas");
        System.out.println("}");
    }

    // MÉTODOS ESPECÍFICOS
    public void edificar(String tipoEdificio) {
        switch (tipoEdificio.toLowerCase()) {
            case "casa":
                if (anhadirCasa()) {
                    System.out.println("Casa construída en " + this.getNombre());
                }
                break;
            case "hotel":
                if (anhadirHotel()) {
                    System.out.println("Hotel construído en " + this.getNombre());
                }
                break;
            case "piscina":
                if (anhadirPiscina()) {
                    System.out.println("Piscina construída en " + this.getNombre());
                }
                break;
            case "pista_deporte":
                if (anhadirPistaDeporte()) {
                    System.out.println("Pista de deporte construída en " + this.getNombre());
                }
                break;
        }
    }

    public void hipotecar() {
        if (puedeHipotecar(this.getDuenho())) {
            this.setHipotecada(true);
            System.out.println(this.getDuenho().getNombre() + " ha hipotecado " + this.getNombre() + " por " +
                    String.format("%,.0f", this.getValorHipoteca()) + "€");
        }
    }

    public boolean estaHipotecada() {
        return this.isHipotecada();
    }

    // MÉTODO de evaluación de casilla
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.getAvatar().getLugar() == this) {
            if (this.getDuenho() == null || this.getDuenho() == banca || this.getDuenho().getNombre().equals("Banca")) {
                System.out.println("¡Este solar está disponible para compra! Usa el comando 'comprar " + this.getNombre() + "' para adquirirla.");
                return true;
            }

            if (this.getDuenho() != null && this.getDuenho() != banca && this.getDuenho() != actual) {
                if (this.isHipotecada()) {
                    System.out.println("El solar " + this.getNombre() + " está hipotecado. No se cobra alquiler.");
                    return true;
                }

                float aPagar = calcularAlquilerTotal();

                if (actual.getFortuna() < aPagar) {
                    System.out.printf("¡NO ERES SOLVENTE! Debes pagar %,.0f€ pero solo tienes %,.0f€\n", aPagar, actual.getFortuna());
                    return false;
                }

                actual.restarFortuna(aPagar);
                actual.sumarPagoDeAlquileres(aPagar);
                this.getDuenho().sumarFortuna(aPagar);
                this.getDuenho().sumarCobroDeAlquileres(aPagar);


                System.out.printf("%s ha pagado %,.0f€ de alquiler a %s\n", actual.getNombre(), aPagar, this.getDuenho().getNombre());
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
                System.out.println("¡Grupo completo! Alquiler doble.");
            }
        }

        System.out.printf("Alquiler de solar: %,.0f€\n", alquilerTotal);
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
}