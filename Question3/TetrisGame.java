package Question3;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Tetris Game Implementation
 * A classic Tetris game built with Java Swing
 * 

 */

/**
 * Class to represent a Tetris block with shape, color and position
 */
class Block {
    int[][] shape; // 2D array representing the block's shape
    Color color; // Color of the block
    int x, y; // Position of the block on the game board

    /**
     * Constructor to initialize a block with a specific shape and color
     * 
     * @param shape The 2D array representing the block's shape
     * @param color The color of the block
     */
    public Block(int[][] shape, Color color) {
        this.shape = shape; // Initialize the block shape
        this.color = color; // Set the block color
        this.x = 0; // Start at the top-left corner (row)
        this.y = 3; // Center horizontally (column)
    }

    /**
     * Rotates the block 90 degrees clockwise
     * This transforms the block's shape matrix
     */
    public void rotate() {
        int[][] rotated = new int[shape[0].length][shape.length]; // Create a new matrix with swapped dimensions
        for (int i = 0; i < shape.length; i++) { // Iterate through rows
            for (int j = 0; j < shape[0].length; j++) { // Iterate through columns
                rotated[j][shape.length - 1 - i] = shape[i][j]; // Apply rotation transformation
            }
        }
        shape = rotated; // Update the shape with the rotated matrix
    }
}

/**
 * Main Tetris game class using Swing
 * Handles game logic, rendering, and user input
 */
public class TetrisGame extends JPanel implements ActionListener, KeyListener {
    // Game constants
    private static final int BOARD_WIDTH = 10; // Width of the game board in tiles
    private static final int BOARD_HEIGHT = 20; // Height of the game board in tiles
    private static final int TILE_SIZE = 30; // Size of each tile in pixels
    private static final int FPS = 10; // Frames per second (reduced for slower speed)
    private static final int DELAY = 1000 / FPS; // Delay between frames in milliseconds
    
    // Game state variables
    private final int[][] gameBoard; // 2D array representing the game board (0=empty, 1=filled)
    private final Queue<Block> blockQueue; // Queue to store the sequence of falling blocks
    private Block currentBlock; // Current falling block
    private int score; // Player's score
    private final javax.swing.Timer timer; // Timer for game updates
    private boolean isPaused = false; // Flag to track if game is paused
    
    // UI constants
    private static final Color BOARD_BACKGROUND = new Color(20, 20, 20); // Dark background for game board
    private static final Color GRID_COLOR = new Color(50, 50, 50); // Color for grid lines
    private static final Color BORDER_COLOR = new Color(100, 100, 100); // Color for border
    private static final Font SCORE_FONT = new Font("Arial", Font.BOLD, 16); // Font for score display
    private static final Font GAME_OVER_FONT = new Font("Arial", Font.BOLD, 24); // Font for game over message

    // Predefined colors for blocks - using slightly muted professional colors
    private static final Color[] COLORS = {
        new Color(220, 20, 60),   // Crimson
        new Color(65, 105, 225),  // Royal Blue
        new Color(46, 139, 87),   // Sea Green
        new Color(218, 165, 32),  // Goldenrod
        new Color(255, 140, 0),   // Dark Orange
        new Color(0, 139, 139),   // Dark Cyan
        new Color(148, 0, 211)    // Dark Violet
    };

    /**
     * Constructor to initialize the game
     * Sets up the game board, UI, and starts the game timer
     */
    public TetrisGame() {
        // Set up the panel properties
        setPreferredSize(new Dimension(BOARD_WIDTH * TILE_SIZE + 2, BOARD_HEIGHT * TILE_SIZE + 2)); // Add space for border
        setBackground(BOARD_BACKGROUND); // Set background color
        setBorder(new LineBorder(BORDER_COLOR, 1)); // Add a border
        setFocusable(true); // Enable keyboard focus
        addKeyListener(this); // Register for keyboard events

        // Initialize game state
        gameBoard = new int[BOARD_HEIGHT][BOARD_WIDTH]; // Initialize empty game board
        blockQueue = new LinkedList<>(); // Initialize empty block queue
        score = 0; // Initialize score to zero
        generateNewBlock(); // Generate the first block
        
        // Start the game timer
        timer = new javax.swing.Timer(DELAY, this); // Create timer with specified delay
        timer.start(); // Start the timer
    }

