package partida;
import java.util.Random;

public class Dado {
    //El dado solo tiene un atributo en nuestro caso: su valor.
    private int valor;
    private Random rand;

    public Dado(){
        this.rand = new Random();
        this.valor = 1; //Valor inicial del dado.
    }

    //Metodo para simular lanzamiento de un dado: devolverá un valor aleatorio entre 1 y 6.
    public int hacerTirada() {
        this.valor = rand.nextInt(6) + 1; //nextInt(6) devuelve un valor entre 0 y 5, por eso sumamos 1.
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
