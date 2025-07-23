/*package jogocalculadora;*/

public class RankingEntry {
    private final String nome;
    private final int vitorias;

    public RankingEntry(String nome, int vitorias) {
        this.nome = nome;
        this.vitorias = vitorias;
    }

    public String getNome() {
        return nome;
    }

    public int getVitorias() {
        return vitorias;
    }
}
