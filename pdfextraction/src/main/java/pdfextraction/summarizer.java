package pdfextraction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import org.asynchttpclient.*;
import static org.asynchttpclient.Dsl.*;

public class summarizer {

    public static void summarize(String text) throws InterruptedException {

        try {
            AsyncHttpClient client = new DefaultAsyncHttpClient();

            String requestBody = String.format("{\"text\":\"%s\"}", text);

            Request request = client.prepare("POST", "https://api.cohere.ai/v1/summarize")
                    .setHeader("accept", "application/json")
                    .setHeader("content-type", "application/json")
                    .setHeader("authorization", "Bearer K2qeYNh4ziHhIpysVaBCc8AZAU3ygJsoMASBYj6K")
                    .setBody(requestBody)
                    .build();

            Response response = client.executeRequest(request).get();

            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getResponseBody());


            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Method Name: readTxtFile
     * Description: is given a file path, reads it and stores content into a variable that
     * is then returned to where it was called from
     * @return
     */
    public static String readTxtFile(String filePath) throws InterruptedException {
        StringBuilder content = new StringBuilder();

        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine());
                content.append("\n"); // Add a new line after each line read
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            e.printStackTrace();
        }

        return content.toString();
    }
    public static String extractKeyword(String text) throws InterruptedException {
        // Define common stopwords that we want to remove from the text
        Set<String> stopwords = new HashSet<>(Arrays.asList("the", "and", "is", "a", "an", "of", "in", "to"));

        // Remove punctuation and convert the text to lowercase
        String cleanedText = text.replaceAll("[^a-zA-Z ]", "").toLowerCase();

        // Tokenize the text into individual words
        String[] words = cleanedText.split("\\s+");

        // Create a HashMap to store the frequency of each word
        Map<String, Integer> wordFrequency = new HashMap<>();

        // Count the frequency of each word
        for (String word : words) {
            if (!stopwords.contains(word)) {
                wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
            }
        }

        // Find the most frequent word(s)
        List<String> mostFrequentWords = new ArrayList<>();
        int maxFrequency = 0;

        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
            int frequency = entry.getValue();
            if (frequency > maxFrequency) {
                maxFrequency = frequency;
                mostFrequentWords.clear();
                mostFrequentWords.add(entry.getKey());
            } else if (frequency == maxFrequency) {
                mostFrequentWords.add(entry.getKey());
            }
        }

        // If there are multiple words with the same frequency, concatenate them as key phrases
        return String.join(", ", mostFrequentWords);
    }

}
