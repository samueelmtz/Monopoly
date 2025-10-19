package monopoly;

import partida.*;
import java.util.ArrayList;

public class Casilla {

    //Atributos:
    private String nombre; //Nombre de la casilla
    private String tipo; //Tipo de casilla (Solar, Especial, Transporte, Servicios, Comunidad, Suerte y Impuesto).
    private float valor; //Valor de esa casilla (en la mayoría será valor de compra, en la casilla parking se usará como el bote).
    private int posicion; //Posición que ocupa la casilla en el tablero (entero entre 1 y 40).
    private Jugador duenho; //Dueño de la casilla (por defecto sería la banca).
    private Grupo grupo; //Grupo al que pertenece la casilla (si es solar).
    private float impuesto; //Cantidad a pagar por caer en la casilla: el alquiler en solares/servicios/transportes o impuestos.
    private float hipoteca; //Valor otorgado por hipotecar una casilla
    private ArrayList<Avatar> avatares; //Avatares que están situados en la casilla.

     //Constructores:
    public Casilla() {
        this.avatares = new ArrayList<>();
        this.valor = 0;
        this.impuesto = 0;
        this.hipoteca = 0;
    }//Parametros vacios

    /*Constructor para casillas tipo Solar, Servicios o Transporte:
     * Parámetros: nombre casilla, tipo (debe ser solar, serv. o transporte), posición en el tablero, valor y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, float valor, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.valor = valor;
        this.impuesto = valor * 0.10f;
        this.hipoteca = valor/2f;
        this.duenho = duenho;
        this.avatares = new ArrayList<Avatar>();
    }

    /*Constructor utilizado para inicializar las casillas de tipo IMPUESTOS.
     * Parámetros: nombre, posición en el tablero, impuesto establecido y dueño.
     */
    public Casilla(String nombre, int posicion, float impuesto, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = "Impuesto";
        this.posicion = posicion;
        this.impuesto = impuesto;
        this.duenho = duenho;
        this.avatares = new ArrayList<>();
        this.valor = 0;
        this.hipoteca = 0;
    }

