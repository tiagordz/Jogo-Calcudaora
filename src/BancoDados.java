/*package jogocalculadora;*/

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BancoDados {

    private static final String URL = "jdbc:mysql://localhost:3306/jogo_calculadora";
    private static final String USER = "root";
    private static final String PASSWORD = "Root";

    // Conexão com banco de dados
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Insere o nome do jogador, se ainda não existir
    public static void registrarJogador(String nome) {
        String sql = "INSERT IGNORE INTO jogadores (nome) VALUES (?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao registrar jogador: " + e.getMessage());
        }
    }

    // Atualiza ou insere vitória
    public static void registrarVitoria(String nome) {
        String sqlUpdate = "UPDATE ranking_vitorias SET vitorias = vitorias + 1 WHERE nome = ?";
        String sqlInsert = "INSERT INTO ranking_vitorias (nome, vitorias) VALUES (?, 1)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {

            stmt.setString(1, nome);
            int rows = stmt.executeUpdate();

            if (rows == 0) {
                try (PreparedStatement stmt2 = conn.prepareStatement(sqlInsert)) {
                    stmt2.setString(1, nome);
                    stmt2.executeUpdate();
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao registrar vitória: " + e.getMessage());
        }
    }

    // Retorna uma lista com o ranking de vitórias
    public static List<RankingEntry> buscarRankingVitorias() {
        List<RankingEntry> ranking = new ArrayList<>();
        String sql = "SELECT nome, vitorias FROM ranking_vitorias ORDER BY vitorias DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nome = rs.getString("nome");
                int vitorias = rs.getInt("vitorias");
                ranking.add(new RankingEntry(nome, vitorias));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar ranking: " + e.getMessage());
        }

        return ranking;
    }

    // Salva ou atualiza o progresso (fase atual e pontuação)
    public static void salvarProgresso(String nome, int faseAtual, int pontuacao) {
        String sqlUpdate = "UPDATE progresso_jogador SET fase_atual = ?, pontuacao = ? WHERE nome = ?";
        String sqlInsert = "INSERT INTO progresso_jogador (nome, fase_atual, pontuacao) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {

            stmtUpdate.setInt(1, faseAtual);
            stmtUpdate.setInt(2, pontuacao);
            stmtUpdate.setString(3, nome);
            int rows = stmtUpdate.executeUpdate();

            if (rows == 0) {
                try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                    stmtInsert.setString(1, nome);
                    stmtInsert.setInt(2, faseAtual);
                    stmtInsert.setInt(3, pontuacao);
                    stmtInsert.executeUpdate();
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar progresso: " + e.getMessage());
        }
    }

    // Carrega o progresso (fase atual e pontuação) do jogador
    public static int[] carregarProgresso(String nome) {
        String sql = "SELECT fase_atual, pontuacao FROM progresso_jogador WHERE nome = ?";
        int[] progresso = new int[]{1, 0}; // valores padrão: fase 1 e pontuação 0

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    progresso[0] = rs.getInt("fase_atual");
                    progresso[1] = rs.getInt("pontuacao");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao carregar progresso: " + e.getMessage());
        }

        return progresso;
    }
}