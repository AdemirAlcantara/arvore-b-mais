package admeguimas.arvorebmais;

public class NoPilha {

    private No info;
    private int pos;
    private NoPilha prox;

    public NoPilha(No info) {
        this(info, 0, null);
    }

    public NoPilha(No info, NoPilha prox) {
        this(info, 0, prox);
    }

    public NoPilha(No info, int pos, NoPilha prox) {
        this.info = info;
        this.pos = pos;
        this.prox = prox;
    }

    public No getInfo() {
        return info;
    }

    public void setInfo(No info) {
        this.info = info;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public NoPilha getProx() {
        return prox;
    }

    public void setProx(NoPilha prox) {
        this.prox = prox;
    }
}
