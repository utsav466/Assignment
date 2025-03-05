package Question3;

import java.util.*;

class NetworkConnection {
    /**
     * Union-Find (Disjoint Set) data structure for efficiently managing connected components.
     */
    static class UnionFind {
        int[] parent, rank;
        
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i; // Initialize each node as its own parent
        }
        
        /**
         * Finds the representative (root) of a node using path compression for efficiency.
         */
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }
        
        /**
         * Unites two subsets if they are different, using union by rank.
         */
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) return false; // Already in the same set
            
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            return true;
        }
    }

    /**
     * Computes the minimum cost to connect all devices in the network.
     * 
     * @param n          Number of devices
     * @param modules    Cost of installing communication modules on each device
     * @param connections Array of available connections [device1, device2, cost]
     * @return Minimum total cost to connect all devices
     */
    public static int minTotalCost(int n, int[] modules, int[][] connections) {
        List<int[]> edges = new ArrayList<>();
        
        // Convert connections to 0-based index and store them in the edges list
        for (int[] conn : connections) {
            edges.add(new int[]{conn[0] - 1, conn[1] - 1, conn[2]});
        }
        
        // Introduce a virtual node (index `n`) that can connect to each device via module installation
        for (int i = 0; i < n; i++) {
            edges.add(new int[]{n, i, modules[i]});
        }
        
        // Sort edges based on cost (for Kruskal’s Algorithm)
        edges.sort(Comparator.comparingInt(a -> a[2]));
        
        UnionFind uf = new UnionFind(n + 1); // Include the extra virtual node
        int totalCost = 0, edgesUsed = 0;
        
        // Apply Kruskal's algorithm to construct the Minimum Spanning Tree (MST)
        for (int[] edge : edges) {
            if (uf.union(edge[0], edge[1])) { // Add the edge if it doesn't form a cycle
                totalCost += edge[2];
                edgesUsed++;
                if (edgesUsed == n) break; // Stop when all devices are connected
            }
        }
        
        return totalCost;
    }

    /**
     * Main function to test the implementation with sample cases.
     */
    public static void main(String[] args) {
        // Test Case 1
        int n1 = 3;
        int[] modules1 = {1, 2, 2};
        int[][] connections1 = {{1, 2, 1}, {2, 3, 1}};
        System.out.println("Test Case 1 - Expected: 3, Output: " + minTotalCost(n1, modules1, connections1));
        
        // Test Case 2 (Similar to Solution 2's format)
        int n2 = 4;
        int[] modules2 = {5, 3, 2, 1};
        int[][] connections2 = {{1, 2, 4}, {2, 3, 1}, {3, 4, 3}, {1, 4, 2}};
        System.out.println("Test Case 2 - Expected: 6, Output: " + minTotalCost(n2, modules2, connections2));
    }
}
