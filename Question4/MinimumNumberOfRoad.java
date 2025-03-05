package Question4;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MinimumNumberOfRoad {
    /**
     * Main method where the program execution begins. It processes two inputs:
     * an array of package locations and a list of roads between locations. 
     * It then computes the minimum number of roads that need to be traversed to collect all packages for each input.
     */
    public static void main(String[] args) {
        // Sample Input 1: Package locations and roads between them
        int[] packages1 = {1, 0, 0, 0, 0, 1};
        int[][] roads1 = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}};

        // Sample Input 2: Package locations and roads between them
        int[] packages2 = {0, 0, 0, 1, 1, 0, 0, 1};
        int[][] roads2 = {{0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {5, 6}, {5, 7}};

        // Calculate the minimum number of roads to traverse for Input 1
        int minRoads1 = findMinRoads(packages1, roads1);
        System.out.println("Minimum number of roads to traverse for Input 1: " + minRoads1);

        // Calculate the minimum number of roads to traverse for Input 2
        int minRoads2 = findMinRoads(packages2, roads2);
        System.out.println("Minimum number of roads to traverse for Input 2: " + minRoads2);
    }

    /**
     * This method finds the minimum number of roads to traverse in order to collect all the packages.
     * It considers the number of roads needed for collection and backtracking for each starting location.
     *
     * @param packages Array representing the locations with packages (1 means package is present, 0 means no package).
     * @param roads Array of roads represented by pairs of connected locations.
     * @return The minimum number of roads to traverse.
     */
    public static int findMinRoads(int[] packages, int[][] roads) {
        int n = packages.length; // Number of locations (nodes)
        List<List<Integer>> graph = buildGraph(n, roads); // Create the graph based on roads

        int minRoads = Integer.MAX_VALUE; // Initialize the minimum roads as a large value

        // Try each location as the starting point
        for (int start = 0; start < n; start++) {
            boolean[] visited = new boolean[n]; // Track visited locations during traversal
            int roadsTraversed = 0; // Counter for the number of roads traversed

            // Perform BFS to collect all packages within a distance of 2 roads
            roadsTraversed += bfs(start, graph, packages, visited);

            // Backtrack to the start location
            roadsTraversed += backtrack(start, graph, visited);

            // Update the minimum roads if fewer roads are found
            minRoads = Math.min(minRoads, roadsTraversed);
        }

        return minRoads; // Return the minimum number of roads to traverse
    }

    /**
     * Builds a graph where each node has a list of its neighbors based on the roads provided.
     *
     * @param n The total number of locations.
     * @param roads The roads connecting the locations.
     * @return A list of lists where each list contains the neighbors of a particular location.
     */
    private static List<List<Integer>> buildGraph(int n, int[][] roads) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        // Add edges (roads) to the graph
        for (int[] road : roads) {
            int u = road[0];
            int v = road[1];
            graph.get(u).add(v);
            graph.get(v).add(u); // Undirected graph
        }

        return graph;
    }

    /**
     * This method performs a BFS starting from a location to collect all packages within a distance of 2 roads.
     * It also counts the roads traversed during the process.
     *
     * @param start The starting location.
     * @param graph The graph representing the locations and roads.
     * @param packages The array containing package data at each location.
     * @param visited The array keeping track of visited locations.
     * @return The number of roads traversed while collecting packages.
     */
    private static int bfs(int start, List<List<Integer>> graph, int[] packages, boolean[] visited) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start); // Add the start location to the queue
        visited[start] = true;

        int roadsTraversed = 0;

        // Perform BFS up to a maximum distance of 2
        for (int level = 0; level < 2; level++) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int current = queue.poll(); // Dequeue the current location

                // If there is a package at the current location, collect it
                if (packages[current] == 1) {
                    packages[current] = 0; // Mark package as collected
                }

                // Explore the neighbors of the current location
                for (int neighbor : graph.get(current)) {
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        queue.add(neighbor); // Add neighbor to the queue
                        roadsTraversed++; // Count the road traversed
                    }
                }
            }
        }

        return roadsTraversed;
    }

    /**
     * This method performs backtracking to return to the starting location, counting the roads traversed.
     *
     * @param start The starting location.
     * @param graph The graph representing the locations and roads.
     * @param visited The array keeping track of visited locations.
     * @return The number of roads traversed while backtracking to the starting location.
     */
    private static int backtrack(int start, List<List<Integer>> graph, boolean[] visited) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        visited[start] = true;

        int roadsTraversed = 0;

        // Backtrack using BFS until the starting location is reached
        while (!queue.isEmpty()) {
            int current = queue.poll();

            // If we reach the start location, stop the backtracking
            if (current == start) {
                break;
            }

            // Explore the neighbors of the current location
            for (int neighbor : graph.get(current)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(neighbor);
                    roadsTraversed++; // Count the road traversed
                }
            }
        }

        return roadsTraversed;
    }
    
}
