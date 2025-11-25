import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.util.LinkedList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public class Snake extends JFrame {

    // Variable para la cola (aunque no se usa en la lógica de juego simple, la mantengo)
    private Queues<String> messageQueue = null;
    
    // El score es propiedad de la clase principal para ser accesible
    private int score = 0; 
    
    private Board gamePanel = null; 

    // --- Constantes del Juego ---
    private static final int GAME_WIDTH = 600;
    private static final int GAME_HEIGHT = 600; 
    private static final int ALL_DOTS = 3600; // Máximo de segmentos (60x60)
    private static final int DOT_SIZE = 10;     // Tamaño de cada segmento (10x10 píxeles)
    private static final int RANDOM_POS = 60; // Max coord for apple (600 / 10)

    /**
     * Clase interna para la superficie de dibujo del juego.
     * Extiende JPanel e implementa ActionListener para el bucle del juego.
     */
    private class Board extends JPanel implements ActionListener {
        private Timer timer;
        private final int x[] = new int[ALL_DOTS]; // Coordenadas X de los segmentos
        private final int y[] = new int[ALL_DOTS]; // Coordenadas Y de los segmentos
        private int dots; // Longitud actual de la serpiente
        
        // Variables de estado y dirección
        private boolean movingLeft = false;
        private boolean movingRight = false; 
        private boolean movingUp = false;
        private boolean movingDown = false;
        private boolean inGame = false; // El juego empieza en el menú
        
        // Coordenadas de la comida
        private int apple_x;
        private int apple_y;

        // Variables para el botón de START
        private int buttonX, buttonY, buttonWidth, buttonHeight;


        public Board() {
            // Set up the panel
            setBackground(Color.BLACK);
            setForeground(Color.WHITE); 

            // Add listeners
            setFocusable(true);
            addKeyListener(new TAdapter());
            addMouseListener(new MAdapter());

            // Timer for the game loop (100ms update rate)
            timer = new Timer(100, this);
            timer.stop(); // El timer no debe empezar hasta que se presione START
        }

        /**
         * Inicializa la posición de la serpiente y la manzana.
         */
        private void initGame() {
            // 1. Resetear el estado del juego
            inGame = true;
            dots = 3; // Longitud inicial
            Snake.this.score = 0; // Resetear el score de la clase principal

            // 2. Inicializar la posición de la serpiente (en el centro)
            for (int i = 0; i < dots; i++) {
                // Posicionar la cabeza y los segmentos iniciales
                x[i] = 200 - i * DOT_SIZE;
                y[i] = 200;
            }

            // 3. Resetear la dirección (empezar a moverse a la derecha)
            movingLeft = false;
            movingRight = true; 
            movingUp = false;
            movingDown = false;

            // 4. Colocar la comida inicial
            locateApple();

            // 5. Iniciar el Timer
            if (!timer.isRunning()) {
                timer.start();
            }
        }

        /**
         * Coloca la manzana en una posición aleatoria de la cuadrícula.
         */
        private void locateApple() {
            int r = (int) (Math.random() * RANDOM_POS);
            apple_x = ((r * DOT_SIZE)); 

            r = (int) (Math.random() * RANDOM_POS);
            apple_y = ((r * DOT_SIZE));
            
            // Asegurar que la manzana no caiga en el cuerpo de la serpiente
            for (int i = 0; i < dots; i++) {
                if (x[i] == apple_x && y[i] == apple_y) {
                    locateApple(); // Rerun if position clashes
                    return;
                }
            }
        }

        /**
         * Comprueba si la cabeza de la serpiente ha comido la manzana.
         */
        private void checkApple() {
            if ((x[0] == apple_x) && (y[0] == apple_y)) {
                dots++; 
                Snake.this.score++; 
                locateApple(); 
            }
        }

        /**
         * Mueve la serpiente: cada segmento toma la posición del anterior.
         */
        private void move() {
            // Mueve los segmentos del cuerpo
            for (int i = dots; i > 0; i--) {
                x[i] = x[i - 1]; 
                y[i] = y[i - 1]; 
            }

            // Mueve la cabeza (x[0], y[0])
            if (movingLeft) {
                x[0] -= DOT_SIZE;
            }
            if (movingRight) {
                x[0] += DOT_SIZE;
            }
            if (movingUp) {
                y[0] -= DOT_SIZE;
            }
            if (movingDown) {
                y[0] += DOT_SIZE;
            }
        }

        /**
         * Comprueba colisiones con los bordes o consigo misma.
         */
        private void checkCollision() {
            // Colisión con sí mismo (cabeza choca con el cuerpo)
            for (int i = dots; i > 0; i--) {
                if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                    inGame = false;
                }
            }

            // Colisión con los bordes
            if (y[0] >= GAME_HEIGHT || y[0] < 0 || x[0] >= GAME_WIDTH || x[0] < 0) {
                inGame = false;
            }

            if (!inGame) {
                timer.stop();
            }
        }


        // --- Bucle principal del juego (Game Loop) ---
        @Override
        public void actionPerformed(ActionEvent e) {
            if (inGame) {
                checkApple();
                checkCollision();
                if(inGame) { // Mover solo si no ha habido colisión
                     move(); 
                }
            }
            // Llama a paintComponent para redibujar la pantalla
            repaint();
        }

        // --- Lógica de Dibujo ---
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if (inGame) {
                doDrawing(g); 
            } else {
                drawingMainFrameMenuGame(g);
                // Sincroniza la pintura para evitar problemas de parpadeo
                Toolkit.getDefaultToolkit().sync();
            }
        }

        /**
         * Dibuja los elementos del juego (serpiente, manzana, score).
         */
        private void doDrawing(Graphics g) {
            // 1. Dibujar el Score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Monospaced", Font.PLAIN, 16));
            g.drawString("Score: " + Snake.this.score, 10, 20); 

            // 2. Dibujar la Comida (Apple)
            g.setColor(Color.RED); 
            g.fillOval(apple_x, apple_y, DOT_SIZE, DOT_SIZE); 

            // 3. Dibujar la Serpiente
            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    // Cabeza
                    g.setColor(Color.GREEN.darker()); 
                } else {
                    // Cuerpo
                    g.setColor(Color.GREEN); 
                }
                g.fillRect(x[i], y[i], DOT_SIZE, DOT_SIZE);
            }
        }

        /**
         * Dibuja el menú principal o la pantalla de Game Over.
         */
        private void drawingMainFrameMenuGame(Graphics g){
            // --- 1. Dibujar Título ---
            String title = "SNAKE RETRO";
            g.setColor(new Color(255, 255, 0)); 
            g.setFont(new Font("Monospaced", Font.BOLD, 64)); 
            
            FontMetrics fmTitle = g.getFontMetrics();
            int xTitle = (GAME_WIDTH - fmTitle.stringWidth(title)) / 2;
            int yTitle = GAME_HEIGHT / 4; 
            g.drawString(title, xTitle, yTitle);

            // --- 2. Dibujar Botón de "START" (o mensaje de reinicio) ---
            String option1;
            if (Snake.this.score > 0) {
                // Game Over screen
                option1 = "GAME OVER! Final Score: " + Snake.this.score;
                g.setFont(new Font("Monospaced", Font.BOLD, 24));
                g.setColor(Color.RED);
            } else {
                // Main Menu screen
                option1 = "START GAME";
                g.setFont(new Font("Monospaced", Font.BOLD, 32));
                g.setColor(Color.WHITE);
            }

            FontMetrics fmMenu = g.getFontMetrics();
            int textWidth = fmMenu.stringWidth(option1);
            int textHeight = fmMenu.getHeight();

            // Dimensiones y posición del rectángulo/botón
            buttonWidth = textWidth + 40; 
            buttonHeight = textHeight + 10;
            buttonX = (GAME_WIDTH - buttonWidth) / 2;
            buttonY = (GAME_HEIGHT / 2) - (buttonHeight / 2) + 20; 

            // Dibuja el borde
            g.setColor(new Color(50, 50, 50)); 
            g.fillRect(buttonX + 4, buttonY + 4, buttonWidth, buttonHeight);

            // Dibuja el cuerpo del botón
            g.setColor(new Color(0, 150, 0)); 
            g.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);

            // Dibuja el texto del botón
            g.setColor(Color.WHITE); 
            g.setFont(new Font("Monospaced", Font.BOLD, 32)); // Vuelve a la fuente de 32 para el texto central
            int xText = buttonX + (buttonWidth - textWidth) / 2;
            int yText = buttonY + textHeight - 4; 
            g.drawString(option1, xText, yText);


            // --- 3. Dibujar Opción "QUIT/START Hint" ---
            String hint = "Press SPACE or Click to Start/Restart";
            String quit = "Press ESC to Quit";
            g.setFont(new Font("Monospaced", Font.PLAIN, 18));
            fmMenu = g.getFontMetrics();
            
            g.setColor(Color.YELLOW);
            int xHint = (GAME_WIDTH - fmMenu.stringWidth(hint)) / 2;
            int yHint = buttonY + buttonHeight + 40; 
            g.drawString(hint, xHint, yHint);

            g.setColor(Color.RED); 
            int xOption2 = (GAME_WIDTH - fmMenu.stringWidth(quit)) / 2;
            int yOption2 = yHint + 30; // Debajo del hint
            g.drawString(quit, xOption2, yOption2);
        }

        // --- CLASE ADAPTADORA PARA EL RATÓN ---
        private class MAdapter extends MouseAdapter {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!inGame) {
                    int mx = e.getX();
                    int my = e.getY();

                    // Comprueba si el click está dentro de los límites del "botón"
                    if (mx >= buttonX && mx <= buttonX + buttonWidth &&
                        my >= buttonY && my <= buttonY + buttonHeight) {
                        
                        initGame(); 
                        requestFocusInWindow(); 
                    }
                }
            }
        }   

        // Inner class to handle keyboard events
        private class TAdapter extends KeyAdapter {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                // Lógica de movimiento (prohíbe el giro de 180 grados)
                if ((key == KeyEvent.VK_LEFT) && (!movingRight)) {
                    movingLeft = true;
                    movingUp = false;
                    movingDown = false;
                }

                if ((key == KeyEvent.VK_RIGHT) && (!movingLeft)) {
                    movingRight = true;
                    movingUp = false;
                    movingDown = false;
                }

                if ((key == KeyEvent.VK_UP) && (!movingDown)) {
                    movingUp = true;
                    movingRight = false;
                    movingLeft = false;
                }

                if ((key == KeyEvent.VK_DOWN) && (!movingUp)) {
                    movingDown = true;
                    movingRight = false;
                    movingLeft = false;
                }

                // Tecla ESC para salir
                if (key == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
        
                // Iniciar/Reiniciar juego con SPACE si no está en juego
                if (key == KeyEvent.VK_SPACE && !inGame) {
                    initGame();
                    repaint();
                }
            }
        }
    }

    // Constructor de la clase Snake (JFrame)
    public Snake() {
        messageQueue = new Queues<>(); 
        gamePanel = new Board();
        
        // Configurar el JFrame
        add(gamePanel); 
        
        setTitle("Snake Retro Game"); 
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null); 
        setResizable(false);
        setVisible(true);
    }
    
    // El método main para iniciar la aplicación
    public static void main(String[] args) {
        // Ejecutar la interfaz gráfica en el Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(() -> {
            new Snake();
        });
    }
}
