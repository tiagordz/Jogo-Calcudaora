/*package jogocalculadora;*/

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class Ranking extends JFrame {

    private JTable tabela;
    private DefaultTableModel modelo;
    private static final Font FONTE_TITULO = new Font("SansSerif", Font.BOLD, 28);
    private static final Font FONTE_TABELA = new Font("SansSerif", Font.PLAIN, 16);
    private static final Color COR_CABECALHO = new Color(59, 130, 246);
    private static final Color COR_TABELA = new Color(245, 247, 250);
    private static final Color COR_SELECAO = new Color(191, 219, 254);

    private String nomeNovo;
    private int vitoriasNovo;
    private JogoCalculadoraGUI jogoRef;

    public Ranking(JogoCalculadoraGUI jogo, String nome, int vitorias) {
        this.jogoRef = jogo;
        this.nomeNovo = nome;
        this.vitoriasNovo = vitorias;

        configurarJanela();
    }

    public Ranking() {
        this(null, null, -1);
    }

    private void configurarJanela() {
        setTitle("Ranking de Vitórias");
        setSize(540, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        adicionarCabecalho();
        configurarTabela();
        carregarRanking();
        adicionarBotaoVoltar();

        setVisible(true);
    }

    private void adicionarCabecalho() {
        JPanel painelCabecalho = new JPanel();
        painelCabecalho.setBackground(COR_CABECALHO);
        painelCabecalho.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titulo = new JLabel("🏆 Ranking de Vitórias");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(FONTE_TITULO);
        painelCabecalho.add(titulo);
        add(painelCabecalho, BorderLayout.NORTH);
    }

    private void configurarTabela() {
        modelo = new DefaultTableModel(new Object[]{"Posição", "Nome", "Vitórias"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        tabela.setFont(FONTE_TABELA);
        tabela.setRowHeight(36);
        tabela.setBackground(COR_TABELA);
        tabela.setShowGrid(false);
        tabela.setSelectionBackground(COR_SELECAO);
        tabela.setSelectionForeground(Color.BLACK);

        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(centro);
        }

        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 18));
        header.setBackground(COR_CABECALHO);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void adicionarBotaoVoltar() {
        JButton btnVoltar = new JButton("← Voltar para o Jogo");
        btnVoltar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnVoltar.setBackground(new Color(237, 242, 247));
        btnVoltar.setFocusPainted(false);
        btnVoltar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnVoltar.addActionListener(e -> {
            this.dispose();
            if (jogoRef != null) {
                jogoRef.setEnabled(true);
                jogoRef.toFront();
                jogoRef.requestFocus();
            }
        });

        JPanel painel = new JPanel();
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        painel.add(btnVoltar);
        add(painel, BorderLayout.SOUTH);
    }

    // 🔄 Método atualizado para usar List<RankingEntry>
    private void carregarRanking() {
        modelo.setRowCount(0);

        List<RankingEntry> ranking = BancoDados.buscarRankingVitorias();
        int pos = 1;

        for (RankingEntry entry : ranking) {
            String posicaoStr;
            switch (pos) {
                case 1 -> posicaoStr = "🥇";
                case 2 -> posicaoStr = "🥈";
                case 3 -> posicaoStr = "🥉";
                default -> posicaoStr = String.valueOf(pos);
            }

            modelo.addRow(new Object[]{posicaoStr, entry.getNome(), entry.getVitorias()});
            pos++;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Ranking(null, "JogadorTeste", 5));
    }
}