    /**
     * Generates a new random Tetris block and adds it to the queue
     * Creates blocks with different shapes and colors
     */
    private void generateNewBlock() {
        Random random = new Random(); // Create random number generator
        
        // Define all possible Tetris block shapes
        int[][][] shapes = {
            {{1, 1, 1, 1}},           // I-block
            {{1, 1}, {1, 1}},         // O-block
            {{1, 1, 1}, {0, 1, 0}},   // T-block
            {{1, 1, 0}, {0, 1, 1}},   // Z-block
            {{0, 1, 1}, {1, 1, 0}},   // S-block
            {{1, 0, 0}, {1, 1, 1}},   // L-block
            {{0, 0, 1}, {1, 1, 1}}    // J-block
        };
        
        // Select a random shape and color
        int[][] shape = shapes[random.nextInt(shapes.length)]; // Choose random shape
        Color color = COLORS[random.nextInt(COLORS.length)]; // Choose random color
        
        // Create and queue the new block
        Block block = new Block(shape, color); // Create new block
        blockQueue.add(block); // Add to queue
        
        // If there's no current block, get one from the queue
        if (currentBlock == null) {
            currentBlock = blockQueue.poll(); // Get first block from queue
        }
    }

    /**
     * Checks if a block can move to a new position
     * 
     * @param block The block to check
     * @param newX The new X position
     * @param newY The new Y position
     * @return true if the move is valid, false otherwise
     */
    private boolean canMove(Block block, int newX, int newY) {
        // Check each cell of the block
        for (int i = 0; i < block.shape.length; i++) { // Iterate through rows
            for (int j = 0; j < block.shape[0].length; j++) { // Iterate through columns
                if (block.shape[i][j] != 0) { // If this cell is part of the block
                    int x = newX + i; // Calculate absolute row position
                    int y = newY + j; // Calculate absolute column position
                    
                    // Check if position is out of bounds or already occupied
                    if (x < 0 || x >= BOARD_HEIGHT || y < 0 || y >= BOARD_WIDTH || gameBoard[x][y] != 0) {
                        return false; // Invalid move
                    }
                }
            }
        }
        return true; // Move is valid
    }

    /**
     * Moves the current block one position to the left
     * if the move is valid
     */
    public void moveLeft() {
        if (canMove(currentBlock, currentBlock.x, currentBlock.y - 1)) { // Check if can move left
            currentBlock.y--; // Decrease Y coordinate (move left)
        }
    }

    /**
     * Moves the current block one position to the right
     * if the move is valid
     */
    public void moveRight() {
        if (canMove(currentBlock, currentBlock.x, currentBlock.y + 1)) { // Check if can move right
            currentBlock.y++; // Increase Y coordinate (move right)
        }
    }

    /**
     * Rotates the current block if the rotation is valid
     * Creates a temporary rotated block to check validity
     */
    public void rotateBlock() {
        // Create a copy of the current block to test rotation
        Block rotatedBlock = new Block(currentBlock.shape, currentBlock.color); // Create copy
        rotatedBlock.rotate(); // Rotate the copy
        
        // Check if the rotated position is valid
        if (canMove(rotatedBlock, currentBlock.x, currentBlock.y)) { // If rotation is valid
            currentBlock.rotate(); // Apply rotation to actual block
        }
    }

    /**
     * Moves the current block one position down
     * If not possible, places the block on the board
     * 
     * @return true if the block moved down, false if it was placed
     */
    public boolean moveDown() {
        if (canMove(currentBlock, currentBlock.x + 1, currentBlock.y)) { // Check if can move down
            currentBlock.x++; // Increase X coordinate (move down)
            return true; // Block moved successfully
        } else {
            placeBlock(); // Place the block on the board
            return false; // Block couldn't move
        }
    }

