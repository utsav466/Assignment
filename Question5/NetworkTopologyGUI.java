package Question5;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

/**
 * NetworkTopologyGUI is a graphical user interface for visualizing and optimizing network topology.
 * It allows users to add nodes, create edges, and apply optimization algorithms.
 */
public class NetworkTopologyGUI extends JFrame {
    
    /** Stores the nodes in the network */
    private final Map<String, Node> nodes;
    
    /** Stores the edges connecting the nodes */
    private final List<Edge> edges;
    
    /** Canvas panel for drawing nodes and edges */
    private final JPanel canvas;

    /**
     * Constructor initializes the GUI, sets up the layout, and creates control buttons.
     */
    public NetworkTopologyGUI() {
        nodes = new HashMap<>(); // Initialize the node storage
        edges = new ArrayList<>(); // Initialize the edge storage
        canvas = new CanvasPanel(); // Create the drawing panel

        // Set up the frame properties
        setTitle("Network Topology Optimizer"); // Set window title
        setSize(800, 600); // Set window dimensions
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Enable closing the application
        setLayout(new BorderLayout()); // Set layout manager
        add(canvas, BorderLayout.CENTER); // Add the canvas to the center

        // Create a control panel for buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(1, 3)); // Arrange buttons in a row

        // Button to add a new node
        JButton addNodeButton = new JButton("Add Node");
        addNodeButton.addActionListener(e -> addNode());
        controlPanel.add(addNodeButton);

        // Button to add an edge between nodes
        JButton addEdgeButton = new JButton("Add Edge");
        addEdgeButton.addActionListener(e -> addEdge());
        controlPanel.add(addEdgeButton);

        // Button to optimize the network
        JButton optimizeButton = new JButton("Optimize Network");
        optimizeButton.addActionListener(e -> optimizeNetwork());
        controlPanel.add(optimizeButton);

        add(controlPanel, BorderLayout.SOUTH); // Add control panel to the bottom
    }

    /**
     * Adds a new node to the network.
     * Prompts the user for a node name and creates the node with a default position.
     */
    private void addNode() {
        String nodeName = JOptionPane.showInputDialog(this, "Enter Node Name:"); // Get user input
        if (nodeName != null && !nodeName.trim().isEmpty()) { // Validate input
            Node newNode = new Node(nodeName, new Point(100, 100)); // Create new node
            nodes.put(nodeName, newNode); // Store node in the map
            canvas.repaint(); // Refresh the canvas
        }
    }

    /**
     * Adds an edge between two nodes.
     * Prompts the user for node names and connection properties.
     */
    private void addEdge() {
        String nodeA = JOptionPane.showInputDialog(this, "Enter the first node:"); // Get first node
        String nodeB = JOptionPane.showInputDialog(this, "Enter the second node:"); // Get second node
        if (nodeA != null && nodeB != null && !nodeA.trim().isEmpty() && !nodeB.trim().isEmpty()) { // Validate input
            int cost = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter the cost of this connection:")); // Get cost
            int bandwidth = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter the bandwidth of this connection:")); // Get bandwidth
            Edge newEdge = new Edge(nodeA, nodeB, cost, bandwidth); // Create edge
            edges.add(newEdge); // Store edge in the list
            canvas.repaint(); // Refresh the canvas
        }
    }

    /**
     * Optimizes the network topology using minimum spanning tree (MST) algorithms.
     * Currently, this is a placeholder implementation.
     */
    private void optimizeNetwork() {
        JOptionPane.showMessageDialog(this, "Network optimization algorithms applied!", "Optimization Complete", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * CanvasPanel is a custom JPanel used to draw network nodes and edges.
     */
    class CanvasPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Clear the panel
            drawNodes(g); // Draw nodes
            drawEdges(g); // Draw edges
        }

        /**
         * Draws the nodes on the canvas.
         * @param g Graphics object for rendering
         */
        private void drawNodes(Graphics g) {
            for (Node node : nodes.values()) {
                g.setColor(Color.BLUE); // Set node color
                g.fillOval(node.position.x - 15, node.position.y - 15, 30, 30); // Draw node circle
                g.setColor(Color.WHITE); // Set text color
                g.drawString(node.name, node.position.x - 10, node.position.y + 5); // Draw node name
            }
        }

        /**
         * Draws edges between nodes on the canvas.
         * @param g Graphics object for rendering
         */
        private void drawEdges(Graphics g) {
            for (Edge edge : edges) {
                Node nodeA = nodes.get(edge.nodeA); // Get first node
                Node nodeB = nodes.get(edge.nodeB); // Get second node
                g.setColor(Color.BLACK); // Set edge color
                g.drawLine(nodeA.position.x, nodeA.position.y, nodeB.position.x, nodeB.position.y); // Draw edge line
                g.setColor(Color.RED); // Set text color
                g.drawString("Cost: " + edge.cost + " Bandwidth: " + edge.bandwidth, // Display edge properties
                        (nodeA.position.x + nodeB.position.x) / 2, (nodeA.position.y + nodeB.position.y) / 2);
            }
        }
    }

    /**
     * Represents a node (device) in the network.
     */
    static class Node {
        String name; // Node name
        Point position; // Node position on canvas

        /**
         * Constructor for creating a new node.
         * @param name Node name
         * @param position Node position
         */
        Node(String name, Point position) {
            this.name = name;
            this.position = position;
        }
    }

    /**
     * Represents an edge (connection) between two nodes.
     */
    static class Edge {
        String nodeA, nodeB; // Connected nodes
        int cost, bandwidth; // Connection properties

        /**
         * Constructor for creating a new edge.
         * @param nodeA First node name
         * @param nodeB Second node name
         * @param cost Connection cost
         * @param bandwidth Connection bandwidth
         */
        Edge(String nodeA, String nodeB, int cost, int bandwidth) {
            this.nodeA = nodeA;
            this.nodeB = nodeB;
            this.cost = cost;
            this.bandwidth = bandwidth;
        }
    }

    /**
     * Main method to launch the GUI application.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NetworkTopologyGUI networkGraph = new NetworkTopologyGUI(); // Create GUI instance
            networkGraph.setVisible(true); // Show GUI
        });
    }
}