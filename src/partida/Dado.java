package partida;
import java.util.Random;
import monopoly.Juego;

public class Dado {
    private int valor; // Valor del dado
    private final Random rand; //Valor aleatorio
    private final boolean forzado; // Indica si el dado está forzado
    private final int valorForzado; // Valor forzado

    //CONSTRUCTOR PRINCIPAL
    public Dado(){
        this.rand = new Random();
        this.valor = 1;
        this.forzado = false;
        this.valorForzado = 1;
    }

    // Método para simular lanzamiento normal
    public int hacerTirada() {
        if (this.forzado) {
            this.valor = this.valorForzado;
            Juego.consola.imprimir("Dado forzado a " + this.valor);
        } else {
            this.valor = rand.nextInt(6) + 1;
        }
        return this.valor;
    }

    //GETTERS Y SETTERS:
    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
}