    /**
     * Places the current block on the game board
     * Marks the cells as filled and prepares the next block
     */
    private void placeBlock() {
        // Add the block to the game board
        for (int i = 0; i < currentBlock.shape.length; i++) { // Iterate through rows
            for (int j = 0; j < currentBlock.shape[0].length; j++) { // Iterate through columns
                if (currentBlock.shape[i][j] != 0) { // If this cell is part of the block
                    gameBoard[currentBlock.x + i][currentBlock.y + j] = 1; // Mark as filled
                }
            }
        }
        
        checkCompletedRows(); // Check for completed rows
        currentBlock = blockQueue.poll(); // Get the next block from queue
        generateNewBlock(); // Generate a new block for the queue
    }

    /**
     * Checks for completed rows and updates the score
     * A row is complete when all cells are filled
     */
    private void checkCompletedRows() {
        for (int i = 0; i < BOARD_HEIGHT; i++) { // Check each row
            boolean isComplete = true; // Assume row is complete
            
            // Check if all cells in the row are filled
            for (int j = 0; j < BOARD_WIDTH; j++) { // Check each column
                if (gameBoard[i][j] == 0) { // If any cell is empty
                    isComplete = false; // Row is not complete
                    break; // No need to check further
                }
            }
            
            if (isComplete) { // If row is complete
                removeRow(i); // Remove the row
                score += 100; // Increase the score
            }
        }
    }

    /**
     * Removes a completed row and shifts all rows above it down
     * 
     * @param row The index of the row to remove
     */
    private void removeRow(int row) {
        // Shift all rows above the completed row down
        for (int i = row; i > 0; i--) { // Start from completed row and move up
            gameBoard[i] = Arrays.copyOf(gameBoard[i - 1], BOARD_WIDTH); // Copy row above
        }
        
        Arrays.fill(gameBoard[0], 0); // Clear the top row (now empty)
    }

    /**
     * Checks if the game is over
     * Game is over when blocks reach the top of the board
     * 
     * @return true if game is over, false otherwise
     */
    public boolean isGameOver() {
        // Check if any cell in the top row is filled
        for (int j = 0; j < BOARD_WIDTH; j++) { // Check each column in top row
            if (gameBoard[0][j] != 0) { // If any cell is filled
                return true; // Game is over
            }
        }
        return false; // Game is not over
    }

    /**
     * Toggles the pause state of the game
     */
    public void togglePause() {
        isPaused = !isPaused; // Toggle pause state
        if (isPaused) { // If game is now paused
            timer.stop(); // Stop the timer
        } else { // If game is now unpaused
            timer.start(); // Restart the timer
        }
        repaint(); // Redraw the game board
    }

