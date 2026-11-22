package admeguimas.arvorebmais;

public class Pilha {

    private NoPilha topo;

    public Pilha() {
        this.topo = null;
    }

    public Pilha(No info) {
        this.topo = new NoPilha(info);
    }

    public void push(No info) {
        this.topo = new NoPilha(info, topo);
    }

    public void push(No info, int pos) {
        this.topo = new NoPilha(info, pos, topo);
    }

    public No pop() {
        if (!pilhaVazia()) {
            NoPilha aux = topo;
            topo = topo.getProx();
            return aux.getInfo();
        }
        return null;
    }

    public NoPilha popNoPilha() {
        if (!pilhaVazia()) {
            NoPilha aux = topo;
            topo = topo.getProx();
            return aux;
        }
        return null;
    }

    public boolean pilhaVazia() {
        return topo == null;
    }

    public void exibePilha() {
        NoPilha aux = topo;
        while (aux != null) {
            No no = aux.getInfo();
            if (no != null) {
                System.out.print("[");
                for (int i = 0; i < no.getTl(); i++) {
                    if (i > 0) {
                        System.out.print(", ");
                    }
                    System.out.print(no.getvInfo(i));
                }
                System.out.println("] (pos=" + aux.getPos() + ")");
            }
            aux = aux.getProx();
        }
    }
}
