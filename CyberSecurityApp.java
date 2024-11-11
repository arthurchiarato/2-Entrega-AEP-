package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class CyberSecurityApp {
    private JFrame frame;
    private JTextArea inputArea;
    private JTextArea resultArea;
    private JTextArea historyArea;
    private ArrayList<String> history;

    public CyberSecurityApp() {
        history = new ArrayList<>();
        frame = new JFrame("Segurança Digital para Idosos");
        frame.setSize(700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Cores da interface
        Color backgroundColor = new Color(240, 248, 255); // Alice Blue
        Color panelColor = new Color(255, 255, 255); // Branco
        Color instructionColor = new Color(173, 216, 230); // Light Blue

        // Tela de boas-vindas
        JTextArea welcomeArea = new JTextArea();
        welcomeArea.setText("Bem-vindo ao App de Segurança Digital!\n\n" +
                "Digite um texto suspeito e pressione Enter ou clique em 'Verificar'.\n" +
                "Fique atento às mensagens de alerta!\n");
        welcomeArea.setEditable(false);
        welcomeArea.setBackground(instructionColor);
        welcomeArea.setBorder(BorderFactory.createTitledBorder("Instruções"));
        welcomeArea.setLineWrap(true);
        welcomeArea.setWrapStyleWord(true);
        welcomeArea.setFont(new Font("Arial", Font.PLAIN, 14));

        // Painel de entrada (com FlowLayout alinhado à esquerda)
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(panelColor);
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Alinha os botões à esquerda
        JLabel label = new JLabel("Digite o texto suspeito:");
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Aumentando o tamanho do campo de texto
        inputArea = new JTextArea(12, 50); // Aumentando para 12 linhas e 50 colunas
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setBackground(Color.WHITE);
        inputArea.setFont(new Font("Arial", Font.PLAIN, 14));
        inputArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Botões: Verificar e Limpar
        JButton checkButton = createStyledButton("Verificar", new Color(50, 205, 50)); // Verde
        JButton clearButton = createStyledButton("Limpar", new Color(255, 69, 0)); // Vermelho

        inputPanel.add(label);
        inputPanel.add(new JScrollPane(inputArea)); // Adiciona a rolagem caso necessário
        inputPanel.add(Box.createVerticalStrut(10)); // Ajuste o espaçamento entre componentes
        inputPanel.add(checkButton);
        inputPanel.add(Box.createVerticalStrut(5));
        inputPanel.add(clearButton);

        // Área de resultados e histórico
        resultArea = new JTextArea(5, 40);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBackground(Color.WHITE);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        resultArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        historyArea = new JTextArea(10, 40);
        historyArea.setEditable(false);
        historyArea.setBackground(Color.WHITE);
        historyArea.setFont(new Font("Arial", Font.PLAIN, 14));
        historyArea.setBorder(BorderFactory.createTitledBorder("Histórico de Verificações"));

        // Dicas de Segurança
        JTextArea tipsArea = new JTextArea();
        tipsArea.setText(".Dicas de Segurança:\n" +
                "- Não clique em links suspeitos.\n" +
                "- Nunca compartilhe senhas.\n" +
                "- Use autenticação de dois fatores.\n");
        tipsArea.setEditable(false);
        tipsArea.setBackground(instructionColor);
        tipsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        tipsArea.setBorder(BorderFactory.createTitledBorder("Dicas de Segurança"));

        // Adicionando componentes ao frame
        frame.add(welcomeArea, BorderLayout.NORTH);
        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(tipsArea, BorderLayout.WEST);
        frame.add(resultArea, BorderLayout.EAST);
        frame.add(historyArea, BorderLayout.SOUTH);

        // Adiciona eventos aos botões
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verifyInput();
            }
        });

        // Verifica ao pressionar Enter
        inputArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume(); // Impede a nova linha
                    verifyInput();
                }
            }
        });

        // Limpar histórico e campos
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputArea.setText("");
                resultArea.setText("");
                history.clear();
                updateHistory();
                JOptionPane.showMessageDialog(frame, "Histórico e campo de entrada limpos!");
            }
        });

        frame.getContentPane().setBackground(backgroundColor);
        frame.setVisible(true);
    }

    // Método para criar botões estilizados
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false); // Remove o contorno de foco
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(backgroundColor.darker(), 2), // Borda sutil
                BorderFactory.createEmptyBorder(10, 20, 10, 20) // Aumenta o espaçamento interno
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Cursor de mão

        // Efeito hover (passar o mouse)
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker()); // Torna a cor um pouco mais escura
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor); // Volta à cor original
            }
        });

        return button;
    }

    private void verifyInput() {
        String inputText = inputArea.getText();
        String result = checkForFraud(inputText);
        setResultText(result);
        history.add(inputText + " : " + result);
        updateHistory();
        inputArea.setText(""); // Limpa o campo de entrada após a verificação
    }

    private String checkForFraud(String text) {
        if (text.contains("clique aqui") || text.contains("senha") || text.contains("urgente")) {
            return "Cuidado! Esse texto pode ser uma tentativa de fraude.";
        } else {
            return "Texto seguro. Mas continue atento às ameaças!";
        }
    }

    private void setResultText(String result) {
        resultArea.setText(result);
        if (result.contains("Cuidado")) {
            resultArea.setForeground(Color.RED); // Texto de alerta em vermelho
        } else {
            resultArea.setForeground(Color.GREEN); // Texto seguro em verde
        }
    }

    private void updateHistory() {
        StringBuilder historyText = new StringBuilder();
        for (String entry : history) {
            historyText.append(entry).append("\n");
        }
        historyArea.setText(historyText.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CyberSecurityApp::new);
    }
}
