import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.Timer;

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
        private final int gridSize = 20;
        private final int cellSize = 30;
        private ArrayList<Point> snake;
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
            // Create a three-segment snake
            snake.add(new Point(10, 10)); // Head
            snake.add(new Point(9, 10));  // Body segment 1
            snake.add(new Point(8, 10));  // Body segment 2
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
                    if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
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
            initializeSnake();
            spawnFood();
            requestFocusInWindow();
        }

        private void startTimer() {
            gameTimer = new Timer(150, e -> updateSnake());
            gameTimer.start();
        }

        private void updateSnake() {
            if (gameOver) return;

            direction = nextDirection;
            Point head = snake.get(0);
            Point newHead = new Point(head.x + direction.x, head.y + direction.y);

            // Check wall collision
            if (newHead.x < 0 || newHead.x >= gridSize || newHead.y < 0 || newHead.y >= gridSize) {
                endGame();
                return;
            }

            // Check self collision
            if (snake.contains(newHead)) {
                endGame();
                return;
            }

            snake.add(0, newHead);

            // Check food collision
            if (newHead.equals(food)) {
                score++;
                spawnFood();
            } else {
                snake.remove(snake.size() - 1);
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
            g.setColor(new Color(22, 28, 33));
            g.fillRect(0, 0, getWidth(), getHeight());

            // Draw the snake in green
            g.setColor(Color.GREEN);
            for (Point segment : snake) {
                g.fillRect(segment.x * cellSize, segment.y * cellSize, cellSize, cellSize);
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