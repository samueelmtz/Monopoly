package monopoly;

import excepciones.ExcepcionFondosInsuficientes;
import excepciones.ExcepcionPropiedadNoPertenece;
import excepciones.ExcepcionTratoInvalido;
import partida.Jugador;
import monopoly.casilla.Propiedad;

public class Tratos {

    //Atributos
    private final String id; //Identificador de la clase
    private static int contadorTratos = 0; //Atributo para añadirle al id un número identificativo
    private final Jugador ofertante; //Jugador que propone el trato
    private final Jugador receptor; //Jugador que recibe la oferta
    private final Propiedad propiedadOfrecida; // Propiedad que ofrece el jugador que propone
    private final Propiedad propiedadDemandada; // Propiedad que solicita el jugador que propone
    private final float dineroOfrecido; // Dinero que ofrece el jugador que propone
    private final float dineroDemandado; // Dinero que solicita el jugador que propone

    //Constructor
    public Tratos(Jugador ofertante, Jugador receptor, Propiedad propiedadOfrecida, Propiedad propiedadDemandada, float dineroOfrecido, float dineroDemandado) {
        this.id = "Trato" + contadorTratos++;
        this.ofertante = ofertante;
        this.receptor = receptor;
        this.propiedadOfrecida = propiedadOfrecida;
        this.propiedadDemandada = propiedadDemandada;
        this.dineroOfrecido = dineroOfrecido;
        this.dineroDemandado = dineroDemandado;
    }

    //Métodos
    //Método para comprobar que un trato es válido
    public boolean esTratoValido(){
        //El receptor no puede ser el mismo ofertante
        if(ofertante.equals(receptor)){
            return false;
        }

        //Si hay propiedad ofrecida, debe pertenecer al ofertante
        if (propiedadOfrecida != null) {
            if (!propiedadOfrecida.perteneceAJugador(ofertante)) {
                return false;
            }
        }

        //Si hay propiedad demandada, debe pertenecer al receptor
        if (propiedadDemandada != null) {
            if (!propiedadDemandada.perteneceAJugador(receptor)) {
                return false;
            }
        }

        //Debe haber algo en demanda
        boolean hayAlgoOfrecido = (propiedadOfrecida != null) || (dineroOfrecido > 0);
        boolean hayAlgoDemandado = (propiedadDemandada != null) || (dineroDemandado > 0);

        if (!hayAlgoOfrecido || !hayAlgoDemandado) {
            return false;
        }

        //No se puede intercambiar la misma propiedad
        if (propiedadOfrecida != null && propiedadOfrecida.equals(propiedadDemandada)) {
            return false;
        }

        // El dinero no puede ser negativo
        if (dineroOfrecido < 0 || dineroDemandado < 0) {
            return false;
        }

        // Las propiedades no pueden estar hipotecadas
        if (propiedadOfrecida != null && propiedadOfrecida.isHipotecada()) {
            return false;
        }
        if (propiedadDemandada != null && propiedadDemandada.isHipotecada()) {
            return false;
        }

        return true;
    }

    //Método para transferir la propiedad de un jugador a otro
    private void transferirPropiedad(Propiedad propiedad, Jugador de, Jugador a) {
        de.eliminarPropiedad(propiedad);
        a.anhadirPropiedad(propiedad);
        propiedad.setDuenho(a);
    }

    //Método para transferir el dinero de un jugador a otro
    private void transferirDinero(float cantidad, Jugador de, Jugador a) {
        if(cantidad > 0) {
            de.restarFortuna(cantidad);
            a.sumarFortuna(cantidad);
        }
    }

    //Método para aceptar el trato
// Método para aceptar el trato
    public boolean aceptar() {
        try {
            if (!esTratoValido()) {
                throw new ExcepcionTratoInvalido(
                        this.id,
                        "el trato no es válido según las reglas del juego"
                );
            }

            // Verificar que ambos jugadores tienen suficiente dinero
            if (dineroOfrecido > 0 && ofertante.getFortuna() < dineroOfrecido) {
                throw new ExcepcionFondosInsuficientes(
                        ofertante.getNombre(),
                        dineroOfrecido,
                        ofertante.getFortuna(),
                        "pagar " + String.format("%,.0f", dineroOfrecido) + "€ en el trato " + this.id
                );
            }

            if (dineroDemandado > 0 && receptor.getFortuna() < dineroDemandado) {
                throw new ExcepcionFondosInsuficientes(
                        receptor.getNombre(),
                        dineroDemandado,
                        receptor.getFortuna(),
                        "pagar " + String.format("%,.0f", dineroDemandado) + "€ en el trato " + this.id
                );
            }

            // Verificar propiedades
            if (propiedadOfrecida != null && !propiedadOfrecida.getDuenho().equals(ofertante)) {
                throw new ExcepcionPropiedadNoPertenece(
                        ofertante.getNombre(),
                        propiedadOfrecida.getNombre()
                );
            }

            if (propiedadDemandada != null && !propiedadDemandada.getDuenho().equals(receptor)) {
                throw new ExcepcionPropiedadNoPertenece(
                        receptor.getNombre(),
                        propiedadDemandada.getNombre()
                );
            }

            // Realizar el intercambio de dinero primero
            if (dineroOfrecido > 0) {
                transferirDinero(dineroOfrecido, ofertante, receptor);
            }
            if (dineroDemandado > 0) {
                transferirDinero(dineroDemandado, receptor, ofertante);
            }

            // Luego realizar el intercambio de propiedades
            if (propiedadOfrecida != null) {
                transferirPropiedad(propiedadOfrecida, ofertante, receptor);
            }
            if (propiedadDemandada != null) {
                transferirPropiedad(propiedadDemandada, receptor, ofertante);
            }

            return true;

        } catch (ExcepcionTratoInvalido e) {
            Juego.consola.imprimir("✗ " + e.getMessage());
            return false;
        } catch (ExcepcionFondosInsuficientes e) {
            Juego.consola.imprimir("✗ " + e.getMessage());
            return false;
        } catch (ExcepcionPropiedadNoPertenece e) {
            Juego.consola.imprimir("✗ " + e.getMessage());
            return false;
        } catch (Exception e) {
            Juego.consola.imprimir("⚠ Error inesperado al aceptar trato: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //Getters
    public String getId(){
        return id;
    }

    public Jugador getOfertante(){
        return ofertante;
    }

    public Jugador getReceptor(){
        return receptor;
    }

    public Propiedad getPropiedadOfrecida(){
        return propiedadOfrecida;
    }

    public Propiedad getPropiedadDemandada(){
        return propiedadDemandada;
    }

    public float getDineroOfrecido(){
        return dineroOfrecido;
    }

    public float getDineroDemandado(){
        return dineroDemandado;
    }
}

