package admeguimas.arvorebmais;

public class No {

    private int[] vInfo;
    private No[] vLig;
    private int tl;
    private No prox;
    private No ant;

    public No(int n) {
        vInfo = new int[n];
        vLig = new No[n + 1];
        tl = 0;
        prox = null;
        ant = null;
    }

    public No(int n, int info) {
        this(n);
        vInfo[0] = info;
        tl = 1;
    }

    public int buscaPos(int info) {
        int pos = 0;
        while (pos < tl && info > vInfo[pos])
            pos++;
        return pos;
    }

    public int buscaPosInterno(int info) {
        int pos = 0;
        while (pos < tl && info >= vInfo[pos])
            pos++;
        return pos;
    }

    public void remanejar(int pos) {
        vLig[tl + 1] = vLig[tl];
        for (int i = tl; i > pos; i--) {
            vInfo[i] = vInfo[i - 1];
            vLig[i] = vLig[i - 1];
        }
    }

    public void remanejarExclusao(int pos) {
        for (int i = pos; i < tl - 1; i++) {
            vInfo[i] = vInfo[i + 1];
            vLig[i] = vLig[i + 1];
        }
        vLig[tl - 1] = vLig[tl];
        vLig[tl] = null;
    }

    public int getvInfo(int p) {
        return vInfo[p];
    }

    public void setvInfo(int p, int info) {
        vInfo[p] = info;
    }

    public No getvLig(int p) {
        return vLig[p];
    }

    public void setvLig(int p, No lig) {
        vLig[p] = lig;
    }

    public int getTl() {
        return tl;
    }

    public void setTl(int tl) {
        this.tl = tl;
    }

    public No getProx() {
        return prox;
    }

    public void setProx(No prox) {
        this.prox = prox;
    }

    public No getAnt() {
        return ant;
    }

    public void setAnt(No ant) {
        this.ant = ant;
    }
}
