package Question1;

public class CriticalTemperature {

    /**
     * Finds the least number of measurements needed to identify the critical temperature.
     * @param k The quantity of identical material samples.
     * @param n The count of temperature levels.
     * @return The least number of measurements needed.
     */
    
    public static int findCriticalTemperature(int k, int n) {
        // dp[i][j] represents the maximum number of temperature levels
        // we can check with 'i' samples and 'j' measurements.
        int[][] dp = new int[k + 1][n + 1];
        
        int attempts = 0;
        
        // Keep increasing attempts until we can check all 'n' levels
        while (dp[k][attempts] < n) {
            attempts++;
            for (int i = 1; i <= k; i++) {
                // If a sample breaks at this temperature, we test lower temperatures.
                // If it doesn't break, we check above.
                dp[i][attempts] = dp[i - 1][attempts - 1] + dp[i][attempts - 1] + 1;
            }
        }
        
        return attempts;
    }

    public static void main(String[] args) {
        // Test cases
        testFindCriticalTemperature(1, 2, 2);
        testFindCriticalTemperature(2, 6, 3);
        testFindCriticalTemperature(3, 14, 4);
        testFindCriticalTemperature(4, 20, 5);
        testFindCriticalTemperature(2, 10, 4);
        testFindCriticalTemperature(3, 25, 5);
    }

    /**
     * Test method to validate the findCriticalTemperature function.
     *
     * @param k The number of samples.
     * @param n The number of temperature levels.
     * @param expected The expected result.
     */
    private static void testFindCriticalTemperature(int k, int n, int expected) {
        int result = findCriticalTemperature(k, n);
        System.out.printf("Test case: k=%d, n=%d\n", k, n);
        System.out.printf("Expected: %d, Actual: %d\n", expected, result);
        System.out.println(result == expected ? "PASSED" : "FAILED");
        System.out.println();
    }
}
