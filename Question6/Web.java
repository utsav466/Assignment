package Question6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Multi-threaded web crawler that fetches web pages, extracts links, and crawls new pages.
 */
public class Web {
    
    private static final int THREAD_POOL_SIZE = 5; // Defines the number of threads for concurrent crawling
    private static final Set<String> visitedUrls = new HashSet<>(); // Keeps track of visited URLs to prevent duplicate crawling
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE); // Creates a fixed thread pool for managing tasks

    public static void main(String[] args) {
        String seedUrl = "https://example.com"; // Initial URL to start crawling
        visitedUrls.add(seedUrl); // Adds the seed URL to the visited set to avoid revisiting
        threadPool.submit(new CrawlTask(seedUrl)); // Submits the first URL to the thread pool for processing

        try {
            Thread.sleep(5000); // Allows the crawler to run for 5 seconds before stopping
        } catch (InterruptedException e) {
            e.printStackTrace(); // Prints an error if the sleep is interrupted
        }
        
        threadPool.shutdown(); // Shuts down the thread pool to stop further task submissions
    }

    /**
     * Runnable task for crawling web pages.
     */
    static class CrawlTask implements Runnable {
        private final String url; // Stores the URL to be crawled

        public CrawlTask(String url) {
            this.url = url; // Assigns the URL to the instance variable
        }

        @Override
        public void run() {
            try {
                String content = fetchWebPage(url); // Fetches web page content
                String title = extractTitle(content); // Extracts the page title
                System.out.println("Crawled: " + url + " -> Title: " + title); // Displays the crawled URL and title
                
                Set<String> newUrls = extractUrls(content); // Extracts links from the page
                
                for (String newUrl : newUrls) { // Iterates through extracted URLs
                    synchronized (visitedUrls) { // Ensures thread-safe access to shared data
                        if (!visitedUrls.contains(newUrl)) { // Checks if the URL has been visited
                            visitedUrls.add(newUrl); // Marks URL as visited
                            threadPool.submit(new CrawlTask(newUrl)); // Submits a new task to crawl the URL
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error crawling " + url + ": " + e.getMessage()); // Prints error message if crawling fails
            }
        }

        /**
         * Fetches the HTML content of a web page.
         */
        private String fetchWebPage(String url) throws IOException {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection(); // Opens an HTTP connection
            connection.setRequestMethod("GET"); // Sets HTTP method to GET

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); // Reads response
            StringBuilder content = new StringBuilder(); // Stores the content of the page
            String line;
            
            while ((line = reader.readLine()) != null) { // Reads line by line
                content.append(line); // Appends each line to content
            }
            reader.close(); // Closes the reader
            
            return content.toString(); // Returns the fetched content as a string
        }

        /**
         * Extracts the title of the web page from its HTML content.
         */
        private String extractTitle(String content) {
            int titleStart = content.indexOf("<title>"); // Finds the start index of the <title> tag
            int titleEnd = content.indexOf("</title>"); // Finds the end index of the </title> tag
            
            if (titleStart != -1 && titleEnd != -1) { // Checks if both title tags exist
                return content.substring(titleStart + 7, titleEnd).trim(); // Extracts and returns the title
            }
            return "No Title"; // Returns a default message if no title is found
        }

        /**
         * Extracts all URLs from the HTML content by searching for 'href' attributes.
         */
        private Set<String> extractUrls(String content) {
            Set<String> urls = new HashSet<>(); // Creates a set to store unique URLs
            int hrefStart;
            int hrefEnd = 0;
            
            while ((hrefStart = content.indexOf("href=\"", hrefEnd)) != -1) { // Finds occurrences of href="URL"
                hrefEnd = content.indexOf("\"", hrefStart + 6); // Finds the closing quote of the href attribute
                if (hrefEnd != -1) {
                    String newUrl = content.substring(hrefStart + 6, hrefEnd); // Extracts the URL
                    
                    if (newUrl.startsWith("http")) { // Ensures only absolute URLs are added
                        urls.add(newUrl); // Adds the URL to the set
                    }
                }
            }
            return urls; // Returns the set of extracted URLs
        }
    }
}
