package Question2;

import java.util.Arrays; // Import the Arrays class for utility functions

public class EmployeeRewards {
    /**
     * Determines the minimum number of rewards needed to distribute to employees.
     *
     * @param ratings The array of employee performance ratings.
     * @return The minimum number of rewards needed.
     */
    public static int minRewards(int[] ratings) {
        int n = ratings.length; // Get the number of employees
        int[] rewards = new int[n]; // Create an array to store the rewards for each employee
        Arrays.fill(rewards, 1);  // Initialize all rewards to 1, since every employee gets at least one reward

        // Forward pass: compare each employee with the previous one
        for (int i = 1; i < n; i++) { // Loop through the employees from left to right
            if (ratings[i] > ratings[i - 1]) { // If the current employee has a higher rating than the previous one
                rewards[i] = rewards[i - 1] + 1; // Assign one more reward than the previous employee
            }
        }

        // Backward pass: compare each employee with the next one
        for (int i = n - 2; i >= 0; i--) { // Loop through the employees from right to left
            if (ratings[i] > ratings[i + 1]) { // If the current employee has a higher rating than the next one
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1); // Ensure they have more rewards than the next employee
            }
        }

        // Sum up all rewards to get the total count
        return Arrays.stream(rewards).sum(); // Return the total number of rewards required
    }

    public static void main(String[] args) {
        // Test cases
        testMinRewards(new int[]{1, 0, 2}, 5); // Test case 1
        testMinRewards(new int[]{1, 2, 2}, 4); // Test case 2
        testMinRewards(new int[]{1, 2, 3, 4, 5}, 15); // Test case 3
    }

    /**
     * Test method to validate the minRewards function.
     *
     * @param ratings The array of employee performance ratings.
     * @param expected The expected minimum number of rewards.
     */
    private static void testMinRewards(int[] ratings, int expected) {
        int result = minRewards(ratings); // Call minRewards function
        System.out.printf("Test case: ratings=%s\n", Arrays.toString(ratings)); // Print input ratings
        System.out.printf("Expected: %d, Actual: %d\n", expected, result); // Print expected vs actual result
        System.out.println(result == expected ? "PASSED" : "FAILED"); // Print if the test case passed or failed
        System.out.println(); // Print an empty line for readability
    }
}
