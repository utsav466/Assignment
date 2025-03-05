package Question2;

import java.util.Arrays;

public class ClosestPoints {
    /**
     * Finds the lexicographically smallest pair of points with the smallest Manhattan distance.
     *
     * @param xCoords The array of x-coordinates of the points.
     * @param yCoords The array of y-coordinates of the points.
     * @return The indices of the closest pair of points.
     */
    public static int[] findClosestPair(int[] xCoords, int[] yCoords) {
        int n = xCoords.length; // Get the number of points
        int[] result = new int[]{0, 1};  // Initialize the result array with first two indices
        int minDistance = Integer.MAX_VALUE; // Initialize the minimum Manhattan distance to max value

        // Iterate through all pairs of points
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Calculate Manhattan distance between points i and j
                int distance = Math.abs(xCoords[i] - xCoords[j]) + Math.abs(yCoords[i] - yCoords[j]);
                
                // If a smaller distance is found, or if the distance is the same but (i, j) is lexicographically smaller
                if (distance < minDistance || (distance == minDistance && (i < result[0] || (i == result[0] && j < result[1])))) {
                    minDistance = distance; // Update the minimum distance
                    result[0] = i; // Update the first index of the closest pair
                    result[1] = j; // Update the second index of the closest pair
                }
            }
        }

        return result; // Return the indices of the closest pair
    }

    public static void main(String[] args) {
        // Test cases
        testFindClosestPair(
            new int[]{1, 2, 3, 2, 4}, // x-coordinates of points
            new int[]{2, 3, 1, 2, 3}, // y-coordinates of points
            new int[]{0, 3} // Expected output
        );
        testFindClosestPair(
            new int[]{1, 1, 1}, // x-coordinates of points
            new int[]{1, 1, 1}, // y-coordinates of points
            new int[]{0, 1} // Expected output
        );
        testFindClosestPair(
            new int[]{1, 2, 3}, // x-coordinates of points
            new int[]{4, 5, 6}, // y-coordinates of points
            new int[]{0, 1} // Expected output
        );
    }

    /**
     * Test method to validate the findClosestPair function.
     *
     * @param xCoords The array of x-coordinates of the points.
     * @param yCoords The array of y-coordinates of the points.
     * @param expected The expected indices of the closest pair.
     */
    private static void testFindClosestPair(int[] xCoords, int[] yCoords, int[] expected) {
        int[] result = findClosestPair(xCoords, yCoords); // Call the function to get the result
        
        // Print test case details
        System.out.printf("Test case: xCoords=%s, yCoords=%s\n", 
                          Arrays.toString(xCoords), Arrays.toString(yCoords));
        System.out.printf("Expected: %s, Actual: %s\n", 
                          Arrays.toString(expected), Arrays.toString(result));
        
        // Print whether the test case passed or failed
        System.out.println(Arrays.equals(result, expected) ? "PASSED" : "FAILED");
        System.out.println();
    }
}
    