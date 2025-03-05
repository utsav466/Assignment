package Question4;

import java.text.*;
import java.util.*;

public class Tweets {
    /**
     * Main method where the program starts.
     * It processes a list of tweets, filters out those not from February 2024,
     * counts hashtag occurrences, sorts them, and displays the top 3 trending hashtags.
     */
    public static void main(String[] args) {
        // Sample dataset: user_id, tweet_id, tweet_content, tweet_date
        List<Map<String, String>> tweets = new ArrayList<>();

        // Adding sample tweets using the helper method createTweet
        tweets.add(createTweet("201", "21", "Loving the vibes today! #GoodVibes #ChillMode", "2024-02-02"));
        tweets.add(createTweet("202", "22", "Work hustle never stops! #Grind #Hustle", "2024-02-03"));
        tweets.add(createTweet("203", "23", "Exploring new AI trends! #AI #TechWorld", "2024-02-04"));
        tweets.add(createTweet("204", "24", "Sunny days ahead! #GoodVibes #Sunshine", "2025-02-05"));
        tweets.add(createTweet("205", "25", "AI revolution is here! #AI #FutureTech", "2024-02-06"));
        tweets.add(createTweet("205", "25", "AI revolution is here! #AI #FutureTech", "2025-03-07"));

        tweets.add(createTweet("206", "26", "Success comes with persistence! #Hustle #Motivation", "2024-02-07"));
        tweets.add(createTweet("207", "27", "Nature therapy always works. #Peaceful #NatureLover", "2024-02-08"));

        // Date format to check the tweet date (YYYY-MM-DD)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;

        try {
            // Parse the start and end dates for February 2024
            startDate = dateFormat.parse("2024-02-01");
            endDate = dateFormat.parse("2024-02-29");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // A map to store hashtag frequency (hashtag -> count)
        Map<String, Integer> hashtagFrequency = new HashMap<>();

        // Loop through each tweet in the list
        for (Map<String, String> tweet : tweets) {
            // Get the tweet's date and check if it is within February 2024
            String tweetDateStr = tweet.get("tweet_date");

            try {
                Date tweetDate = dateFormat.parse(tweetDateStr);
                
                // Only process the tweet if it's in February 2024
                if (!tweetDate.before(startDate) && !tweetDate.after(endDate)) {
                    // Get the tweet's content
                    String tweetContent = tweet.get("tweet");

                    // Split the content into individual words
                    String[] words = tweetContent.split(" ");
                    
                    // Loop through each word and check if it's a hashtag (starts with #)
                    for (String word : words) {
                        if (word.startsWith("#")) {
                            // Convert hashtag to lowercase for case-insensitive counting
                            String hashtag = word.toLowerCase();
                            
                            // Update the frequency count of the hashtag in the map
                            hashtagFrequency.put(hashtag, hashtagFrequency.getOrDefault(hashtag, 0) + 1);
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Create a list of entries from the hashtag frequency map to sort
        List<Map.Entry<String, Integer>> sortedHashtags = new ArrayList<>(hashtagFrequency.entrySet());
        
        // Sort hashtags: First by frequency in descending order, then alphabetically
        sortedHashtags.sort((a, b) -> {
            // Compare frequency first (descending order)
            int frequencyComparison = b.getValue().compareTo(a.getValue());
            if (frequencyComparison != 0) return frequencyComparison;
            
            // If frequencies are equal, compare alphabetically (ascending order)
            return a.getKey().compareTo(b.getKey());
        });

        // Print the header of the table
        System.out.println("+-------------+---------+");
        System.out.println("|   HASHTAG   |  COUNT  |");
        System.out.println("+-------------+---------+");
        
        // Print the top 3 hashtags or fewer if there are not enough hashtags
        for (int i = 0; i < Math.min(3, sortedHashtags.size()); i++) {
            // Get the entry (hashtag and its count)
            Map.Entry<String, Integer> entry = sortedHashtags.get(i);
            
            // Print the hashtag and its count in a formatted manner
            System.out.printf("| %-11s | %-7d |%n", entry.getKey(), entry.getValue());
        }
        
        // Print the footer of the table
        System.out.println("+-------------+---------+");
    }

    /**
     * Helper method to create a tweet's data.
     * @param userId The ID of the user who posted the tweet
     * @param tweetId The unique ID of the tweet
     * @param tweet The content of the tweet
     * @param tweetDate The date when the tweet was posted
     * @return A map representing the tweet with user_id, tweet_id, tweet, and tweet_date
     */
    private static Map<String, String> createTweet(String userId, String tweetId, String tweet, String tweetDate) {
        // Create a map to store tweet information
        Map<String, String> tweetData = new HashMap<>();
        
        // Put user_id, tweet_id, tweet content, and tweet_date in the map
        tweetData.put("user_id", userId);
        tweetData.put("tweet_id", tweetId);
        tweetData.put("tweet", tweet);
        tweetData.put("tweet_date", tweetDate);
        
        // Return the map containing the tweet's information
        return tweetData;
    }
}
