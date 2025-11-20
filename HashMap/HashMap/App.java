import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class App extends JFrame {

    private JTextField searchField;
    private JPanel cardsContainer, topPanel;
    private JScrollPane scrollPane;
    private HashMap<String, String> hashmap;
    private String filename = "/home/luanvape20/Desktop/structurData/HashMap/HashMap/SearchData.txt";

    // Variables para mover la ventana
    private int mouseX, mouseY;

    public App() {
        setTitle("Buscador de Usuarios");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // ✅ Quitar los bordes tradicionales
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top panel (barra de título personalizada)
        topPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(45, 45, 45);
                Color color2 = new Color(70, 70, 70);
                int width = getWidth();
                int height = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, color1, width, 0, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Permitir arrastrar la ventana desde el topPanel
        topPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
        topPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen() - mouseX;
                int y = e.getYOnScreen() - mouseY;
                setLocation(x, y);
            }
        });

        // Botón de cerrar personalizado
        JButton closeButton = new JButton("✕");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(45, 45, 45));
        closeButton.setBorder(null);
        closeButton.setFocusPainted(false);
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> System.exit(0));
        closeButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                closeButton.setBackground(Color.RED);
            }
            public void mouseExited(MouseEvent e) {
                closeButton.setBackground(new Color(45, 45, 45));
            }
        });

        // Campo de búsqueda y botón
        searchField = new JTextField();
        searchField.setFont(new Font("Roboto", Font.PLAIN, 16));
        searchField.setBackground(new Color(60, 60, 60));
        searchField.setForeground(Color.WHITE);
        searchField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JButton searchButton = new JButton("Buscar");
        searchButton.setFont(new Font("Roboto", Font.BOLD, 14));
        searchButton.setBackground(new Color(255, 140, 0));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);

        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setOpaque(false);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(closeButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Contenedor de cards
        cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setBackground(new Color(35, 35, 35));

        scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        readFile();

        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                performSearch();
            }
        });
        searchButton.addActionListener(e -> performSearch());

        performSearch();
    }

    private void readFile() {
        hashmap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    hashmap.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error leyendo el archivo: " + e.getMessage());
        }
    }

    private void performSearch() {
        String query = searchField.getText().trim().toLowerCase();
        cardsContainer.removeAll();

        boolean found = false;
        for (String key : hashmap.keySet()) {
            String value = hashmap.get(key);
            if (key.toLowerCase().contains(query) || value.toLowerCase().contains(query)) {
                cardsContainer.add(createCard(key, value));
                found = true;
            }
        }

        if (!found) {
            cardsContainer.add(createCard("No se encontraron resultados", ""));
        }

        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

    private JPanel createCard(String title, String subtitle) {
        JPanel card = new JPanel();
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setBackground(new Color(50, 50, 50));
        card.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        card.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 16));

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setForeground(new Color(200, 200, 200));
        subtitleLabel.setFont(new Font("Roboto", Font.PLAIN, 14));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(subtitleLabel, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(70, 70, 70));
            }
            public void mouseExited(MouseEvent e) {
                card.setBackground(new Color(50, 50, 50));
            }
        });

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().setVisible(true));
    }
}

