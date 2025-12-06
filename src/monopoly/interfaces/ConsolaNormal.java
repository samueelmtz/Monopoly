// monopoly/interfaces/ConsolaNormal.java
package monopoly.interfaces;

import java.util.Scanner;

public class ConsolaNormal implements Consola {
    private static final Scanner scanner = new Scanner(System.in);

    public ConsolaNormal() {
    }

    @Override
    public void imprimir(String mensaje) {
        System.out.println(mensaje);
    }

    @Override
    public String leer(String descripcion) {
        System.out.print(descripcion);
        return scanner.nextLine();
    }
}
