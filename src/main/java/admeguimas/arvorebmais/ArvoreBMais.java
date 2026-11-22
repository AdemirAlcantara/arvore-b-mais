package admeguimas.arvorebmais;

public class ArvoreBMais {

    private No raiz;
    private int n; // ordem
    private int m; // minimo de valores num no nao-raiz para facilitar as implementações

    public ArvoreBMais(int n) {
        this.n = n;
        this.m = (n - 1) / 2;
        raiz = null;
    }

    public No getRaiz() {
        return raiz;
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    private No navegarAteFolha(int info) {
        if (raiz != null) {
            No no = raiz;
            while (no.getvLig(0) != null)
                no = no.getvLig(no.buscaPosInterno(info));
            return no;
        }
        return null;
    }

    private No navegarAteFolha(int info, Pilha pilha) {
        if (raiz != null) {
            No no = raiz;
            while (no.getvLig(0) != null) {
                pilha.push(no);
                no = no.getvLig(no.buscaPosInterno(info));
            }
            return no;
        }
        return null;
    }

    public No buscaNo(int info) {
        if (raiz != null) {
            No folha = navegarAteFolha(info);
            int pos = folha.buscaPos(info);
            if (pos < folha.getTl() && folha.getvInfo(pos) == info)
                return folha;
        }
        return null;
    }

    public No buscarPai(No folha, int info) {
        No no = raiz, noPai = raiz;
        while (no != null && no != folha) {
            noPai = no;
            no = no.getvLig(no.buscaPosInterno(info));
        }
        return noPai;
    }

    public void inserir(int info) {
        if (raiz == null)
            raiz = new No(n, info);
        else {
            Pilha pilha = new Pilha();
            No folha = navegarAteFolha(info, pilha);
            int pos = folha.buscaPos(info);
            if (pos == folha.getTl() || folha.getvInfo(pos) != info) {
                folha.remanejar(pos);
                folha.setvInfo(pos, info);
                folha.setTl(folha.getTl() + 1);
                if (folha.getTl() > n - 1)
                    split(folha, pilha);
            }
        }
    }

    private void split(No no, Pilha pilha) {
        No cx1, cx2, noPai;
        int info, pos, meio, j;
        while (no.getTl() > n - 1) {
            cx1 = new No(n);
            cx2 = new No(n);
            if (no.getvLig(0) == null) {
                meio = n / 2;
                for (int i = 0; i < meio; i++)
                    cx1.setvInfo(i, no.getvInfo(i));
                cx1.setTl(meio);

                j = 0;
                for (int i = meio; i < no.getTl(); i++) {
                    cx2.setvInfo(j, no.getvInfo(i));
                    j++;
                }
                cx2.setTl(j);

                cx1.setAnt(no.getAnt());
                if (no.getAnt() != null)
                    no.getAnt().setProx(cx1);
                cx1.setProx(cx2);
                cx2.setAnt(cx1);
                cx2.setProx(no.getProx());
                if (no.getProx() != null)
                    no.getProx().setAnt(cx2);

                info = cx2.getvInfo(0);
            } else {
                for (int i = 0; i < m; i++) {
                    cx1.setvInfo(i, no.getvInfo(i));
                    cx1.setvLig(i, no.getvLig(i));
                }
                cx1.setvLig(m, no.getvLig(m));
                cx1.setTl(m);

                info = no.getvInfo(m);

                j = 0;
                for (int i = m + 1; i < no.getTl(); i++) {
                    cx2.setvInfo(j, no.getvInfo(i));
                    cx2.setvLig(j, no.getvLig(i));
                    j++;
                }
                cx2.setvLig(j, no.getvLig(no.getTl()));
                cx2.setTl(j);
            }

            if (pilha.pilhaVazia()) {
                raiz = new No(n, info);
                raiz.setvLig(0, cx1);
                raiz.setvLig(1, cx2);
                no = raiz;
            } else {
                noPai = pilha.pop();
                pos = noPai.buscaPos(info);
                noPai.remanejar(pos);
                noPai.setvInfo(pos, info);
                noPai.setTl(noPai.getTl() + 1);
                noPai.setvLig(pos, cx1);
                noPai.setvLig(pos + 1, cx2);
                no = noPai;
            }
        }
    }

    public void exclusao(int info) {
        if (raiz != null) {
            Pilha pilha = new Pilha();
            No folha = navegarAteFolha(info, pilha);
            int pos = folha.buscaPos(info);
            if (pos < folha.getTl() && folha.getvInfo(pos) == info) {
                folha.remanejarExclusao(pos);
                folha.setTl(folha.getTl() - 1);
                if (folha == raiz) {
                    if (folha.getTl() == 0)
                        raiz = null;
                } else {
                    if (folha.getTl() < m)
                        redistribuirConcatenar(folha, pilha);
                    atualizarInfoInterna(info);
                }
            }
        }
    }

    private void redistribuirConcatenar(No no, Pilha pilha) {
        No noPai, irmaE, irmaD, esq, dir;
        int posPai, posSep;
        boolean resolvido = false;
        while (!resolvido && no != raiz && no.getTl() < m) {
            noPai = pilha.pop();
            posPai = 0;
            while (posPai <= noPai.getTl() && noPai.getvLig(posPai) != no)
                posPai++;

            irmaE = null;
            irmaD = null;
            if (posPai > 0)
                irmaE = noPai.getvLig(posPai - 1);
            if (posPai < noPai.getTl())
                irmaD = noPai.getvLig(posPai + 1);

            if (irmaE != null && irmaE.getTl() > m) {
                no.remanejar(0);
                if (no.getvLig(0) == null) {
                    no.setvInfo(0, irmaE.getvInfo(irmaE.getTl() - 1));
                    noPai.setvInfo(posPai - 1, no.getvInfo(0));
                } else {
                    no.setvInfo(0, noPai.getvInfo(posPai - 1));
                    no.setvLig(0, irmaE.getvLig(irmaE.getTl()));
                    noPai.setvInfo(posPai - 1, irmaE.getvInfo(irmaE.getTl() - 1));
                    irmaE.setvLig(irmaE.getTl(), null);
                }
                no.setTl(no.getTl() + 1);
                irmaE.setTl(irmaE.getTl() - 1);
                resolvido = true;
            } else if (irmaD != null && irmaD.getTl() > m) {
                if (no.getvLig(0) == null) {
                    no.setvInfo(no.getTl(), irmaD.getvInfo(0));
                    no.setTl(no.getTl() + 1);
                    irmaD.remanejarExclusao(0);
                    irmaD.setTl(irmaD.getTl() - 1);
                    noPai.setvInfo(posPai, irmaD.getvInfo(0));
                } else {
                    no.setvInfo(no.getTl(), noPai.getvInfo(posPai));
                    no.setTl(no.getTl() + 1);
                    no.setvLig(no.getTl(), irmaD.getvLig(0));
                    noPai.setvInfo(posPai, irmaD.getvInfo(0));
                    irmaD.remanejarExclusao(0);
                    irmaD.setTl(irmaD.getTl() - 1);
                }
                resolvido = true;
            } else {
                // sempre junta no Nó da esquerda
                if (irmaE != null) {
                    esq = irmaE;
                    dir = no;
                    posSep = posPai - 1;
                } else {
                    esq = no;
                    dir = irmaD;
                    posSep = posPai;
                }

                if (dir.getvLig(0) == null) {
                    for (int i = 0; i < dir.getTl(); i++) {
                        esq.setvInfo(esq.getTl(), dir.getvInfo(i));
                        esq.setTl(esq.getTl() + 1);
                    }
                    esq.setProx(dir.getProx());
                    if (dir.getProx() != null)
                        dir.getProx().setAnt(esq);
                } else {
                    esq.setvInfo(esq.getTl(), noPai.getvInfo(posSep));
                    esq.setTl(esq.getTl() + 1);
                    for (int i = 0; i < dir.getTl(); i++) {
                        esq.setvLig(esq.getTl(), dir.getvLig(i));
                        esq.setvInfo(esq.getTl(), dir.getvInfo(i));
                        esq.setTl(esq.getTl() + 1);
                    }
                    esq.setvLig(esq.getTl(), dir.getvLig(dir.getTl()));
                }

                noPai.remanejarExclusao(posSep);
                noPai.setTl(noPai.getTl() - 1);
                noPai.setvLig(posSep, esq);

                if (noPai == raiz && noPai.getTl() == 0) {
                    raiz = esq;
                    resolvido = true;
                } else
                    no = noPai;
            }
        }
    }

    private void atualizarInfoInterna(int info) {
        No no = raiz;
        boolean atualizado = false;
        while (!atualizado && no != null && no.getvLig(0) != null) {
            int pos = no.buscaPos(info);
            if (pos < no.getTl() && no.getvInfo(pos) == info) {
                no.setvInfo(pos, menorInfoSub(no.getvLig(pos + 1)));
                atualizado = true;
            } else
                no = no.getvLig(pos);
        }
    }

    private int menorInfoSub(No no) {
        while (no.getvLig(0) != null)
            no = no.getvLig(0);
        return no.getvInfo(0);
    }

    public void exibirFolhas() {
        if (raiz == null)
            System.out.println("(arvore vazia)");
        else {
            No noAtual = raiz;
            while (noAtual.getvLig(0) != null)
                noAtual = noAtual.getvLig(0);

            String saida = "";
            while (noAtual != null) {
                for (int i = 0; i < noAtual.getTl(); i++)
                    saida = saida + noAtual.getvInfo(i) + " ";
                noAtual = noAtual.getProx();
            }
            System.out.println(saida.trim());
        }
    }

    public void inOrdem() {
        if (raiz == null)
            System.out.println("(arvore vazia)");
        else {
            Pilha pilha = new Pilha();
            NoPilha topo;
            No noAtual;
            int pos;
            String saida = "";
            pilha.push(raiz, 0);
            while (!pilha.pilhaVazia()) {
                topo = pilha.popNoPilha();
                noAtual = topo.getInfo();
                pos = topo.getPos();
                if (noAtual.getvLig(0) == null) {
                    for (int i = 0; i < noAtual.getTl(); i++)
                        saida = saida + noAtual.getvInfo(i) + " ";
                } else if (pos <= noAtual.getTl()) {
                    pilha.push(noAtual, pos + 1);
                    pilha.push(noAtual.getvLig(pos), 0);
                }
            }
            System.out.println(saida.trim());
        }
    }
}
