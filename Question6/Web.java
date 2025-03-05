package  Question6;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class Web {
    private final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();  // Thread-safe set
    private final ConcurrentLinkedQueue<String> urlQueue = new ConcurrentLinkedQueue<>();
    private final ExecutorService executorService;
    private final int maxThreads;
    private final int maxPagesToCrawl;

    public Web(int maxThreads, int maxPagesToCrawl) {
        this.executorService = Executors.newFixedThreadPool(maxThreads);
        this.maxThreads = maxThreads;
        this.maxPagesToCrawl = maxPagesToCrawl;
    }

    public void startCrawling(String startUrl) {
        urlQueue.add(startUrl);

        for (int i = 0; i < maxThreads; i++) {
            executorService.submit(this::processUrls);
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void processUrls() {
        while (!urlQueue.isEmpty() && visitedUrls.size() < maxPagesToCrawl) {
            String url = urlQueue.poll();
            if (url == null || visitedUrls.contains(url)) {
                continue;
            }

            visitedUrls.add(url);
            System.out.println("Crawling: " + url);

            try {
                String content = fetchContent(url);
                Set<String> extractedUrls = extractUrls(content);
                urlQueue.addAll(extractedUrls);
            } catch (Exception e) {
                System.err.println("Failed to fetch: " + url);
            }
        }
    }

    private String fetchContent(String urlString) throws Exception {
        StringBuilder content = new StringBuilder();
        URL url = new URI(urlString).toURL();  // Fixes deprecation warning
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        return content.toString();
    }

    private Set<String> extractUrls(String content) {
        Set<String> urls = new HashSet<>();
        // Simple regex to find URLs (this can be improved)
        String regex = "http[s]?://\\S+";
        content.lines().forEach(line -> {
            if (line.matches(regex)) {
                urls.add(line);
            }
        });
        return urls;
    }

    public static void main(String[] args) {
        Web crawler = new Web(5, 50);  // 5 threads, crawl up to 50 pages
        crawler.startCrawling("https://example.com");
    }
}
