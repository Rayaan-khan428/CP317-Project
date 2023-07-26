package pdfextraction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;

public class summarizer {

    public static void summarize(String text) throws InterruptedException {

	try {
	    AsyncHttpClient client = new DefaultAsyncHttpClient();

	    String apiKey = "K2qeYNh4ziHhIpysVaBCc8AZAU3ygJsoMASBYj6K";

	    String requestBody = String.format(
		    "{\"text\":\"%s\", \"length\":\"medium\", \"format\":\"bullets\", \"model\":\"summarize-xlarge\"}",
		    text);

	    Request request = client.prepare("POST", "https://api.cohere.ai/v1/summarize")
		    .setHeader("accept", "application/json").setHeader("content-type", "application/json")
		    .setHeader("authorization", "Bearer " + apiKey).setBody(requestBody).build();

	    Response response = client.executeRequest(request).get();

	    System.out.println("Status Code: " + response.getStatusCode());
	    System.out.println("Response Body: " + response.getResponseBody());

	    client.close();

	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    /**
     * Method Name: readTxtFile Description: is given a file path, reads it and
     * stores content into a variable that is then returned to where it was called
     * from
     * 
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