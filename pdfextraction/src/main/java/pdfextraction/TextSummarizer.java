package pdfextraction;

import java.util.*;
import org.asynchttpclient.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class TextSummarizer {

    public static void summarize(ArrayList<Slide> presentation) throws InterruptedException {

        // Create a Gson object
        Gson gson = new Gson();

        for (int i = 0; i <  presentation.size(); i++) {

            Response response = null;
            try {

                AsyncHttpClient client = new DefaultAsyncHttpClient();
                String apiKey = "";

                String text = presentation.get(i).getParagraph();

                String regex = "\\*{3}(?:END|START) OF PAGE \\d+\\*{3}";
                String preSummarization = text.replaceAll(regex, "");
                preSummarization = preSummarization.replaceAll("\\s+", " ").trim();

                String requestBody = String.format("{\"text\":\"%s\", \"length\":\"short\", \"format\":\"bullets\", \"model\":\"summarize-xlarge\"}", preSummarization);

                Request request = client.prepare("POST", "https://api.cohere.ai/v1/summarize")
                        .setHeader("accept", "application/json")
                        .setHeader("content-type", "application/json")
                        .setHeader("authorization", "Bearer " + apiKey)
                        .setBody(requestBody)
                        .build();

                // send the request and wait for response
                response = client.executeRequest(request).get();

                // Parse the JSON string to a JsonObject
                JsonObject jsonObject = gson.fromJson(response.getResponseBody(), JsonObject.class);

                // Extract the "summary" attribute
                String summary = jsonObject.get("summary").getAsString();

                summary = summary.replaceAll(regex, "");

                // update presentation paragraph to bullet points
                presentation.get(i).setParagraph(summary);

                System.out.println("-----------------------Text Being Summarized----------------------");
                System.out.println("PreSummarization: ");
                System.out.println(preSummarization);
                System.out.println("\nAfterSummarization: ");
                System.out.println(summary);
                System.out.println("\n");

                client.close();

            } catch (NullPointerException e) {
                assert response != null;
                System.out.println(response.getResponseBody());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}