    /**
     * Paints the game board and all game elements
     * 
     * @param g The Graphics context to paint on
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Call superclass method
        Graphics2D g2d = (Graphics2D) g; // Cast to Graphics2D for better rendering
        
        // Enable anti-aliasing for smoother rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw the game board background
        g2d.setColor(BOARD_BACKGROUND); // Set background color
        g2d.fillRect(0, 0, getWidth(), getHeight()); // Fill the entire panel
        
        // Draw the grid lines
        g2d.setColor(GRID_COLOR); // Set grid color
        for (int i = 0; i <= BOARD_HEIGHT; i++) { // Draw horizontal lines
            g2d.drawLine(0, i * TILE_SIZE, BOARD_WIDTH * TILE_SIZE, i * TILE_SIZE);
        }
        for (int j = 0; j <= BOARD_WIDTH; j++) { // Draw vertical lines
            g2d.drawLine(j * TILE_SIZE, 0, j * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE);
        }
        
        // Draw the filled cells on the game board
        for (int i = 0; i < BOARD_HEIGHT; i++) { // Iterate through rows
            for (int j = 0; j < BOARD_WIDTH; j++) { // Iterate through columns
                if (gameBoard[i][j] != 0) { // If cell is filled
                    // Draw filled cell with gradient
                    GradientPaint gradient = new GradientPaint(
                        j * TILE_SIZE, i * TILE_SIZE, Color.LIGHT_GRAY,
                        (j + 1) * TILE_SIZE, (i + 1) * TILE_SIZE, Color.DARK_GRAY
                    );
                    g2d.setPaint(gradient); // Set gradient paint
                    g2d.fillRect(j * TILE_SIZE + 1, i * TILE_SIZE + 1, TILE_SIZE - 1, TILE_SIZE - 1); // Fill cell
                    
                    // Draw cell border
                    g2d.setColor(Color.GRAY); // Set border color
                    g2d.drawRect(j * TILE_SIZE + 1, i * TILE_SIZE + 1, TILE_SIZE - 2, TILE_SIZE - 2); // Draw border
                }
            }
        }

        // Draw the current falling block
        if (currentBlock != null) { // If there is a current block
            // Create gradient for current block
            GradientPaint blockGradient = new GradientPaint(
                0, 0, currentBlock.color,
                TILE_SIZE, TILE_SIZE, currentBlock.color.darker()
            );
            g2d.setPaint(blockGradient); // Set gradient paint
            
            // Draw each cell of the current block
            for (int i = 0; i < currentBlock.shape.length; i++) { // Iterate through rows
                for (int j = 0; j < currentBlock.shape[0].length; j++) { // Iterate through columns
                    if (currentBlock.shape[i][j] != 0) { // If this cell is part of the block
                        // Calculate position
                        int x = (currentBlock.y + j) * TILE_SIZE; // X coordinate
                        int y = (currentBlock.x + i) * TILE_SIZE; // Y coordinate
                        
                        // Draw filled cell
                        g2d.fillRect(x + 1, y + 1, TILE_SIZE - 1, TILE_SIZE - 1); // Fill cell
                        
                        // Draw cell border
                        g2d.setColor(currentBlock.color.darker()); // Set border color
                        g2d.drawRect(x + 1, y + 1, TILE_SIZE - 2, TILE_SIZE - 2); // Draw border
                    }
                }
            }
        }

        // Draw the score
        g2d.setColor(Color.WHITE); // Set text color
        g2d.setFont(SCORE_FONT); // Set font
        g2d.drawString("Score: " + score, 10, 20); // Draw score text
        
        // Draw pause message if game is paused
        if (isPaused) { // If game is paused
            g2d.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
            g2d.fillRect(0, 0, getWidth(), getHeight()); // Fill the entire panel
            
            g2d.setColor(Color.WHITE); // Set text color
            g2d.setFont(GAME_OVER_FONT); // Set font
            String pauseMsg = "PAUSED"; // Pause message
            FontMetrics fm = g2d.getFontMetrics(); // Get font metrics
            int msgWidth = fm.stringWidth(pauseMsg); // Calculate text width
            
            // Draw centered pause message
            g2d.drawString(pauseMsg, (getWidth() - msgWidth) / 2, getHeight() / 2); // Draw centered text
        }
        
        // Draw game over message if game is over
        if (isGameOver() && !timer.isRunning()) { // If game is over and timer is stopped
            g2d.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
            g2d.fillRect(0, 0, getWidth(), getHeight()); // Fill the entire panel
            
            g2d.setColor(Color.WHITE); // Set text color
            g2d.setFont(GAME_OVER_FONT); // Set font
            String gameOverMsg = "GAME OVER"; // Game over message
            FontMetrics fm = g2d.getFontMetrics(); // Get font metrics
            int msgWidth = fm.stringWidth(gameOverMsg); // Calculate text width
            
            // Draw centered game over message
            g2d.drawString(gameOverMsg, (getWidth() - msgWidth) / 2, getHeight() / 2 - 20); // Draw centered text
            
            // Draw final score
            String scoreMsg = "Final Score: " + score; // Score message
            msgWidth = fm.stringWidth(scoreMsg); // Calculate text width
            g2d.drawString(scoreMsg, (getWidth() - msgWidth) / 2, getHeight() / 2 + 20); // Draw centered text
        }
    }

    /**
     * Handles game updates on timer ticks
     * Moves the current block down and checks game state
     * 
     * @param e The ActionEvent (timer tick)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver()) { // If game is not over
            if (!moveDown()) { // Try to move block down
                if (isGameOver()) { // Check if game is now over
                    timer.stop(); // Stop the timer
                    repaint(); // Redraw to show game over
                }
            }
            repaint(); // Redraw the game board
        }
    }

    /**
     * Handles key press events for game controls
     * 
     * @param e The KeyEvent
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (!isPaused && timer.isRunning()) { // Only process if game is running
            switch (e.getKeyCode()) { // Check which key was pressed
                case KeyEvent.VK_LEFT: // Left arrow key
                    moveLeft(); // Move block left
                    break;
                case KeyEvent.VK_RIGHT: // Right arrow key
                    moveRight(); // Move block right
                    break;
                case KeyEvent.VK_UP: // Up arrow key
                    rotateBlock(); // Rotate block
                    break;
                case KeyEvent.VK_DOWN: // Down arrow key
                    moveDown(); // Move block down
                    break;
                case KeyEvent.VK_SPACE: // Space key
                    // Hard drop - move block down until it can't move further
                    while (moveDown()) {} // Keep moving down until blocked
                    break;
            }
        }
        
        // Pause/unpause with P key regardless of game state
        if (e.getKeyCode() == KeyEvent.VK_P) { // P key
            togglePause(); // Toggle pause state
        }
        
        repaint(); // Redraw the game board
    }

    /**
     * Required by KeyListener interface but not used
     * 
     * @param e The KeyEvent
     */
    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }

    /**
     * Required by KeyListener interface but not used
     * 
     * @param e The KeyEvent
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    /**
     * Main method to run the game
     * Creates the game window and starts the game
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Use SwingUtilities to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            // Create and set up the window
            JFrame frame = new JFrame("Tetris Game"); // Create window with title
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit when closed
            
            // Create game panel
            TetrisGame game = new TetrisGame(); // Create game instance
            
            // Create a container panel with margin
            JPanel container = new JPanel(); // Create container panel
            container.setBackground(new Color(40, 40, 40)); // Set background color
            container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add margin
            container.setLayout(new BorderLayout()); // Use BorderLayout
            container.add(game, BorderLayout.CENTER); // Add game to center
            
            // Add instructions panel
            JPanel instructionsPanel = new JPanel(); // Create instructions panel
            instructionsPanel.setBackground(new Color(40, 40, 40)); // Set background color
            instructionsPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0)); // Add top margin
            instructionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center align content
            
            // Create instructions label
            JLabel instructionsLabel = new JLabel(
                "<html><center>Controls: ← → Move | ↑ Rotate | ↓ Soft Drop<br>" +
                "Space: Hard Drop | P: Pause</center></html>"
            ); // Create label with HTML formatting
            instructionsLabel.setForeground(Color.LIGHT_GRAY); // Set text color
            instructionsLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // Set font
            
            instructionsPanel.add(instructionsLabel); // Add label to panel
            container.add(instructionsPanel, BorderLayout.SOUTH); // Add instructions to bottom
            
            // Add container to frame
            frame.add(container); // Add container to frame
            frame.pack(); // Size frame to fit content
            frame.setLocationRelativeTo(null); // Center on screen
            frame.setResizable(false); // Prevent resizing
            frame.setVisible(true); // Show the window
            
            // Request focus for the game panel
            game.requestFocusInWindow(); // Set keyboard focus to game
        });
    }
}