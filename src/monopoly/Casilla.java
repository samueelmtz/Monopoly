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
    private float alquiler; //Alquiler base de la casilla (solo para solares).

     //Constructores:
    public Casilla() {
        this.avatares = new ArrayList<>();
        this.valor = 0;
        this.impuesto = 0;
        this.hipoteca = 0;
    }//Parametros vacios

    /*Constructor para casillas tipo Solar (añade el valor alquiler)
     * Parámetros: nombre casilla, tipo, posición, valor, alquiler y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, float valor, float alquiler, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.valor = valor;
        this.impuesto = alquiler;
        this.duenho = duenho;
        this.avatares = new ArrayList<Avatar>();
    }
    /*Constructor para casillas tipo Servicios o Transporte:
     * Parámetros: nombre casilla, tipo (debe ser serv. o transporte), posición en el tablero, valor y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, float valor, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.valor = valor;
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

                    case "Transporte":
                        aPagar = Valor.ALQUILER_TRANSPORTE;
                        System.out.printf("Alquiler de transporte: %,.0f€\n", aPagar);
                        break;

                    case "Servicios":
                        int x = 4;
                        aPagar = (float) tirada * x * Valor.FACTOR_SERVICIO;
                        System.out.printf("Alquiler de servicio: dados(%d) * %d * %,.0f€ = %,.0f€\n", tirada, x, Valor.FACTOR_SERVICIO, aPagar);
                        break;

                    default:
                        break;
                }

                // Comprobación de solvencia para pagos a otros jugadores
                if (aPagar > 0) {
                    if (actual.getFortuna() < aPagar) {
                        System.out.printf("¡NO ERES SOLVENTE! Debes pagar %,.0f€ pero solo tienes %,.0f€\n", aPagar, actual.getFortuna());
                        System.out.println("Debes hipotecar propiedades o declararte en bancarrota.");
                        return false;
                    }

                    // Aplicar pago a otro jugador
                    actual.restarFortuna(aPagar);
                    if (receptor != null) {
                        receptor.sumarFortuna(aPagar);
                    }

                    System.out.printf("%s ha pagado %,.0f€ a %s\n", actual.getNombre(), aPagar, receptor.getNombre());
                    return true;
                }
            }

            // TERCERO: Procesar casillas de impuestos (solo mostrar mensaje, el pago se hace en Menu)
            if (this.tipo.equals("Impuesto")) {
                float aPagar = this.impuesto;
                System.out.printf("Impuesto a pagar: %,.0f€\n", aPagar);


                if (actual.getFortuna() < aPagar) {
                    System.out.printf("¡NO ERES SOLVENTE! Debes pagar %,.0f€ pero solo tienes %,.0f€\n", aPagar, actual.getFortuna());
                    System.out.println("Debes hipotecar propiedades o declararte en bancarrota.");
                    return false;
                }

                // Devolver true para indicar que es solvente, el pago se procesa en Menu
                return true;
            }

            // Casillas especiales
            switch (this.tipo) {
                case "Parking":
                    System.out.println("Has caído en Parking.");
                    break;
                case "IrCarcel":
                    System.out.println("¡Has caído en Ir a la Cárcel!");
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
        return (this.tipo.equals("Solar") || this.tipo.equals("Transporte") || this.tipo.equals("Servicios"));
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
                if (this.grupo != null) {
                    info.append("\tColor del grupo: ").append(this.grupo.getColorGrupo()).append("\n");
                }
                info.append("\tDueño: ").append(this.duenho.getNombre()).append("\n");
                info.append(String.format("\tPrecio: %,.0f€\n", this.valor));
                info.append(String.format("\tAlquiler: %,.0f€\n", this.impuesto));

                // Usar valores específicos según el nombre del solar
                float precioCasa = getPrecioCasa();
                float precioHotel = getPrecioHotel();
                float precioPiscina = getPrecioPiscina();
                float precioPista = getPrecioPistaDeporte();
                float alquilerCasa = getAlquilerCasa();
                float alquilerHotel = getAlquilerHotel();
                float alquilerPiscina = getAlquilerPiscina();
                float alquilerPista = getAlquilerPistaDeporte();

                info.append(String.format("\tPrecio casa: %,.0f€\n", precioCasa));
                info.append(String.format("\tPrecio hotel: %,.0f€\n", precioHotel));
                info.append(String.format("\tPrecio piscina: %,.0f€\n", precioPiscina));
                info.append(String.format("\tPrecio pista de deporte: %,.0f€\n", precioPista));
                info.append(String.format("\tAlquiler casa: %,.0f€\n", alquilerCasa));
                info.append(String.format("\tAlquiler hotel: %,.0f€\n", alquilerHotel));
                info.append(String.format("\tAlquiler piscina: %,.0f€\n", alquilerPiscina));
                info.append(String.format("\tAlquiler pista de deporte: %,.0f€\n", alquilerPista));
                break;

            case "Transporte":
                info.append("\tTipo: ").append(this.tipo).append("\n");
                info.append("\tDueño: ").append(this.duenho.getNombre()).append("\n");
                info.append(String.format("\tPrecio: %,.0f€\n", this.valor));
                info.append(String.format("\tPago por caer: %,.0f€\n", Valor.ALQUILER_TRANSPORTE)); // Usar constante en lugar de cálculo
                break;

            case "Servicio":
                info.append("\tTipo: ").append(this.tipo).append("\n");
                info.append("\tDueño: ").append(this.duenho.getNombre()).append("\n");
                info.append(String.format("\tPrecio: %,.0f€\n", this.valor));
                info.append(String.format("\tPago por caer: dados * x * %,.0f€\n", Valor.FACTOR_SERVICIO));
                info.append("\t\t(x=4 si se posee una casilla de este tipo, x=10 si se poseen 2)\n");
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
            System.out.println("{");
            System.out.println("    tipo: " + this.tipo + ",");
            System.out.println("    nombre: " + this.nombre + ",");

            if (this.tipo.equals("Solar") && this.grupo != null) {
                System.out.println("    grupo: " + this.grupo.getColorGrupo() + ",");
            }

            System.out.println("    valor: " + String.format("%,.0f", this.valor) + "€");
            System.out.println("}");
        }
        else{
            System.out.println("La casilla " + this.nombre + " ya está vendida a " + this.duenho.getNombre());
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

    // Métodos auxiliares para obtener los valores específicos de valores de casas, hoteles, piscinas y pistas de deporte
    private float getPrecioCasa() {
        switch(this.nombre) {
            case "Solar1": case "Solar2": return 500000;
            case "Solar3": case "Solar4": case "Solar5": return 500000;
            case "Solar6": case "Solar7": case "Solar8":
            case "Solar9": case "Solar10": case "Solar11": return 1000000;
            case "Solar12": case "Solar13": case "Solar14":
            case "Solar15": case "Solar16": case "Solar17": return 1500000;
            case "Solar18": case "Solar19": case "Solar20":
            case "Solar21": case "Solar22": return 2000000;
            default: return this.valor * 0.60f;
        }
    }

    private float getPrecioHotel() {
        return getPrecioCasa(); // Según PDF, mismo precio que casa
    }

    private float getPrecioPiscina() {
        switch(this.nombre) {
            case "Solar1": case "Solar2": return 100000;
            case "Solar3": case "Solar4": case "Solar5": return 100000;
            case "Solar6": case "Solar7": case "Solar8":
            case "Solar9": case "Solar10": case "Solar11": return 200000;
            case "Solar12": case "Solar13": case "Solar14":
            case "Solar15": case "Solar16": case "Solar17": return 300000;
            case "Solar18": case "Solar19": case "Solar20":
            case "Solar21": case "Solar22": return 400000;
            default: return this.valor * 0.40f;
        }
    }

    private float getPrecioPistaDeporte() {
        switch(this.nombre) {
            case "Solar1": case "Solar2": return 200000;
            case "Solar3": case "Solar4": case "Solar5": return 200000;
            case "Solar6": case "Solar7": case "Solar8":
            case "Solar9": case "Solar10": case "Solar11": return 400000;
            case "Solar12": case "Solar13": case "Solar14":
            case "Solar15": case "Solar16": case "Solar17": return 600000;
            case "Solar18": case "Solar19": case "Solar20":
            case "Solar21": case "Solar22": return 800000;
            default: return this.valor * 1.25f;
        }
    }

    private float getAlquilerCasa() {
        switch(this.nombre) {
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
            default: return this.impuesto * 5f;
        }
    }

    private float getAlquilerHotel() {
        switch(this.nombre) {
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
            default: return this.impuesto * 70f;
        }
    }

    private float getAlquilerPiscina() {
        switch(this.nombre) {
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
            default: return this.impuesto * 25f;
        }
    }

    private float getAlquilerPistaDeporte() {
        return getAlquilerPiscina(); // Según PDF, mismo alquiler que piscina
    }

    public ArrayList<Avatar> getAvatares() {
        return avatares;
    }
}

