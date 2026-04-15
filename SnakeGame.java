import java.awt.*;
import java.awt.event.*;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.Timer;


// cool snake game


public class SnakeGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snake Starter");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 700);
            frame.setLocationRelativeTo(null);
            frame.add(new SnakeGame().new SnakeStarter());
            frame.setVisible(true);
        });
    }

    public class SnakeStarter extends JPanel {
        // Grid configuration: 28 x 28 cells with 25 pixels per cell = 700 x 700 pixel game area
        private final int gridSize = 28;
        private final int cellSize = 25;
        private ArrayList<Point> snake;
        private java.util.HashMap<Point, Color> segmentColors;  // Track color for each segment
        private Color currentGrowthColor;  // Color for next growth segment
        private Point direction = new Point(1, 0);  // Moving right
        private Point nextDirection = new Point(1, 0);
        private Timer gameTimer;
        private Point food;
        private int score = 0;
        private boolean gameOver = false;
        private Random random = new Random();

        public SnakeStarter() {
            setBackground(Color.BLACK);
            setFocusable(true);
            requestFocusInWindow();
            initializeSnake();
            spawnFood();
            setupKeyListener();
            startTimer();
        }

        private void initializeSnake() {
            snake = new ArrayList<>();
            segmentColors = new java.util.HashMap<>();
            // Create a three-segment snake starting near center, all dark green initially
            Color darkGreen = new Color(0, 100, 0);
            snake.add(new Point(10, 10)); // Head
            snake.add(new Point(9, 10));  // Body segment 1
            snake.add(new Point(8, 10));  // Body segment 2
            
            // Assign dark green to all starting segments
            for (Point segment : snake) {
                segmentColors.put(segment, darkGreen);
            }
            
            // Initialize next growth color to a random color
            currentGrowthColor = generateRandomColor();
        }

        private Color generateRandomColor() {
            // Generate a vibrant random color (avoid too dark colors)
            return new Color(
                100 + random.nextInt(156),  // 100-255
                100 + random.nextInt(156),  // 100-255
                100 + random.nextInt(156)   // 100-255
            );
        }

        private void spawnFood() {
            Point newFood;
            do {
                newFood = new Point(random.nextInt(gridSize), random.nextInt(gridSize));
            } while (snake.contains(newFood));
            food = newFood;
        }

        private void setupKeyListener() {
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == (KeyEvent.VK_ENTER) && gameOver) {
                        resetGame();
                        return;
                    }

                    if (gameOver) return;

                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            if (direction.y == 0) {  // Prevent reversing
                                nextDirection = new Point(0, -1);
                            }
                            break;
                        case KeyEvent.VK_DOWN:
                            if (direction.y == 0) {  // Prevent reversing
                                nextDirection = new Point(0, 1);
                            }
                            break;
                        case KeyEvent.VK_LEFT:
                            if (direction.x == 0) {  // Prevent reversing
                                nextDirection = new Point(-1, 0);
                            }
                            break;
                        case KeyEvent.VK_RIGHT:
                            if (direction.x == 0) {  // Prevent reversing
                                nextDirection = new Point(1, 0);
                            }
                            break;
                    }
                }
            });
        }

        private void resetGame() {
            direction = new Point(1, 0);
            nextDirection = new Point(1, 0);
            score = 0;
            gameOver = false;
            snake.clear();
            segmentColors.clear();
            initializeSnake();
            spawnFood();
            requestFocusInWindow();
        }

        private void startTimer() {
            gameTimer = new Timer(150, e -> updateSnake());
            gameTimer.start();
        }

        private void updateSnake() {
            // Skip update if game is already over
            if (gameOver) return;

            // Update direction based on player input
            direction = nextDirection;
            Point head = snake.get(0);
            Point newHead = new Point(head.x + direction.x, head.y + direction.y);

            // Check wall collision (boundaries: 0 to gridSize-1 for 700x700 play area)
            if (newHead.x < 0 || newHead.x >= gridSize || newHead.y < 0 || newHead.y >= gridSize) {
                endGame();
                return;
            }

            // Check self collision (segment hit itself)
            if (snake.contains(newHead)) {
                endGame();
                return;
            }

            // Add new head to snake
            snake.add(0, newHead);
            segmentColors.put(newHead, segmentColors.getOrDefault(snake.get(1), new Color(0, 100, 0)));

            // Check food collision and grow snake
            if (newHead.equals(food)) {
                score++;  // Increment score
                // Assign current growth color to the new head
                segmentColors.put(newHead, currentGrowthColor);
                // Generate new color for next growth
                currentGrowthColor = generateRandomColor();
                spawnFood();  // Spawn new food pellet
            } else {
                // Remove tail and its color entry
                Point tail = snake.remove(snake.size() - 1);
                segmentColors.remove(tail);
            }

            repaint();
        }

        private void endGame() {
            gameOver = true;
            gameTimer.stop();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Fill entire game area (700x700 pixels) with background color
            g.setColor(new Color(22, 28, 33));
            g.fillRect(0, 0, getWidth(), getHeight());

            // Draw the snake segments with their respective colors
            for (int i = 0; i < snake.size(); i++) {
                Point segment = snake.get(i);
                Color segmentColor = segmentColors.getOrDefault(segment, new Color(0, 100, 0));
                g.setColor(segmentColor);
                g.fillRect(segment.x * cellSize, segment.y * cellSize, cellSize, cellSize);
            }

            // Draw beady eyes on the head with white spots
            if (!snake.isEmpty()) {
                Point head = snake.get(0);
                int headX = head.x * cellSize;
                int headY = head.y * cellSize;
                
                // Determine eye positions based on direction
                int eye1X, eye1Y, eye2X, eye2Y;
                if (direction.x == 1) {  // Moving right
                    eye1X = headX + cellSize - 8;
                    eye1Y = headY + 5;
                    eye2X = headX + cellSize - 8;
                    eye2Y = headY + cellSize - 10;
                } else if (direction.x == -1) {  // Moving left
                    eye1X = headX + 3;
                    eye1Y = headY + 5;
                    eye2X = headX + 3;
                    eye2Y = headY + cellSize - 10;
                } else if (direction.y == -1) {  // Moving up
                    eye1X = headX + 5;
                    eye1Y = headY + 3;
                    eye2X = headX + cellSize - 10;
                    eye2Y = headY + 3;
                } else {  // Moving down
                    eye1X = headX + 5;
                    eye1Y = headY + cellSize - 8;
                    eye2X = headX + cellSize - 10;
                    eye2Y = headY + cellSize - 8;
                }
                
                // Draw black eyes
                g.setColor(Color.BLACK);
                g.fillOval(eye1X, eye1Y, 5, 5);
                g.fillOval(eye2X, eye2Y, 5, 5);
                
                // Draw white spots (pupils)
                g.setColor(Color.WHITE);
                g.fillOval(eye1X + 1, eye1Y + 1, 3, 3);
                g.fillOval(eye2X + 1, eye2Y + 1, 3, 3);
            }

            // Draw food in red
            if (food != null) {
                g.setColor(Color.RED);
                g.fillRect(food.x * cellSize, food.y * cellSize, cellSize, cellSize);
            }

            // Draw score in top-left
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Score: " + score, 10, 20);

            // Draw game over message
            if (gameOver) {
                g.setColor(new Color(0, 0, 0, 180));
                g.fillRect(0, 0, getWidth(), getHeight());

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 32));
                FontMetrics fm = g.getFontMetrics();
                String gameOverText = "GAME OVER";
                int x = (getWidth() - fm.stringWidth(gameOverText)) / 2;
                int y = getHeight() / 2 - 40;
                g.drawString(gameOverText, x, y);

                g.setFont(new Font("Arial", Font.BOLD, 24));
                String scoreText = "Final Score: " + score;
                fm = g.getFontMetrics();
                x = (getWidth() - fm.stringWidth(scoreText)) / 2;
                y = getHeight() / 2 + 20;
                g.drawString(scoreText, x, y);

                g.setFont(new Font("Arial", Font.PLAIN, 16));
                String resetText = "Press R to play again";
                fm = g.getFontMetrics();
                x = (getWidth() - fm.stringWidth(resetText)) / 2;
                y = getHeight() / 2 + 80;
                g.drawString(resetText, x, y);
            }
        }
    }
}