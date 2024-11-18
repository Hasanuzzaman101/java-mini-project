import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MazeSolver extends JFrame {

    private static final int ROWS = 10; // Maze Rows
    private static final int COLS = 10; // Maze Columns
    private static final int SIZE = 50; // Cell size
    private static final int WALL = 1; // Wall value
    private static final int PATH = 0; // Path value
    private static final int START = 2; // Start point value
    private static final int END = 3; // End point value

    private int[][] maze = new int[ROWS][COLS];
    private boolean[][] visited = new boolean[ROWS][COLS];
    private Stack<Point> path = new Stack<>();
    private Point start, end;

    public MazeSolver() {
        setTitle("Maze Solver");
        setSize(COLS * SIZE, ROWS * SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        generateMaze();

        // Add MouseListener to display the maze with starting and ending points
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int row = e.getY() / SIZE;
                int col = e.getX() / SIZE;

                if (maze[row][col] == WALL) {
                    maze[row][col] = PATH;
                } else {
                    maze[row][col] = WALL;
                }
                repaint();
            }
        });

        JButton solveButton = new JButton("Solve Maze");
        solveButton.addActionListener(e -> solveMaze());
        add(solveButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void generateMaze() {
        // Initialize the maze with a basic layout
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                maze[i][j] = (Math.random() < 0.3) ? WALL : PATH; // Random walls
            }
        }
        maze[0][0] = START; // Start at top-left corner
        maze[ROWS - 1][COLS - 1] = END; // End at bottom-right corner
        start = new Point(0, 0);
        end = new Point(ROWS - 1, COLS - 1);
    }

    private void solveMaze() {
        if (dfs(start.x, start.y)) {
            JOptionPane.showMessageDialog(this, "Path Found!");
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "No Path Found!");
        }
    }

    private boolean dfs(int x, int y) {
        if (x < 0 || y < 0 || x >= ROWS || y >= COLS || visited[x][y] || maze[x][y] == WALL) {
            return false;
        }

        visited[x][y] = true;
        path.push(new Point(x, y));

        // If we reach the end point
        if (x == end.x && y == end.y) {
            return true;
        }

        // Try moving in 4 directions (up, down, left, right)
        if (dfs(x + 1, y) || dfs(x - 1, y) || dfs(x, y + 1) || dfs(x, y - 1)) {
            return true;
        }

        // Backtrack if no path is found
        path.pop();
        return false;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw maze
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (maze[i][j] == WALL) {
                    g.setColor(Color.BLACK);
                } else if (maze[i][j] == START) {
                    g.setColor(Color.GREEN);
                } else if (maze[i][j] == END) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(j * SIZE, i * SIZE, SIZE, SIZE);
                g.setColor(Color.GRAY);
                g.drawRect(j * SIZE, i * SIZE, SIZE, SIZE);
            }
        }

        // Draw the path
        g.setColor(Color.BLUE);
        for (Point p : path) {
            g.fillRect(p.y * SIZE + SIZE / 4, p.x * SIZE + SIZE / 4, SIZE / 2, SIZE / 2);
        }
    }

    public static void main(String[] args) {
        new MazeSolver();
    }
}
