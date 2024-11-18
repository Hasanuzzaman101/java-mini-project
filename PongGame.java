import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PongGame extends JPanel implements ActionListener, KeyListener {
    private final int WIDTH = 800, HEIGHT = 600; // Game window size
    private int paddleWidth = 20, paddleHeight = 100; // Paddle dimensions
    private int ballSize = 20; // Ball size

    private int player1X = 30, player1Y = HEIGHT / 2 - paddleHeight / 2; // Player 1 paddle position
    private int player2X = WIDTH - 30 - paddleWidth, player2Y = HEIGHT / 2 - paddleHeight / 2; // Player 2 paddle position
    private int ballX = WIDTH / 2 - ballSize / 2, ballY = HEIGHT / 2 - ballSize / 2; // Ball position
    private int ballSpeedX = -5, ballSpeedY = 5; // Ball speed (X and Y directions)

    private int player1Score = 0, player2Score = 0; // Player scores

    private Timer timer;

    public PongGame() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        timer = new Timer(1000 / 60, this); // 60 FPS
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Move the ball
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        // Ball collision with top and bottom walls
        if (ballY <= 0 || ballY + ballSize >= HEIGHT) {
            ballSpeedY = -ballSpeedY;
        }

        // Ball collision with paddles
        if (ballX <= player1X + paddleWidth && ballY + ballSize >= player1Y && ballY <= player1Y + paddleHeight) {
            ballSpeedX = -ballSpeedX;
        }

        if (ballX + ballSize >= player2X && ballY + ballSize >= player2Y && ballY <= player2Y + paddleHeight) {
            ballSpeedX = -ballSpeedX;
        }

        // Scoring (Ball goes out of bounds)
        if (ballX <= 0) {
            player2Score++;
            resetBall();
        }

        if (ballX + ballSize >= WIDTH) {
            player1Score++;
            resetBall();
        }

        // Redraw the game
        repaint();
    }

    private void resetBall() {
        ballX = WIDTH / 2 - ballSize / 2;
        ballY = HEIGHT / 2 - ballSize / 2;
        ballSpeedX = -ballSpeedX;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw paddles
        g.setColor(Color.WHITE);
        g.fillRect(player1X, player1Y, paddleWidth, paddleHeight); // Player 1 paddle
        g.fillRect(player2X, player2Y, paddleWidth, paddleHeight); // Player 2 paddle

        // Draw ball
        g.fillRect(ballX, ballY, ballSize, ballSize);

        // Draw scores
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        g.drawString("Player 1: " + player1Score, 50, 50);
        g.drawString("Player 2: " + player2Score, WIDTH - 200, 50);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Player 1 controls (W, S)
        if (keyCode == KeyEvent.VK_W && player1Y > 0) {
            player1Y -= 10; // Move paddle up
        }
        if (keyCode == KeyEvent.VK_S && player1Y + paddleHeight < HEIGHT) {
            player1Y += 10; // Move paddle down
        }

        // Player 2 controls (Up, Down Arrow keys)
        if (keyCode == KeyEvent.VK_UP && player2Y > 0) {
            player2Y -= 10; // Move paddle up
        }
        if (keyCode == KeyEvent.VK_DOWN && player2Y + paddleHeight < HEIGHT) {
            player2Y += 10; // Move paddle down
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pong Game");
        PongGame pongGame = new PongGame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(pongGame);
        frame.pack();
        frame.setVisible(true);
    }
}
