/*package jogocalculadora;*/

import javax.swing.*;
import java.awt.*;

public class TelaLogin {
    private JFrame loginFrame;
    private JTextField txtNome;
    private JLabel lblErro;
    public static String nomeJogador;

    public TelaLogin() {
        loginFrame = new JFrame("C4LCUDA0R4");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(450, 330);
        loginFrame.setResizable(false);
        loginFrame.setLocationRelativeTo(null);

        // Painel com imagem de fundo
        LoginPanel background = new LoginPanel("/jogocalculadora/Imagens/imagem.jpg");
        background.setLayout(null);

        JLabel lblTitulo = new JLabel("C4LCUDA0R4", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblTitulo.setForeground(new Color(0, 102, 204));
        lblTitulo.setBounds(50, 20, 350, 60);
        background.add(lblTitulo);

        JLabel lblNome = new JLabel("SEU NOME:");
        lblNome.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblNome.setForeground(Color.WHITE);
        lblNome.setBounds(60, 110, 330, 25);
        background.add(lblNome);

        txtNome = new JTextField();
        txtNome.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtNome.setBounds(60, 145, 320, 30);
        background.add(txtNome);

        lblErro = new JLabel("Por favor, digite seu nome (mínimo 2 caracteres).");
        lblErro.setForeground(Color.RED);
        lblErro.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblErro.setBounds(60, 180, 320, 20);
        lblErro.setVisible(false);
        background.add(lblErro);

        JButton btnJogar = new JButton("JOGAR");
        btnJogar.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnJogar.setForeground(Color.WHITE);
        btnJogar.setBackground(new Color(0, 153, 76));
        btnJogar.setFocusPainted(false);
        btnJogar.setBounds(150, 210, 130, 40);
        background.add(btnJogar);

        loginFrame.setContentPane(background);
        loginFrame.getRootPane().setDefaultButton(btnJogar);

        btnJogar.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            if (nome.isEmpty() || nome.length() < 2) {
                lblErro.setVisible(true);
            } else {
                lblErro.setVisible(false);

                // 👉 Armazena o nome globalmente
                nomeJogador = nome;

                // 👉 Registra o jogador no banco de dados
                BancoDados.registrarJogador(nome);

              // Inicia o jogo passando o nome
                new JogoCalculadoraGUI(nomeJogador, 0); // vitorias inicia em 0
                loginFrame.dispose();
            }
        });

        loginFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaLogin::new);
    }
}

        