    /*Constructor utilizado para crear las otras casillas (Suerte, Caja de comunidad y Especiales):
     * Parámetros: nombre, tipo de la casilla (será uno de los que queda), posición en el tablero y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.duenho = duenho;
        this.avatares = new ArrayList<>();
        this.valor = 0;
        this.impuesto = 0;
        this.hipoteca = 0;
    }

    //Método utilizado para eliminar un avatar al array de avatares en casilla.
    public void eliminarAvatar(Avatar avatar){
        this.avatares.remove(avatar);
    }

    //Método utilizado para añadir un avatar del array de avatares en casilla.
    public void anhadirAvatar(Avatar avatar){
        this.avatares.add(avatar);
    }

    /*Método para evaluar qué hacer en una casilla concreta. Parámetros:
     * - Jugador cuyo avatar está en esa casilla.
     * - La banca (para ciertas comprobaciones).
     * - El valor de la tirada: para determinar impuesto a pagar en casillas de servicios.
     * Valor devuelto: true en caso de ser solvente (es decir, de cumplir las deudas), y false
     * en caso de no cumplirlas.*/
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.getAvatar().getLugar() == this) {

            // PRIMERO: Verificar si la casilla es comprable y pertenece a la banca
            if (this.esTipoComprable() && (this.duenho == null || this.duenho == banca || this.duenho.getNombre().equals("Banca"))) {
                System.out.println("¡Esta casilla está disponible para compra! Usa el comando 'comprar " + this.nombre + "' para adquirirla.");
                return true;
            }

            // SEGUNDO: Si la casilla tiene dueño (no es la banca), evaluar pagos
            if (this.duenho != null && this.duenho != banca && this.duenho != actual) {

                float aPagar = 0f;
                Jugador receptor = this.duenho;

                switch (this.tipo) {
                    case "Solar":
                        aPagar = this.impuesto;
                        System.out.printf("Alquiler de solar: %,.0f€\n", aPagar);
                        break;

                    case "Transporte": {
                        // CORRECCIÓN: Siempre 250.000€ según especificación
                        aPagar = Valor.ALQUILER_TRANSPORTE;
                        System.out.printf("Alquiler de transporte: %,.0f€\n", aPagar);
                        break;
                    }

                    case "Servicio": {
                        // CORRECCIÓN: Siempre multiplicador 4 según especificación
                        int x = 4;
                        aPagar = (float) tirada * x * Valor.FACTOR_SERVICIO;
                        System.out.printf("Alquiler de servicio: dados(%d) * %d * %,.0f€ = %,.0f€\n",
                                tirada, x, Valor.FACTOR_SERVICIO, aPagar);
                        break;
                    }

                    case "Impuesto":
                        aPagar = this.impuesto;
                        receptor = banca;
                        System.out.printf("Impuesto a pagar: %,.0f€\n", aPagar);
                        break;

                    // Parking, Suerte, Comunidad, Especiales
                    case "Parking":
                        System.out.println("Has caído en Parking. Función de bote por implementar.");
                        return true;

                    default:
                        return true;
                }

                // Comprobación de solvencia
                if (actual.getFortuna() < aPagar) {
                    System.out.printf("¡NO ERES SOLVENTE! Debes pagar %,.0f€ pero solo tienes %,.0f€\n",
                            aPagar, actual.getFortuna());
                    System.out.println("Debes hipotecar propiedades o declararte en bancarrota.");
                    return false;
                }

                // Aplicar pago
                actual.restarFortuna(aPagar);
                actual.sumarGastos(aPagar);
                if (receptor != null) {
                    receptor.sumarFortuna(aPagar);
                }

                System.out.printf("%s ha pagado %,.0f€ a %s\n",
                        actual.getNombre(), aPagar, receptor.getNombre());
                return true;
            }

            // Casillas especiales
            switch (this.tipo) {
                case "Parking":
                    System.out.println("Has caído en Parking. Función de bote por implementar.");
                    break;
                case "IrCarcel":
                    System.out.println("¡Has caído en Ir a la Cárcel!");
                    //actual.encarcelar();
                    break;
                case "Carcel":
                    System.out.println("Estás de visita en la Cárcel!");
                    break;
                default:
                    System.out.println("No tiene nada que pagar.\n");
            }
            return true;

        } else {
            System.out.println("El jugador no está sobre la casilla.\n");
            return false;
        }
    }


    /*Metodo auxiliar para verificar si el tipo de una casilla la hace comprable*/
    public boolean esTipoComprable() {
        return (this.tipo.equals("Solar") || this.tipo.equals("Transporte") || this.tipo.equals("Servicio"));
    }

    /*Método usado para comprar una casilla determinada. Parámetros:
     * - Jugador que solicita la compra de la casilla.
     * - Banca del monopoly (es el dueño de las casillas no compradas aún).*/
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        if(this.esTipoComprable()) { //Comprobar si la casilla es de un tipo que se puede comprar
            if(solicitante.getAvatar().getLugar()==this) { //Verificar que el jugador está sobre la casilla
                // Verificar si pertenece a la banca (dueño es null o es la banca)
                if(this.duenho == null || this.duenho == banca || this.duenho.getNombre().equals("Banca")) {
                    if (solicitante.getFortuna()>=this.valor) { //Verificar que se tiene el saldo suficiente para pagarla
                        solicitante.restarFortuna(this.valor);
                        solicitante.sumarGastos(this.valor);

                        // Si la casilla tenía dueño anterior (banca), eliminarla de sus propiedades
                        if(this.duenho != null && this.duenho.getNombre().equals("Banca")) {
                            this.duenho.eliminarPropiedad(this);
                        }

                        solicitante.anhadirPropiedad(this);
                        this.duenho = solicitante;

                        System.out.printf("%s ha comprado la propiedad %s por el precio de %,.0f€\n",
                                solicitante.getNombre(), this.nombre, this.valor);
                        System.out.printf("Fortuna actual de %s: %,.0f€\n",
                                solicitante.getNombre(), solicitante.getFortuna());
                    }
                    else {
                        System.out.printf("No tienes dinero para comprar esta casilla. Necesitas %,.0f€ pero tienes %,.0f€\n",
                                this.valor, solicitante.getFortuna());
                    }
                }
                else {
                    System.out.println("Esta casilla no está en venta. Pertenece a: " + this.duenho.getNombre());
                }
            }
            else {
                System.out.println("¡Tienes que caer en la casilla para poder comprarla!\n");
            }
        }
        else {
            System.out.println("¡¡Esta casilla no se puede comprar!!\n");
        }
    }


    /*Metodo para añadir valor a una casilla. Utilidad:
     * - Sumar valor a la casilla de parking.
     * - Sumar valor a las casillas de solar al no comprarlas tras cuatro vueltas de todos los jugadores.
     * Este metodo toma como argumento la cantidad a añadir del valor de la casilla.*/
    public void sumarValor(float suma) {
        this.valor += suma;
    }

    /*Metodo para mostrar información sobre una casilla.
     * Devuelve una cadena con información específica de cada tipo de casilla.*/
    public void infoCasilla() {
        StringBuilder info = new StringBuilder();
        info.append("{\n");

        // La completamos con la información correspondiente en función del tipo de casilla
        switch (this.tipo) {
            case "Solar":
                info.append("\tTipo: ").append(this.tipo).append("\n");
                info.append("\tColor del grupo: ").append(this.grupo.getColorGrupo()).append("\n");
                info.append("\tDueño: ").append(this.duenho.getNombre()).append("\n");
                info.append(String.format("\tPrecio: %,.0f€\n", this.valor));
                info.append(String.format("\tAlquiler: %,.0f€\n", this.impuesto));
                info.append(String.format("\tHipoteca: %,.0f€\n", this.hipoteca));
                info.append(String.format("\tPrecio casa: %,.0f€\n", this.valor * 0.60f));
                info.append(String.format("\tPrecio hotel: %,.0f€\n", this.valor * 0.60f));
                info.append(String.format("\tPrecio piscina: %,.0f€\n", this.valor * 0.40f));
                info.append(String.format("\tPrecio pista de deporte: %,.0f€\n", this.valor * 1.25f));
                info.append(String.format("\tAlquiler casa: %,.0f€\n", this.impuesto * 5f));
                info.append(String.format("\tAlquiler hotel: %,.0f€\n", this.impuesto * 70f));
                info.append(String.format("\tAlquiler piscina: %,.0f€\n", this.impuesto * 25f));
                info.append(String.format("\tAlquiler pista de deporte: %,.0f€\n", this.impuesto * 25f));
                break;

            case "Transporte":
                info.append("\tTipo: ").append(this.tipo).append("\n");
                info.append("\tDueño: ").append(this.duenho.getNombre()).append("\n");
                info.append(String.format("\tPrecio: %,.0f€\n", this.valor));
                info.append(String.format("\tPago por caer: %,.0f€\n",
                        Valor.ALQUILER_TRANSPORTE)); // Usar constante en lugar de cálculo
                info.append(String.format("\tHipoteca: %,.0f€\n", this.hipoteca));
                break;

            case "Servicio":
                info.append("\tTipo: ").append(this.tipo).append("\n");
                info.append("\tDueño: ").append(this.duenho.getNombre()).append("\n");
                info.append(String.format("\tPrecio: %,.0f€\n", this.valor));
                info.append(String.format("\tPago por caer: dados * x * %,.0f€\n", Valor.FACTOR_SERVICIO));
                info.append("\t\t(x=4 si se posee una casilla de este tipo, x=10 si se poseen 2)\n");
                info.append(String.format("\tHipoteca: %,.0f€\n", this.hipoteca));
                break;

            case "Impuesto":
                info.append("\tTipo: Impuesto\n");
                info.append(String.format("\tA pagar: %,.0f€\n", this.impuesto));
                break;

            case "Especial":
                switch (this.nombre) {
                    case "Parking":
                        info.append("\tTipo: Especial (Parking)\n");
                        // Aquí necesitarías acceso al tablero para obtener el bote actual
                        info.append("\tBote: Información no disponible\n");
                        info.append("\tJugadores: [");
                        if (this.avatares != null && !this.avatares.isEmpty()) {
                            for (int i = 0; i < this.avatares.size(); i++) {
                                info.append(this.avatares.get(i).getJugador().getNombre());
                                if (i < this.avatares.size() - 1) info.append(", ");
                            }
                        } else {
                            info.append("-");
                        }
                        info.append("]\n");
                        break;

                    case "Carcel":
                        info.append("\tTipo: Especial (Cárcel)\n");
                        info.append(String.format("\tSalir pagando: %,.0f€\n", Valor.SALIR_CARCEL));
                        info.append("\tJugadores: [");
                        if (this.avatares != null && !this.avatares.isEmpty()) {
                            for (int i = 0; i < this.avatares.size(); i++) {
                                Jugador j = this.avatares.get(i).getJugador();
                                info.append(j.getNombre()).append(",").append(j.getTiradasCarcel());
                                if (i < this.avatares.size() - 1) info.append(" ");
                            }
                        } else {
                            info.append("-");
                        }
                        info.append("]\n");
                        break;

                    case "Salida":
                        info.append("\tTipo: Especial (Salida)\n");
                        info.append(String.format("\tRecompensa al pasar: %,.0f€\n", Valor.SUMA_VUELTA));
                        break;

                    case "IrCarcel":
                        info.append("\tTipo: Especial (Ir a Cárcel)\n");
                        info.append("\tAcción: Enviar al jugador a la Cárcel\n");
                        break;

                    default:
                        info.append("\tTipo: Especial\n");
                        info.append("\tNombre: ").append(this.nombre).append("\n");
                }
                break;

            default:
                info.append("\tTipo no reconocido: ").append(this.tipo).append("\n");
        }

        info.append("}");

        // IMPRIMIR EL RESULTADO
        System.out.println(info.toString());
    }


    /* Método para mostrar información de una casilla en venta.
     * Valor devuelto: texto con esa información.
     */
    public void casEnVenta() {
        if(this.duenho == null || this.duenho.getNombre().equals("Banca")){
            System.out.println("La casilla " + this.nombre + " está en venta por el precio de " + this.valor + "€\n");
        }
        else{
            System.out.println("La casilla ya está vendida o no está disponible.\n");
        }
    }

    //GETTERS Y SETTERS
    public String getNombre(){
        return nombre;
    }

    public String getTipo(){
        return tipo;
    }

    public void setTipo(String tipo_casilla){
        this.tipo = tipo_casilla;
    }

    public float getValor(){
        return valor;
    }

    public void setValor(float valor_casilla){
        this.valor = valor_casilla;
    }

    public int getPosicion(){
        return posicion;
    }

    public void setPosicion(int posicion){
        this.posicion = posicion;
    }

    public Jugador getDuenho() {
        return duenho;
    }

    public void setDuenho(Jugador duenho){
        this.duenho = duenho;
    }

    public float getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(float impuesto){
        this.impuesto = impuesto;
    }

    public float getHipoteca() {
        return hipoteca;
    }

    public void setHipoteca(float hipoteca){
        this.hipoteca = hipoteca;
    }

    public Grupo getGrupo() {
        return grupo;
    }
    public void setGrupo(Grupo grupo){
        this.grupo = grupo;
    }

    public ArrayList<Avatar> getAvatares() {
        return avatares;
    }
}

