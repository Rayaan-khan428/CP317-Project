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

            String test = "South America, known for its breathtaking landscapes and rich biodiversity, is a haven for a vast array of lizard species. These reptilian inhabitants contribute significantly to the continent's intricate ecosystems, playing vital ecological roles that influence plant and insect populations, serve as prey for predators, and contribute to overall ecosystem balance. In this comprehensive research, we delve into the fascinating world of South American lizards, exploring the diverse families that call this continent home. Our study focuses on three remarkable lizard species - the Green Iguana (Iguana iguana), the Caiman Lizard (Dracaena guianensis), and the Tepui Lizard (Pristidactylus urichi) - highlighting their unique characteristics, habitats, and ecological significance. Additionally, we delve into the threats these lizards face and examine the conservation efforts in place to protect these captivating reptiles and ensure the preservation of their essential roles within South American ecosystems. Through this exploration, we hope to gain a deeper understanding of the importance of these reptilian inhabitants, encouraging further research and conservation initiatives to safeguard their future and the biodiversity they contribute to the magnificent continent of South America.";
            String apiKey = "K2qeYNh4ziHhIpysVaBCc8AZAU3ygJsoMASBYj6K";

            String requestBody = String.format("{\"text\":\"%s\", \"length\":\"medium\", \"format\":\"bullets\", \"model\":\"summarize-xlarge\"}", test);

            Request request = client.prepare("POST", "https://api.cohere.ai/v1/summarize")
                    .setHeader("accept", "application/json")
                    .setHeader("content-type", "application/json")
                    .setHeader("authorization", "Bearer " + apiKey)
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

}

