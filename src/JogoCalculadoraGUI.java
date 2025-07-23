/*package jogocalculadora;
*/
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.sql.SQLException;

public class JogoCalculadoraGUI extends JFrame {
    private JLabel lblResultado, lblOperador, lblVidas, lblMensagem, lblPontos, lblFase, lblCronometro, lblVitorias;
    private JTextField txtNum1, txtNum2;
    private JButton btnVerificar, btnSair, btnReiniciar, btnRanking;

    private int vidas = 3;
    private int pontos = 0;
    private double resultadoCorreto;
    private char operadorAtual;
    private double num1, num2;
    private java.util.Set<String> desafiosUsados = new java.util.HashSet<>();
    private Timer timer;
    private int tempoRestante;
    private int vitorias = 0;
    private String nomeJogador;

    public JogoCalculadoraGUI(String nomeJogador, int vitorias) {
        super("JOGO C4LCUD0R4");
        this.nomeJogador = nomeJogador;
        this.vitorias = vitorias;
        
        
        // CARREGAR PROGRESSO DO BANCO
int[] progresso = BancoDados.carregarProgresso(nomeJogador);
int faseCarregada = progresso[0];
int pontosCarregues = progresso[1];
this.pontos = pontosCarregues;
this.ultimaFaseRegistrada = faseCarregada;

        BackgroundPanel background = new BackgroundPanel("/jogocalculadora/Imagens/fundo.jpg");
        background.setLayout(new BorderLayout());
        setContentPane(background);

        JLabel titulo = new JLabel("C4LCUDA0R4");
        titulo.setFont(new Font("Inter", Font.BOLD, 48));
        titulo.setForeground(Color.BLUE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        background.add(titulo, BorderLayout.NORTH);

        JPanel painelCard = new JPanel();
        painelCard.setOpaque(false);
        painelCard.setLayout(new BoxLayout(painelCard, BoxLayout.Y_AXIS));
        painelCard.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
        background.add(painelCard, BorderLayout.CENTER);

        lblResultado = new JLabel("Resultado: ?");
        lblResultado.setFont(new Font("Inter", Font.BOLD, 28));
        lblResultado.setForeground(Color.decode("#374151")); // #374151 is rgb(55,65,81)
        lblResultado.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelCard.add(lblResultado);
        painelCard.add(Box.createVerticalStrut(20));

        JPanel painelConta = new JPanel();
        painelConta.setOpaque(false);
        painelConta.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0));

        lblOperador = new JLabel("?");
        lblOperador.setFont(new Font("Inter", Font.BOLD, 36));
        lblOperador.setForeground(Color.decode("#374151"));
        painelConta.add(lblOperador);

        txtNum1 = new JTextField(5);
        txtNum2 = new JTextField(5);
        Font inputFont = new Font("Inter", Font.PLAIN, 22);
        txtNum1.setFont(inputFont);
        txtNum2.setFont(inputFont);

