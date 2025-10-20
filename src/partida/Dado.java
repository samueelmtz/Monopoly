package partida;
import java.util.Random;

public class Dado {
    private int valor;
    private Random rand;
    private boolean forzado; // Indica si el dado está forzado
    private int valorForzado; // Valor forzado

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
            System.out.println("Dado forzado a " + this.valor);
        } else {
            this.valor = rand.nextInt(6) + 1;
        }
        return this.valor;
    }


    //Getters y setters:
    public int getValor() {
        return valor;
    }
    public void setValor(int valor) {
        this.valor = valor;
    }
}
