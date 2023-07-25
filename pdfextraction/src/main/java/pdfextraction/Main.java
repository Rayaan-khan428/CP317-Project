package pdfextraction;

import java.io.File;
import java.io.IOException;

import com.google.gson.stream.JsonReader;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        String inputFileName = "/Users/rayaankhan/repos/CP317-Project/pdfextraction/sample_reports/cloudcomputing.pdf"; // where PDF is located
        String prettyJson;

        // Where results of pdf extraction will be output
        String outputFolder = System.getProperty("user.dir");
        outputFolder += "/pdfextraction/src";

        // Create a new folder named "output" to store images and result.txt
        File folder = new File(outputFolder + "/output");
        folder.mkdirs();
        outputFolder = folder.getAbsolutePath(); // Update outputFolder with the new folder path

        // create a TextExtraction Object
        TextExtraction pdfExtractor = new TextExtraction(outputFolder);
        PDDocument document = PDDocument.load(new File(inputFileName));

        // extract Images & Text from the Document
        pdfExtractor.runExtraction(document);

        // Read the JSON file
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(new FileReader("output.json")).getAsJsonObject();

        // Extract the content from the "text" field
        String extractedText = jsonObject.get("text").getAsString();

        // Print the content to the console
        System.out.println("Extracted Text:");
        System.out.println(extractedText);

        try (FileWriter fileWriter = new FileWriter("/Users/rayaankhan/repos/CP317-Project/pdfextraction/src/output/result.txt")) {
            fileWriter.write(extractedText);
            System.out.println("String has been saved to 'result.txt' successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // parse text and break up into paragraphs and store in a json
         TextSegmenter.divide(outputFolder);

        // summarize every paragraph

        document.close();

        //To delete the output folder
//        System.out.println("Output folder deleting...");
//        FileUtils.deleteDirectory(new File(outputFolder));
//        System.out.println("Output folder deleted.");

    }
}
