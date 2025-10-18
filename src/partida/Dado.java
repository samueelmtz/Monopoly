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
            System.out.println("DEBUG: Dado forzado a " + this.valor);
        } else {
            this.valor = rand.nextInt(6) + 1;
        }
        return this.valor;
    }

    // Método para forzar el dado a un valor específico
    public void forzarDado(int valor) {
        if (valor >= 1 && valor <= 6) {
            this.forzado = true;
            this.valorForzado = valor;
            System.out.println("Dado forzado a: " + valor);
        } else {
            System.out.println("Error: El valor del dado debe estar entre 1 y 6");
        }
    }

    // Método para desactivar el forzado
    public void desactivarForzado() {
        this.forzado = false;
        System.out.println("Forzado de dados desactivado");
    }

    // Método para verificar si el dado está forzado
    public boolean isForzado() {
        return forzado;
    }

    //Getters y setters:
    public int getValor() {
        return valor;
    }
    public void setValor(int valor) {
        this.valor = valor;
    }
}
