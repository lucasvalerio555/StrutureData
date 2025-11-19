import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App extends JFrame {
    private JTextField textField;
    private JPanel stackPanel;
    private Stack<String> stack;

    public App() {
        setTitle("Validador de Expresiones");
        setSize(750, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        stack = new Stack<>();

        // PANEL DE FONDO CON DEGRADADO
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(225, 235, 245),
                    0, h, new Color(200, 220, 240)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        backgroundPanel.setLayout(new BorderLayout(20, 20));
        backgroundPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(backgroundPanel);

        // PANEL SUPERIOR: tarjeta para input
        JPanel cardTop = new RoundedPanel(new Color(255, 255, 255, 230));
        cardTop.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        cardTop.setOpaque(false);
        cardTop.setPreferredSize(new Dimension(700, 210));

        textField = new JTextField(30);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        textField.setForeground(new Color(50, 50, 50));
        textField.setBackground(new Color(245, 245, 245));
        textField.setCaretColor(new Color(70, 130, 180));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cardTop.add(textField);

        JButton validateButton = new JButton("Validar Expresión");
        validateButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        validateButton.setForeground(Color.WHITE);
        validateButton.setBackground(new Color(70, 130, 180));
        validateButton.setFocusPainted(false);
        validateButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        validateButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        validateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                validateButton.setBackground(new Color(90, 150, 200));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                validateButton.setBackground(new Color(70, 130, 180));
            }
        });

        cardTop.add(validateButton);
        backgroundPanel.add(cardTop, BorderLayout.NORTH);

        // PANEL CENTRAL: “tarjeta” de la pila
        JPanel cardCenter = new RoundedPanel(new Color(255, 255, 255, 230));
        cardCenter.setLayout(new BorderLayout());
        cardCenter.setBorder(new EmptyBorder(20, 20, 20, 20));

        stackPanel = new JPanel();
        stackPanel.setLayout(new BoxLayout(stackPanel, BoxLayout.Y_AXIS));
        stackPanel.setOpaque(false);

        JScrollPane scroll = new JScrollPane(stackPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        cardCenter.add(scroll, BorderLayout.CENTER);

        backgroundPanel.add(cardCenter, BorderLayout.CENTER);

        // PANEL INFERIOR: texto informativo
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        JLabel footer = new JLabel("Usa la pila interna para validar tu expresión");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        footer.setForeground(new Color(80, 80, 80));
        bottomPanel.add(footer);

        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Acción del botón
        validateButton.addActionListener(e -> validateExpression());

        setVisible(true);
    }

    // Validar la expresión con las regex que ya tienes
    private void validateExpression() {
        String texto = textField.getText().trim();
        String regexBasico = "[-+*/%()0-9\\s]+";
        if (!texto.matches(regexBasico)) {
            JOptionPane.showMessageDialog(this,
                "La expresión contiene caracteres no permitidos",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        stackPanel.removeAll();
        stack = new Stack<>();
        char[] chars = texto.toCharArray();
        for (char c : chars) {
            stack.push(String.valueOf(c));
            addStackLabel(String.valueOf(c));
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stack.Size(); i++) {
            sb.append(stack.get(i));
        }
        String textoPila = sb.toString();

        String regexErrores =
            "(?:\\bsino\\s+más\\b)"
            + "|(?:[+\\-*/]{2,})"
            + "|(?:\\d+\\s*[+\\-*/]\\s*$)"
            + "|(?:^[+*/]\\s*\\d+)";

        Pattern p = Pattern.compile(regexErrores, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(textoPila);

        if (m.find()) {
            JOptionPane.showMessageDialog(this,
                "Error en la expresión desde la pila",
                "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Expresión válida",
                "OK", JOptionPane.INFORMATION_MESSAGE);
        }

        stackPanel.revalidate();
        stackPanel.repaint();
    }

    private void addStackLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setOpaque(true);
        lbl.setBackground(new Color(70, 130, 180));
        lbl.setForeground(Color.WHITE);
        lbl.setBorder(new EmptyBorder(5, 15, 5, 15));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        stackPanel.add(lbl, 0);
    }

    // Panel con bordes redondeados personalizado
    static class RoundedPanel extends JPanel {
        private Color backgroundColor;
        private int cornerRadius = 20;

        public RoundedPanel(Color bgColor) {
            super();
            backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            int width = getWidth();
            int height = getHeight();
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // sombra sutil
            graphics.setColor(new Color(0, 0, 0, 30));
            graphics.fillRoundRect(4, 4, width - 8, height - 8, arcs.width, arcs.height);

            // panel principal
            graphics.setColor(backgroundColor);
            graphics.fillRoundRect(0, 0, width - 4, height - 4, arcs.width, arcs.height);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }
}