        Border inputBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#d1d5db")), // #d1d5db roughly rgb(209,213,219)
                BorderFactory.createEmptyBorder(8, 12, 8, 12));
        txtNum1.setBorder(inputBorder);
        txtNum2.setBorder(inputBorder);

        painelConta.add(txtNum1);
        painelConta.add(lblOperador);
        painelConta.add(txtNum2);

        painelCard.add(painelConta);
        painelCard.add(Box.createVerticalStrut(30));

        JPanel painelInfo = new JPanel();
        painelInfo.setOpaque(false);
        painelInfo.setLayout(new GridLayout(1, 5, 40, 0));

        lblVidas = new JLabel("Vidas: ❤ ❤ ❤");
        lblVidas.setFont(new Font("Inter", Font.BOLD, 13));
        lblVidas.setForeground(Color.decode("#ff4500")); // vermelho forte
        lblVidas.setHorizontalAlignment(SwingConstants.CENTER);
        painelInfo.add(lblVidas);

        lblPontos = new JLabel("Pontos: 0");
        lblPontos.setFont(new Font("Inter", Font.BOLD, 15));
        lblPontos.setForeground(Color.decode("#00ff7f")); // verde limão
        lblPontos.setHorizontalAlignment(SwingConstants.CENTER);
        painelInfo.add(lblPontos);

        lblFase = new JLabel("Fase: 1");
        lblFase.setFont(new Font("Inter", Font.BOLD, 15));
        lblFase.setForeground(Color.decode("#0066cc")); // azul vibrante
        lblFase.setHorizontalAlignment(SwingConstants.CENTER);
        painelInfo.add(lblFase);

        lblCronometro = new JLabel("Tempo: 00:00");
        lblCronometro.setFont(new Font("Inter", Font.BOLD, 14));
        lblCronometro.setForeground(Color.decode("#6B21A8")); // amarelo ouro
        lblCronometro.setHorizontalAlignment(SwingConstants.CENTER);
        painelInfo.add(lblCronometro);

        lblVitorias = new JLabel("Vitórias: " + vitorias);
        lblVitorias.setFont(new Font("Inter", Font.BOLD, 15));
        lblVitorias.setForeground(Color.decode("#FFD700")); // verde limão
        lblVitorias.setHorizontalAlignment(SwingConstants.CENTER);
        painelInfo.add(lblVitorias);

        painelCard.add(painelInfo);
        painelCard.add(Box.createVerticalStrut(20));

        lblMensagem = new JLabel("");
        lblMensagem.setFont(new Font("Inter", Font.PLAIN, 20));
        lblMensagem.setForeground(Color.decode("#6b7280"));
        lblMensagem.setHorizontalAlignment(SwingConstants.CENTER);
        painelCard.add(lblMensagem);
        painelCard.add(Box.createVerticalStrut(40));

        JPanel painelBotoes = new JPanel();
        painelBotoes.setOpaque(false);
        painelBotoes.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 0));

        btnVerificar = createStyledButton("Verificar", new Color(0, 100, 0));
        btnReiniciar = createStyledButton("Reiniciar", new Color(0, 0, 255));
        btnSair = createStyledButton("Sair", new Color(220, 38, 38));
        btnRanking = createStyledButton("Ranking", new Color(255, 255, 0));

        painelBotoes.add(btnVerificar);
        painelBotoes.add(btnReiniciar);
        painelBotoes.add(btnSair);
        painelBotoes.add(btnRanking);
        painelCard.add(painelBotoes);

        btnVerificar.addActionListener(e -> verificarResposta());
        btnSair.addActionListener(e -> System.exit(0));
        btnReiniciar.addActionListener(e -> reiniciarJogo());
        btnRanking.addActionListener(e -> abrirRanking());

        gerarNovoDesafio();

        setSize(720, 600);
        setMinimumSize(new Dimension(600, 520));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground().darker());
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);
                g2.dispose();
            }
        };

        button.setFont(new Font("Inter", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void abrirRanking() {
        Ranking ranking = new Ranking(this, nomeJogador, vitorias);
        ranking.setVisible(true);
        this.setEnabled(false); // desabilita a janela do jogo enquanto ranking aberto
    }

    private void gerarNovoDesafio() {
        if (timer != null && timer.isRunning())
            timer.stop();

        Random random = new Random();
        char[] operadores = {'+', '-', '*', '/'};

        int tentativas = 0;
        final int MAX_TENTATIVAS = 100;

        String assinatura;
        char ultimoOperador = ' '; // Variável para armazenar o último operador utilizado

        do {
            // Garante que o operador atual não seja o mesmo que o anterior
            do {
                operadorAtual = operadores[random.nextInt(4)];
            } while (operadorAtual == ultimoOperador); // Verifica se o operador é o mesmo que o anterior

            num1 = random.nextDouble() * 50; // Gera um número entre 0 e 1000
            num2 = random.nextDouble() * 50; // Gera um número entre 0 e 1000
            if (operadorAtual == '/' && num2 == 0) num2 = 1; // Evita divisão por zero

            switch (operadorAtual) {
                case '+' -> resultadoCorreto = num1 + num2;
                case '-' -> resultadoCorreto = num1 - num2;
                case '*' -> resultadoCorreto = num1 * num2;
                case '/' -> resultadoCorreto = num1 / num2;
            }

            assinatura = String.format("%.2f%c%.2f", num1, operadorAtual, num2);
            tentativas++;

        } while ((desafiosUsados.contains(assinatura) || Math.round(resultadoCorreto) == 1) && tentativas < MAX_TENTATIVAS);

        desafiosUsados.add(assinatura);
        ultimoOperador = operadorAtual; // Atualiza o último operador utilizado

        int resultadoFormatado = (int) Math.round(resultadoCorreto);

        String operadorExibicao = switch (operadorAtual) {
            case '+' -> "+";
            case '-' -> "-";
            case '*' -> "×";
            case '/' -> "÷";
            default -> "?";
        };

        lblResultado.setText("Resultado: " + resultadoFormatado);
        lblOperador.setText(operadorExibicao);
        lblMensagem.setText("");
        txtNum1.setText("");
        txtNum2.setText("");
        atualizarFase();
        iniciarCronometro();
    }

    private void iniciarCronometro() {
        int fase = (pontos / 5) + 1;
        tempoRestante = Math.max(60 - (fase - 1) * 20, 10);

        lblCronometro.setText("Tempo: " + formatarTempo(tempoRestante));
        timer = new Timer(1000, e -> {
            tempoRestante--;
            lblCronometro.setText("Tempo: " + formatarTempo(tempoRestante));
            if (tempoRestante <= 0) {
                timer.stop();
                lblMensagem.setText("⏰ Tempo esgotado! Fim de jogo.");
                btnVerificar.setEnabled(false);
            }
        });
        timer.start();
    }

    private String formatarTempo(int segundosTotais) {
        int minutos = segundosTotais / 60;
        int segundos = segundosTotais % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }

    private void verificarResposta() {
        try {
            double tentativa1 = Double.parseDouble(txtNum1.getText().replace(',', '.'));
            double tentativa2 = Double.parseDouble(txtNum2.getText().replace(',', '.'));
            boolean acertou = switch (operadorAtual) {
                case '+' -> Math.round(tentativa1 + tentativa2) == Math.round(resultadoCorreto);
                case '-' -> Math.round(tentativa1 - tentativa2) == Math.round(resultadoCorreto);
                case '*' -> Math.round(tentativa1 * tentativa2) == Math.round(resultadoCorreto);
                case '/' -> Math.round(tentativa1 / tentativa2) == Math.round(resultadoCorreto);
                default -> false;
            };
            if (acertou) {
                pontos++;
                atualizarPontos();
                lblMensagem.setText("✅ Você acertou!");
                gerarNovoDesafio();
            } else {
                vidas--;
                atualizarVidas();
                if (vidas == 0) {
                    lblMensagem.setText("💀 Fim de jogo!");
                    btnVerificar.setEnabled(false);
                    if (timer != null) timer.stop();
                } else {
                    lblMensagem.setText("❌ Errado! Tente novamente.");
                }
            }
        } catch (NumberFormatException ex) {
            lblMensagem.setText("⚠️ Digite números válidos!");
        }
    }

    private void atualizarVidas() {
        StringBuilder coracoes = new StringBuilder("Vidas: ");
        for (int i = 0; i < vidas; i++) coracoes.append("♥ ");
        lblVidas.setText(coracoes.toString().trim());
    }

    private void atualizarPontos() {
        lblPontos.setText("Pontos: " + pontos);
    }

    private int ultimaFaseRegistrada = 0;

    // [O CÓDIGO COMPLETO FOI MANTIDO IGUAL AO QUE VOCÊ ENVIOU, COM ESTA ÚNICA ALTERAÇÃO NO MÉTODO:]

private void atualizarFase() {
    int faseAtual = (pontos / 5) + 1;
    lblFase.setText("Fase: " + faseAtual);
    
    try {
    BancoDados.salvarProgresso(nomeJogador, faseAtual, pontos);
} catch (Exception e) {
    System.err.println("Erro ao salvar progresso: " + e.getMessage());
}

if (faseAtual % 5 == 0 && faseAtual != ultimaFaseRegistrada) {

    if (faseAtual % 5 == 0 && faseAtual != ultimaFaseRegistrada) {
        ultimaFaseRegistrada = faseAtual;
        vitorias++;
        lblVitorias.setText("Vitórias: " + vitorias);
        lblMensagem.setText("Você conquistou uma vitória");

        // 🔽 REGISTRA A VITÓRIA NO BANCO DE DADOS
        if (nomeJogador != null && !nomeJogador.isEmpty()) {
            try {
                BancoDados.registrarVitoria(nomeJogador); // método que incrementa +1 na tabela MySQL
            } catch (Exception e) {
                System.err.println("Erro ao registrar vitória no banco: " + e.getMessage());
            }
        }
    }
}

}

    private void reiniciarJogo() {
        vidas = 3;
        pontos = 0;
        desafiosUsados.clear();
        btnVerificar.setEnabled(true);
        atualizarVidas();
        atualizarPontos();
        gerarNovoDesafio();
        lblMensagem.setText("Novo jogo iniciado!");
    }

    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                var is = getClass().getResourceAsStream(imagePath);
                if (is != null) {
                    backgroundImage = ImageIO.read(is);
                } else {
                    System.err.println("Imagem não encontrada: " + imagePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private static class DropShadowBorder extends AbstractBorder {
        private final Color shadowColor;
        private final int shadowSize;
        private final int shadowOpacity;
        private final float shadowSpread;
        private final int cornerSize;
        private final boolean topShadow, leftShadow, bottomShadow, rightShadow;

        public DropShadowBorder(Color shadowColor, int shadowSize, int shadowOpacityPercent, float shadowSpread, int cornerSize,
                                boolean topShadow, boolean leftShadow, boolean bottomShadow, boolean rightShadow) {
            this.shadowColor = new Color(shadowColor.getRed(), shadowColor.getGreen(), shadowColor.getBlue(), (int) (255 * (shadowOpacityPercent / 100f)));
            this.shadowSize = shadowSize;
            this.shadowOpacity = shadowOpacityPercent;
            this.shadowSpread = shadowSpread;
            this.cornerSize = cornerSize;
            this.topShadow = topShadow;
            this.leftShadow = leftShadow;
            this.bottomShadow = bottomShadow;
            this.rightShadow = rightShadow;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int shadowAlpha = 50;
            Color shadowCol = new Color(0, 0, 0, shadowAlpha);

            g2.setColor(shadowCol);
            g2.fillRoundRect(
                    x + shadowSize,
                    y + shadowSize,
                    width - shadowSize,
                    height - shadowSize,
                    cornerSize, cornerSize);

            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(shadowSize, shadowSize, shadowSize, shadowSize);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = shadowSize;
            return insets;
        }
    }
}