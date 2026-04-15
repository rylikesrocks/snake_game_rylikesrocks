import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class SnakeGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snake Starter");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 600);
            frame.setLocationRelativeTo(null);
            frame.add(new SnakeGame().new SnakeStarter());
            frame.setVisible(true);
        });
    }

    public class SnakeStarter extends JPanel {
        private final int gridSize = 20;
        private final int cellSize = 25;
        private ArrayList<Point> snake;

        public SnakeStarter() {
            setBackground(Color.BLACK);
            initializeSnake();
        }

        private void initializeSnake() {
            snake = new ArrayList<>();
            // Create a three-segment snake
            snake.add(new Point(10, 10)); // Head
            snake.add(new Point(9, 10));  // Body segment 1
            snake.add(new Point(8, 10));  // Body segment 2
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(22, 28, 39));
            g.fillRect(0, 0, gridSize * cellSize, gridSize * cellSize);

            // Draw the snake in green
            g.setColor(Color.GREEN);
            for (Point segment : snake) {
                g.fillRect(segment.x * cellSize, segment.y * cellSize, cellSize, cellSize);
            }
        }
    }
}