package admeguimas.arvorebmais;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        executar(4, scan);
        System.out.println();
        System.out.println();
        executar(5, scan);
    }

    private static void executar(int n, Scanner scan) {
        ArvoreBMais arvore = new ArvoreBMais(n);
        System.out.println("===== Arvore B+ com n = " + n + " =====");

        System.out.println("Digite um número de inserções:");
        int num = scan.nextInt();
        System.out.println();

        for (int i = 1; i <= num; i++)
            arvore.inserir(i);
        exibir(arvore);
        System.out.println();

        System.out.println("Exclusão de elementos pares:");
        for (int i = 2; i <= num; i = i + 2)
            arvore.exclusao(i);
        exibir(arvore);
        System.out.println();

        System.out.println("Exclusão de elementos ímpares:");
        for (int i = 1; i <= num; i = i + 2)
            arvore.exclusao(i);
        exibir(arvore);
        System.out.println();
    }

    private static void exibir(ArvoreBMais arvore) {
        System.out.print("Folhas: ");
        arvore.exibirFolhas();
        System.out.println();
        System.out.print("In-ordem: ");
        arvore.inOrdem();
        System.out.println();
    }
}
