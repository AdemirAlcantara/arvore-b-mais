package admeguimas.arvorebmais;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ArvoreBMaisTest {

    private static final int total = 300;

    @Test
    void deveCalcularNeM() {
        ArvoreBMais a4 = new ArvoreBMais(4);
        ArvoreBMais a5 = new ArvoreBMais(5);
        assertEquals(4, a4.getN());
        assertEquals(1, a4.getM());
        assertEquals(5, a5.getN());
        assertEquals(2, a5.getM());
    }

    @Test
    void deveEmpilharEDesempilhar() {
        Pilha pilha = new Pilha();
        No no1 = new No(4, 10);
        No no2 = new No(4, 20);
        assertTrue(pilha.pilhaVazia());
        pilha.push(no1);
        pilha.push(no2, 2);
        NoPilha topo = pilha.popNoPilha();
        assertSame(no2, topo.getInfo());
        assertEquals(2, topo.getPos());
        assertSame(no1, pilha.pop());
        assertTrue(pilha.pilhaVazia());
        assertNull(pilha.pop());
    }

    @Test
    void deveReproduzirExemploSlide9ComOrdem4() {
        ArvoreBMais arvore = new ArvoreBMais(4);
        int[] valores = {1, 4, 7, 10, 17, 21, 31, 25, 19, 20, 28, 42};
        boolean[] presente = new boolean[total + 1];
        for (int i = 0; i < valores.length; i++) {
            arvore.inserir(valores[i]);
            presente[valores[i]] = true;
        }

        No raiz = arvore.getRaiz();
        No noEsq = raiz.getvLig(0);
        No noDir = raiz.getvLig(1);
        assertArrayEquals(new int[]{17}, infos(raiz));
        assertArrayEquals(new int[]{7}, infos(noEsq));
        assertArrayEquals(new int[]{20, 25, 31}, infos(noDir));
        assertArrayEquals(new int[]{1, 4}, infos(noEsq.getvLig(0)));
        assertArrayEquals(new int[]{7, 10}, infos(noEsq.getvLig(1)));
        assertArrayEquals(new int[]{17, 19}, infos(noDir.getvLig(0)));
        assertArrayEquals(new int[]{20, 21}, infos(noDir.getvLig(1)));
        assertArrayEquals(new int[]{25, 28}, infos(noDir.getvLig(2)));
        assertArrayEquals(new int[]{31, 42}, infos(noDir.getvLig(3)));
        validar(arvore, presente);
    }

    @Test
    void deveTratarArvoreVaziaRepetidosEInexistentes() {
        for (int n = 4; n <= 5; n++) {
            ArvoreBMais arvore = new ArvoreBMais(n);
            boolean[] presente = new boolean[total + 1];
            arvore.exclusao(10);
            validar(arvore, presente);

            arvore.inserir(10);
            arvore.inserir(10);
            presente[10] = true;
            validar(arvore, presente);

            arvore.exclusao(20);
            validar(arvore, presente);

            arvore.exclusao(10);
            presente[10] = false;
            validar(arvore, presente);
        }
    }

    @Test
    void deveInserirCrescenteEExcluirParesDepoisImpares() {
        for (int n = 4; n <= 5; n++) {
            ArvoreBMais arvore = new ArvoreBMais(n);
            boolean[] presente = new boolean[total + 1];
            for (int i = 1; i <= total; i++) {
                arvore.inserir(i);
                presente[i] = true;
                validar(arvore, presente);
            }
            for (int i = 2; i <= total; i = i + 2) {
                arvore.exclusao(i);
                presente[i] = false;
                validar(arvore, presente);
            }
            for (int i = 1; i <= total; i = i + 2) {
                arvore.exclusao(i);
                presente[i] = false;
                validar(arvore, presente);
            }
            assertNull(arvore.getRaiz());
        }
    }

    @Test
    void deveInserirDecrescenteEExcluirCrescente() {
        for (int n = 4; n <= 5; n++) {
            ArvoreBMais arvore = new ArvoreBMais(n);
            boolean[] presente = new boolean[total + 1];
            for (int i = total; i >= 1; i--) {
                arvore.inserir(i);
                presente[i] = true;
                validar(arvore, presente);
            }
            for (int i = 1; i <= total; i++) {
                arvore.exclusao(i);
                presente[i] = false;
                validar(arvore, presente);
            }
            assertNull(arvore.getRaiz());
        }
    }

    @Test
    void deveExcluirDoFimParaOInicio() {
        for (int n = 4; n <= 5; n++) {
            ArvoreBMais arvore = new ArvoreBMais(n);
            boolean[] presente = new boolean[total + 1];
            for (int i = 1; i <= total; i++) {
                arvore.inserir(i);
                presente[i] = true;
            }
            for (int i = total; i >= 1; i--) {
                arvore.exclusao(i);
                presente[i] = false;
                validar(arvore, presente);
            }
            assertNull(arvore.getRaiz());
        }
    }

    // ---------- auxiliares ----------

    private void validar(ArvoreBMais arvore, boolean[] presente) {
        String esperado = "";
        for (int i = 1; i < presente.length; i++)
            if (presente[i])
                esperado = esperado + i + " ";
        esperado = esperado.trim();
        if (esperado.isEmpty())
            esperado = "(arvore vazia)";

        assertEquals(esperado, capturarSaida(arvore::exibirFolhas));
        assertEquals(esperado, capturarSaida(arvore::inOrdem));

        if (arvore.getRaiz() == null)
            assertEquals("(arvore vazia)", esperado);
        else {
            validarEstrutura(arvore);
            validarEncadeamento(arvore);
        }

        for (int i = 1; i < presente.length; i++)
            assertEquals(presente[i], arvore.buscaNo(i) != null, "buscaNo(" + i + ")");
    }

    private void validarEstrutura(ArvoreBMais arvore) {
        int n = arvore.getN(), m = arvore.getM(), nivelFolha = -1, nivel, minimo;
        Pilha pilha = new Pilha();
        NoPilha topo;
        No no;
        pilha.push(arvore.getRaiz(), 0);
        while (!pilha.pilhaVazia()) {
            topo = pilha.popNoPilha();
            no = topo.getInfo();
            nivel = topo.getPos();

            minimo = m;
            if (no == arvore.getRaiz())
                minimo = 1;
            assertTrue(no.getTl() >= minimo && no.getTl() <= n - 1, "tl invalido: " + no.getTl());

            for (int i = 1; i < no.getTl(); i++)
                assertTrue(no.getvInfo(i - 1) < no.getvInfo(i), "infos fora de ordem");

            if (no.getvLig(0) == null) {
                if (nivelFolha == -1)
                    nivelFolha = nivel;
                assertEquals(nivelFolha, nivel, "folhas em niveis diferentes");
            } else {
                for (int i = 0; i <= no.getTl(); i++) {
                    assertNotNull(no.getvLig(i), "ligacao nula em vLig[" + i + "]");
                    pilha.push(no.getvLig(i), nivel + 1);
                }
                for (int i = no.getTl() + 1; i <= n; i++)
                    assertNull(no.getvLig(i), "ligacao residual em vLig[" + i + "]");
                for (int i = 0; i < no.getTl(); i++) {
                    assertTrue(maiorInfo(no.getvLig(i)) < no.getvInfo(i), "subarvore esquerda invalida");
                    assertTrue(menorInfo(no.getvLig(i + 1)) >= no.getvInfo(i), "subarvore direita invalida");
                }
            }
        }
    }

    private void validarEncadeamento(ArvoreBMais arvore) {
        No noAtual = arvore.getRaiz();
        while (noAtual.getvLig(0) != null)
            noAtual = noAtual.getvLig(0);
        assertNull(noAtual.getAnt());
        while (noAtual.getProx() != null) {
            assertSame(noAtual, noAtual.getProx().getAnt());
            noAtual = noAtual.getProx();
        }
    }

    private int menorInfo(No no) {
        while (no.getvLig(0) != null)
            no = no.getvLig(0);
        return no.getvInfo(0);
    }

    private int maiorInfo(No no) {
        while (no.getvLig(0) != null)
            no = no.getvLig(no.getTl());
        return no.getvInfo(no.getTl() - 1);
    }

    private int[] infos(No no) {
        int[] vet = new int[no.getTl()];
        for (int i = 0; i < no.getTl(); i++)
            vet[i] = no.getvInfo(i);
        return vet;
    }

    private String capturarSaida(Runnable acao) {
        PrintStream original = System.out;
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        System.setOut(new PrintStream(saida));
        acao.run();
        System.setOut(original);
        return saida.toString().trim();
    }
}
